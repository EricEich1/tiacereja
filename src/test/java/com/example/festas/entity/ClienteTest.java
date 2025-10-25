package com.example.festas.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    private Cliente cliente;
    private SolicitacaoOrcamento solicitacao;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        solicitacao = new SolicitacaoOrcamento();
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com todos os campos válidos deve criar cliente corretamente")
    void criarCliente_TodosCamposValidos_DeveCriarCorretamente() {
        // Arrange & Act
        cliente.setId(1L);
        cliente.setNome("João Silva");
        cliente.setTelefone("11999999999");
        cliente.setStatusCadastro("COMPLETO");

        List<SolicitacaoOrcamento> solicitacoes = new ArrayList<>();
        solicitacao.setId(1L);
        solicitacoes.add(solicitacao);
        cliente.setSolicitacoes(solicitacoes);

        // Assert
        assertNotNull(cliente);
        assertEquals(1L, cliente.getId());
        assertEquals("João Silva", cliente.getNome());
        assertEquals("11999999999", cliente.getTelefone());
        assertEquals("COMPLETO", cliente.getStatusCadastro());
        assertEquals(1, cliente.getSolicitacoes().size());
        assertEquals(solicitacao, cliente.getSolicitacoes().get(0));
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com campos nulos deve aceitar valores nulos")
    void criarCliente_CamposNulos_DeveAceitarValoresNulos() {
        // Arrange & Act
        cliente.setId(null);
        cliente.setNome(null);
        cliente.setTelefone(null);
        cliente.setStatusCadastro(null);
        cliente.setSolicitacoes(null);

        // Assert
        assertNull(cliente.getId());
        assertNull(cliente.getNome());
        assertNull(cliente.getTelefone());
        assertNull(cliente.getStatusCadastro());
        assertNull(cliente.getSolicitacoes());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com nome vazio deve ser aceito")
    void criarCliente_NomeVazio_DeveSerAceito() {
        // Arrange & Act
        cliente.setNome("");

        // Assert
        assertEquals("", cliente.getNome());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com nome apenas com espaços deve ser aceito")
    void criarCliente_NomeComEspacos_DeveSerAceito() {
        // Arrange & Act
        cliente.setNome("   ");

        // Assert
        assertEquals("   ", cliente.getNome());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com telefone vazio deve ser aceito")
    void criarCliente_TelefoneVazio_DeveSerAceito() {
        // Arrange & Act
        cliente.setTelefone("");

        // Assert
        assertEquals("", cliente.getTelefone());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com telefone apenas com espaços deve ser aceito")
    void criarCliente_TelefoneComEspacos_DeveSerAceito() {
        // Arrange & Act
        cliente.setTelefone("   ");

        // Assert
        assertEquals("   ", cliente.getTelefone());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com telefone com caracteres especiais deve ser aceito")
    void criarCliente_TelefoneComCaracteresEspeciais_DeveSerAceito() {
        // Arrange & Act
        cliente.setTelefone("(11) 99999-9999");

        // Assert
        assertEquals("(11) 99999-9999", cliente.getTelefone());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com telefone internacional deve ser aceito")
    void criarCliente_TelefoneInternacional_DeveSerAceito() {
        // Arrange & Act
        cliente.setTelefone("+55 11 99999-9999");

        // Assert
        assertEquals("+55 11 99999-9999", cliente.getTelefone());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com status de cadastro válido deve aceitar diferentes status")
    void criarCliente_StatusValidos_DeveAceitarDiferentesStatus() {
        // Arrange & Act
        cliente.setStatusCadastro("COMPLETO");
        assertEquals("COMPLETO", cliente.getStatusCadastro());

        cliente.setStatusCadastro("INCOMPLETO");
        assertEquals("INCOMPLETO", cliente.getStatusCadastro());

        cliente.setStatusCadastro("PENDENTE");
        assertEquals("PENDENTE", cliente.getStatusCadastro());

        cliente.setStatusCadastro("CANCELADO");
        assertEquals("CANCELADO", cliente.getStatusCadastro());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com múltiplas solicitações deve aceitar lista de solicitações")
    void criarCliente_MultiplasSolicitacoes_DeveAceitarListaSolicitacoes() {
        // Arrange
        SolicitacaoOrcamento solicitacao2 = new SolicitacaoOrcamento();
        solicitacao2.setId(2L);

        List<SolicitacaoOrcamento> solicitacoes = new ArrayList<>();
        solicitacoes.add(solicitacao);
        solicitacoes.add(solicitacao2);

        // Act
        cliente.setSolicitacoes(solicitacoes);

        // Assert
        assertEquals(2, cliente.getSolicitacoes().size());
        assertEquals(solicitacao, cliente.getSolicitacoes().get(0));
        assertEquals(solicitacao2, cliente.getSolicitacoes().get(1));
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com lista vazia de solicitações deve ser aceito")
    void criarCliente_ListaVaziaSolicitacoes_DeveSerAceito() {
        // Arrange & Act
        List<SolicitacaoOrcamento> solicitacoesVazias = new ArrayList<>();
        cliente.setSolicitacoes(solicitacoesVazias);

        // Assert
        assertNotNull(cliente.getSolicitacoes());
        assertEquals(0, cliente.getSolicitacoes().size());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com ID negativo deve ser aceito")
    void criarCliente_IdNegativo_DeveSerAceito() {
        // Arrange & Act
        cliente.setId(-1L);

        // Assert
        assertEquals(-1L, cliente.getId());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com ID zero deve ser aceito")
    void criarCliente_IdZero_DeveSerAceito() {
        // Arrange & Act
        cliente.setId(0L);

        // Assert
        assertEquals(0L, cliente.getId());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com nome muito longo deve ser aceito")
    void criarCliente_NomeMuitoLongo_DeveSerAceito() {
        // Arrange & Act
        String nomeLongo = "A".repeat(1000);
        cliente.setNome(nomeLongo);

        // Assert
        assertEquals(nomeLongo, cliente.getNome());
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com telefone muito longo deve ser aceito")
    void criarCliente_TelefoneMuitoLongo_DeveSerAceito() {
        // Arrange & Act
        String telefoneLongo = "1".repeat(100);
        cliente.setTelefone(telefoneLongo);

        // Assert
        assertEquals(telefoneLongo, cliente.getTelefone());
    }
}
