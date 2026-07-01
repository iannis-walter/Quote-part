package fr.quotepart.infrastructure.bdpm;

import fr.quotepart.infrastructure.persistance.SpecialiteEntity;
import fr.quotepart.infrastructure.persistance.SpecialiteRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Charge les dénominations de spécialités parsées dans la base. Idempotent et fusionnant :
 * met à jour la dénomination (par CIS) sans écraser le SMR éventuel.
 */
@Service
public class IngestionSpecialiteBdpm {

    private final SpecialiteRepository repository;

    public IngestionSpecialiteBdpm(SpecialiteRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void enregistrer(List<LigneSpecialiteBdpm> lignes) {
        for (LigneSpecialiteBdpm ligne : lignes) {
            SpecialiteEntity specialite = repository.findById(ligne.codeCis())
                    .orElseGet(() -> new SpecialiteEntity(ligne.codeCis()));
            specialite.setDenomination(ligne.denomination());
            repository.save(specialite);
        }
    }
}
