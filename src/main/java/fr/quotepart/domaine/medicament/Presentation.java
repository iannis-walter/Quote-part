package fr.quotepart.domaine.medicament;

import fr.quotepart.domaine.monnaie.Montant;

/**
 * Conditionnement commercialisé d'une spécialité (la boîte). Porte le prix et la base de remboursement.
 */
public record Presentation(
        CodeCip13 code,
        Montant prix,
        Montant baseRemboursement,
        boolean remboursable,
        Smr smr) {
}
