package fr.quotepart.domaine.remboursement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    void patient_en_ald_pris_en_charge_a_100_pourcent() {
        Presentation presentation = new Presentation(
                new CodeCip13("3400930000001"),
                Montant.euros("20.00"),
                Montant.euros("20.00"),
                true,
                Smr.IMPORTANT);

        Decompte decompte = calcul.calculer(presentation, new ProfilPatient(true, true), bareme);

        assertThat(decompte.tauxApplique()).isEqualTo(Taux.pourcent(100));
        assertThat(decompte.resteACharge()).isEqualTo(Montant.euros("1.00")); // franchise seule
    }

    @Test
    void medicament_non_remboursable_reste_a_charge_egal_au_prix() {
        Presentation presentation = new Presentation(
                new CodeCip13("3400930000002"),
                Montant.euros("8.00"),
                Montant.euros("8.00"),
                false,                    // non remboursable
                Smr.IMPORTANT);

        Decompte decompte = calcul.calculer(presentation, new ProfilPatient(true, false), bareme);

        assertThat(decompte.resteACharge()).isEqualTo(Montant.euros("8.00")); // prix entier, aucune franchise
    }

    @Test
    void hors_parcours_de_soins_le_remboursement_est_minore() {
        Presentation presentation = new Presentation(
                new CodeCip13("3400930000003"),
                Montant.euros("10.00"),
                Montant.euros("10.00"),
                true,
                Smr.IMPORTANT);

        Decompte decompte = calcul.calculer(presentation, new ProfilPatient(false, false), bareme);

        // coefficient hors parcours = 0,60 → remboursement 6,50 × 0,60 = 3,90 ; reste 10 − 3,90 + 1 = 7,10
        assertThat(decompte.remboursementSecu()).isEqualTo(Montant.euros("3.90"));
        assertThat(decompte.resteACharge()).isEqualTo(Montant.euros("7.10"));
    }

    @Test
    void depassement_le_surcout_au_dela_de_la_base_reste_a_charge() {
        Presentation presentation = new Presentation(
                new CodeCip13("3400930000004"),
                Montant.euros("15.00"),   // prix > base (ex. générique à tarif forfaitaire)
                Montant.euros("10.00"),   // base de remboursement
                true,
                Smr.IMPORTANT);

        Decompte decompte = calcul.calculer(presentation, new ProfilPatient(true, false), bareme);

        // remboursement calculé sur la base : 10 × 65% = 6,50 ; reste = 15 − 6,50 + 1 = 9,50 (dont 5 € de dépassement)
        assertThat(decompte.remboursementSecu()).isEqualTo(Montant.euros("6.50"));
        assertThat(decompte.resteACharge()).isEqualTo(Montant.euros("9.50"));
    }

    @Test
    void smr_absent_leve_une_erreur_metier_explicite() {
        Presentation presentation = new Presentation(
                new CodeCip13("3400930000005"),
                Montant.euros("10.00"),
                Montant.euros("10.00"),
                true,
                null);                    // SMR manquant → impossible de déterminer le taux

        assertThatThrownBy(() -> calcul.calculer(presentation, new ProfilPatient(true, false), bareme))
                .isInstanceOf(DonneesMedicamentIncompletesException.class);
    }
}
