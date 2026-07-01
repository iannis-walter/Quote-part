package fr.quotepart.infrastructure.bdpm;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class ParseurSpecialiteBdpmTest {

    private final ParseurSpecialiteBdpm parseur = new ParseurSpecialiteBdpm();

    @Test
    void parse_le_code_et_la_denomination() {
        // Format CIS_bdpm.txt : CIS \t dénomination \t forme \t voies \t ...
        String ligne = "61266250\tDOLIPRANE 1000 mg, comprimé\tcomprimé\torale\tAMM active";

        LigneSpecialiteBdpm specialite = parseur.parse(ligne);

        assertThat(specialite.codeCis()).isEqualTo("61266250");
        assertThat(specialite.denomination()).isEqualTo("DOLIPRANE 1000 mg, comprimé");
    }

    @Test
    void parse_un_flux_en_ignorant_les_lignes_vides() {
        String ligne1 = "61266250\tDOLIPRANE 1000 mg, comprimé\tcomprimé";
        String ligne2 = "61266251\tAMOXICILLINE 500 mg, gélule\tgélule";

        List<LigneSpecialiteBdpm> resultat = parseur.parseToutes(Stream.of(ligne1, "", ligne2));

        assertThat(resultat).extracting(LigneSpecialiteBdpm::denomination)
                .containsExactly("DOLIPRANE 1000 mg, comprimé", "AMOXICILLINE 500 mg, gélule");
    }
}
