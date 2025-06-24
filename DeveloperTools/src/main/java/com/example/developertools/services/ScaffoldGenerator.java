package com.example.developertools.services;

import java.util.Map;

public interface ScaffoldGenerator {
    void generate(Map<String, String> args);
    String getType(); //example: "notification"
}
