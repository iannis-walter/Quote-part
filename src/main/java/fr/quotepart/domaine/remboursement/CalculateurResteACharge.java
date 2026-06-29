package fr.quotepart.domaine.remboursement;

import fr.quotepart.domaine.medicament.Presentation;
import fr.quotepart.domaine.monnaie.Montant;

/**
 * Service de domaine : calcule le reste à charge d'une présentation pour un profil patient,
 * selon le barème en vigueur. Pur — aucune dépendance framework ni persistance.
 */
public class CalculateurResteACharge {

    public Decompte calculer(Presentation presentation, ProfilPatient profil, Bareme bareme) {
        if (!presentation.remboursable()) {
            return Decompte.nonRemboursable(presentation.prix());
        }

        Taux taux = tauxApplicable(presentation, profil, bareme);
        Coefficient coefficient = coefficientApplicable(profil, bareme);

        Montant remboursementSecu = coefficient.appliquerA(taux.appliquerA(presentation.baseRemboursement()));
        Montant ticketModerateur = presentation.baseRemboursement().moins(remboursementSecu);
        Montant franchise = bareme.franchiseMedicaleParBoite();
        Montant resteACharge = presentation.prix().moins(remboursementSecu).plus(franchise);

        return new Decompte(presentation.prix(), presentation.baseRemboursement(), taux,
                remboursementSecu, ticketModerateur, franchise, resteACharge);
    }

    private Taux tauxApplicable(Presentation presentation, ProfilPatient profil, Bareme bareme) {
        if (profil.ald()) {
            return Taux.pourcent(100); // l'ALD prime sur le SMR
        }
        if (presentation.smr() == null) {
            throw new DonneesMedicamentIncompletesException(presentation.code(), "SMR absent");
        }
        return bareme.tauxPour(presentation.smr());
    }

    private Coefficient coefficientApplicable(ProfilPatient profil, Bareme bareme) {
        return profil.parcoursSoinsRespecte()
                ? Coefficient.PLEIN
                : bareme.coefficientHorsParcours();
    }
}
