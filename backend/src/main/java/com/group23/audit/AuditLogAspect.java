package com.group23.audit;

import com.group23.security.AuthenticatedUser;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Applies audit writes to service methods annotated with {@link Audited}.
 *
 * <p>Every call writes exactly one row and does so inside the same transaction
 * as the annotated method. The actor always comes from the authenticated
 * principal; request fields such as {@code actorId} or {@code operatorId} are
 * ignored and redacted from the snapshot.</p>
 *
 * <p>The intentional exclusion list is:</p>
 * <ul>
 *   <li>{@code POST /api/auth/login}: no authenticated actor or target record,
 *       and the request contains credentials that must never enter an audit
 *       payload.</li>
 *   <li>Read-only requests, health checks, OpenAPI and static resources: they
 *       do not mutate business data.</li>
 *   <li>Failed or rejected calls: the aspect records only successful returns,
 *       so an operation rolled back by the method leaves no audit row.</li>
 *   <li>Operations without a collection-point-scoped target: the current
 *       {@code audit_logs.collection_point_id} column is {@code NOT NULL}, so
 *       such operations are not marked {@link Audited} until a separate global
 *       audit scope is approved.</li>
 * </ul>
 *
 * <p>Spring AOP only intercepts calls made through the Spring proxy. Audited
 * methods must therefore be public service methods invoked from another
 * bean.</p>
 */
@Aspect
@Component
public class AuditLogAspect {

    private final AuditLogRepository auditLogRepository;
    private final AuditPayloadSanitizer payloadSanitizer;
    private final TransactionTemplate transactionTemplate;
    private final ExpressionParser expressionParser = new SpelExpressionParser();
    private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    public AuditLogAspect(
            AuditLogRepository auditLogRepository,
            AuditPayloadSanitizer payloadSanitizer,
            PlatformTransactionManager transactionManager) {
        this.auditLogRepository = auditLogRepository;
        this.payloadSanitizer = payloadSanitizer;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @Around("@annotation(audited)")
    public Object writeAuditTrail(ProceedingJoinPoint joinPoint, Audited audited) throws Throwable {
        AuthenticatedUser actor = currentActor();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Map<String, Object> before = payloadSanitizer.sanitizeArguments(method, joinPoint.getArgs());

        try {
            return transactionTemplate.execute(status -> {
                try {
                    Object result = joinPoint.proceed();
                    UUID targetId = evaluateUuid(audited.targetIdExpression(), joinPoint, method, result, "targetId");
                    UUID collectionPointId = evaluateUuid(
                            audited.collectionPointIdExpression(), joinPoint, method, result, "collectionPointId");
                    UUID auditId = auditLogRepository.append(new AuditLogEntry(
                            actor.userId(),
                            audited.action(),
                            audited.targetType(),
                            targetId,
                            before,
                            payloadSanitizer.sanitizeResult(result),
                            collectionPointId));
                    if (result instanceof AuditAttachable attachable) {
                        return attachable.attachAudit(auditId);
                    }
                    return result;
                } catch (RuntimeException | Error exception) {
                    throw exception;
                } catch (Throwable exception) {
                    throw new AuditedMethodException(exception);
                }
            });
        } catch (AuditedMethodException exception) {
            throw exception.getCause();
        }
    }

    private UUID evaluateUuid(
            String expression, ProceedingJoinPoint joinPoint, Method method, Object result, String fieldName) {
        StandardEvaluationContext context = new StandardEvaluationContext(joinPoint.getTarget());
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        Object[] arguments = joinPoint.getArgs();
        if (parameterNames != null) {
            for (int index = 0; index < arguments.length && index < parameterNames.length; index++) {
                context.setVariable(parameterNames[index], arguments[index]);
            }
        }
        for (int index = 0; index < arguments.length; index++) {
            context.setVariable("a" + index, arguments[index]);
            context.setVariable("p" + index, arguments[index]);
        }
        context.setVariable("result", result);

        Object value = expressionParser.parseExpression(expression).getValue(context);
        if (value instanceof UUID uuid) {
            return uuid;
        }
        if (value instanceof String text && !text.isBlank()) {
            return UUID.fromString(text);
        }
        throw new IllegalStateException("audited operation must resolve a non-null " + fieldName);
    }

    private static AuthenticatedUser currentActor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof AuthenticatedUser actor)) {
            throw new IllegalStateException("audited operation requires an authenticated principal");
        }
        return actor;
    }

    private static final class AuditedMethodException extends RuntimeException {

        private AuditedMethodException(Throwable cause) {
            super(cause);
        }

        @Override
        public synchronized Throwable getCause() {
            return super.getCause();
        }
    }
}
