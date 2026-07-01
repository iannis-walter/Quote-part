package fr.quotepart.api;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import fr.quotepart.domaine.medicament.Smr;
import fr.quotepart.infrastructure.persistance.PresentationEntity;
import fr.quotepart.infrastructure.persistance.PresentationRepository;
import fr.quotepart.infrastructure.persistance.SpecialiteEntity;
import fr.quotepart.infrastructure.persistance.SpecialiteRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class MedicamentControllerIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PresentationRepository presentations;

    @Autowired
    private SpecialiteRepository specialites;

    @Test
    void retourne_le_detail_d_un_medicament() throws Exception {
        specialites.save(new SpecialiteEntity("61266250", Smr.IMPORTANT));
        presentations.save(new PresentationEntity("3400920095517", "61266250", new BigDecimal("10.00"), 65, true));

        mockMvc.perform(get("/medicaments/3400920095517"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cip13").value("3400920095517"))
                .andExpect(jsonPath("$.smr").value("IMPORTANT"))
                .andExpect(jsonPath("$.remboursable").value(true));
    }

    @Test
    void retourne_404_si_le_medicament_est_inconnu() throws Exception {
        mockMvc.perform(get("/medicaments/0000000000000"))
                .andExpect(status().isNotFound());
    }

    @Test
    void liste_les_medicaments_avec_leur_denomination() throws Exception {
        SpecialiteEntity specialite = new SpecialiteEntity("61266250", Smr.IMPORTANT);
        specialite.setDenomination("DOLIPRANE 1000 mg, comprimé");
        specialites.save(specialite);
        presentations.save(new PresentationEntity("3400920095517", "61266250", new BigDecimal("10.00"), 65, true));

        mockMvc.perform(get("/medicaments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].cip13").value("3400920095517"))
                .andExpect(jsonPath("$[0].denomination").value("DOLIPRANE 1000 mg, comprimé"));
    }
}
