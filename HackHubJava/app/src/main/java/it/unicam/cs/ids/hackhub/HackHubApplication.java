package it.unicam.cs.ids.hackhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Spring Boot entry point and component-scan root for the HackHub application. */
@SpringBootApplication
public class HackHubApplication {
    public static void main(String[] args) {
        SpringApplication.run(HackHubApplication.class, args);
    }
}
