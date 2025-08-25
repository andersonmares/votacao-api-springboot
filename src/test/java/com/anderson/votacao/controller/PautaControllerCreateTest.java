package com.anderson.votacao.controller;

import com.anderson.votacao.dto.PautaDTO;
import com.anderson.votacao.service.PautaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = PautaController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class,
                KafkaAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "cpf.service.url=http://localhost:65535",
        "spring.kafka.bootstrap-servers=localhost:9092"
})
class PautaControllerCreateTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private PautaService pautaService;

    @Test
    void deveCriarPautaRetornando201() throws Exception {
        PautaDTO resposta = new PautaDTO();
        resposta.setId(1L);
        resposta.setTitulo("Teste");
        resposta.setDescricao("Desc");
        Mockito.when(pautaService.criarPauta(any(PautaDTO.class))).thenReturn(resposta);

        PautaDTO req = new PautaDTO();
        req.setTitulo("Teste");
        req.setDescricao("Desc");

        mockMvc.perform(post("/api/v1/pautas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }
}