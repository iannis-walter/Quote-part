package fr.quotepart.domaine.remboursement;

import fr.quotepart.domaine.medicament.Smr;
import fr.quotepart.domaine.monnaie.Montant;
import java.math.BigDecimal;

/**
 * Double de test du barème : valeurs fixes et connues, pour piloter les calculs sans config réelle.
 */
class BaremeDeTest implements Bareme {

    @Override
    public Taux tauxPour(Smr smr) {
        return switch (smr) {
            case IMPORTANT -> Taux.pourcent(65);
            case MODERE -> Taux.pourcent(30);
            case FAIBLE -> Taux.pourcent(15);
            case INSUFFISANT -> Taux.pourcent(0);
        };
    }

    @Override
    public Coefficient coefficientHorsParcours() {
        return new Coefficient(new BigDecimal("0.60"));
    }

    @Override
    public Taux tauxRegimeLocal() {
        return Taux.pourcent(90);
    }

    @Override
    public Montant franchiseMedicaleParBoite() {
        return Montant.euros("1.00");
    }

    @Override
    public Montant plafondFranchiseAnnuel() {
        return Montant.euros("50.00");
    }
}
