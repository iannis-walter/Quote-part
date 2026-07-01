package fr.quotepart.application;

import fr.quotepart.domaine.medicament.Smr;
import java.math.BigDecimal;

/**
 * Modèle de lecture d'un médicament pour l'affichage (liste, détail).
 * Distinct de la présentation de domaine, qui sert au calcul.
 */
public record MedicamentResume(
        String cip13,
        String denomination,
        BigDecimal prix,
        boolean remboursable,
        Smr smr) {
}
