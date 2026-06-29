package fr.quotepart.infrastructure.bdpm;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class LecteurFichierBdpmTest {

    private final LecteurFichierBdpm lecteur = new LecteurFichierBdpm();

    @Test
    void lit_les_lignes_dun_fichier_encode_en_windows_1252(@TempDir Path dossier) throws IOException {
        Path fichier = dossier.resolve("cis_cip.txt");
        Files.write(fichier, "spécialité à 2,11 €".getBytes(Charset.forName("windows-1252")));

        List<String> lignes = lecteur.lire(fichier);

        assertThat(lignes).containsExactly("spécialité à 2,11 €");
    }
}
