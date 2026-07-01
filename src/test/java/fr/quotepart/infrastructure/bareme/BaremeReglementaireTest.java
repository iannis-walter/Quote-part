package fr.quotepart.infrastructure.bareme;

import static org.assertj.core.api.Assertions.assertThat;

import fr.quotepart.domaine.medicament.Smr;
import fr.quotepart.domaine.monnaie.Montant;
import fr.quotepart.domaine.remboursement.Bareme;
import fr.quotepart.domaine.remboursement.Coefficient;
import fr.quotepart.domaine.remboursement.Taux;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.Test;

class BaremeReglementaireTest {

    private final BaremeProperties properties = new BaremeProperties(
            LocalDate.of(2024, 1, 1),
            Map.of(Smr.IMPORTANT, 65, Smr.MODERE, 30, Smr.FAIBLE, 15, Smr.INSUFFISANT, 0),
            new BigDecimal("0.60"),
            90,
            new BigDecimal("1.00"));

    private final Bareme bareme = new BaremeReglementaire(properties);

    @Test
    void fournit_le_taux_selon_le_smr_depuis_la_configuration() {
        assertThat(bareme.tauxPour(Smr.IMPORTANT)).isEqualTo(Taux.pourcent(65));
        assertThat(bareme.tauxPour(Smr.MODERE)).isEqualTo(Taux.pourcent(30));
        assertThat(bareme.tauxPour(Smr.FAIBLE)).isEqualTo(Taux.pourcent(15));
        assertThat(bareme.tauxPour(Smr.INSUFFISANT)).isEqualTo(Taux.pourcent(0));
    }

    @Test
    void fournit_le_coefficient_hors_parcours_et_la_franchise_depuis_la_configuration() {
        assertThat(bareme.coefficientHorsParcours()).isEqualTo(new Coefficient(new BigDecimal("0.60")));
        assertThat(bareme.franchiseMedicaleParBoite()).isEqualTo(Montant.euros("1.00"));
    }
}
