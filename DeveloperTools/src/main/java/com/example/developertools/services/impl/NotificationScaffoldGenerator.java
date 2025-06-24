package com.example.developertools.services.impl;

import com.example.developertools.services.ScaffoldGenerator;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.List;

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

        generateServiceClass(name, requestDto, responseDto);
    }

    @Override
    public String getType() {
        return "notification";
    }

    private void generateServiceClass(String name, String requestDto, String responseDto) {
        try {
            String className = name + "NotificationServiceImpl";
            String outputPackagePath = "com/example/notification/service/impl";
            String outputDir = "../Notification/src/main/java/" + outputPackagePath;
            String outputFilePath = outputDir + "/" + className + ".java";

            Path outputPath = Paths.get(outputFilePath);
            if (Files.exists(outputPath)) {
                System.out.printf("❌ File already exists: %s%n", outputFilePath);
                System.out.println("🚫 Aborted. No files were overwritten.");
                return;
            }

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

            System.out.printf("✅ Java class generated: %s%n", outputFilePath);

            if (!"BaseRequestDto".equals(requestDto)) {
                System.out.printf("📦 Checking request DTO...%n");
                generateDto(requestDto);
                //System.out.printf("✅ Request DTO to be created: %s.java%n", requestDto);
            }
            if (!"BaseResponseDto".equals(responseDto)) {
                System.out.printf("📦 Checking response DTO...%n");
                generateDto(responseDto);
            }

            addEnumConstantWithJavaParser(name.toUpperCase());

            addEnumToProto(name.toUpperCase());
            regenerateProto();
        } catch (Exception e) {
            System.out.println("❌ Error during service class generation: " + e.getMessage());
        }
    }

    private void generateDto(String dtoName) {
        try {
            String outputDir = "../Notification/src/main/java/com/example/notification/dto";
            String filePath = outputDir + "/" + dtoName + ".java";
            Path path = Paths.get(filePath);

            if (Files.exists(path)) {
                System.out.printf("⚠️ DTO already exists: %s%n", filePath);
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

            System.out.printf("✅ DTO generated from template: %s%n", filePath);

        } catch (Exception e) {
            System.out.println("❌ Error generating DTO: " + e.getMessage());
        }
    }

    private void addEnumConstantWithJavaParser(String newConstant) {
        try {
            String filePath = "../Notification/src/main/java/com/example/notification/util/enums/DeliveryMethod.java";
            Path path = Paths.get(filePath);
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

            // Check if the constant already exists
            if (enumDecl.getEntries().stream().anyMatch(e -> e.getNameAsString().equals(newConstant))) {
                System.out.printf("⚠️ %s already exists in DeliveryMethod enum.%n", newConstant);
                return;
            }

            // Max value'yu bul
            int max = enumDecl.getEntries().stream()
                    .map(e -> e.getArguments().getFirst()  // Optional<Expression>
                            .map(expr -> expr.asIntegerLiteralExpr().asNumber().intValue())
                            .orElse(-1)
                    )
                    .max(Integer::compareTo)
                    .orElse(-1);

            int newValue = max + 1;

            // create new enum constant
            EnumConstantDeclaration newEntry = new EnumConstantDeclaration(newConstant);
            newEntry.addArgument(Integer.toString(newValue));
            enumDecl.addEntry(newEntry);

            // write changes back to file
            Files.writeString(path, cu.toString());

            System.out.printf("✅ Added %s(%d) to DeliveryMethod enum.%n", newConstant, newValue);

        } catch (Exception e) {
            System.out.println("❌ Error using JavaParser: " + e.getMessage());
        }
    }

    private void addEnumToProto(String enumName) {
        try {
            // Resolve the proto file path relative to project root
            Path projectRoot = Paths.get("").toAbsolutePath();
            Path protoPath = projectRoot.resolve("Notification/src/main/proto/Register.proto");

            List<String> lines = Files.readAllLines(protoPath);
            List<String> updated = new ArrayList<>();
            boolean insideEnum = false;
            int maxValue = -1;
            boolean alreadyExists = false;

            for (String line : lines) {
                String trimmed = line.trim();

                // Detect enum declaration
                if (trimmed.startsWith("enum DeliveryMethod")) {
                    insideEnum = true;
                }

                // Check for existing enum entry
                if (insideEnum && trimmed.matches("^" + enumName + "\\s*=\\s*\\d+;")) {
                    alreadyExists = true;
                }

                // Find max existing enum value
                if (insideEnum && trimmed.matches("^[A-Z_]+\\s*=\\s*(\\d+);")) {
                    int value = Integer.parseInt(trimmed.replaceAll("[^0-9]", ""));
                    maxValue = Math.max(maxValue, value);
                }

                // Insert new enum before closing brace
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
            // Resolve proto paths relative to the project root
            Path projectRoot = Paths.get("").toAbsolutePath();
            String protoDir = projectRoot.resolve("Notification/src/main/proto").toString();
            String protoFile = protoDir + "/Register.proto";

            // Run protoc compiler
            ProcessBuilder pb = new ProcessBuilder(
                    "protoc",
                    "--java_out=" + protoDir,
                    "-I=" + protoDir,
                    protoFile
            );

            pb.inheritIO(); // Show output in terminal
            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("✅ Proto generation completed successfully.");
            } else {
                System.out.println("❌ Proto generation failed. Exit code: " + exitCode);
            }
        } catch (Exception e) {
            System.out.println("❌ Error during proto generation: " + e.getMessage());
        }
    }


}
