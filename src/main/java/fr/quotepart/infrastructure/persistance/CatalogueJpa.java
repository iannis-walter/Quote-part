package fr.quotepart.infrastructure.persistance;

import fr.quotepart.application.Catalogue;
import fr.quotepart.domaine.medicament.CodeCip13;
import fr.quotepart.domaine.medicament.Presentation;
import fr.quotepart.domaine.medicament.Smr;
import fr.quotepart.domaine.monnaie.Montant;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * Adaptateur du port {@link Catalogue} : assemble une présentation persistée et le SMR de sa
 * spécialité en un objet de domaine. En v1, la base de remboursement est assimilée au prix.
 */
@Component
public class CatalogueJpa implements Catalogue {

    private final PresentationRepository presentations;
    private final SpecialiteRepository specialites;

    public CatalogueJpa(PresentationRepository presentations, SpecialiteRepository specialites) {
        this.presentations = presentations;
        this.specialites = specialites;
    }

    @Override
    public Optional<Presentation> trouverPresentation(CodeCip13 code) {
        return presentations.findById(code.valeur()).map(this::versDomaine);
    }

    private Presentation versDomaine(PresentationEntity entite) {
        Smr smr = specialites.findById(entite.getCis())
                .map(SpecialiteEntity::getSmr)
                .orElse(null);
        Montant prix = new Montant(entite.getPrix());
        return new Presentation(
                new CodeCip13(entite.getCip13()),
                prix,
                prix, // base de remboursement = prix (simplification v1, cf. PRD §17)
                entite.isRemboursable(),
                smr);
    }
}
