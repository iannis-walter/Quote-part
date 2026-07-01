package fr.quotepart.infrastructure.persistance;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PresentationRepository extends JpaRepository<PresentationEntity, String> {

    List<PresentationEntity> findByCisIn(Collection<String> cis);
}
