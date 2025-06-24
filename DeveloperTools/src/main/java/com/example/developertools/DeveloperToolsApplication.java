package com.example.developertools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.spring.boot.autoconfigure.PicocliAutoConfiguration;

@SpringBootApplication
@ImportAutoConfiguration(PicocliAutoConfiguration.class)
public class DeveloperToolsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeveloperToolsApplication.class, args);
    }

}
