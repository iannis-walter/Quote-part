package fr.quotepart;

import fr.quotepart.domaine.remboursement.CalculateurOrdonnance;
import fr.quotepart.domaine.remboursement.CalculateurResteACharge;
import java.time.Clock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ConfigurationPropertiesScan
public class QuotePartApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuotePartApplication.class, args);
    }

    @Bean
    Clock horloge() {
        return Clock.systemDefaultZone();
    }

    @Bean
    CalculateurResteACharge calculateurResteACharge() {
        return new CalculateurResteACharge();
    }

    @Bean
    CalculateurOrdonnance calculateurOrdonnance(CalculateurResteACharge calculateurResteACharge) {
        return new CalculateurOrdonnance(calculateurResteACharge);
    }
}
