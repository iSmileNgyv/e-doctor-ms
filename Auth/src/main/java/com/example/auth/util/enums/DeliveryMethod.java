package com.example.auth.util.enums;

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
}
