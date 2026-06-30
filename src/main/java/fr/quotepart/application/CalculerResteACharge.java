package fr.quotepart.application;

import fr.quotepart.domaine.medicament.CodeCip13;
import fr.quotepart.domaine.medicament.Presentation;
import fr.quotepart.domaine.remboursement.Bareme;
import fr.quotepart.domaine.remboursement.CalculateurResteACharge;
import fr.quotepart.domaine.remboursement.Decompte;
import fr.quotepart.domaine.remboursement.ProfilPatient;
import org.springframework.stereotype.Service;

/**
 * Cas d'usage : calculer le reste à charge d'un médicament identifié par son CIP13,
 * pour un profil patient, à partir du catalogue persisté et du barème en vigueur.
 */
@Service
public class CalculerResteACharge {

    private final Catalogue catalogue;
    private final CalculateurResteACharge calculateur;
    private final Bareme bareme;

    public CalculerResteACharge(Catalogue catalogue, CalculateurResteACharge calculateur, Bareme bareme) {
        this.catalogue = catalogue;
        this.calculateur = calculateur;
        this.bareme = bareme;
    }

    public Decompte executer(CodeCip13 code, ProfilPatient profil) {
        Presentation presentation = catalogue.trouverPresentation(code)
                .orElseThrow(() -> new MedicamentIntrouvableException(code));
        return calculateur.calculer(presentation, profil, bareme);
    }
}
