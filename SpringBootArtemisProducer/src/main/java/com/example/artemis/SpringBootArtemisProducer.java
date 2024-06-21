package com.example.artemis;
import com.example.artemis.model.Email;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@SpringBootApplication
public class SpringBootArtemisProducer {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootArtemisProducer.class, args);
    }
    private RestTemplate template;
    public SpringBootArtemisProducer(RestTemplate restTemplate) {
        this.template = restTemplate;
    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            int count = 0;
            while (true) {
                template.postForEntity("http://localhost:8080/email", new Email("info@google.com", "Hi google " + count++ + " at " + Instant.now()), String.class);
                Thread.sleep(10000);
            }
        };
    }
}
