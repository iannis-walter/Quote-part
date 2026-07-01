package fr.quotepart.infrastructure.bdpm;

/**
 * Représentation brute d'une ligne du fichier CIS_bdpm.txt : le code et la dénomination d'une spécialité.
 */
public record LigneSpecialiteBdpm(String codeCis, String denomination) {
}
