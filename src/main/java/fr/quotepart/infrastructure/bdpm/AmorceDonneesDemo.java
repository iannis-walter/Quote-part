package fr.quotepart.infrastructure.bdpm;

import fr.quotepart.infrastructure.persistance.PresentationRepository;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * Amorce un échantillon de données BDPM au démarrage si la base est vide, pour que la démo soit
 * immédiatement exploitable. Rejoue le vrai pipeline d'ingestion (parseurs + ingestions).
 * Désactivé par défaut ; activé via {@code quotepart.demo.amorcer=true} (positionné dans docker-compose).
 */
@Component
@ConditionalOnProperty(name = "quotepart.demo.amorcer", havingValue = "true")
public class AmorceDonneesDemo implements ApplicationRunner {

    private final PresentationRepository presentations;
    private final ParseurSpecialiteBdpm parseurSpecialite;
    private final ParseurPresentationBdpm parseurPresentation;
    private final ParseurSmrBdpm parseurSmr;
    private final IngestionSpecialiteBdpm ingestionSpecialite;
    private final IngestionPresentationsBdpm ingestionPresentations;
    private final IngestionSmrBdpm ingestionSmr;

    public AmorceDonneesDemo(PresentationRepository presentations,
                             ParseurSpecialiteBdpm parseurSpecialite,
                             ParseurPresentationBdpm parseurPresentation,
                             ParseurSmrBdpm parseurSmr,
                             IngestionSpecialiteBdpm ingestionSpecialite,
                             IngestionPresentationsBdpm ingestionPresentations,
                             IngestionSmrBdpm ingestionSmr) {
        this.presentations = presentations;
        this.parseurSpecialite = parseurSpecialite;
        this.parseurPresentation = parseurPresentation;
        this.parseurSmr = parseurSmr;
        this.ingestionSpecialite = ingestionSpecialite;
        this.ingestionPresentations = ingestionPresentations;
        this.ingestionSmr = ingestionSmr;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (presentations.count() > 0) {
            return;
        }
        ingestionSpecialite.enregistrer(parseurSpecialite.parseToutes(lignes("echantillon/CIS_bdpm.txt").stream()));
        ingestionPresentations.enregistrer(parseurPresentation.parseToutes(lignes("echantillon/CIS_CIP_bdpm.txt").stream()));
        ingestionSmr.enregistrer(parseurSmr.parseToutes(lignes("echantillon/CIS_HAS_SMR_bdpm.txt").stream()));
    }

    private List<String> lignes(String ressource) {
        try (BufferedReader lecteur = new BufferedReader(
                new InputStreamReader(new ClassPathResource(ressource).getInputStream(), StandardCharsets.UTF_8))) {
            return lecteur.lines().toList();
        } catch (IOException e) {
            throw new IllegalStateException("Échantillon BDPM introuvable : " + ressource, e);
        }
    }
}
