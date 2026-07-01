package fr.quotepart.infrastructure.bdpm;

import java.util.List;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

/**
 * Parse une ligne du fichier officiel CIS_bdpm.txt (tabulé, Windows-1252).
 * Colonnes utilisées : 0=CIS, 1=dénomination.
 */
@Component
public class ParseurSpecialiteBdpm {

    private static final int COLONNE_CIS = 0;
    private static final int COLONNE_DENOMINATION = 1;
    private static final int NOMBRE_COLONNES_MINIMUM = COLONNE_DENOMINATION + 1;

    public List<LigneSpecialiteBdpm> parseToutes(Stream<String> lignes) {
        return lignes.filter(ligne -> !ligne.isBlank())
                .map(this::parse)
                .toList();
    }

    public LigneSpecialiteBdpm parse(String ligne) {
        String[] champs = ligne.split("\t", -1);

        if (champs.length < NOMBRE_COLONNES_MINIMUM) {
            throw new LigneBdpmInvalideException(ligne, champs.length);
        }

        return new LigneSpecialiteBdpm(champs[COLONNE_CIS].trim(), champs[COLONNE_DENOMINATION].trim());
    }
}
