package fr.quotepart.domaine.monnaie;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Montant(BigDecimal valeur) {

    private static final int DECIMALES = 2;
    private static final RoundingMode ARRONDI = RoundingMode.HALF_UP;

    public Montant {
        valeur = valeur.setScale(DECIMALES, ARRONDI);
        if (valeur.signum() < 0) {
            throw new MontantNegatifException(valeur);
        }
    }

    public static final Montant ZERO = Montant.euros("0");

    public static Montant euros(String valeur) {
        return new Montant(new BigDecimal(valeur));
    }

    public Montant plus(Montant autre) {
        return new Montant(valeur.add(autre.valeur));
    }

    public Montant moins(Montant autre) {
        return new Montant(valeur.subtract(autre.valeur));
    }

    public boolean estSuperieurA(Montant autre) {
        return valeur.compareTo(autre.valeur) > 0;
    }
}
