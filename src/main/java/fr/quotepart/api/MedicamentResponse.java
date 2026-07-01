package fr.quotepart.api;

import fr.quotepart.application.MedicamentResume;
import fr.quotepart.domaine.medicament.Smr;
import java.math.BigDecimal;

/**
 * Détail d'un médicament exposé par l'API.
 */
public record MedicamentResponse(
        String cip13,
        String denomination,
        BigDecimal prix,
        boolean remboursable,
        Smr smr) {

    public static MedicamentResponse depuis(MedicamentResume resume) {
        return new MedicamentResponse(
                resume.cip13(),
                resume.denomination(),
                resume.prix(),
                resume.remboursable(),
                resume.smr());
    }
}
