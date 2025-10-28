package com.example.festas.controller;

import com.example.festas.entity.Cliente;
import com.example.festas.entity.Endereco;
import com.example.festas.entity.SolicitacaoOrcamento;
import com.example.festas.entity.TipoEvento;
import com.example.festas.service.SolicitacaoOrcamentoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.containsString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = SolicitacaoOrcamentoController.class,
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
class SolicitacaoOrcamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SolicitacaoOrcamentoService solicitacaoService;

    @Autowired
    private ObjectMapper objectMapper;

    private SolicitacaoOrcamento solicitacao;
    private Cliente cliente;
    private Endereco endereco;
    private TipoEvento tipoEvento;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João Silva");
        cliente.setTelefone("11999999999");

        endereco = new Endereco();
        endereco.setId(1L);
        endereco.setRua("Rua Teste");
        endereco.setNumero("123");
        endereco.setBairro("Centro");
        endereco.setCidade("Cidade Teste");
        endereco.setEstado("SP");
        endereco.setCep("12345-678");

        tipoEvento = new TipoEvento();
        tipoEvento.setId(1L);
        tipoEvento.setNome("Aniversário");

        solicitacao = new SolicitacaoOrcamento();
        solicitacao.setId(1L);
        solicitacao.setCliente(cliente);
        solicitacao.setDataEvento(LocalDate.now().plusDays(30));
        solicitacao.setQuantidadeConvidados(50);
        solicitacao.setValorPretendido(new BigDecimal("5000.00"));
        solicitacao.setStatusOrcamento("PENDENTE");
        solicitacao.setDataCriacao(LocalDateTime.now());
        solicitacao.setEndereco(endereco);
        solicitacao.setTipoEvento(tipoEvento);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de busca de todas as solicitações deve retornar lista com status 200")
    void buscarTodos_DeveRetornarListaComStatus200() throws Exception {
        // Arrange
        List<SolicitacaoOrcamento> solicitacoes = Arrays.asList(solicitacao);
        when(solicitacaoService.buscarTodos()).thenReturn(solicitacoes);

        // Act & Assert
        mockMvc.perform(get("/api/solicitacoes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].statusOrcamento").value("PENDENTE"));

        verify(solicitacaoService, times(1)).buscarTodos();
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de busca por ID com solicitação existente deve retornar solicitação com status 200")
    void buscarPorId_SolicitacaoExistente_DeveRetornarSolicitacaoComStatus200() throws Exception {
        // Arrange
        Long id = 1L;
        when(solicitacaoService.buscarPorId(id)).thenReturn(Optional.of(solicitacao));

        // Act & Assert
        mockMvc.perform(get("/api/solicitacoes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.statusOrcamento").value("PENDENTE"))
                .andExpect(jsonPath("$.quantidadeConvidados").value(50))
                .andExpect(jsonPath("$.valorPretendido").value(5000.00));

        verify(solicitacaoService, times(1)).buscarPorId(id);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de busca por ID com solicitação inexistente deve retornar status 404")
    void buscarPorId_SolicitacaoInexistente_DeveRetornarStatus404() throws Exception {
        // Arrange
        Long id = 999L;
        when(solicitacaoService.buscarPorId(id)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/solicitacoes/{id}", id))
                .andExpect(status().isNotFound());

        verify(solicitacaoService, times(1)).buscarPorId(id);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de criação de solicitação válida deve retornar solicitação criada com status 201")
    void salvar_SolicitacaoValida_DeveRetornarSolicitacaoCriadaComStatus201() throws Exception {
        // Arrange
        SolicitacaoOrcamento novaSolicitacao = new SolicitacaoOrcamento();
        novaSolicitacao.setCliente(cliente);
        novaSolicitacao.setDataEvento(LocalDate.now().plusDays(30));
        novaSolicitacao.setQuantidadeConvidados(25);
        novaSolicitacao.setValorPretendido(new BigDecimal("3000.00"));
        novaSolicitacao.setEndereco(endereco);
        novaSolicitacao.setTipoEvento(tipoEvento);

        SolicitacaoOrcamento solicitacaoSalva = new SolicitacaoOrcamento();
        solicitacaoSalva.setId(2L);
        solicitacaoSalva.setCliente(cliente);
        solicitacaoSalva.setDataEvento(LocalDate.now().plusDays(30));
        solicitacaoSalva.setQuantidadeConvidados(25);
        solicitacaoSalva.setValorPretendido(new BigDecimal("3000.00"));
        solicitacaoSalva.setStatusOrcamento("PENDENTE");
        solicitacaoSalva.setDataCriacao(LocalDateTime.now());
        solicitacaoSalva.setEndereco(endereco);
        solicitacaoSalva.setTipoEvento(tipoEvento);

        when(solicitacaoService.salvar(any(SolicitacaoOrcamento.class))).thenReturn(solicitacaoSalva);

        // Act & Assert
        mockMvc.perform(post("/api/solicitacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novaSolicitacao)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.statusOrcamento").value("PENDENTE"))
                .andExpect(jsonPath("$.quantidadeConvidados").value(25))
                .andExpect(jsonPath("$.valorPretendido").value(3000.00));

        verify(solicitacaoService, times(1)).salvar(any(SolicitacaoOrcamento.class));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de criação de solicitação sem cliente deve retornar status 400")
    void salvar_SolicitacaoSemCliente_DeveRetornarStatus400() throws Exception {
        // Arrange
        SolicitacaoOrcamento solicitacaoInvalida = new SolicitacaoOrcamento();
        solicitacaoInvalida.setDataEvento(LocalDate.now().plusDays(30));
        solicitacaoInvalida.setQuantidadeConvidados(25);
        solicitacaoInvalida.setEndereco(endereco);
        solicitacaoInvalida.setTipoEvento(tipoEvento);
        // Cliente não definido - inválido

        // Act & Assert
        mockMvc.perform(post("/api/solicitacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(solicitacaoInvalida)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de atualização com solicitação existente deve retornar solicitação atualizada com status 200")
    void atualizar_SolicitacaoExistente_DeveRetornarSolicitacaoAtualizadaComStatus200() throws Exception {
        // Arrange
        Long id = 1L;
        SolicitacaoOrcamento solicitacaoAtualizada = new SolicitacaoOrcamento();
        solicitacaoAtualizada.setCliente(cliente);
        solicitacaoAtualizada.setDataEvento(LocalDate.now().plusDays(60));
        solicitacaoAtualizada.setQuantidadeConvidados(100);
        solicitacaoAtualizada.setValorPretendido(new BigDecimal("10000.00"));
        solicitacaoAtualizada.setEndereco(endereco);
        solicitacaoAtualizada.setTipoEvento(tipoEvento);

        SolicitacaoOrcamento solicitacaoRetornada = new SolicitacaoOrcamento();
        solicitacaoRetornada.setId(id);
        solicitacaoRetornada.setCliente(cliente);
        solicitacaoRetornada.setDataEvento(LocalDate.now().plusDays(60));
        solicitacaoRetornada.setQuantidadeConvidados(100);
        solicitacaoRetornada.setValorPretendido(new BigDecimal("10000.00"));
        solicitacaoRetornada.setStatusOrcamento("PENDENTE");
        solicitacaoRetornada.setEndereco(endereco);
        solicitacaoRetornada.setTipoEvento(tipoEvento);

        when(solicitacaoService.atualizar(eq(id), any(SolicitacaoOrcamento.class))).thenReturn(solicitacaoRetornada);

        // Act & Assert
        mockMvc.perform(put("/api/solicitacoes/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(solicitacaoAtualizada)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.quantidadeConvidados").value(100))
                .andExpect(jsonPath("$.valorPretendido").value(10000.00));

        verify(solicitacaoService, times(1)).atualizar(eq(id), any(SolicitacaoOrcamento.class));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de atualização com solicitação inexistente deve retornar status 404")
    void atualizar_SolicitacaoInexistente_DeveRetornarStatus404() throws Exception {
        // Arrange
        Long id = 999L;
        SolicitacaoOrcamento solicitacaoAtualizada = new SolicitacaoOrcamento();
        solicitacaoAtualizada.setCliente(cliente);
        solicitacaoAtualizada.setDataEvento(LocalDate.now().plusDays(60));
        solicitacaoAtualizada.setQuantidadeConvidados(100);
        solicitacaoAtualizada.setEndereco(endereco);
        solicitacaoAtualizada.setTipoEvento(tipoEvento);

        when(solicitacaoService.atualizar(eq(id), any(SolicitacaoOrcamento.class)))
                .thenThrow(new RuntimeException("Solicitação não encontrada com ID: 999"));

        // Act & Assert
        mockMvc.perform(put("/api/solicitacoes/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(solicitacaoAtualizada)))
                .andExpect(status().isNotFound());

        verify(solicitacaoService, times(1)).atualizar(eq(id), any(SolicitacaoOrcamento.class));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de deleção com solicitação existente deve retornar status 204")
    void deletar_SolicitacaoExistente_DeveRetornarStatus204() throws Exception {
        // Arrange
        Long id = 1L;
        doNothing().when(solicitacaoService).deletar(id);

        // Act & Assert
        mockMvc.perform(delete("/api/solicitacoes/{id}", id))
                .andExpect(status().isNoContent());

        verify(solicitacaoService, times(1)).deletar(id);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de deleção com solicitação inexistente deve retornar status 404")
    void deletar_SolicitacaoInexistente_DeveRetornarStatus404() throws Exception {
        // Arrange
        Long id = 999L;
        doThrow(new RuntimeException("Não é possível deletar solicitação inexistente"))
                .when(solicitacaoService).deletar(id);

        // Act & Assert
        mockMvc.perform(delete("/api/solicitacoes/{id}", id))
                .andExpect(status().isNotFound());

        verify(solicitacaoService, times(1)).deletar(id);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de busca por status deve retornar lista de solicitações com status 200")
    void buscarPorStatus_StatusValido_DeveRetornarListaComStatus200() throws Exception {
        // Arrange
        String status = "PENDENTE";
        List<SolicitacaoOrcamento> solicitacoes = Arrays.asList(solicitacao);
        when(solicitacaoService.buscarPorStatus(status)).thenReturn(solicitacoes);

        // Act & Assert
        mockMvc.perform(get("/api/solicitacoes/status")
                .param("status", status))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].statusOrcamento").value("PENDENTE"));

        verify(solicitacaoService, times(1)).buscarPorStatus(status);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de busca por cliente deve retornar lista de solicitações com status 200")
    void buscarPorCliente_ClienteValido_DeveRetornarListaComStatus200() throws Exception {
        // Arrange
        Long clienteId = 1L;
        List<SolicitacaoOrcamento> solicitacoes = Arrays.asList(solicitacao);
        when(solicitacaoService.buscarPorCliente(clienteId)).thenReturn(solicitacoes);

        // Act & Assert
        mockMvc.perform(get("/api/solicitacoes/cliente/{clienteId}", clienteId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].cliente.id").value(1));

        verify(solicitacaoService, times(1)).buscarPorCliente(clienteId);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de busca por status inexistente deve retornar lista vazia com status 200")
    void buscarPorStatus_StatusInexistente_DeveRetornarListaVaziaComStatus200() throws Exception {
        // Arrange
        String status = "INEXISTENTE";
        List<SolicitacaoOrcamento> solicitacoesVazias = Arrays.asList();
        when(solicitacaoService.buscarPorStatus(status)).thenReturn(solicitacoesVazias);

        // Act & Assert
        mockMvc.perform(get("/api/solicitacoes/status")
                .param("status", status))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(solicitacaoService, times(1)).buscarPorStatus(status);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de busca por cliente inexistente deve retornar lista vazia com status 200")
    void buscarPorCliente_ClienteInexistente_DeveRetornarListaVaziaComStatus200() throws Exception {
        // Arrange
        Long clienteId = 999L;
        List<SolicitacaoOrcamento> solicitacoesVazias = Arrays.asList();
        when(solicitacaoService.buscarPorCliente(clienteId)).thenReturn(solicitacoesVazias);

        // Act & Assert
        mockMvc.perform(get("/api/solicitacoes/cliente/{clienteId}", clienteId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(solicitacaoService, times(1)).buscarPorCliente(clienteId);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de criação de solicitação com dados inválidos deve retornar status 400")
    void salvar_SolicitacaoComDadosInvalidos_DeveRetornarStatus400() throws Exception {
        // Arrange
        SolicitacaoOrcamento solicitacaoInvalida = new SolicitacaoOrcamento();
        solicitacaoInvalida.setCliente(cliente);
        // Data do evento não definida - inválido
        // Quantidade de convidados não definida - inválido

        // Act & Assert
        mockMvc.perform(post("/api/solicitacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(solicitacaoInvalida)))
                .andExpect(status().isBadRequest());

        verify(solicitacaoService, never()).salvar(any(SolicitacaoOrcamento.class));
    }

        @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de validação de dados obrigatórios da solicitação")
    void salvar_SolicitacaoComDadosObrigatoriosAusentes_DeveRetornarStatus400ComErros() throws Exception {
        // Arrange
        SolicitacaoOrcamento solicitacaoInvalida = new SolicitacaoOrcamento();
        // Não define nenhum campo obrigatório

        // Act & Assert
        mockMvc.perform(post("/api/solicitacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(solicitacaoInvalida)))
                .andExpect(status().isBadRequest());

        verify(solicitacaoService, never()).salvar(any(SolicitacaoOrcamento.class));
    }
}
