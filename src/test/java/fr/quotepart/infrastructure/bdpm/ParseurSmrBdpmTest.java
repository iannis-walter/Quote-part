package fr.quotepart.infrastructure.bdpm;

import static org.assertj.core.api.Assertions.assertThat;

import fr.quotepart.domaine.medicament.Smr;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ParseurSmrBdpmTest {

    private final ParseurSmrBdpm parseur = new ParseurSmrBdpm();

    @Test
    void parse_une_ligne_smr() {
        // Format CIS_HAS_SMR_bdpm.txt : CIS \t codeHAS \t motif \t date \t valeurSMR \t libellé
        String ligne = "61266250\tCT-12345\tInscription\t20100512\tImportant\tLe SMR est important";

        LigneSmrBdpm smr = parseur.parse(ligne);

        assertThat(smr.codeCis()).isEqualTo("61266250");
        assertThat(smr.smr()).isEqualTo(Smr.IMPORTANT);
    }

    @ParameterizedTest
    @CsvSource({
            "Important,        IMPORTANT",
            "Modéré,           MODERE",
            "Faible,           FAIBLE",
            "Insuffisant,      INSUFFISANT"
    })
    void mappe_chaque_valeur_de_smr(String valeurBdpm, Smr attendu) {
        String ligne = "61266250\tCT-1\tInscription\t20100512\t" + valeurBdpm + "\tlibellé";

        assertThat(parseur.parse(ligne).smr()).isEqualTo(attendu);
    }
}
