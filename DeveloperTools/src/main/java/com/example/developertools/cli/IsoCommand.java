package com.example.developertools.cli;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;

@Component
@Command(
        name = "iso",
        subcommands = { NewCommand.class },
        description = "Developer CLI Tool",
        mixinStandardHelpOptions = true
)
@RequiredArgsConstructor
public class IsoCommand implements Runnable {

    @Override
    public void run() {
        System.out.println("🛠 iso CLI works");
    }
}
