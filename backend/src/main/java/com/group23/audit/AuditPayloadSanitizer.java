package com.group23.audit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class AuditPayloadSanitizer {

    private static final Pattern CHINESE_MOBILE_NUMBER =
            Pattern.compile("(?<!\\d)(1[3-9]\\d{9})(?!\\d)");

    private static final Set<String> REDACTED_KEY_PARTS = Set.of(
            "password",
            "token",
            "authorization",
            "pickupcode",
            "actorid",
            "operatorid"
    );

    private final ObjectMapper objectMapper;
    private final ParameterNameDiscoverer parameterNameDiscoverer =
            new DefaultParameterNameDiscoverer();

    public AuditPayloadSanitizer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Map<String, Object> sanitizeArguments(Method method, Object[] arguments) {
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        Map<String, Object> result = new LinkedHashMap<>();
        for (int index = 0; index < arguments.length; index++) {
            Object argument = arguments[index];
            if (isInfrastructureValue(argument)) {
                continue;
            }
            String name = parameterNames != null && parameterNames.length > index
                    ? parameterNames[index]
                    : "arg" + index;
            result.put(name, sanitizeValue(argument));
        }
        return result;
    }

    public Map<String, Object> sanitizeResult(Object value) {
        if (value == null) {
            return Map.of();
        }

        JsonNode sanitized = sanitizeNode(toJson(value), "");
        if (sanitized.isObject()) {
            return objectMapper.convertValue(
                    sanitized,
                    new TypeReference<LinkedHashMap<String, Object>>() {
                    }
            );
        }
        return Map.of("value", objectMapper.convertValue(sanitized, Object.class));
    }

    public Object sanitizeValue(Object value) {
        JsonNode sanitized = sanitizeNode(toJson(value), "");
        return objectMapper.convertValue(sanitized, Object.class);
    }

    private JsonNode toJson(Object value) {
        try {
            return objectMapper.valueToTree(value);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("audit payload could not be serialized", exception);
        }
    }

    private JsonNode sanitizeNode(JsonNode node, String fieldName) {
        if (node == null || node.isNull()) {
            return objectMapper.nullNode();
        }

        String normalizedField = fieldName.toLowerCase(Locale.ROOT);
        if (isSecret(normalizedField)) {
            return TextNode.valueOf("[REDACTED]");
        }
        if (isPhone(normalizedField) && node.isValueNode()) {
            return TextNode.valueOf(maskPhone(node.asText()));
        }

        if (node.isObject()) {
            ObjectNode sanitized = objectMapper.createObjectNode();
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                sanitized.set(field.getKey(), sanitizeNode(field.getValue(), field.getKey()));
            }
            return sanitized;
        }

        if (node.isArray()) {
            ArrayNode sanitized = objectMapper.createArrayNode();
            for (JsonNode item : node) {
                sanitized.add(sanitizeNode(item, fieldName));
            }
            return sanitized;
        }

        if (node.isTextual()) {
            return TextNode.valueOf(maskEmbeddedPhoneNumbers(node.asText()));
        }

        return node;
    }

    private static boolean isSecret(String fieldName) {
        return REDACTED_KEY_PARTS.stream().anyMatch(fieldName::contains);
    }

    private static boolean isPhone(String fieldName) {
        return fieldName.contains("phone") || fieldName.contains("mobile");
    }

    private static String maskPhone(String value) {
        String digits = value.replaceAll("\\D", "");
        if (digits.length() < 7) {
            return "***";
        }
        return digits.substring(0, 3)
                + "****"
                + digits.substring(digits.length() - 4);
    }

    private static String maskEmbeddedPhoneNumbers(String value) {
        return CHINESE_MOBILE_NUMBER.matcher(value)
                .replaceAll(match -> maskPhone(match.group(1)));
    }

    private static boolean isInfrastructureValue(Object value) {
        if (value == null) {
            return false;
        }
        return value instanceof java.security.Principal
                || value instanceof org.springframework.security.core.Authentication
                || value instanceof jakarta.servlet.ServletRequest
                || value instanceof jakarta.servlet.ServletResponse;
    }
}
