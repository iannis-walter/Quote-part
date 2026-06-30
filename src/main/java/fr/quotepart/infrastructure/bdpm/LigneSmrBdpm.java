package fr.quotepart.infrastructure.bdpm;

import fr.quotepart.domaine.medicament.Smr;

/**
 * Représentation brute d'une ligne du fichier CIS_HAS_SMR_bdpm.txt : un SMR rattaché à une spécialité (CIS).
 */
public record LigneSmrBdpm(String codeCis, Smr smr) {
}
