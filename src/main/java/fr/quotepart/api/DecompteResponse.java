package fr.quotepart.api;

import fr.quotepart.domaine.remboursement.Decompte;
import java.math.BigDecimal;

/**
 * Réponse détaillée d'un calcul de reste à charge, pour la transparence de chaque ligne.
 */
public record DecompteResponse(
        BigDecimal prix,
        BigDecimal baseRemboursement,
        int tauxPourcent,
        BigDecimal remboursementSecu,
        BigDecimal ticketModerateur,
        BigDecimal franchiseMedicale,
        BigDecimal resteACharge) {

    public static DecompteResponse depuis(Decompte decompte) {
        return new DecompteResponse(
                decompte.prix().valeur(),
                decompte.baseRemboursement().valeur(),
                decompte.tauxApplique().pourcentage(),
                decompte.remboursementSecu().valeur(),
                decompte.ticketModerateur().valeur(),
                decompte.franchiseMedicale().valeur(),
                decompte.resteACharge().valeur());
    }
}
