package fr.quotepart.infrastructure.bdpm;

import static org.assertj.core.api.Assertions.assertThat;

import fr.quotepart.infrastructure.persistance.PresentationRepository;
import fr.quotepart.infrastructure.persistance.SpecialiteEntity;
import fr.quotepart.infrastructure.persistance.SpecialiteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@TestPropertySource(properties = "quotepart.demo.amorcer=true")
class AmorceDonneesDemoIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private PresentationRepository presentations;

    @Autowired
    private SpecialiteRepository specialites;

    @Test
    void amorce_l_echantillon_bdpm_au_demarrage() {
        assertThat(presentations.count()).isGreaterThan(0);
        assertThat(specialites.findById("61266250"))
                .get()
                .extracting(SpecialiteEntity::getDenomination).asString()
                .contains("DOLIPRANE");
    }
}
