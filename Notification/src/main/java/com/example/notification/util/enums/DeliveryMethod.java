package com.example.notification.util.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DeliveryMethod {
    SMS(0),
    EMAIL(1);
    private final Integer value;
    DeliveryMethod(Integer value) {
        this.value = value;
    }
    @JsonValue
    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @JsonCreator
    public static DeliveryMethod fromValue(int value) {
        for (DeliveryMethod method : values()) {
            if (method.value == value) {
                return method;
            }
        }
        throw new IllegalArgumentException("Invalid delivery method value: " + value);
    }

}
