package fr.quotepart.domaine.remboursement;

/**
 * Situation du patient influençant le calcul.
 *
 * @param parcoursSoinsRespecte respect du parcours de soins coordonné
 * @param ald                   prise en charge à 100 % (ALD, maternité, invalidité)
 * @param c2s                   bénéficiaire de la Complémentaire santé solidaire → aucun reste à charge
 * @param regimeLocal           régime local d'Alsace-Moselle → remboursement complété
 */
public record ProfilPatient(
        boolean parcoursSoinsRespecte,
        boolean ald,
        boolean c2s,
        boolean regimeLocal) {

    public ProfilPatient(boolean parcoursSoinsRespecte, boolean ald) {
        this(parcoursSoinsRespecte, ald, false, false);
    }
}
