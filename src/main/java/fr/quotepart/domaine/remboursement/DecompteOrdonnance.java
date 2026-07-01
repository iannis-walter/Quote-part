package fr.quotepart.domaine.remboursement;

import fr.quotepart.domaine.monnaie.Montant;
import java.util.List;

/**
 * Résultat du calcul d'une ordonnance : le détail de chaque ligne, la franchise réellement
 * appliquée (après plafonnement annuel) et le reste à charge total.
 */
public record DecompteOrdonnance(
        List<Decompte> lignes,
        Montant franchiseAppliquee,
        Montant totalResteACharge) {
}
