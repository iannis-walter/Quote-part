package fr.quotepart.domaine.remboursement;

import fr.quotepart.domaine.medicament.Smr;
import fr.quotepart.domaine.monnaie.Montant;

/**
 * Barème réglementaire en vigueur. Les valeurs sont externalisées et datées (cf. PRD §6) :
 * le domaine ne les connaît pas en dur, il les lit via ce port.
 */
public interface Bareme {

    Taux tauxPour(Smr smr);

    Montant franchiseMedicaleParBoite();
}
