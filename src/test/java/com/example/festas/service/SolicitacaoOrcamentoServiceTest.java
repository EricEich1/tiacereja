package com.example.festas.service;

import com.example.festas.entity.Cliente;
import com.example.festas.entity.SolicitacaoOrcamento;
import com.example.festas.repository.SolicitacaoOrcamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitacaoOrcamentoServiceTest {

    @Mock
    private SolicitacaoOrcamentoRepository solicitacaoRepository;

    @InjectMocks
    private SolicitacaoOrcamentoService solicitacaoService;

    private SolicitacaoOrcamento solicitacao;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João Silva");
        cliente.setTelefone("11999999999");

        solicitacao = new SolicitacaoOrcamento();
        solicitacao.setId(1L);
        solicitacao.setCliente(cliente);
        solicitacao.setDataEvento(LocalDate.now().plusDays(30));
        solicitacao.setQuantidadeConvidados(50);
        solicitacao.setValorPretendido(new BigDecimal("5000.00"));
        solicitacao.setStatusOrcamento("PENDENTE");
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com solicitação válida deve definir status como PENDENTE e data de criação")
    void salvar_SolicitacaoValida_DeveDefinirStatusPendenteEDataCriacao() {
        // Arrange
        when(solicitacaoRepository.save(any(SolicitacaoOrcamento.class))).thenReturn(solicitacao);

        // Act
        SolicitacaoOrcamento resultado = solicitacaoService.salvar(solicitacao);

        // Assert
        assertNotNull(resultado);
        assertEquals("PENDENTE", resultado.getStatusOrcamento());
        assertNotNull(resultado.getDataCriacao());
        verify(solicitacaoRepository, times(1)).save(solicitacao);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com solicitação sem cliente deve lançar exceção")
    void salvar_SolicitacaoSemCliente_DeveLancarExcecao() {
        // Arrange
        solicitacao.setCliente(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> solicitacaoService.salvar(solicitacao));
        
        assertEquals("Não é possível criar solicitação sem associar a um cliente", exception.getMessage());
        verify(solicitacaoRepository, never()).save(any(SolicitacaoOrcamento.class));
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com cliente sem ID deve lançar exceção")
    void salvar_ClienteSemId_DeveLancarExcecao() {
        // Arrange
        cliente.setId(null);
        solicitacao.setCliente(cliente);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> solicitacaoService.salvar(solicitacao));
        
        assertEquals("Não é possível criar solicitação sem associar a um cliente", exception.getMessage());
        verify(solicitacaoRepository, never()).save(any(SolicitacaoOrcamento.class));
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com status já definido deve manter o status original")
    void salvar_SolicitacaoComStatusDefinido_DeveManterStatusOriginal() {
        // Arrange
        solicitacao.setStatusOrcamento("APROVADO");
        when(solicitacaoRepository.save(any(SolicitacaoOrcamento.class))).thenReturn(solicitacao);

        // Act
        SolicitacaoOrcamento resultado = solicitacaoService.salvar(solicitacao);

        // Assert
        assertNotNull(resultado);
        assertEquals("APROVADO", resultado.getStatusOrcamento());
        assertNotNull(resultado.getDataCriacao());
        verify(solicitacaoRepository, times(1)).save(solicitacao);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de atualização com solicitação existente deve retornar solicitação atualizada")
    void atualizar_SolicitacaoExistente_DeveRetornarSolicitacaoAtualizada() {
        // Arrange
        Long id = 1L;
        SolicitacaoOrcamento solicitacaoAtualizada = new SolicitacaoOrcamento();
        solicitacaoAtualizada.setCliente(cliente);
        solicitacaoAtualizada.setDataEvento(LocalDate.now().plusDays(60));
        solicitacaoAtualizada.setQuantidadeConvidados(100);
        solicitacaoAtualizada.setValorPretendido(new BigDecimal("10000.00"));

        LocalDateTime dataCriacaoOriginal = LocalDateTime.now().minusDays(1);
        solicitacao.setDataCriacao(dataCriacaoOriginal);

        when(solicitacaoRepository.findById(id)).thenReturn(Optional.of(solicitacao));
        when(solicitacaoRepository.save(any(SolicitacaoOrcamento.class))).thenReturn(solicitacaoAtualizada);

        // Act
        SolicitacaoOrcamento resultado = solicitacaoService.atualizar(id, solicitacaoAtualizada);

        // Assert
        assertNotNull(resultado);
        assertEquals(100, resultado.getQuantidadeConvidados());
        assertEquals(new BigDecimal("10000.00"), resultado.getValorPretendido());
        assertNotNull(resultado.getDataCriacao());
        verify(solicitacaoRepository, times(1)).findById(id);
        verify(solicitacaoRepository, times(1)).save(any(SolicitacaoOrcamento.class));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de atualização com solicitação inexistente deve lançar exceção")
    void atualizar_SolicitacaoInexistente_DeveLancarExcecao() {
        // Arrange
        Long id = 999L;
        when(solicitacaoRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> solicitacaoService.atualizar(id, solicitacao));
        
        assertEquals("Solicitação não encontrada com ID: 999", exception.getMessage());
        verify(solicitacaoRepository, times(1)).findById(id);
        verify(solicitacaoRepository, never()).save(any(SolicitacaoOrcamento.class));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de deleção com solicitação existente deve deletar com sucesso")
    void deletar_SolicitacaoExistente_DeveDeletarComSucesso() {
        // Arrange
        Long id = 1L;
        when(solicitacaoRepository.existsById(id)).thenReturn(true);

        // Act
        assertDoesNotThrow(() -> solicitacaoService.deletar(id));

        // Assert
        verify(solicitacaoRepository, times(1)).existsById(id);
        verify(solicitacaoRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de deleção com solicitação inexistente deve lançar exceção")
    void deletar_SolicitacaoInexistente_DeveLancarExcecao() {
        // Arrange
        Long id = 999L;
        when(solicitacaoRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> solicitacaoService.deletar(id));
        
        assertEquals("Não é possível deletar solicitação inexistente", exception.getMessage());
        verify(solicitacaoRepository, times(1)).existsById(id);
        verify(solicitacaoRepository, never()).deleteById(id);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de busca por status deve retornar lista de solicitações")
    void buscarPorStatus_StatusValido_DeveRetornarListaSolicitacoes() {
        // Arrange
        String status = "PENDENTE";
        List<SolicitacaoOrcamento> solicitacoesEsperadas = Arrays.asList(solicitacao);
        when(solicitacaoRepository.findByStatusOrcamento(status)).thenReturn(solicitacoesEsperadas);

        // Act
        List<SolicitacaoOrcamento> resultado = solicitacaoService.buscarPorStatus(status);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("PENDENTE", resultado.get(0).getStatusOrcamento());
        verify(solicitacaoRepository, times(1)).findByStatusOrcamento(status);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de busca por cliente deve retornar lista ordenada por data")
    void buscarPorCliente_ClienteValido_DeveRetornarListaOrdenada() {
        // Arrange
        Long clienteId = 1L;
        List<SolicitacaoOrcamento> solicitacoesEsperadas = Arrays.asList(solicitacao);
        when(solicitacaoRepository.findByClienteIdOrderByDataCriacaoDesc(clienteId)).thenReturn(solicitacoesEsperadas);

        // Act
        List<SolicitacaoOrcamento> resultado = solicitacaoService.buscarPorCliente(clienteId);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(clienteId, resultado.get(0).getCliente().getId());
        verify(solicitacaoRepository, times(1)).findByClienteIdOrderByDataCriacaoDesc(clienteId);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de busca por ID com solicitação existente deve retornar solicitação")
    void buscarPorId_SolicitacaoExistente_DeveRetornarSolicitacao() {
        // Arrange
        Long id = 1L;
        when(solicitacaoRepository.findById(id)).thenReturn(Optional.of(solicitacao));

        // Act
        Optional<SolicitacaoOrcamento> resultado = solicitacaoService.buscarPorId(id);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        assertEquals(cliente.getId(), resultado.get().getCliente().getId());
        verify(solicitacaoRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de busca por ID com solicitação inexistente deve retornar Optional vazio")
    void buscarPorId_SolicitacaoInexistente_DeveRetornarOptionalVazio() {
        // Arrange
        Long id = 999L;
        when(solicitacaoRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<SolicitacaoOrcamento> resultado = solicitacaoService.buscarPorId(id);

        // Assert
        assertFalse(resultado.isPresent());
        verify(solicitacaoRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de busca de todas as solicitações deve retornar lista completa")
    void buscarTodos_DeveRetornarListaCompleta() {
        // Arrange
        SolicitacaoOrcamento solicitacao2 = new SolicitacaoOrcamento();
        solicitacao2.setId(2L);
        solicitacao2.setCliente(cliente);
        solicitacao2.setDataEvento(LocalDate.now().plusDays(60));
        solicitacao2.setQuantidadeConvidados(25);

        List<SolicitacaoOrcamento> solicitacoesEsperadas = Arrays.asList(solicitacao, solicitacao2);
        when(solicitacaoRepository.findAll()).thenReturn(solicitacoesEsperadas);

        // Act
        List<SolicitacaoOrcamento> resultado = solicitacaoService.buscarTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(solicitacaoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com quantidade de convidados zero deve ser aceito")
    void salvar_SolicitacaoComZeroConvidados_DeveSerAceita() {
        // Arrange
        solicitacao.setQuantidadeConvidados(0);
        when(solicitacaoRepository.save(any(SolicitacaoOrcamento.class))).thenReturn(solicitacao);

        // Act
        SolicitacaoOrcamento resultado = solicitacaoService.salvar(solicitacao);

        // Assert
        assertNotNull(resultado);
        assertEquals(0, resultado.getQuantidadeConvidados());
        assertEquals("PENDENTE", resultado.getStatusOrcamento());
        verify(solicitacaoRepository, times(1)).save(solicitacao);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com valor pretendido nulo deve ser aceito")
    void salvar_SolicitacaoComValorNulo_DeveSerAceita() {
        // Arrange
        solicitacao.setValorPretendido(null);
        when(solicitacaoRepository.save(any(SolicitacaoOrcamento.class))).thenReturn(solicitacao);

        // Act
        SolicitacaoOrcamento resultado = solicitacaoService.salvar(solicitacao);

        // Assert
        assertNotNull(resultado);
        assertNull(resultado.getValorPretendido());
        assertEquals("PENDENTE", resultado.getStatusOrcamento());
        verify(solicitacaoRepository, times(1)).save(solicitacao);
    }

        @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de validação de quantidade de solicitações por cliente")
    void validarQuantidadeSolicitacoesPorCliente_LimiteExcedido_DeveLancarExcecao() {
        // Arrange
        Long clienteId = 1L;
        List<SolicitacaoOrcamento> solicitacoesExistentes = Arrays.asList(
            solicitacao, solicitacao, solicitacao, solicitacao, solicitacao
        );
        when(solicitacaoRepository.findByClienteIdAndDataCriacaoBetween(
            eq(clienteId), 
            any(LocalDateTime.class), 
            any(LocalDateTime.class)
        )).thenReturn(solicitacoesExistentes);

        SolicitacaoOrcamento novaSolicitacao = new SolicitacaoOrcamento();
        novaSolicitacao.setCliente(cliente);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            solicitacaoService.validarLimiteSolicitacoesPorCliente(novaSolicitacao);
        });
        
        assertEquals("Cliente excedeu o limite de 5 solicitações por mês", exception.getMessage());
        verify(solicitacaoRepository, times(1))
            .findByClienteIdAndDataCriacaoBetween(
                eq(clienteId), 
                any(LocalDateTime.class), 
                any(LocalDateTime.class)
            );
    }
}
