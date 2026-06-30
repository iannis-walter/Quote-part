package fr.quotepart.api;

import fr.quotepart.domaine.medicament.Presentation;
import fr.quotepart.domaine.medicament.Smr;
import java.math.BigDecimal;

/**
 * Détail d'une présentation exposé par l'API.
 */
public record MedicamentResponse(
        String cip13,
        BigDecimal prix,
        boolean remboursable,
        Smr smr) {

    public static MedicamentResponse depuis(Presentation presentation) {
        return new MedicamentResponse(
                presentation.code().valeur(),
                presentation.prix().valeur(),
                presentation.remboursable(),
                presentation.smr());
    }
}
