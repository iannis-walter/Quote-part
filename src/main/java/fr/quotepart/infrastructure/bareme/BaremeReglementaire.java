package fr.quotepart.infrastructure.bareme;

import fr.quotepart.domaine.medicament.Smr;
import fr.quotepart.domaine.monnaie.Montant;
import fr.quotepart.domaine.remboursement.Bareme;
import fr.quotepart.domaine.remboursement.Coefficient;
import fr.quotepart.domaine.remboursement.Taux;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

/**
 * Implémentation du barème adossée à la configuration ({@link BaremeProperties}).
 * Le domaine reste agnostique : il ne voit que le port {@link Bareme}.
 */
@Component
public class BaremeReglementaire implements Bareme {

    private final BaremeProperties properties;

    public BaremeReglementaire(BaremeProperties properties) {
        this.properties = properties;
    }

    @Override
    public Taux tauxPour(Smr smr) {
        Integer pourcentage = properties.tauxParSmr().get(smr);
        if (pourcentage == null) {
            throw new BaremeIncompletException(smr);
        }
        return Taux.pourcent(pourcentage);
    }

    @Override
    public Coefficient coefficientHorsParcours() {
        return new Coefficient(properties.coefficientHorsParcours());
    }

    @Override
    public Taux tauxRegimeLocal() {
        return Taux.pourcent(properties.tauxRegimeLocal());
    }

    @Override
    public Montant franchiseMedicaleParBoite() {
        return new Montant(properties.franchiseMedicaleParBoite());
    }

    public LocalDate dateEffet() {
        return properties.dateEffet();
    }
}
