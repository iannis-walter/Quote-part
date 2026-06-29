package fr.quotepart.infrastructure.bdpm;

import fr.quotepart.domaine.medicament.CodeCip13;
import fr.quotepart.domaine.monnaie.Montant;
import fr.quotepart.domaine.remboursement.Taux;

/**
 * Représentation brute d'une ligne du fichier CIS_CIP_bdpm.txt, après parsing.
 * {@code tauxRemboursement} est null quand la présentation n'est pas remboursable.
 */
public record LignePresentationBdpm(
        String codeCis,
        CodeCip13 codeCip13,
        Montant prix,
        Taux tauxRemboursement,
        boolean remboursable) {
}
