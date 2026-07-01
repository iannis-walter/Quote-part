package fr.quotepart.api;

import fr.quotepart.domaine.monnaie.Montant;
import fr.quotepart.domaine.remboursement.Decompte;
import java.math.BigDecimal;

/**
 * Réponse détaillée d'un calcul de reste à charge, pour la transparence de chaque ligne.
 * {@code resteApresComplementaire} est renseigné seulement si une complémentaire a été fournie.
 */
public record DecompteResponse(
        BigDecimal prix,
        BigDecimal baseRemboursement,
        int tauxPourcent,
        BigDecimal remboursementSecu,
        BigDecimal ticketModerateur,
        BigDecimal franchiseMedicale,
        BigDecimal resteACharge,
        BigDecimal resteApresComplementaire) {

    public static DecompteResponse depuis(Decompte decompte) {
        return depuis(decompte, null);
    }

    public static DecompteResponse depuis(Decompte decompte, Montant resteApresComplementaire) {
        return new DecompteResponse(
                decompte.prix().valeur(),
                decompte.baseRemboursement().valeur(),
                decompte.tauxApplique().pourcentage(),
                decompte.remboursementSecu().valeur(),
                decompte.ticketModerateur().valeur(),
                decompte.franchiseMedicale().valeur(),
                decompte.resteACharge().valeur(),
                resteApresComplementaire != null ? resteApresComplementaire.valeur() : null);
    }
}
