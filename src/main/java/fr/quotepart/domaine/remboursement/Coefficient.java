package fr.quotepart.domaine.remboursement;

import fr.quotepart.domaine.monnaie.Montant;
import java.math.BigDecimal;

/**
 * Coefficient multiplicateur appliqué au remboursement (ex. minoration hors parcours de soins).
 */
public record Coefficient(BigDecimal valeur) {

    public static final Coefficient PLEIN = new Coefficient(BigDecimal.ONE);

    public Montant appliquerA(Montant montant) {
        return new Montant(montant.valeur().multiply(valeur));
    }
}
