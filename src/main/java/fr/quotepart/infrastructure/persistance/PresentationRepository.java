package fr.quotepart.infrastructure.persistance;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PresentationRepository extends JpaRepository<PresentationEntity, String> {
}
