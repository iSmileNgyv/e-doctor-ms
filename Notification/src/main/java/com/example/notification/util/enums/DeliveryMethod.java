package com.example.notification.util.enums;

import lombok.Getter;

@Getter
public enum DeliveryMethod {
    SMS("sms"),
    EMAIL("email");

    private final String value;
    DeliveryMethod(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
