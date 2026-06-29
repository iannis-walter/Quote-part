package fr.quotepart.infrastructure.bdpm;

import fr.quotepart.domaine.medicament.CodeCip13;
import fr.quotepart.domaine.monnaie.Montant;
import fr.quotepart.domaine.remboursement.Taux;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

/**
 * Parse une ligne du fichier officiel CIS_CIP_bdpm.txt (tabulé, Windows-1252).
 * Colonnes utilisées : 0=CIS, 6=CIP13, 8=taux de remboursement, 9=prix.
 */
@Component
public class ParseurPresentationBdpm {

    private static final int COLONNE_CIS = 0;
    private static final int COLONNE_CIP13 = 6;
    private static final int COLONNE_TAUX = 8;
    private static final int COLONNE_PRIX = 9;
    private static final int NOMBRE_COLONNES_MINIMUM = COLONNE_PRIX + 1;

    public List<LignePresentationBdpm> parseToutes(Stream<String> lignes) {
        return lignes.filter(ligne -> !ligne.isBlank())
                .map(this::parse)
                .toList();
    }

    public LignePresentationBdpm parse(String ligne) {
        String[] champs = ligne.split("\t", -1);

        if (champs.length < NOMBRE_COLONNES_MINIMUM) {
            throw new LigneBdpmInvalideException(ligne, champs.length);
        }

        String codeCis = champs[COLONNE_CIS].trim();
        CodeCip13 codeCip13 = new CodeCip13(champs[COLONNE_CIP13].trim());
        Montant prix = parsePrix(champs[COLONNE_PRIX]);

        String tauxBrut = champs[COLONNE_TAUX].trim();
        boolean remboursable = !tauxBrut.isBlank();
        Taux taux = remboursable ? parseTaux(tauxBrut) : null;

        return new LignePresentationBdpm(codeCis, codeCip13, prix, taux, remboursable);
    }

    private Montant parsePrix(String brut) {
        return new Montant(new BigDecimal(brut.trim().replace(',', '.')));
    }

    private Taux parseTaux(String brut) {
        int pourcentage = Integer.parseInt(brut.replace("%", "").trim());
        return Taux.pourcent(pourcentage);
    }
}
