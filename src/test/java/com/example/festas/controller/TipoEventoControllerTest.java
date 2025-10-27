package com.example.festas.controller;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import com.example.festas.entity.TipoEvento;
import com.example.festas.service.TipoEventoService;
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

@WebMvcTest(TipoEventoController.class)
class TipoEventoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TipoEventoService tipoEventoService;

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve buscar todos os tipos de evento e retornar status 200")
    void buscarTodos_DeveRetornarListaComStatus200() throws Exception {
        when(tipoEventoService.buscarTodos()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/tipos-evento"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve buscar tipo de evento por ID existente e retornar status 200")
    void buscarPorId_Existente_DeveRetornarStatus200() throws Exception {
        TipoEvento tipo = new TipoEvento();
        tipo.setId(1L);
        when(tipoEventoService.buscarPorId(1L)).thenReturn(Optional.of(tipo));

        mockMvc.perform(get("/api/tipos-evento/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve buscar tipo de evento por ID inexistente e retornar status 404")
    void buscarPorId_Inexistente_DeveRetornarStatus404() throws Exception {
        when(tipoEventoService.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tipos-evento/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve salvar tipo de evento válido e retornar status 201")
    void salvar_Valido_DeveRetornarStatus201() throws Exception {
        TipoEvento tipo = new TipoEvento();
        tipo.setId(1L);
        tipo.setNome("Evento Teste");

        when(tipoEventoService.salvar(any(TipoEvento.class))).thenReturn(tipo);

        mockMvc.perform(post("/api/tipos-evento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tipo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve atualizar tipo de evento existente e retornar status 200")
    void atualizar_Existente_DeveRetornarStatus200() throws Exception {
        TipoEvento tipo = new TipoEvento();
        tipo.setId(1L);
        tipo.setNome("Evento Teste");

        when(tipoEventoService.atualizar(eq(1L), any(TipoEvento.class))).thenReturn(tipo);

        mockMvc.perform(put("/api/tipos-evento/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tipo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve falhar ao atualizar tipo de evento inexistente e retornar 404")
    void atualizar_Inexistente_DeveRetornarStatus404() throws Exception {
        TipoEvento tipo = new TipoEvento();
        when(tipoEventoService.atualizar(eq(99L), any(TipoEvento.class))).thenThrow(new RuntimeException());

        mockMvc.perform(put("/api/tipos-evento/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tipo)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve deletar tipo de evento existente e retornar status 204")
    void deletar_Existente_DeveRetornarStatus204() throws Exception {
        mockMvc.perform(delete("/api/tipos-evento/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve falhar ao deletar tipo de evento inexistente e retornar 404")
    void deletar_Inexistente_DeveRetornarStatus404() throws Exception {
        when(tipoEventoService.atualizar(eq(99L), any(TipoEvento.class))).thenThrow(new RuntimeException());

        mockMvc.perform(delete("/api/tipos-evento/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve buscar por nome e retornar status 200")
    void buscarPorNome_DeveRetornarStatus200() throws Exception {
        when(tipoEventoService.buscarPorNome("Teste")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/tipos-evento/buscar").param("nome", "Teste"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve buscar por capacidade e retornar status 200")
    void buscarPorCapacidade_DeveRetornarStatus200() throws Exception {
        when(tipoEventoService.buscarPorCapacidade(50)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/tipos-evento/capacidade/50"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve falhar ao buscar tipo evento por ID inexistente e retornar 404")
    void buscarPorId_Inexistente_DeveRetornarStatus404_Controller() throws Exception {
        when(tipoEventoService.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tipos-evento/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve falhar ao atualizar tipo evento inexistente e retornar 404")
    void atualizar_Inexistente_DeveRetornarStatus404_Controller() throws Exception {
        TipoEvento tipo = new TipoEvento(); // Tipo válido para o corpo
        tipo.setNome("Nome Válido");
        // Simula o service lançando a exceção que o controller captura
        when(tipoEventoService.atualizar(eq(99L), any(TipoEvento.class))).thenThrow(new RuntimeException("Não encontrado"));

        mockMvc.perform(put("/api/tipos-evento/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tipo)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve falhar ao deletar tipo evento inexistente e retornar 404")
    void deletar_Inexistente_DeveRetornarStatus404_Controller() throws Exception {
        // Simula o service lançando a exceção que o controller captura
        doThrow(new RuntimeException("Não encontrado")).when(tipoEventoService).deletar(99L);

        mockMvc.perform(delete("/api/tipos-evento/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve falhar ao salvar tipo evento inválido (sem nome) e retornar 400")
    void salvar_Invalido_DeveRetornarStatus400() throws Exception {
        TipoEvento tipoInvalido = new TipoEvento(); // Sem nome

        mockMvc.perform(post("/api/tipos-evento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tipoInvalido)))
                .andExpect(status().isBadRequest()); // Espera erro de validação
    }
}