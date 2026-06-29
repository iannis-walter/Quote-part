package fr.quotepart.infrastructure.bdpm;

import static org.assertj.core.api.Assertions.assertThat;

import fr.quotepart.domaine.medicament.CodeCip13;
import fr.quotepart.domaine.monnaie.Montant;
import fr.quotepart.domaine.remboursement.Taux;
import org.junit.jupiter.api.Test;

class ParseurPresentationBdpmTest {

    private final ParseurPresentationBdpm parseur = new ParseurPresentationBdpm();

    @Test
    void parse_une_ligne_de_presentation_remboursable() {
        // Format CIS_CIP_bdpm.txt : CIS \t CIP7 \t libellé \t statut \t état \t date \t CIP13 \t agrément \t taux \t prix \t prixTotal \t honoraire \t indications
        String ligne = "61266250\t2009551\tplaquette PVC\tactive\tcommercialisée\t12/05/2010"
                + "\t3400920095517\toui\t65 %\t2,11\t2,11\t0,00\tindications";

        LignePresentationBdpm presentation = parseur.parse(ligne);

        assertThat(presentation.codeCip13()).isEqualTo(new CodeCip13("3400920095517"));
        assertThat(presentation.prix()).isEqualTo(Montant.euros("2.11"));
        assertThat(presentation.tauxRemboursement()).isEqualTo(Taux.pourcent(65));
        assertThat(presentation.remboursable()).isTrue();
    }

    @Test
    void parse_une_ligne_non_remboursable_quand_le_taux_est_absent() {
        String ligne = "61266250\t2009551\tplaquette PVC\tactive\tcommercialisée\t12/05/2010"
                + "\t3400920095517\tnon\t\t8,50\t8,50\t0,00\tindications";

        LignePresentationBdpm presentation = parseur.parse(ligne);

        assertThat(presentation.remboursable()).isFalse();
        assertThat(presentation.tauxRemboursement()).isNull();
        assertThat(presentation.prix()).isEqualTo(Montant.euros("8.50"));
    }
}
