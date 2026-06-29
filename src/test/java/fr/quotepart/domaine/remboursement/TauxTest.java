package fr.quotepart.domaine.remboursement;

import static org.assertj.core.api.Assertions.assertThat;

import fr.quotepart.domaine.monnaie.Montant;
import org.junit.jupiter.api.Test;

class TauxTest {

    @Test
    void applique_un_pourcentage_a_un_montant() {
        Taux taux = Taux.pourcent(65);

        assertThat(taux.appliquerA(Montant.euros("10.00"))).isEqualTo(Montant.euros("6.50"));
    }
}
