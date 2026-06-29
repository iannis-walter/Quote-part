package fr.quotepart.infrastructure.bdpm;

import static org.assertj.core.api.Assertions.assertThat;

import fr.quotepart.domaine.medicament.CodeCip13;
import fr.quotepart.domaine.monnaie.Montant;
import fr.quotepart.domaine.remboursement.Taux;
import fr.quotepart.infrastructure.persistance.PresentationRepository;
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
@Import(IngestionPresentationsBdpm.class)
class IngestionPresentationsBdpmIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private IngestionPresentationsBdpm ingestion;

    @Autowired
    private PresentationRepository repository;

    @Test
    void reingerer_les_memes_lignes_ne_duplique_pas_et_met_a_jour() {
        ingestion.enregistrer(List.of(
                new LignePresentationBdpm("61266250", new CodeCip13("3400920095517"), Montant.euros("2.11"), Taux.pourcent(65), true)));

        // même CIP13, prix modifié → mise à jour, pas de doublon
        ingestion.enregistrer(List.of(
                new LignePresentationBdpm("61266250", new CodeCip13("3400920095517"), Montant.euros("2.50"), Taux.pourcent(65), true)));

        assertThat(repository.count()).isEqualTo(1);
        assertThat(repository.findById("3400920095517")).get()
                .extracting(presentation -> presentation.getPrix())
                .isEqualTo(new java.math.BigDecimal("2.50"));
    }
}
