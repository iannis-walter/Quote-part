package fr.quotepart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import fr.quotepart.domaine.medicament.CodeCip13;
import fr.quotepart.domaine.medicament.Smr;
import fr.quotepart.domaine.monnaie.Montant;
import fr.quotepart.domaine.remboursement.Decompte;
import fr.quotepart.domaine.remboursement.ProfilPatient;
import fr.quotepart.domaine.remboursement.Taux;
import fr.quotepart.infrastructure.persistance.PresentationEntity;
import fr.quotepart.infrastructure.persistance.PresentationRepository;
import fr.quotepart.infrastructure.persistance.SpecialiteEntity;
import fr.quotepart.infrastructure.persistance.SpecialiteRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class CalculerResteAChargeIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private CalculerResteACharge calculerResteACharge;

    @Autowired
    private PresentationRepository presentations;

    @Autowired
    private SpecialiteRepository specialites;

    @Test
    void calcule_le_reste_a_charge_sur_donnees_persistees() {
        specialites.save(new SpecialiteEntity("61266250", Smr.IMPORTANT));
        presentations.save(new PresentationEntity("3400920095517", "61266250", new BigDecimal("10.00"), 65, true));

        Decompte decompte = calculerResteACharge.executer(
                new CodeCip13("3400920095517"), new ProfilPatient(true, false));

        assertThat(decompte.tauxApplique()).isEqualTo(Taux.pourcent(65));
        assertThat(decompte.resteACharge()).isEqualTo(Montant.euros("4.50")); // 3,50 TM + 1,00 franchise
    }

    @Test
    void applique_la_base_de_remboursement_stockee_pour_le_depassement() {
        specialites.save(new SpecialiteEntity("61266250", Smr.IMPORTANT));
        PresentationEntity generique =
                new PresentationEntity("3400920095599", "61266250", new BigDecimal("3.45"), 65, true);
        generique.setBaseRemboursement(new BigDecimal("3.00")); // tarif forfaitaire < prix
        presentations.save(generique);

        Decompte decompte = calculerResteACharge.executer(
                new CodeCip13("3400920095599"), new ProfilPatient(true, false));

        assertThat(decompte.remboursementSecu()).isEqualTo(Montant.euros("1.95")); // 3,00 × 65%
        assertThat(decompte.resteACharge()).isEqualTo(Montant.euros("2.50")); // 3,45 − 1,95 + 1,00
    }

    @Test
    void echoue_si_le_medicament_est_introuvable() {
        assertThatThrownBy(() -> calculerResteACharge.executer(
                new CodeCip13("0000000000000"), new ProfilPatient(true, false)))
                .isInstanceOf(MedicamentIntrouvableException.class);
    }
}
