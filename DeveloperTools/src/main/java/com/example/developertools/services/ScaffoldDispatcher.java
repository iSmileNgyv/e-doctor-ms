package com.example.developertools.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ScaffoldDispatcher {
    private final List<ScaffoldGenerator> generators;

    public void dispatch(String type, Map<String, String> args) {
        generators.stream()
                .filter(g -> g.getType().equalsIgnoreCase(type))
                .findFirst()
                .ifPresentOrElse(
                        g -> g.generate(args),
                        () -> System.out.printf("❌ Unknown scaffold type: %s%n", type)
                );
    }
}
