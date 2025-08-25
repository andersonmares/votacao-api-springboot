package com.anderson.votacao.integration;

import com.anderson.votacao.client.CpfClient;
import com.anderson.votacao.dto.SessaoDTO;
import com.anderson.votacao.entity.Pauta;
import com.anderson.votacao.repository.PautaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiIntegrationTest {

    @MockBean
    private CpfClient cpfClient;

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private PautaRepository pautaRepository;

    private Pauta pauta;

    @BeforeEach
    void setup() {
        pauta = pautaRepository.save(Pauta.builder().titulo("Teste").descricao("Desc").build());
    }

    @Test
    void deveAbrirSessaoFluxoCompleto() throws Exception {
        SessaoDTO dto = SessaoDTO.builder().pautaId(pauta.getId()).build();
        mockMvc.perform(post("/api/v1/sessoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }
}