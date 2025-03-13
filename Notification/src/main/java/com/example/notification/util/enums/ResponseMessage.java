package com.example.notification.util.enums;

public enum ResponseMessage {
    NOTIFICATION_REGISTER_SUCCESS_MESSAGE("NOTIFICATION_REGISTER_SUCCESS_MESSAGE");

    private final String value;

    ResponseMessage(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}