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

        Taux taux = profil.ald() ? Taux.pourcent(100) : bareme.tauxPour(presentation.smr());
        Coefficient coefficient = Coefficient.PLEIN;

        Montant remboursementSecu = coefficient.appliquerA(taux.appliquerA(presentation.baseRemboursement()));
        Montant ticketModerateur = presentation.baseRemboursement().moins(remboursementSecu);
        Montant franchise = bareme.franchiseMedicaleParBoite();
        Montant resteACharge = presentation.prix().moins(remboursementSecu).plus(franchise);

        return new Decompte(presentation.prix(), presentation.baseRemboursement(), taux,
                remboursementSecu, ticketModerateur, franchise, resteACharge);
    }
}
