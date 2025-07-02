package com.example.developertools.cli;

import com.example.developertools.services.impl.StorageScaffoldGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.util.HashMap;
import java.util.Map;

@CommandLine.Command(name = "storage", description = "Generate storage scaffold")
@Component
@RequiredArgsConstructor
public class StorageCommand implements Runnable {

    private final StorageScaffoldGenerator generator;

    @CommandLine.Parameters(index = "0", description = "Storage name (e.g., GoogleDrive)")
    private String name;

    @Override
    public void run() {
        // Prepare args for generator
        Map<String, String> args = new HashMap<>();
        args.put("name", name);

        // Invoke scaffold generator
        generator.generate(args);
    }
}
