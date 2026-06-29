package fr.quotepart.infrastructure.bdpm;

import static org.assertj.core.api.Assertions.assertThat;

import fr.quotepart.infrastructure.persistance.PresentationRepository;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class SynchronisationBdpmIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private SynchronisationBdpm synchronisation;

    @Autowired
    private PresentationRepository repository;

    @Test
    void synchronise_un_fichier_bdpm_et_trace_la_date(@TempDir Path dossier) throws IOException {
        Path fichier = dossier.resolve("CIS_CIP_bdpm.txt");
        String ligne = "61266250\t2009551\tplaquette PVC\tactive\tcommercialisée\t12/05/2010"
                + "\t3400920095517\toui\t65 %\t2,11\t2,11\t0,00\tindications";
        Files.write(fichier, ligne.getBytes(Charset.forName("windows-1252")));

        synchronisation.synchroniser(fichier);

        assertThat(repository.findById("3400920095517")).isPresent();
        assertThat(synchronisation.derniereSynchro()).isPresent();
    }
}
