package fr.quotepart.api;

import static org.hamcrest.Matchers.closeTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class CalculControllerIT {

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
    void calcule_le_reste_a_charge_via_l_api() throws Exception {
        specialites.save(new SpecialiteEntity("61266250", Smr.IMPORTANT));
        presentations.save(new PresentationEntity("3400920095517", "61266250", new BigDecimal("10.00"), 65, true));

        mockMvc.perform(post("/calculs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"cip13":"3400920095517","profil":{"parcoursSoinsRespecte":true,"ald":false}}"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tauxPourcent").value(65))
                .andExpect(jsonPath("$.remboursementSecu").value(closeTo(6.50, 0.001)))
                .andExpect(jsonPath("$.resteACharge").value(closeTo(4.50, 0.001)));
    }

    @Test
    void retourne_404_si_medicament_introuvable() throws Exception {
        mockMvc.perform(post("/calculs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"cip13":"0000000000000","profil":{"parcoursSoinsRespecte":true,"ald":false}}"""))
                .andExpect(status().isNotFound());
    }
}
