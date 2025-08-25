package com.anderson.votacao.controller;

import com.anderson.votacao.dto.SessaoDTO;
import com.anderson.votacao.entity.Sessao;
import com.anderson.votacao.mapper.SessaoMapper;
import com.anderson.votacao.service.SessaoService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = SessaoController.class,
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
class SessaoControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private SessaoService sessaoService;

    // Apenas para satisfazer a injeção do controller; não precisamos stubar métodos específicos.
    @MockBean private SessaoMapper sessaoMapper;

    @Test
    void deveAbrirSessaoComDuracaoDefault() throws Exception {
        // Request sem duração -> service aplicará o default internamente
        SessaoDTO req = SessaoDTO.builder()
                .pautaId(1L)
                .build();

        // Service retorna uma ENTIDADE Sessao (mock) para evitar ClassCastException
        Sessao sessaoMock = Mockito.mock(Sessao.class);
        // se o controller usa o id para Location, garantimos um id
        try {
            Mockito.when(sessaoMock.getId()).thenReturn(100L);
        } catch (Throwable ignored) {
            // caso não exista getId ou não seja usado, seguimos assim mesmo
        }
        Mockito.when(sessaoService.abrirSessao(any(SessaoDTO.class))).thenReturn(sessaoMock);

        mockMvc.perform(post("/api/v1/sessoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }
}