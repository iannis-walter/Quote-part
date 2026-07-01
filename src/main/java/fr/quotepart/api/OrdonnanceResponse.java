package fr.quotepart.api;

import fr.quotepart.domaine.remboursement.DecompteOrdonnance;
import java.math.BigDecimal;
import java.util.List;

/**
 * Réponse du calcul d'une ordonnance : le détail par ligne, la franchise appliquée (après
 * plafonnement) et le reste à charge total.
 */
public record OrdonnanceResponse(
        List<DecompteResponse> lignes,
        BigDecimal franchiseAppliquee,
        BigDecimal totalResteACharge) {

    public static OrdonnanceResponse depuis(DecompteOrdonnance decompte) {
        return new OrdonnanceResponse(
                decompte.lignes().stream().map(DecompteResponse::depuis).toList(),
                decompte.franchiseAppliquee().valeur(),
                decompte.totalResteACharge().valeur());
    }
}
