package fr.quotepart.domaine.remboursement;

import fr.quotepart.domaine.monnaie.Montant;

/**
 * Complémentaire santé (mutuelle) : prend en charge une part du ticket modérateur.
 * La franchise médicale n'est jamais couverte par une complémentaire.
 */
public record Complementaire(Taux tauxTicketModerateur) {

    /** Reste à charge réel du patient une fois la complémentaire appliquée. */
    public Montant resteApres(Decompte decompte) {
        Montant priseEnCharge = tauxTicketModerateur.appliquerA(decompte.ticketModerateur());
        return decompte.resteACharge().moins(priseEnCharge);
    }
}
