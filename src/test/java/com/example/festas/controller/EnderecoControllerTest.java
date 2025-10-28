package com.example.festas.controller;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import com.example.festas.entity.Endereco;
import com.example.festas.service.EnderecoService;
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


@WebMvcTest(controllers = EnderecoController.class,
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
class EnderecoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EnderecoService enderecoService;

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve buscar todos os endereços e retornar status 200")
    void buscarTodos_DeveRetornarListaComStatus200() throws Exception {
        when(enderecoService.buscarTodos()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/enderecos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve buscar endereço por ID existente e retornar status 200")
    void buscarPorId_Existente_DeveRetornarStatus200() throws Exception {
        Endereco endereco = new Endereco();
        endereco.setId(1L);
        when(enderecoService.buscarPorId(1L)).thenReturn(Optional.of(endereco));

        mockMvc.perform(get("/api/enderecos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve buscar endereço por ID inexistente e retornar status 404")
    void buscarPorId_Inexistente_DeveRetornarStatus404() throws Exception {
        when(enderecoService.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/enderecos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve salvar endereço válido e retornar status 201")
    void salvar_Valido_DeveRetornarStatus201() throws Exception {
        Endereco endereco = new Endereco();
        endereco.setId(1L);
        endereco.setRua("Rua Teste");
        endereco.setNumero("123");
        endereco.setBairro("Bairro");
        endereco.setCidade("Cidade");
        endereco.setEstado("SP");
        endereco.setCep("12345-678");

        when(enderecoService.salvar(any(Endereco.class))).thenReturn(endereco);

        mockMvc.perform(post("/api/enderecos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(endereco)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve atualizar endereço existente e retornar status 200")
    void atualizar_Existente_DeveRetornarStatus200() throws Exception {
        Endereco endereco = new Endereco();
        endereco.setId(1L);
        endereco.setRua("Rua Teste");
        endereco.setNumero("123");
        endereco.setBairro("Bairro");
        endereco.setCidade("Cidade");
        endereco.setEstado("SP");
        endereco.setCep("12345-678");

        when(enderecoService.atualizar(eq(1L), any(Endereco.class))).thenReturn(endereco);

        mockMvc.perform(put("/api/enderecos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(endereco)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve falhar ao atualizar endereço inexistente e retornar 404")
    void atualizar_Inexistente_DeveRetornarStatus404() throws Exception {
        Endereco endereco = new Endereco();
        endereco.setRua("Rua Teste");
        endereco.setNumero("123");
        endereco.setBairro("Centro");
        endereco.setCidade("Cidade Teste");
        endereco.setEstado("SP");
        endereco.setCep("12345-678");
        when(enderecoService.atualizar(eq(99L), any(Endereco.class))).thenThrow(new RuntimeException());

        mockMvc.perform(put("/api/enderecos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(endereco)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve deletar endereço existente e retornar status 204")
    void deletar_Existente_DeveRetornarStatus204() throws Exception {
        mockMvc.perform(delete("/api/enderecos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve falhar ao deletar endereço inexistente e retornar 404")
    void deletar_Inexistente_DeveRetornarStatus404() throws Exception {
        doThrow(new RuntimeException("Não encontrado")).when(enderecoService).deletar(99L);

        mockMvc.perform(delete("/api/enderecos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve buscar por cidade e retornar status 200")
    void buscarPorCidade_DeveRetornarStatus200() throws Exception {
        when(enderecoService.buscarPorCidade("Cidade")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/enderecos/cidade").param("cidade", "Cidade"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve buscar por estado e retornar status 200")
    void buscarPorEstado_DeveRetornarStatus200() throws Exception {
        when(enderecoService.buscarPorEstado("SP")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/enderecos/estado/SP"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve falhar ao buscar endereço por ID inexistente e retornar status 404")
    void buscarPorId_Inexistente_DeveRetornarStatus404_Controller() throws Exception {
        when(enderecoService.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/enderecos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve falhar ao atualizar endereço inexistente e retornar 404")
    void atualizar_Inexistente_DeveRetornarStatus404_Controller() throws Exception {
        Endereco endereco = new Endereco(); // Endereço válido para o corpo
        endereco.setRua("Rua Nova");
        endereco.setNumero("1");
        endereco.setBairro("B");
        endereco.setCidade("C");
        endereco.setEstado("E");
        endereco.setCep("1");
        // Simula o service lançando a exceção que o controller captura
        when(enderecoService.atualizar(eq(99L), any(Endereco.class))).thenThrow(new RuntimeException("Não encontrado"));

        mockMvc.perform(put("/api/enderecos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(endereco)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve falhar ao deletar endereço inexistente e retornar 404")
    void deletar_Inexistente_DeveRetornarStatus404_Controller() throws Exception {
        // Simula o service lançando a exceção que o controller captura
        doThrow(new RuntimeException("Não encontrado")).when(enderecoService).deletar(99L);

        mockMvc.perform(delete("/api/enderecos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve falhar ao salvar endereço inválido (sem rua) e retornar 400")
    void salvar_Invalido_DeveRetornarStatus400() throws Exception {
        Endereco enderecoInvalido = new Endereco(); // Sem rua
        enderecoInvalido.setNumero("123");
        enderecoInvalido.setBairro("Bairro");
        enderecoInvalido.setCidade("Cidade");
        enderecoInvalido.setEstado("SP");
        enderecoInvalido.setCep("12345-678");

        mockMvc.perform(post("/api/enderecos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enderecoInvalido)))
                .andExpect(status().isBadRequest()); // Espera erro de validação
    }
}