package fr.quotepart.infrastructure.persistance;

import fr.quotepart.application.Catalogue;
import fr.quotepart.application.MedicamentResume;
import fr.quotepart.domaine.medicament.CodeCip13;
import fr.quotepart.domaine.medicament.Presentation;
import fr.quotepart.domaine.medicament.Smr;
import fr.quotepart.domaine.monnaie.Montant;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * Adaptateur du port {@link Catalogue} : assemble présentation persistée et spécialité (dénomination,
 * SMR). Pour le calcul, la base de remboursement est assimilée au prix (simplification v1, cf. PRD §17).
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

    @Override
    public List<MedicamentResume> lister() {
        return presentations.findAll().stream().map(this::versResume).toList();
    }

    @Override
    public Optional<MedicamentResume> resume(CodeCip13 code) {
        return presentations.findById(code.valeur()).map(this::versResume);
    }

    private Presentation versDomaine(PresentationEntity entite) {
        Smr smr = specialites.findById(entite.getCis()).map(SpecialiteEntity::getSmr).orElse(null);
        Montant prix = new Montant(entite.getPrix());
        // Base de remboursement stockée (TFR) si présente, sinon assimilée au prix (défaut v1).
        Montant base = entite.getBaseRemboursement() != null ? new Montant(entite.getBaseRemboursement()) : prix;
        return new Presentation(new CodeCip13(entite.getCip13()), prix, base, entite.isRemboursable(), smr);
    }

    private MedicamentResume versResume(PresentationEntity entite) {
        SpecialiteEntity specialite = specialites.findById(entite.getCis()).orElse(null);
        return new MedicamentResume(
                entite.getCip13(),
                specialite != null ? specialite.getDenomination() : null,
                entite.getPrix(),
                entite.isRemboursable(),
                specialite != null ? specialite.getSmr() : null);
    }
}
