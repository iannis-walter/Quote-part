package fr.quotepart.infrastructure.bareme;

import fr.quotepart.domaine.medicament.Smr;

/**
 * Levée quand le barème configuré ne définit pas de taux pour un SMR donné.
 */
public class BaremeIncompletException extends RuntimeException {

    public BaremeIncompletException(Smr smr) {
        super("Aucun taux configuré dans le barème pour le SMR : " + smr);
    }
}
