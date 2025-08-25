package com.anderson.votacao.performance;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PerformanceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Disabled("Teste de performance opcional - habilite localmente quando necessário")
    void listaPautasEmMenosDe500ms() throws Exception {
        long start = System.currentTimeMillis();
        mockMvc.perform(get("/api/v1/pautas"))
                .andExpect(status().isOk());
        long took = System.currentTimeMillis() - start;
        assert took < 500 : "Request demorou " + took + "ms";
    }
}