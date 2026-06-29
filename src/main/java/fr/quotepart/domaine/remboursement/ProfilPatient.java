package fr.quotepart.domaine.remboursement;

/**
 * Situation du patient influençant le calcul : respect du parcours de soins et statut ALD.
 */
public record ProfilPatient(boolean parcoursSoinsRespecte, boolean ald) {
}
