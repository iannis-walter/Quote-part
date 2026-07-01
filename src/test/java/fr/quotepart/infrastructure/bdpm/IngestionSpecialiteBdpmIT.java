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
@Import({IngestionSpecialiteBdpm.class, IngestionSmrBdpm.class})
class IngestionSpecialiteBdpmIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private IngestionSpecialiteBdpm ingestionSpecialite;

    @Autowired
    private IngestionSmrBdpm ingestionSmr;

    @Autowired
    private SpecialiteRepository repository;

    @Test
    void fusionne_denomination_et_smr_sur_la_meme_specialite() {
        ingestionSpecialite.enregistrer(List.of(new LigneSpecialiteBdpm("61266250", "DOLIPRANE 1000 mg, comprimé")));
        ingestionSmr.enregistrer(List.of(new LigneSmrBdpm("61266250", Smr.IMPORTANT)));

        SpecialiteEntity specialite = repository.findById("61266250").orElseThrow();
        assertThat(specialite.getDenomination()).isEqualTo("DOLIPRANE 1000 mg, comprimé");
        assertThat(specialite.getSmr()).isEqualTo(Smr.IMPORTANT);
        assertThat(repository.count()).isEqualTo(1);
    }
}
