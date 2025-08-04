package com.anderson.votacao.controller;

import com.anderson.votacao.dto.PautaDTO;
import com.anderson.votacao.service.PautaService;
import com.anderson.votacao.service.VotoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PautaController.class)
public class PautaControllerTest {

    @MockBean
    private VotoService votoService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PautaService pautaService;

    @Test
    void deveCriarPautaRetornando201() throws Exception {

        PautaDTO pautaDTO = new PautaDTO();
        pautaDTO.setId(1L);
        pautaDTO.setDescricao("Teste");
        Mockito.when(pautaService.criarPauta(any(PautaDTO.class))).thenReturn(pautaDTO);

        mockMvc.perform(post("/api/v1/pautas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"descricao\":\"Teste\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }
}