package fr.quotepart.infrastructure.bdpm;

import static org.assertj.core.api.Assertions.assertThat;

import fr.quotepart.domaine.medicament.Smr;
import fr.quotepart.infrastructure.persistance.SpecialiteEntity;
import fr.quotepart.infrastructure.persistance.SpecialiteRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(IngestionSmrBdpm.class)
class IngestionSmrBdpmIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private IngestionSmrBdpm ingestion;

    @Autowired
    private SpecialiteRepository repository;

    @Test
    void reingerer_le_meme_smr_ne_duplique_pas_et_met_a_jour() {
        ingestion.enregistrer(List.of(new LigneSmrBdpm("61266250", Smr.MODERE)));

        // même CIS, SMR réévalué → mise à jour, pas de doublon
        ingestion.enregistrer(List.of(new LigneSmrBdpm("61266250", Smr.IMPORTANT)));

        assertThat(repository.count()).isEqualTo(1);
        assertThat(repository.findById("61266250")).get()
                .extracting(SpecialiteEntity::getSmr)
                .isEqualTo(Smr.IMPORTANT);
    }
}
