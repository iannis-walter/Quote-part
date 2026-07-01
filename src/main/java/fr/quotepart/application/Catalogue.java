package fr.quotepart.application;

import fr.quotepart.domaine.medicament.CodeCip13;
import fr.quotepart.domaine.medicament.Presentation;
import java.util.List;
import java.util.Optional;

/**
 * Port sortant : accès au catalogue de médicaments.
 * Le domaine de calcul consomme {@link Presentation} ; l'affichage consomme {@link MedicamentResume}.
 */
public interface Catalogue {

    Optional<Presentation> trouverPresentation(CodeCip13 code);

    List<MedicamentResume> lister();

    Optional<MedicamentResume> resume(CodeCip13 code);
}
