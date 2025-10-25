package com.example.festas.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SolicitacaoOrcamentoTest {

    private SolicitacaoOrcamento solicitacao;
    private Cliente cliente;
    private Endereco endereco;
    private TipoEvento tipoEvento;
    private TemaFesta temaFesta;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João Silva");
        cliente.setTelefone("11999999999");

        endereco = new Endereco();
        endereco.setId(1L);
        endereco.setRua("Rua das Flores");
        endereco.setNumero("123");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");

        tipoEvento = new TipoEvento();
        tipoEvento.setId(1L);
        tipoEvento.setNome("Aniversário");

        temaFesta = new TemaFesta();
        temaFesta.setId(1L);
        temaFesta.setNome("Festa do Super-Herói");

        solicitacao = new SolicitacaoOrcamento();
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com todos os campos válidos deve criar solicitação corretamente")
    void criarSolicitacao_TodosCamposValidos_DeveCriarCorretamente() {
        // Arrange & Act
        solicitacao.setId(1L);
        solicitacao.setCliente(cliente);
        solicitacao.setDataEvento(LocalDate.now().plusDays(30));
        solicitacao.setEndereco(endereco);
        solicitacao.setQuantidadeConvidados(50);
        solicitacao.setPrecisaMesasCadeiras(true);
        solicitacao.setTipoEvento(tipoEvento);
        solicitacao.setValorPretendido(new BigDecimal("5000.00"));
        solicitacao.setStatusOrcamento("PENDENTE");
        solicitacao.setDataCriacao(LocalDateTime.now());

        List<TemaFesta> temas = new ArrayList<>();
        temas.add(temaFesta);
        solicitacao.setTemas(temas);

        // Assert
        assertNotNull(solicitacao);
        assertEquals(1L, solicitacao.getId());
        assertEquals(cliente, solicitacao.getCliente());
        assertEquals(LocalDate.now().plusDays(30), solicitacao.getDataEvento());
        assertEquals(endereco, solicitacao.getEndereco());
        assertEquals(50, solicitacao.getQuantidadeConvidados());
        assertTrue(solicitacao.getPrecisaMesasCadeiras());
        assertEquals(tipoEvento, solicitacao.getTipoEvento());
        assertEquals(new BigDecimal("5000.00"), solicitacao.getValorPretendido());
        assertEquals("PENDENTE", solicitacao.getStatusOrcamento());
        assertNotNull(solicitacao.getDataCriacao());
        assertEquals(1, solicitacao.getTemas().size());
        assertEquals(temaFesta, solicitacao.getTemas().get(0));
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com campos nulos deve aceitar valores nulos")
    void criarSolicitacao_CamposNulos_DeveAceitarValoresNulos() {
        // Arrange & Act
        solicitacao.setId(null);
        solicitacao.setCliente(null);
        solicitacao.setDataEvento(null);
        solicitacao.setEndereco(null);
        solicitacao.setQuantidadeConvidados(null);
        solicitacao.setPrecisaMesasCadeiras(null);
        solicitacao.setTipoEvento(null);
        solicitacao.setValorPretendido(null);
        solicitacao.setStatusOrcamento(null);
        solicitacao.setDataCriacao(null);
        solicitacao.setTemas(null);

        // Assert
        assertNull(solicitacao.getId());
        assertNull(solicitacao.getCliente());
        assertNull(solicitacao.getDataEvento());
        assertNull(solicitacao.getEndereco());
        assertNull(solicitacao.getQuantidadeConvidados());
        assertNull(solicitacao.getPrecisaMesasCadeiras());
        assertNull(solicitacao.getTipoEvento());
        assertNull(solicitacao.getValorPretendido());
        assertNull(solicitacao.getStatusOrcamento());
        assertNull(solicitacao.getDataCriacao());
        assertNull(solicitacao.getTemas());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com quantidade de convidados zero deve ser aceito")
    void criarSolicitacao_QuantidadeConvidadosZero_DeveSerAceito() {
        // Arrange & Act
        solicitacao.setQuantidadeConvidados(0);

        // Assert
        assertEquals(0, solicitacao.getQuantidadeConvidados());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com quantidade de convidados negativa deve ser aceito")
    void criarSolicitacao_QuantidadeConvidadosNegativa_DeveSerAceito() {
        // Arrange & Act
        solicitacao.setQuantidadeConvidados(-10);

        // Assert
        assertEquals(-10, solicitacao.getQuantidadeConvidados());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com valor pretendido zero deve ser aceito")
    void criarSolicitacao_ValorPretendidoZero_DeveSerAceito() {
        // Arrange & Act
        solicitacao.setValorPretendido(BigDecimal.ZERO);

        // Assert
        assertEquals(BigDecimal.ZERO, solicitacao.getValorPretendido());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com valor pretendido negativo deve ser aceito")
    void criarSolicitacao_ValorPretendidoNegativo_DeveSerAceito() {
        // Arrange & Act
        solicitacao.setValorPretendido(new BigDecimal("-1000.00"));

        // Assert
        assertEquals(new BigDecimal("-1000.00"), solicitacao.getValorPretendido());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com múltiplos temas deve aceitar lista de temas")
    void criarSolicitacao_MultiplosTemas_DeveAceitarListaTemas() {
        // Arrange
        TemaFesta tema2 = new TemaFesta();
        tema2.setId(2L);
        tema2.setNome("Festa do Príncipe");

        List<TemaFesta> temas = new ArrayList<>();
        temas.add(temaFesta);
        temas.add(tema2);

        // Act
        solicitacao.setTemas(temas);

        // Assert
        assertEquals(2, solicitacao.getTemas().size());
        assertEquals(temaFesta, solicitacao.getTemas().get(0));
        assertEquals(tema2, solicitacao.getTemas().get(1));
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com status de orçamento válido deve aceitar diferentes status")
    void criarSolicitacao_StatusValidos_DeveAceitarDiferentesStatus() {
        // Arrange & Act
        solicitacao.setStatusOrcamento("PENDENTE");
        assertEquals("PENDENTE", solicitacao.getStatusOrcamento());

        solicitacao.setStatusOrcamento("APROVADO");
        assertEquals("APROVADO", solicitacao.getStatusOrcamento());

        solicitacao.setStatusOrcamento("REJEITADO");
        assertEquals("REJEITADO", solicitacao.getStatusOrcamento());

        solicitacao.setStatusOrcamento("CANCELADO");
        assertEquals("CANCELADO", solicitacao.getStatusOrcamento());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com data de evento no passado deve ser aceito")
    void criarSolicitacao_DataEventoPassado_DeveSerAceito() {
        // Arrange & Act
        LocalDate dataPassado = LocalDate.now().minusDays(10);
        solicitacao.setDataEvento(dataPassado);

        // Assert
        assertEquals(dataPassado, solicitacao.getDataEvento());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com data de evento muito distante deve ser aceito")
    void criarSolicitacao_DataEventoDistante_DeveSerAceito() {
        // Arrange & Act
        LocalDate dataFuturo = LocalDate.now().plusYears(2);
        solicitacao.setDataEvento(dataFuturo);

        // Assert
        assertEquals(dataFuturo, solicitacao.getDataEvento());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com precisão de mesas e cadeiras false deve ser aceito")
    void criarSolicitacao_PrecisaMesasCadeirasFalse_DeveSerAceito() {
        // Arrange & Act
        solicitacao.setPrecisaMesasCadeiras(false);

        // Assert
        assertFalse(solicitacao.getPrecisaMesasCadeiras());
    }
}
