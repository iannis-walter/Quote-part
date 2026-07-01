package fr.quotepart.domaine.remboursement;

import fr.quotepart.domaine.medicament.Presentation;
import fr.quotepart.domaine.monnaie.Montant;
import java.util.List;

/**
 * Calcule le reste à charge d'une ordonnance entière : somme des lignes, en plafonnant la
 * franchise médicale sur l'année (les franchises au-delà du plafond ne sont pas facturées).
 */
public class CalculateurOrdonnance {

    private final CalculateurResteACharge calculateur;

    public CalculateurOrdonnance(CalculateurResteACharge calculateur) {
        this.calculateur = calculateur;
    }

    public DecompteOrdonnance calculer(List<Presentation> presentations,
                                       ProfilPatient profil,
                                       Bareme bareme,
                                       Montant franchiseDejaConsommee) {
        List<Decompte> lignes = presentations.stream()
                .map(presentation -> calculateur.calculer(presentation, profil, bareme))
                .toList();

        Montant resteHorsFranchise = Montant.ZERO;
        Montant franchiseNominale = Montant.ZERO;
        for (Decompte ligne : lignes) {
            resteHorsFranchise = resteHorsFranchise.plus(ligne.resteACharge().moins(ligne.franchiseMedicale()));
            franchiseNominale = franchiseNominale.plus(ligne.franchiseMedicale());
        }

        Montant franchiseAppliquee = plafonner(franchiseNominale, franchiseDejaConsommee, bareme);
        return new DecompteOrdonnance(lignes, franchiseAppliquee, resteHorsFranchise.plus(franchiseAppliquee));
    }

    private Montant plafonner(Montant franchiseNominale, Montant dejaConsommee, Bareme bareme) {
        Montant plafond = bareme.plafondFranchiseAnnuel();
        Montant restant = dejaConsommee.estSuperieurA(plafond) ? Montant.ZERO : plafond.moins(dejaConsommee);
        return franchiseNominale.estSuperieurA(restant) ? restant : franchiseNominale;
    }
}
