package fr.quotepart.infrastructure.bdpm;

import fr.quotepart.domaine.medicament.Smr;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

/**
 * Parse une ligne du fichier officiel CIS_HAS_SMR_bdpm.txt (tabulé, Windows-1252).
 * Colonnes utilisées : 0=CIS, 4=valeur du SMR.
 */
@Component
public class ParseurSmrBdpm {

    private static final int COLONNE_CIS = 0;
    private static final int COLONNE_VALEUR_SMR = 4;
    private static final int NOMBRE_COLONNES_MINIMUM = COLONNE_VALEUR_SMR + 1;

    public List<LigneSmrBdpm> parseToutes(Stream<String> lignes) {
        return lignes.filter(ligne -> !ligne.isBlank())
                .map(this::parse)
                .toList();
    }

    public LigneSmrBdpm parse(String ligne) {
        String[] champs = ligne.split("\t", -1);

        if (champs.length < NOMBRE_COLONNES_MINIMUM) {
            throw new LigneBdpmInvalideException(ligne, champs.length);
        }

        return new LigneSmrBdpm(champs[COLONNE_CIS].trim(), versSmr(champs[COLONNE_VALEUR_SMR].trim()));
    }

    private Smr versSmr(String valeur) {
        return switch (valeur) {
            case "Important" -> Smr.IMPORTANT;
            case "Modéré" -> Smr.MODERE;
            case "Faible" -> Smr.FAIBLE;
            case "Insuffisant" -> Smr.INSUFFISANT;
            default -> throw new IllegalArgumentException("Valeur de SMR inconnue : " + valeur);
        };
    }
}
