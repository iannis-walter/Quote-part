package fr.quotepart.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Corps de la requête de calcul : un code CIP13 et le profil du patient.
 */
public record CalculRequest(
        @NotBlank String cip13,
        @NotNull @Valid ProfilRequest profil) {

    public record ProfilRequest(
            boolean parcoursSoinsRespecte,
            boolean ald,
            boolean c2s,
            boolean regimeLocal) {
    }
}
