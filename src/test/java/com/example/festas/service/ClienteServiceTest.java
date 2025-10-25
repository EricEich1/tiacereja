package com.example.festas.service;

import com.example.festas.entity.Cliente;
import com.example.festas.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;
    private Cliente clienteCompleto;
    private Cliente clienteIncompleto;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João Silva");
        cliente.setTelefone("11999999999");

        clienteCompleto = new Cliente();
        clienteCompleto.setId(2L);
        clienteCompleto.setNome("Maria Santos");
        clienteCompleto.setTelefone("11888888888");
        clienteCompleto.setStatusCadastro("COMPLETO");

        clienteIncompleto = new Cliente();
        clienteIncompleto.setId(3L);
        clienteIncompleto.setNome("Pedro Costa");
        clienteIncompleto.setTelefone(null);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com cliente válido e telefone preenchido deve definir status como COMPLETO")
    void salvar_ClienteComTelefone_DeveDefinirStatusCompleto() {
        // Arrange
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteCompleto);

        // Act
        Cliente resultado = clienteService.salvar(clienteCompleto);

        // Assert
        assertNotNull(resultado);
        assertEquals("COMPLETO", resultado.getStatusCadastro());
        verify(clienteRepository, times(1)).save(clienteCompleto);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com cliente sem telefone deve definir status como INCOMPLETO")
    void salvar_ClienteSemTelefone_DeveDefinirStatusIncompleto() {
        // Arrange
        clienteIncompleto.setTelefone("");
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteIncompleto);

        // Act
        Cliente resultado = clienteService.salvar(clienteIncompleto);

        // Assert
        assertNotNull(resultado);
        assertEquals("INCOMPLETO", resultado.getStatusCadastro());
        verify(clienteRepository, times(1)).save(clienteIncompleto);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com telefone nulo deve definir status como INCOMPLETO")
    void salvar_ClienteComTelefoneNulo_DeveDefinirStatusIncompleto() {
        // Arrange
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteIncompleto);

        // Act
        Cliente resultado = clienteService.salvar(clienteIncompleto);

        // Assert
        assertNotNull(resultado);
        assertEquals("INCOMPLETO", resultado.getStatusCadastro());
        verify(clienteRepository, times(1)).save(clienteIncompleto);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com telefone apenas com espaços deve definir status como INCOMPLETO")
    void salvar_ClienteComTelefoneComEspacos_DeveDefinirStatusIncompleto() {
        // Arrange
        clienteIncompleto.setTelefone("   ");
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteIncompleto);

        // Act
        Cliente resultado = clienteService.salvar(clienteIncompleto);

        // Assert
        assertNotNull(resultado);
        assertEquals("INCOMPLETO", resultado.getStatusCadastro());
        verify(clienteRepository, times(1)).save(clienteIncompleto);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de atualização com cliente existente deve retornar cliente atualizado")
    void atualizar_ClienteExistente_DeveRetornarClienteAtualizado() {
        // Arrange
        Long id = 1L;
        Cliente clienteAtualizado = new Cliente();
        clienteAtualizado.setNome("João Silva Atualizado");
        clienteAtualizado.setTelefone("11777777777");

        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteAtualizado);

        // Act
        Cliente resultado = clienteService.atualizar(id, clienteAtualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("João Silva Atualizado", resultado.getNome());
        assertEquals("11777777777", resultado.getTelefone());
        assertEquals("COMPLETO", resultado.getStatusCadastro());
        verify(clienteRepository, times(1)).findById(id);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de atualização com cliente inexistente deve lançar exceção")
    void atualizar_ClienteInexistente_DeveLancarExcecao() {
        // Arrange
        Long id = 999L;
        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> clienteService.atualizar(id, cliente));
        
        assertEquals("Cliente não encontrado com ID: 999", exception.getMessage());
        verify(clienteRepository, times(1)).findById(id);
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de deleção com cliente existente deve deletar com sucesso")
    void deletar_ClienteExistente_DeveDeletarComSucesso() {
        // Arrange
        Long id = 1L;
        when(clienteRepository.existsById(id)).thenReturn(true);

        // Act
        assertDoesNotThrow(() -> clienteService.deletar(id));

        // Assert
        verify(clienteRepository, times(1)).existsById(id);
        verify(clienteRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de deleção com cliente inexistente deve lançar exceção")
    void deletar_ClienteInexistente_DeveLancarExcecao() {
        // Arrange
        Long id = 999L;
        when(clienteRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> clienteService.deletar(id));
        
        assertEquals("Não é possível deletar cliente inexistente", exception.getMessage());
        verify(clienteRepository, times(1)).existsById(id);
        verify(clienteRepository, never()).deleteById(id);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de busca por nome deve retornar lista de clientes")
    void buscarPorNome_NomeValido_DeveRetornarListaClientes() {
        // Arrange
        String nome = "João";
        List<Cliente> clientesEsperados = Arrays.asList(cliente);
        when(clienteRepository.findByNomeIgnoreCaseContaining(nome)).thenReturn(clientesEsperados);

        // Act
        List<Cliente> resultado = clienteService.buscarPorNome(nome);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("João Silva", resultado.get(0).getNome());
        verify(clienteRepository, times(1)).findByNomeIgnoreCaseContaining(nome);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de busca por telefone deve retornar cliente")
    void buscarPorTelefone_TelefoneValido_DeveRetornarCliente() {
        // Arrange
        String telefone = "11999999999";
        when(clienteRepository.findByTelefone(telefone)).thenReturn(cliente);

        // Act
        Cliente resultado = clienteService.buscarPorTelefone(telefone);

        // Assert
        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        assertEquals("11999999999", resultado.getTelefone());
        verify(clienteRepository, times(1)).findByTelefone(telefone);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de busca por status deve retornar lista de clientes")
    void buscarPorStatus_StatusValido_DeveRetornarListaClientes() {
        // Arrange
        String status = "COMPLETO";
        List<Cliente> clientesEsperados = Arrays.asList(clienteCompleto);
        when(clienteRepository.findByStatusCadastro(status)).thenReturn(clientesEsperados);

        // Act
        List<Cliente> resultado = clienteService.buscarPorStatus(status);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("COMPLETO", resultado.get(0).getStatusCadastro());
        verify(clienteRepository, times(1)).findByStatusCadastro(status);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de busca por ID com cliente existente deve retornar cliente")
    void buscarPorId_ClienteExistente_DeveRetornarCliente() {
        // Arrange
        Long id = 1L;
        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));

        // Act
        Optional<Cliente> resultado = clienteService.buscarPorId(id);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("João Silva", resultado.get().getNome());
        verify(clienteRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de busca por ID com cliente inexistente deve retornar Optional vazio")
    void buscarPorId_ClienteInexistente_DeveRetornarOptionalVazio() {
        // Arrange
        Long id = 999L;
        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Cliente> resultado = clienteService.buscarPorId(id);

        // Assert
        assertFalse(resultado.isPresent());
        verify(clienteRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Cenário de busca de todos os clientes deve retornar lista completa")
    void buscarTodos_DeveRetornarListaCompleta() {
        // Arrange
        List<Cliente> clientesEsperados = Arrays.asList(cliente, clienteCompleto, clienteIncompleto);
        when(clienteRepository.findAll()).thenReturn(clientesEsperados);

        // Act
        List<Cliente> resultado = clienteService.buscarTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        verify(clienteRepository, times(1)).findAll();
    }
}
