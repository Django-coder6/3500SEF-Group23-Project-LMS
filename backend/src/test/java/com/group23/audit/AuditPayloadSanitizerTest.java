package com.group23.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AuditPayloadSanitizerTest {

    private final AuditPayloadSanitizer sanitizer =
            new AuditPayloadSanitizer(new ObjectMapper().findAndRegisterModules());

    @Test
    void masksPhoneNumbersAndRedactsSecretsBeforeTheyReachTheAuditTable() {
        Map<String, Object> result = sanitizer.sanitizeResult(new Object() {
            public String getRecipientPhone() {
                return "13812345678";
            }

            public String getPassword() {
                return "plain-text-password";
            }

            public String getPickupCode() {
                return "4821";
            }
        });

        assertThat(result)
                .containsEntry("recipientPhone", "138****5678")
                .containsEntry("password", "[REDACTED]")
                .containsEntry("pickupCode", "[REDACTED]");
        assertThat(result.toString())
                .doesNotContain("13812345678")
                .doesNotContain("plain-text-password")
                .doesNotContain("4821");
    }

    @Test
    void masksShortSyntheticPhoneNumbersAsWell() {
        assertThat(sanitizer.sanitizeResult(Map.of("phone", "00000001")))
                .containsEntry("phone", "000****0001");
    }

    @Test
    void masksPhoneNumbersEmbeddedInFreeText() {
        assertThat(sanitizer.sanitizeResult(Map.of(
                "note",
                "Call 13812345678 before collection."
        ))).containsEntry("note", "Call 138****5678 before collection.");
    }
}
