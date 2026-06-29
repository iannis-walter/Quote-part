package fr.quotepart.domaine.remboursement;

import fr.quotepart.domaine.medicament.CodeCip13;

/**
 * Levée quand une donnée indispensable au calcul est absente.
 * On préfère échouer explicitement plutôt que produire un reste à charge silencieusement faux.
 */
public class DonneesMedicamentIncompletesException extends RuntimeException {

    public DonneesMedicamentIncompletesException(CodeCip13 code, String detail) {
        super("Données incomplètes pour la présentation " + code.valeur() + " : " + detail);
    }
}
