package com.example.developertools;

import com.example.developertools.cli.IsoCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
@RequiredArgsConstructor
public class IsoCliBootstrap implements CommandLineRunner {

    private final IsoCommand isoCommand;
    private final CommandLine.IFactory factory;
    @Override
    public void run(String... args) {
        int exitCode = new CommandLine(isoCommand, factory).execute(args);
        System.exit(exitCode);

    }
}
