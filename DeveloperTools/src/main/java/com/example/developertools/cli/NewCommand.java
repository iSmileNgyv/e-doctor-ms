package com.example.developertools.cli;

import org.springframework.stereotype.Component;
import picocli.CommandLine;
import java.util.List;

@CommandLine.Command(name = "new", description = "Generate scaffolds", subcommands = {
        NotificationCommand.class,
        StorageCommand.class
})
@Component
public class NewCommand implements Runnable {
    @CommandLine.Unmatched
    List<String> unmatchedArgs;
    @Override
    public void run() {
        if (unmatchedArgs != null && !unmatchedArgs.isEmpty()) {
            System.out.println("❌ Unknown command: " + String.join(" ", unmatchedArgs));
            System.out.println("✅ Example: iso new notification Telegram");
            System.out.println("👉 Available generators: notification");
        } else {
            System.out.println("ℹ️ Usage: iso new [TYPE] [ARGS]");
            System.out.println("👉 Available generators: notification");
        }
    }
}
