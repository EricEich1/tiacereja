package com.example.festas.controller;

import com.example.festas.entity.Cliente;
import com.example.festas.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    private Cliente cliente;
    private Cliente clienteCompleto;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João Silva");
        cliente.setTelefone("11999999999");
        cliente.setStatusCadastro("COMPLETO");

        clienteCompleto = new Cliente();
        clienteCompleto.setId(2L);
        clienteCompleto.setNome("Maria Santos");
        clienteCompleto.setTelefone("11888888888");
        clienteCompleto.setStatusCadastro("COMPLETO");
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de busca de todos os clientes deve retornar lista com status 200")
    void buscarTodos_DeveRetornarListaComStatus200() throws Exception {
        // Arrange
        List<Cliente> clientes = Arrays.asList(cliente, clienteCompleto);
        when(clienteService.buscarTodos()).thenReturn(clientes);

        // Act & Assert
        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("João Silva"))
                .andExpect(jsonPath("$[1].nome").value("Maria Santos"));

        verify(clienteService, times(1)).buscarTodos();
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de busca por ID com cliente existente deve retornar cliente com status 200")
    void buscarPorId_ClienteExistente_DeveRetornarClienteComStatus200() throws Exception {
        // Arrange
        Long id = 1L;
        when(clienteService.buscarPorId(id)).thenReturn(Optional.of(cliente));

        // Act & Assert
        mockMvc.perform(get("/api/clientes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.telefone").value("11999999999"))
                .andExpect(jsonPath("$.statusCadastro").value("COMPLETO"));

        verify(clienteService, times(1)).buscarPorId(id);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de busca por ID com cliente inexistente deve retornar status 404")
    void buscarPorId_ClienteInexistente_DeveRetornarStatus404() throws Exception {
        // Arrange
        Long id = 999L;
        when(clienteService.buscarPorId(id)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/clientes/{id}", id))
                .andExpect(status().isNotFound());

        verify(clienteService, times(1)).buscarPorId(id);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de criação de cliente válido deve retornar cliente criado com status 201")
    void salvar_ClienteValido_DeveRetornarClienteCriadoComStatus201() throws Exception {
        // Arrange
        Cliente novoCliente = new Cliente();
        novoCliente.setNome("Pedro Costa");
        novoCliente.setTelefone("11777777777");

        Cliente clienteSalvo = new Cliente();
        clienteSalvo.setId(3L);
        clienteSalvo.setNome("Pedro Costa");
        clienteSalvo.setTelefone("11777777777");
        clienteSalvo.setStatusCadastro("COMPLETO");

        when(clienteService.salvar(any(Cliente.class))).thenReturn(clienteSalvo);

        // Act & Assert
        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novoCliente)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.nome").value("Pedro Costa"))
                .andExpect(jsonPath("$.telefone").value("11777777777"))
                .andExpect(jsonPath("$.statusCadastro").value("COMPLETO"));

        verify(clienteService, times(1)).salvar(any(Cliente.class));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de criação de cliente com dados inválidos deve retornar status 400")
    void salvar_ClienteComDadosInvalidos_DeveRetornarStatus400() throws Exception {
        // Arrange
        Cliente clienteInvalido = new Cliente();
        clienteInvalido.setNome(""); // Nome vazio - inválido
        clienteInvalido.setTelefone(""); // Telefone vazio - inválido

        // Act & Assert
        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteInvalido)))
                .andExpect(status().isBadRequest());

        verify(clienteService, never()).salvar(any(Cliente.class));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de atualização com cliente existente deve retornar cliente atualizado com status 200")
    void atualizar_ClienteExistente_DeveRetornarClienteAtualizadoComStatus200() throws Exception {
        // Arrange
        Long id = 1L;
        Cliente clienteAtualizado = new Cliente();
        clienteAtualizado.setNome("João Silva Atualizado");
        clienteAtualizado.setTelefone("11666666666");

        Cliente clienteRetornado = new Cliente();
        clienteRetornado.setId(id);
        clienteRetornado.setNome("João Silva Atualizado");
        clienteRetornado.setTelefone("11666666666");
        clienteRetornado.setStatusCadastro("COMPLETO");

        when(clienteService.atualizar(eq(id), any(Cliente.class))).thenReturn(clienteRetornado);

        // Act & Assert
        mockMvc.perform(put("/api/clientes/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteAtualizado)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João Silva Atualizado"))
                .andExpect(jsonPath("$.telefone").value("11666666666"));

        verify(clienteService, times(1)).atualizar(eq(id), any(Cliente.class));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de atualização com cliente inexistente deve retornar status 404")
    void atualizar_ClienteInexistente_DeveRetornarStatus404() throws Exception {
        // Arrange
        Long id = 999L;
        Cliente clienteAtualizado = new Cliente();
        clienteAtualizado.setNome("Cliente Inexistente");
        clienteAtualizado.setTelefone("11555555555");

        when(clienteService.atualizar(eq(id), any(Cliente.class)))
                .thenThrow(new RuntimeException("Cliente não encontrado com ID: 999"));

        // Act & Assert
        mockMvc.perform(put("/api/clientes/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteAtualizado)))
                .andExpect(status().isNotFound());

        verify(clienteService, times(1)).atualizar(eq(id), any(Cliente.class));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de deleção com cliente existente deve retornar status 204")
    void deletar_ClienteExistente_DeveRetornarStatus204() throws Exception {
        // Arrange
        Long id = 1L;
        doNothing().when(clienteService).deletar(id);

        // Act & Assert
        mockMvc.perform(delete("/api/clientes/{id}", id))
                .andExpect(status().isNoContent());

        verify(clienteService, times(1)).deletar(id);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de deleção com cliente inexistente deve retornar status 404")
    void deletar_ClienteInexistente_DeveRetornarStatus404() throws Exception {
        // Arrange
        Long id = 999L;
        doThrow(new RuntimeException("Não é possível deletar cliente inexistente"))
                .when(clienteService).deletar(id);

        // Act & Assert
        mockMvc.perform(delete("/api/clientes/{id}", id))
                .andExpect(status().isNotFound());

        verify(clienteService, times(1)).deletar(id);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de busca por nome deve retornar lista de clientes com status 200")
    void buscarPorNome_NomeValido_DeveRetornarListaComStatus200() throws Exception {
        // Arrange
        String nome = "João";
        List<Cliente> clientes = Arrays.asList(cliente);
        when(clienteService.buscarPorNome(nome)).thenReturn(clientes);

        // Act & Assert
        mockMvc.perform(get("/api/clientes/buscar")
                .param("nome", nome))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nome").value("João Silva"));

        verify(clienteService, times(1)).buscarPorNome(nome);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de busca por telefone com cliente existente deve retornar cliente com status 200")
    void buscarPorTelefone_ClienteExistente_DeveRetornarClienteComStatus200() throws Exception {
        // Arrange
        String telefone = "11999999999";
        when(clienteService.buscarPorTelefone(telefone)).thenReturn(cliente);

        // Act & Assert
        mockMvc.perform(get("/api/clientes/telefone")
                .param("telefone", telefone))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.telefone").value("11999999999"));

        verify(clienteService, times(1)).buscarPorTelefone(telefone);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de busca por telefone com cliente inexistente deve retornar status 404")
    void buscarPorTelefone_ClienteInexistente_DeveRetornarStatus404() throws Exception {
        // Arrange
        String telefone = "99999999999";
        when(clienteService.buscarPorTelefone(telefone)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/clientes/telefone")
                .param("telefone", telefone))
                .andExpect(status().isNotFound());

        verify(clienteService, times(1)).buscarPorTelefone(telefone);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de busca por status deve retornar lista de clientes com status 200")
    void buscarPorStatus_StatusValido_DeveRetornarListaComStatus200() throws Exception {
        // Arrange
        String status = "COMPLETO";
        List<Cliente> clientes = Arrays.asList(cliente, clienteCompleto);
        when(clienteService.buscarPorStatus(status)).thenReturn(clientes);

        // Act & Assert
        mockMvc.perform(get("/api/clientes/status/{status}", status))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].statusCadastro").value("COMPLETO"))
                .andExpect(jsonPath("$[1].statusCadastro").value("COMPLETO"));

        verify(clienteService, times(1)).buscarPorStatus(status);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de busca por nome vazio deve retornar lista vazia com status 200")
    void buscarPorNome_NomeVazio_DeveRetornarListaVaziaComStatus200() throws Exception {
        // Arrange
        String nome = "";
        List<Cliente> clientesVazios = Arrays.asList();
        when(clienteService.buscarPorNome(nome)).thenReturn(clientesVazios);

        // Act & Assert
        mockMvc.perform(get("/api/clientes/buscar")
                .param("nome", nome))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(clienteService, times(1)).buscarPorNome(nome);
    }
}
