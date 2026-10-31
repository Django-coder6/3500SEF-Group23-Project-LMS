package com.group23.pickup;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.junit.jupiter.api.Test;

class PickupJsonContractTest {

    @Test
    void pickupCodeValueCannotLeakWhenTheEntityIsSerialized() throws NoSuchMethodException {
        JsonIgnore annotation = PickupCode.class.getMethod("getCode").getAnnotation(JsonIgnore.class);

        assertThat(annotation).isNotNull();
    }
}
