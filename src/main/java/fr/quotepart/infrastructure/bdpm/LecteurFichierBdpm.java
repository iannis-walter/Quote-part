package fr.quotepart.infrastructure.bdpm;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Lit un fichier officiel BDPM ligne à ligne. Les fichiers de l'ANSM sont encodés en Windows-1252 :
 * lire en UTF-8 corromprait les accents et le symbole €.
 */
@Component
public class LecteurFichierBdpm {

    private static final Charset ENCODAGE_BDPM = Charset.forName("windows-1252");

    public List<String> lire(Path fichier) {
        try {
            return Files.readAllLines(fichier, ENCODAGE_BDPM);
        } catch (IOException e) {
            throw new LectureFichierBdpmException(fichier, e);
        }
    }
}
