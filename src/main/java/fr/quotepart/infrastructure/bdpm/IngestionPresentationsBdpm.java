package fr.quotepart.infrastructure.bdpm;

import fr.quotepart.infrastructure.persistance.PresentationEntity;
import fr.quotepart.infrastructure.persistance.PresentationRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Charge des lignes BDPM parsées dans la base. Idempotent : la clé primaire étant le CIP13,
 * réenregistrer une présentation la met à jour au lieu de la dupliquer — rejouer une synchro est sûr.
 */
@Service
public class IngestionPresentationsBdpm {

    private final PresentationRepository repository;

    public IngestionPresentationsBdpm(PresentationRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void enregistrer(List<LignePresentationBdpm> lignes) {
        lignes.stream().map(this::versEntite).forEach(repository::save);
    }

    private PresentationEntity versEntite(LignePresentationBdpm ligne) {
        Integer taux = ligne.remboursable() ? ligne.tauxRemboursement().pourcentage() : null;
        return new PresentationEntity(
                ligne.codeCip13().valeur(),
                ligne.codeCis(),
                ligne.prix().valeur(),
                taux,
                ligne.remboursable());
    }
}
