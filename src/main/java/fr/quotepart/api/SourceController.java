package fr.quotepart.api;

import fr.quotepart.infrastructure.bdpm.SynchronisationBdpm;
import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Expose la provenance des données — exigence de conformité de la licence BDPM
 * (mention de la source et de la date de mise à jour).
 */
@RestController
@RequestMapping("/source")
public class SourceController {

    private static final String SOURCE =
            "Base de Données Publique des Médicaments (BDPM — ANSM / HAS / UNCAM) — base-donnees-publique.medicaments.gouv.fr";
    private static final String LICENCE =
            "Réutilisation soumise à la mention de la source et de la date de mise à jour ; données non dénaturées.";

    private final SynchronisationBdpm synchronisation;

    public SourceController(SynchronisationBdpm synchronisation) {
        this.synchronisation = synchronisation;
    }

    @GetMapping
    public SourceResponse source() {
        return new SourceResponse(SOURCE, LICENCE, synchronisation.derniereSynchro().orElse(null));
    }

    public record SourceResponse(String source, String licence, LocalDateTime derniereSynchronisation) {
    }
}
