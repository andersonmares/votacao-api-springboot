package com.anderson.votacao.controller;

import com.anderson.votacao.dto.SessaoDTO;
import com.anderson.votacao.entity.Sessao;
import com.anderson.votacao.service.SessaoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SessaoController.class)
public class SessaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessaoService sessaoService;

    @Test
    void deveAbrirSessaoComDuracaoDefault() throws Exception {
        Sessao sessao = new Sessao();
        sessao.setId(1L);
        Mockito.when(sessaoService.abrirSessao(Mockito.any(SessaoDTO.class))).thenReturn(sessao);

        mockMvc.perform(post("/api/v1/sessoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"pautaId\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }
}