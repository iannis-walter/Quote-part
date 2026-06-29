package fr.quotepart.infrastructure.bdpm;

/**
 * Levée quand une ligne du fichier BDPM ne respecte pas la structure attendue.
 * On échoue explicitement plutôt que de laisser passer une donnée tronquée.
 */
public class LigneBdpmInvalideException extends RuntimeException {

    public LigneBdpmInvalideException(String ligne, int nombreColonnes) {
        super("Ligne BDPM invalide (" + nombreColonnes + " colonnes) : " + ligne);
    }
}
