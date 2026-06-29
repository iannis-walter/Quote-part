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

        Taux taux;
        if (profil.ald()) {
            taux = Taux.pourcent(100);
        } else {
            if (presentation.smr() == null) {
                throw new DonneesMedicamentIncompletesException(presentation.code(), "SMR absent");
            }
            taux = bareme.tauxPour(presentation.smr());
        }

        Coefficient coefficient = profil.parcoursSoinsRespecte()
                ? Coefficient.PLEIN
                : bareme.coefficientHorsParcours();

        Montant remboursementSecu = coefficient.appliquerA(taux.appliquerA(presentation.baseRemboursement()));
        Montant ticketModerateur = presentation.baseRemboursement().moins(remboursementSecu);
        Montant franchise = bareme.franchiseMedicaleParBoite();
        Montant resteACharge = presentation.prix().moins(remboursementSecu).plus(franchise);

        return new Decompte(presentation.prix(), presentation.baseRemboursement(), taux,
                remboursementSecu, ticketModerateur, franchise, resteACharge);
    }
}
