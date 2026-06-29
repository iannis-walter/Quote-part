package fr.quotepart.domaine.remboursement;

import fr.quotepart.domaine.monnaie.Montant;
import java.math.BigDecimal;

public record Taux(BigDecimal fraction) {

    public static Taux pourcent(int pourcentage) {
        return new Taux(BigDecimal.valueOf(pourcentage).movePointLeft(2));
    }

    public Montant appliquerA(Montant base) {
        return new Montant(base.valeur().multiply(fraction));
    }
}
