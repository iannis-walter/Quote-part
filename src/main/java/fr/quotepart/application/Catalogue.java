package fr.quotepart.application;

import fr.quotepart.domaine.medicament.CodeCip13;
import fr.quotepart.domaine.medicament.Presentation;
import java.util.Optional;

/**
 * Port sortant : accès au catalogue de médicaments, exprimé en termes de domaine.
 * L'implémentation (infrastructure) assemble la présentation et son SMR.
 */
public interface Catalogue {

    Optional<Presentation> trouverPresentation(CodeCip13 code);
}
