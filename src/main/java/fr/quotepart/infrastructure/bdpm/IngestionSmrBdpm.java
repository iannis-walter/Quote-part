package fr.quotepart.infrastructure.bdpm;

import fr.quotepart.infrastructure.persistance.SpecialiteEntity;
import fr.quotepart.infrastructure.persistance.SpecialiteRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Charge les SMR parsés dans la base. Idempotent : la clé primaire étant le CIS,
 * réenregistrer un SMR le met à jour au lieu de le dupliquer.
 */
@Service
public class IngestionSmrBdpm {

    private final SpecialiteRepository repository;

    public IngestionSmrBdpm(SpecialiteRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void enregistrer(List<LigneSmrBdpm> lignes) {
        lignes.stream()
                .map(ligne -> new SpecialiteEntity(ligne.codeCis(), ligne.smr()))
                .forEach(repository::save);
    }
}
