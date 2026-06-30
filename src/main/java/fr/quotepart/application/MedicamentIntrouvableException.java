package fr.quotepart.application;

import fr.quotepart.domaine.medicament.CodeCip13;

/**
 * Levée quand aucune présentation ne correspond au code demandé.
 */
public class MedicamentIntrouvableException extends RuntimeException {

    public MedicamentIntrouvableException(CodeCip13 code) {
        super("Médicament introuvable pour le code : " + code.valeur());
    }
}
