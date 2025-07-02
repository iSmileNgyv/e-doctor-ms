package com.example.notification.util.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

public enum DeliveryMethod {

    SMS(0), EMAIL(1), TELEGRAM(2);

    private final Integer value;

    DeliveryMethod(Integer value) {
        this.value = value;
    }

    @JsonValue
    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @JsonCreator
    public static DeliveryMethod fromValue(Integer value) {
        for (DeliveryMethod method : values()) {
            if (Objects.equals(method.value, value)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Invalid delivery method value: " + value);
    }
}
