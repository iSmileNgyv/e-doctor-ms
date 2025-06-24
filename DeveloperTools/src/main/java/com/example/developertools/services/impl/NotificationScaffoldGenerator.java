package com.example.developertools.services.impl;

import com.example.developertools.services.ScaffoldGenerator;
import com.github.javaparser.JavaParser;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Component
public class NotificationScaffoldGenerator implements ScaffoldGenerator {

    @Override
    public void generate(Map<String, String> args) {
        String name = args.get("name");
        if (name == null) {
            System.out.println("❌ You must provide the class name (e.g., Telegram)");
            return;
        }

        String requestDto = args.getOrDefault("request", "BaseRequestDto");
        String responseDto = args.getOrDefault("response", "BaseResponseDto");

        System.out.printf("⚙️ Generating: %s with [%s, %s]%n", name, requestDto, responseDto);

        boolean serviceExists = checkServiceClassExists(name);
        if (!serviceExists) {
            generateServiceClass(name, requestDto, responseDto);
        } else {
            System.out.printf("⚠️ Service class already exists: %sNotificationServiceImpl.java%n", name);
        }

        if (!"BaseRequestDto".equals(requestDto)) {
            System.out.printf("📦 Checking request DTO...%n");
            generateDto(requestDto);
        }
        if (!"BaseResponseDto".equals(responseDto)) {
            System.out.printf("📦 Checking response DTO...%n");
            generateDto(responseDto);
        }

        addEnumConstantWithJavaParser(name.toUpperCase(Locale.ENGLISH));
        addEnumToProto(name.toUpperCase(Locale.ENGLISH));
        regenerateProto();
        addServiceToFactory(name.toLowerCase(Locale.ENGLISH), name.toUpperCase(Locale.ENGLISH));
    }

    @Override
    public String getType() {
        return "notification";
    }

    private void generateServiceClass(String name, String requestDto, String responseDto) {
        try {
            Path projectRoot = findProjectRoot();
            if (projectRoot == null) return;
            String className = name + "NotificationServiceImpl";
            Path outputPath = projectRoot.resolve("Notification/src/main/java/com/example/notification/service/impl/" + className + ".java");

            InputStream is = getClass().getClassLoader().getResourceAsStream("templates/NotificationTemplate.java");
            if (is == null) {
                System.out.println("❌ Template file not found.");
                return;
            }

            String template = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            String content = template
                    .replace("{{CLASS_NAME}}", className)
                    .replace("{{REQUEST_DTO}}", requestDto)
                    .replace("{{RESPONSE_DTO}}", responseDto);

            Files.createDirectories(outputPath.getParent());
            Files.writeString(outputPath, content);
            System.out.printf("✅ Java class generated: %s%n", outputPath);

        } catch (Exception e) {
            System.out.println("❌ Error during service class generation: " + e.getMessage());
        }
    }

    private void generateDto(String dtoName) {
        try {
            Path projectRoot = findProjectRoot();
            if (projectRoot == null) return;
            Path path = projectRoot.resolve("Notification/src/main/java/com/example/notification/dto/" + dtoName + ".java");

            if (Files.exists(path)) {
                System.out.printf("⚠️ DTO already exists: %s%n", path);
                return;
            }

            String templateFile = dtoName.contains("Request")
                    ? "templates/NotificationRequestDtoTemplate.java"
                    : "templates/NotificationResponseDtoTemplate.java";

            InputStream is = getClass().getClassLoader().getResourceAsStream(templateFile);
            if (is == null) {
                System.out.printf("❌ DTO template not found: %s%n", templateFile);
                return;
            }

            String template = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            String content = template.replace("{{CLASS_NAME}}", dtoName);

            Files.createDirectories(path.getParent());
            Files.writeString(path, content);

            System.out.printf("✅ DTO generated from template: %s%n", path);

        } catch (Exception e) {
            System.out.println("❌ Error generating DTO: " + e.getMessage());
        }
    }

    private void addEnumConstantWithJavaParser(String newConstant) {
        try {
            Path projectRoot = findProjectRoot();
            if (projectRoot == null) return;
            Path path = projectRoot.resolve("Notification/src/main/java/com/example/notification/util/enums/DeliveryMethod.java");
            if (!Files.exists(path)) {
                System.out.println("❌ DeliveryMethod.java not found.");
                return;
            }

            CompilationUnit cu = StaticJavaParser.parse(path);

            Optional<EnumDeclaration> enumOpt = cu.getEnumByName("DeliveryMethod");
            if (enumOpt.isEmpty()) {
                System.out.println("❌ DeliveryMethod enum not found.");
                return;
            }

            EnumDeclaration enumDecl = enumOpt.get();

            if (enumDecl.getEntries().stream().anyMatch(e -> e.getNameAsString().equals(newConstant))) {
                System.out.printf("⚠️ %s already exists in DeliveryMethod enum.%n", newConstant);
                return;
            }

            int max = enumDecl.getEntries().stream()
                    .map(e -> e.getArguments().getFirst()
                            .map(expr -> expr.asIntegerLiteralExpr().asNumber().intValue())
                            .orElse(-1))
                    .max(Integer::compareTo)
                    .orElse(-1);

            int newValue = max + 1;
            EnumConstantDeclaration newEntry = new EnumConstantDeclaration(newConstant);
            newEntry.addArgument(Integer.toString(newValue));
            enumDecl.addEntry(newEntry);

            Files.writeString(path, cu.toString());
            System.out.printf("✅ Added %s(%d) to DeliveryMethod enum.%n", newConstant, newValue);

        } catch (Exception e) {
            System.out.println("❌ Error using JavaParser: " + e.getMessage());
        }
    }

    private void addEnumToProto(String enumName) {
        try {
            Path projectRoot = findProjectRoot();
            if (projectRoot == null) return;
            Path protoPath = projectRoot.resolve("Notification/src/main/proto/Register.proto");

            List<String> lines = Files.readAllLines(protoPath);
            List<String> updated = new ArrayList<>();
            boolean insideEnum = false;
            int maxValue = -1;
            boolean alreadyExists = false;

            for (String line : lines) {
                String trimmed = line.trim();

                if (trimmed.startsWith("enum DeliveryMethod")) {
                    insideEnum = true;
                }
                if (insideEnum && trimmed.matches("^" + enumName + "\\s*=\\s*\\d+;")) {
                    alreadyExists = true;
                }
                if (insideEnum && trimmed.matches("^[A-Z_]+\\s*=\\s*(\\d+);")) {
                    int value = Integer.parseInt(trimmed.replaceAll("[^0-9]", ""));
                    maxValue = Math.max(maxValue, value);
                }
                if (insideEnum && trimmed.equals("}")) {
                    if (!alreadyExists) {
                        updated.add("  " + enumName + " = " + (maxValue + 1) + ";");
                        System.out.printf("✅ Added `%s = %d;` to DeliveryMethod enum.%n", enumName, maxValue + 1);
                    }
                    insideEnum = false;
                }
                updated.add(line);
            }

            Files.write(protoPath, updated);

        } catch (Exception e) {
            System.out.println("❌ Error updating proto file: " + e.getMessage());
        }
    }

    private void regenerateProto() {
        try {
            Path projectRoot = findProjectRoot();
            if (projectRoot == null) return;
            Path notificationDir = projectRoot.resolve("Notification");
            Path gradlewPath = notificationDir.resolve("gradlew");
            boolean useWrapper = Files.exists(gradlewPath);
            String command = useWrapper ? "./gradlew" : "gradle";

            ProcessBuilder pb = new ProcessBuilder(command, "generateProto");
            pb.directory(notificationDir.toFile());
            pb.inheritIO();

            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("✅ Proto generation completed via " + (useWrapper ? "./gradlew" : "gradle"));
            } else {
                System.out.println("❌ Proto generation failed with exit code: " + exitCode);
            }

        } catch (Exception e) {
            System.out.println("❌ Error during proto generation: " + e.getMessage());
        }
    }

    public void addServiceToFactory(String beanName, String enumConstant) {
        try {
            Path projectRoot = findProjectRoot();
            if (projectRoot == null) return;
            Path filePath = projectRoot.resolve("Notification/src/main/java/com/example/notification/service/NotificationServiceFactory.java");

            JavaParser parser = new JavaParser();
            CompilationUnit cu = parser.parse(filePath).getResult().orElseThrow();

            ClassOrInterfaceDeclaration clazz = cu.getClassByName("NotificationServiceFactory")
                    .orElseThrow(() -> new IllegalStateException("NotificationServiceFactory not found"));
            ConstructorDeclaration constructor = clazz.getConstructors().stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Constructor not found"));

            boolean paramExists = constructor.getParameters().stream()
                    .anyMatch(p -> p.getNameAsString().equals(beanName));
            if (!paramExists) {
                String typeName = capitalize(beanName) + "NotificationServiceImpl";
                Parameter newParam = new Parameter(new ClassOrInterfaceType(null, typeName), beanName);
                constructor.addParameter(newParam);
                System.out.printf("➕ Added constructor parameter: %s %s%n", typeName, beanName);
            }

            BlockStmt body = constructor.getBody();
            boolean alreadyHasPut = body.getStatements().stream()
                    .filter(Statement::isExpressionStmt)
                    .map(stmt -> stmt.asExpressionStmt().getExpression())
                    .filter(Expression::isMethodCallExpr)
                    .map(Expression::asMethodCallExpr)
                    .anyMatch(expr ->
                            expr.getNameAsString().equals("put") &&
                                    expr.getScope().isPresent() &&
                                    expr.getScope().get().toString().equals("serviceMap") &&
                                    expr.getArgument(0).toString().equals("DeliveryMethod." + enumConstant));

            if (!alreadyHasPut) {
                String putLine = String.format("serviceMap.put(DeliveryMethod.%s, %s);", enumConstant, beanName);
                body.addStatement(putLine);
                System.out.printf("✅ Added serviceMap.put: %s%n", putLine);
            } else {
                System.out.printf("ℹ️  serviceMap.put for %s already exists. Skipping.%n", enumConstant);
            }

            Files.writeString(filePath, cu.toString());
        } catch (Exception e) {
            System.out.println("❌ Error updating NotificationServiceFactory: " + e.getMessage());
        }
    }

    private String capitalize(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase(Locale.ENGLISH) + input.substring(1);
    }

    private boolean checkServiceClassExists(String name) {
        Path projectRoot = findProjectRoot();
        if (projectRoot == null) return false;
        Path outputPath = projectRoot.resolve("Notification/src/main/java/com/example/notification/service/impl/" + name + "NotificationServiceImpl.java");
        return Files.exists(outputPath);
    }

    private Path findProjectRoot() {
        Path current = Paths.get("").toAbsolutePath();

        while (current != null) {
            if (Files.exists(current.resolve("settings.gradle")) ||
                    Files.exists(current.resolve(".git")) ||
                    Files.exists(current.resolve("Notification/build.gradle"))) {
                return current;
            }
            current = current.getParent();
        }

        System.out.println("❌ Project root not found. Please run this command from the root of the your project.");
        return null;
    }
}
