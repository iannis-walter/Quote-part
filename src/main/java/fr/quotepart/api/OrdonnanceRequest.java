package fr.quotepart.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * Corps de la requête de calcul d'ordonnance : les codes des médicaments, le profil patient,
 * et la franchise déjà consommée dans l'année (optionnelle, 0 par défaut).
 */
public record OrdonnanceRequest(
        @NotEmpty List<String> cip13s,
        @NotNull @Valid CalculRequest.ProfilRequest profil,
        BigDecimal franchiseDejaConsommee) {
}
