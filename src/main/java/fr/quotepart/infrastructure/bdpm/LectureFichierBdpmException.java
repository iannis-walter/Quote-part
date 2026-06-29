package fr.quotepart.infrastructure.bdpm;

import java.nio.file.Path;

/**
 * Levée quand un fichier BDPM ne peut pas être lu (absent, illisible).
 */
public class LectureFichierBdpmException extends RuntimeException {

    public LectureFichierBdpmException(Path fichier, Throwable cause) {
        super("Lecture impossible du fichier BDPM : " + fichier, cause);
    }
}
