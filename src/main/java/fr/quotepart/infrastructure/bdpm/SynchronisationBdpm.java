package fr.quotepart.infrastructure.bdpm;

import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Orchestration de la synchronisation BDPM : lire le fichier → parser → ingérer (idempotent).
 * La date de dernière synchronisation est tracée pour exposition ultérieure (endpoint /source).
 */
@Component
public class SynchronisationBdpm {

    private final LecteurFichierBdpm lecteur;
    private final ParseurPresentationBdpm parseur;
    private final IngestionPresentationsBdpm ingestion;
    private final Clock horloge;
    private final String cheminFichierPresentations;

    private LocalDateTime derniereSynchro;

    public SynchronisationBdpm(LecteurFichierBdpm lecteur,
                               ParseurPresentationBdpm parseur,
                               IngestionPresentationsBdpm ingestion,
                               Clock horloge,
                               @Value("${bdpm.fichier-presentations:}") String cheminFichierPresentations) {
        this.lecteur = lecteur;
        this.parseur = parseur;
        this.ingestion = ingestion;
        this.horloge = horloge;
        this.cheminFichierPresentations = cheminFichierPresentations;
    }

    /** Déclenchée par planification ; sans fichier configuré, ne fait rien. */
    @Scheduled(cron = "${bdpm.cron:0 0 3 * * *}")
    public void synchroniserDepuisFichierConfigure() {
        if (cheminFichierPresentations == null || cheminFichierPresentations.isBlank()) {
            return;
        }
        synchroniser(Path.of(cheminFichierPresentations));
    }

    public void synchroniser(Path fichierPresentations) {
        var lignes = parseur.parseToutes(lecteur.lire(fichierPresentations).stream());
        ingestion.enregistrer(lignes);
        derniereSynchro = LocalDateTime.now(horloge);
    }

    public Optional<LocalDateTime> derniereSynchro() {
        return Optional.ofNullable(derniereSynchro);
    }
}
