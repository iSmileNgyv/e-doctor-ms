package com.example.developertools.cli;

import com.example.developertools.services.impl.NotificationScaffoldGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.util.HashMap;
import java.util.Map;

@CommandLine.Command(name = "notification", description = "Generate notification scaffold")
@Component
@RequiredArgsConstructor
public class NotificationCommand implements Runnable {

    private final NotificationScaffoldGenerator generator;

    @CommandLine.Parameters(index = "0", description = "Notification name (e.g., Telegram)")
    private String name;

    @CommandLine.Parameters(index = "1", arity = "0..1", description = "Optional keyword: request or response")
    private String flag1;

    @CommandLine.Parameters(index = "2", arity = "0..1", description = "Optional keyword: request or response")
    private String flag2;

    @Override
    public void run() {
        Map<String, String> args = new HashMap<>();
        args.put("name", name);

        if ("request".equalsIgnoreCase(flag1)) {
            args.put("request", name + "NotificationRequestDto");
        } else if ("response".equalsIgnoreCase(flag1)) {
            args.put("response", name + "NotificationResponseDto");
        }

        if ("request".equalsIgnoreCase(flag2)) {
            args.put("request", name + "NotificationRequestDto");
        } else if ("response".equalsIgnoreCase(flag2)) {
            args.put("response", name + "NotificationResponseDto");
        }

        generator.generate(args);
    }
}
