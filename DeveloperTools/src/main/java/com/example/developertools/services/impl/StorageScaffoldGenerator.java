package com.example.developertools.services.impl;
import com.example.developertools.services.ScaffoldGenerator;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Component
public class StorageScaffoldGenerator implements ScaffoldGenerator {

    @Override
    public String getType() {
        return "storage";
    }

    @Override
    public void generate(Map<String, String> args) {
        String name = args.get("name");
        if (name == null || name.isEmpty()) {
            System.out.println("❌ You must provide the storage name (e.g., GoogleDrive)");
            return;
        }

        // Normalize class and bean names
        String className = name + "StorageImpl"; // e.g. GoogleDriveStorageImpl

        try {
            Path projectRoot = findProjectRoot();
            if (projectRoot == null) return; // error already printed

            // template path
            String templatePath = "templates/StorageTemplate.java";
            InputStream is = getClass().getClassLoader().getResourceAsStream(templatePath);
            if (is == null) {
                System.out.println("❌ Template file not found: " + templatePath);
                return;
            }

            String template = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            String content = template
                    .replace("{{CLASS_NAME}}", className)
                    .replace("{{STORAGE_NAME}}", name);

            // output file path relative to project root
            Path outputPath = projectRoot.resolve("storage/src/main/java/com/example/storage/service/storage/impl/" + className + ".java");

            if (Files.exists(outputPath)) {
                System.out.println("⚠️ Storage implementation already exists: " + outputPath);
                return;
            }

            Files.createDirectories(outputPath.getParent());
            Files.writeString(outputPath, content);
            System.out.println("✅ Storage implementation generated: " + outputPath);

        } catch (Exception e) {
            System.out.println("❌ Error generating storage implementation: " + e.getMessage());
        }
    }

    // helper to find project root
    private Path findProjectRoot() {
        Path current = Paths.get(".").toAbsolutePath();
        while (current != null) {
            if (Files.exists(current.resolve("settings.gradle")) ||
                    Files.exists(current.resolve(".git")) ||
                    Files.exists(current.resolve("Storage/build.gradle"))) {
                return current;
            }
            current = current.getParent();
        }
        System.out.println("❌ Project root not found. Please run from project root.");
        return null;
    }
}
