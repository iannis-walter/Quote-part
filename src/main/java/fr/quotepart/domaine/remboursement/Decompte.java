package fr.quotepart.domaine.remboursement;

import fr.quotepart.domaine.monnaie.Montant;

/**
 * Résultat détaillé et transparent d'un calcul de reste à charge.
 */
public record Decompte(
        Montant prix,
        Montant baseRemboursement,
        Taux tauxApplique,
        Montant remboursementSecu,
        Montant ticketModerateur,
        Montant franchiseMedicale,
        Montant resteACharge) {
}
