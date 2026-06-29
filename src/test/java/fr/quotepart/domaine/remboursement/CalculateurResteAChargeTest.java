package fr.quotepart.domaine.remboursement;

import static org.assertj.core.api.Assertions.assertThat;

import fr.quotepart.domaine.medicament.CodeCip13;
import fr.quotepart.domaine.medicament.Presentation;
import fr.quotepart.domaine.medicament.Smr;
import fr.quotepart.domaine.monnaie.Montant;
import org.junit.jupiter.api.Test;

class CalculateurResteAChargeTest {

    private final CalculateurResteACharge calcul = new CalculateurResteACharge();
    private final Bareme bareme = new BaremeDeTest();

    @Test
    void smr_important_dans_le_parcours_de_soins() {
        Presentation presentation = new Presentation(
                new CodeCip13("3400930000000"),
                Montant.euros("10.00"),   // prix
                Montant.euros("10.00"),   // base de remboursement
                true,                     // remboursable
                Smr.IMPORTANT);

        Decompte decompte = calcul.calculer(presentation, new ProfilPatient(true, false), bareme);

        assertThat(decompte.tauxApplique()).isEqualTo(Taux.pourcent(65));
        assertThat(decompte.remboursementSecu()).isEqualTo(Montant.euros("6.50"));
        assertThat(decompte.resteACharge()).isEqualTo(Montant.euros("4.50")); // 3,50 TM + 1,00 franchise
    }
}
