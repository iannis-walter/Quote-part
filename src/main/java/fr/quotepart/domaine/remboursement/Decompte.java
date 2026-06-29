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

    /** Médicament non remboursable : tout le prix reste à charge, aucune franchise. */
    static Decompte nonRemboursable(Montant prix) {
        return new Decompte(prix, prix, Taux.pourcent(0), Montant.ZERO, prix, Montant.ZERO, prix);
    }
}
