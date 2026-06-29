package fr.quotepart.domaine.monnaie;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Montant(BigDecimal valeur) {

    private static final int DECIMALES = 2;
    private static final RoundingMode ARRONDI = RoundingMode.HALF_UP;

    public Montant {
        valeur = valeur.setScale(DECIMALES, ARRONDI);
    }

    public static Montant euros(String valeur) {
        return new Montant(new BigDecimal(valeur));
    }
}
