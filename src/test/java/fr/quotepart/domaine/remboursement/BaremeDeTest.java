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
        };
    }

    @Override
    public Coefficient coefficientHorsParcours() {
        return new Coefficient(new BigDecimal("0.60"));
    }

    @Override
    public Montant franchiseMedicaleParBoite() {
        return Montant.euros("1.00");
    }
}
