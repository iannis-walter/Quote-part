package fr.quotepart.infrastructure.bareme;

import fr.quotepart.domaine.medicament.Smr;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Valeurs réglementaires du barème, externalisées et datées (PRD §6) — jamais codées en dur.
 * Liées depuis la configuration sous le préfixe {@code bareme}.
 */
@ConfigurationProperties(prefix = "bareme")
public record BaremeProperties(
        LocalDate dateEffet,
        Map<Smr, Integer> tauxParSmr,
        BigDecimal coefficientHorsParcours,
        Integer tauxRegimeLocal,
        BigDecimal franchiseMedicaleParBoite,
        BigDecimal plafondFranchiseAnnuel) {
}
