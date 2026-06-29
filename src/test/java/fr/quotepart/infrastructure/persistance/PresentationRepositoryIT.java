package fr.quotepart.infrastructure.persistance;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PresentationRepositoryIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private PresentationRepository repository;

    @Test
    void enregistre_et_relit_une_presentation() {
        repository.save(new PresentationEntity("3400920095517", "61266250", new BigDecimal("2.11"), 65, true));

        Optional<PresentationEntity> relu = repository.findById("3400920095517");

        assertThat(relu).isPresent();
        assertThat(relu.get().getCis()).isEqualTo("61266250");
        assertThat(relu.get().getPrix()).isEqualByComparingTo("2.11");
        assertThat(relu.get().isRemboursable()).isTrue();
    }
}
