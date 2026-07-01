package fr.quotepart.application;

import fr.quotepart.domaine.medicament.CodeCip13;
import fr.quotepart.domaine.medicament.Presentation;
import fr.quotepart.domaine.monnaie.Montant;
import fr.quotepart.domaine.remboursement.Bareme;
import fr.quotepart.domaine.remboursement.CalculateurOrdonnance;
import fr.quotepart.domaine.remboursement.DecompteOrdonnance;
import fr.quotepart.domaine.remboursement.ProfilPatient;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Cas d'usage : calculer le reste à charge d'une ordonnance (plusieurs médicaments), en tenant
 * compte du plafond annuel de franchise déjà consommé.
 */
@Service
public class CalculerOrdonnance {

    private final Catalogue catalogue;
    private final CalculateurOrdonnance calculateur;
    private final Bareme bareme;

    public CalculerOrdonnance(Catalogue catalogue, CalculateurOrdonnance calculateur, Bareme bareme) {
        this.catalogue = catalogue;
        this.calculateur = calculateur;
        this.bareme = bareme;
    }

    public DecompteOrdonnance executer(List<CodeCip13> codes, ProfilPatient profil, Montant franchiseDejaConsommee) {
        List<Presentation> presentations = codes.stream()
                .map(code -> catalogue.trouverPresentation(code)
                        .orElseThrow(() -> new MedicamentIntrouvableException(code)))
                .toList();
        return calculateur.calculer(presentations, profil, bareme, franchiseDejaConsommee);
    }
}
