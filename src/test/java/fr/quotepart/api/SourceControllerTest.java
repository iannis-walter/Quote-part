package fr.quotepart.api;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import fr.quotepart.infrastructure.bdpm.SynchronisationBdpm;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SourceController.class)
class SourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SynchronisationBdpm synchronisation;

    @Test
    void expose_la_source_et_la_date_de_derniere_synchro() throws Exception {
        when(synchronisation.derniereSynchro())
                .thenReturn(Optional.of(LocalDateTime.of(2026, 6, 30, 3, 0, 0)));

        mockMvc.perform(get("/source"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.source").value(containsString("BDPM")))
                .andExpect(jsonPath("$.derniereSynchronisation").value("2026-06-30T03:00:00"));
    }
}
