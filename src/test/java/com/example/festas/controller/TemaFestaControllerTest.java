package com.example.festas.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import com.example.festas.entity.TemaFesta;
import com.example.festas.service.TemaFestaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.doThrow;


@WebMvcTest(controllers = TemaFestaController.class, 
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
        },
        excludeFilters = {
                @org.springframework.context.annotation.ComponentScan.Filter(
                        type = org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE,
                        classes = com.example.festas.security.SecurityFilter.class
                )
        })
class TemaFestaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TemaFestaService temaService;

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve buscar todos os temas e retornar status 200")
    void buscarTodos_DeveRetornarListaComStatus200() throws Exception {
        when(temaService.buscarTodos()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/temas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve buscar tema por ID existente e retornar status 200")
    void buscarPorId_Existente_DeveRetornarStatus200() throws Exception {
        TemaFesta tema = new TemaFesta();
        tema.setId(1L);
        when(temaService.buscarPorId(1L)).thenReturn(Optional.of(tema));

        mockMvc.perform(get("/api/temas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve buscar tema por ID inexistente e retornar status 404")
    void buscarPorId_Inexistente_DeveRetornarStatus404() throws Exception {
        when(temaService.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/temas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve salvar tema válido e retornar status 201")
    void salvar_Valido_DeveRetornarStatus201() throws Exception {
        TemaFesta tema = new TemaFesta();
        tema.setId(1L);
        tema.setNome("Tema Teste");

        when(temaService.salvar(any(TemaFesta.class))).thenReturn(tema);

        mockMvc.perform(post("/api/temas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tema)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve atualizar tema existente e retornar status 200")
    void atualizar_Existente_DeveRetornarStatus200() throws Exception {
        TemaFesta tema = new TemaFesta();
        tema.setId(1L);
        tema.setNome("Tema Teste");

        when(temaService.atualizar(eq(1L), any(TemaFesta.class))).thenReturn(tema);

        mockMvc.perform(put("/api/temas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tema)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve falhar ao atualizar tema inexistente e retornar 404")
    void atualizar_Inexistente_DeveRetornarStatus404() throws Exception {
        TemaFesta tema = new TemaFesta();
        tema.setNome("Nome Válido");
        when(temaService.atualizar(eq(99L), any(TemaFesta.class))).thenThrow(new RuntimeException());

        mockMvc.perform(put("/api/temas/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tema)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve deletar tema existente e retornar status 204")
    void deletar_Existente_DeveRetornarStatus204() throws Exception {
        mockMvc.perform(delete("/api/temas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve falhar ao deletar tema inexistente e retornar 404")
    void deletar_Inexistente_DeveRetornarStatus404() throws Exception {
        doThrow(new RuntimeException("Não encontrado")).when(temaService).deletar(99L);

        mockMvc.perform(delete("/api/temas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve buscar por nome e retornar status 200")
    void buscarPorNome_DeveRetornarStatus200() throws Exception {
        when(temaService.buscarPorNome("Teste")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/temas/buscar").param("nome", "Teste"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve buscar ativos e retornar status 200")
    void buscarAtivos_DeveRetornarStatus200() throws Exception {
        when(temaService.buscarAtivos()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/temas/ativos"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve falhar ao buscar tema por ID inexistente e retornar status 404")
    void buscarPorId_Inexistente_DeveRetornarStatus404_Controller() throws Exception {
        when(temaService.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/temas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve falhar ao atualizar tema inexistente e retornar 404")
    void atualizar_Inexistente_DeveRetornarStatus404_Controller() throws Exception {
        TemaFesta tema = new TemaFesta(); // Tema válido para o corpo
        tema.setNome("Nome Válido");
        // Simula o service lançando a exceção que o controller captura
        when(temaService.atualizar(eq(99L), any(TemaFesta.class))).thenThrow(new RuntimeException("Não encontrado"));

        mockMvc.perform(put("/api/temas/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tema)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve falhar ao deletar tema inexistente e retornar 404")
    void deletar_Inexistente_DeveRetornarStatus404_Controller() throws Exception {
        // Simula o service lançando a exceção que o controller captura
        doThrow(new RuntimeException("Não encontrado")).when(temaService).deletar(99L);

        mockMvc.perform(delete("/api/temas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve falhar ao salvar tema inválido (sem nome) e retornar 400")
    void salvar_Invalido_DeveRetornarStatus400() throws Exception {
        TemaFesta temaInvalido = new TemaFesta(); // Sem nome

        mockMvc.perform(post("/api/temas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(temaInvalido)))
                .andExpect(status().isBadRequest()); // Espera erro de validação
    }
}