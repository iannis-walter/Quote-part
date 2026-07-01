package fr.quotepart.infrastructure.persistance;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialiteRepository extends JpaRepository<SpecialiteEntity, String> {

    List<SpecialiteEntity> findByDenominationContainingIgnoreCase(String terme);
}
