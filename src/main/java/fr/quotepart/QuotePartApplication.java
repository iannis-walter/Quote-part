package fr.quotepart;

import java.time.Clock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class QuotePartApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuotePartApplication.class, args);
    }

    @Bean
    Clock horloge() {
        return Clock.systemDefaultZone();
    }
}
