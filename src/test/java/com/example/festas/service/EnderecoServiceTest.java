package com.example.festas.service;

import com.example.festas.entity.Endereco;
import com.example.festas.repository.EnderecoRepository;
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
class EnderecoServiceTest {

    @Mock
    private EnderecoRepository enderecoRepository;

    @InjectMocks
    private EnderecoService enderecoService;

    private Endereco endereco;

    @BeforeEach
    void setUp() {
        endereco = new Endereco();
        endereco.setId(1L);
        endereco.setRua("Rua A");
        endereco.setNumero("123");
        endereco.setBairro("Centro");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        endereco.setCep("01310-100");
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de busca de todos os endereços deve retornar lista não vazia")
    void buscarTodos_DeveRetornarListaNaoVazia() {
        // Arrange
        List<Endereco> enderecosEsperados = Arrays.asList(endereco);
        when(enderecoRepository.findAll()).thenReturn(enderecosEsperados);

        // Act
        List<Endereco> enderecosRetornados = enderecoService.buscarTodos();

        // Assert
        assertNotNull(enderecosRetornados);
        assertFalse(enderecosRetornados.isEmpty());
        assertEquals(1, enderecosRetornados.size());
        verify(enderecoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de busca por ID com endereço existente deve retornar endereço")
    void buscarPorId_EnderecoExistente_DeveRetornarEndereco() {
        // Arrange
        Long id = 1L;
        when(enderecoRepository.findById(id)).thenReturn(Optional.of(endereco));

        // Act
        Optional<Endereco> enderecoRetornado = enderecoService.buscarPorId(id);

        // Assert
        assertTrue(enderecoRetornado.isPresent());
        assertEquals(endereco.getRua(), enderecoRetornado.get().getRua());
        verify(enderecoRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de busca por ID com endereço inexistente deve retornar vazio")
    void buscarPorId_EnderecoInexistente_DeveRetornarVazio() {
        // Arrange
        Long id = 999L;
        when(enderecoRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Endereco> enderecoRetornado = enderecoService.buscarPorId(id);

        // Assert
        assertTrue(enderecoRetornado.isEmpty());
        verify(enderecoRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de salvamento de endereço deve retornar endereço salvo")
    void salvar_DeveRetornarEnderecoSalvo() {
        // Arrange
        Endereco novoEndereco = new Endereco();
        novoEndereco.setRua("Rua Nova");
        novoEndereco.setNumero("456");
        novoEndereco.setBairro("Novo Bairro");
        novoEndereco.setCidade("Rio de Janeiro");
        novoEndereco.setEstado("RJ");
        novoEndereco.setCep("20000-000");

        when(enderecoRepository.save(any(Endereco.class))).thenReturn(novoEndereco);

        // Act
        Endereco enderecoSalvo = enderecoService.salvar(novoEndereco);

        // Assert
        assertNotNull(enderecoSalvo);
        verify(enderecoRepository, times(1)).save(novoEndereco);
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de atualização com endereço existente deve retornar endereço atualizado")
    void atualizar_EnderecoExistente_DeveRetornarEnderecoAtualizado() {
        // Arrange
        Long id = 1L;
        Endereco enderecoAtualizado = new Endereco();
        enderecoAtualizado.setRua("Rua A Atualizada");

        when(enderecoRepository.findById(id)).thenReturn(Optional.of(endereco));
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);

        // Act
        Endereco result = enderecoService.atualizar(id, enderecoAtualizado);

        // Assert
        assertNotNull(result);
        verify(enderecoRepository, times(1)).findById(id);
        verify(enderecoRepository, times(1)).save(any(Endereco.class));
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de atualização com endereço inexistente deve lançar exceção")
    void atualizar_EnderecoInexistente_DeveLancarExcecao() {
        // Arrange
        Long id = 999L;
        Endereco enderecoAtualizado = new Endereco();
        enderecoAtualizado.setRua("Rua Inexistente");

        when(enderecoRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> enderecoService.atualizar(id, enderecoAtualizado));
        
        assertTrue(exception.getMessage().contains("Endereço não encontrado"));
        verify(enderecoRepository, times(1)).findById(id);
        verify(enderecoRepository, never()).save(any(Endereco.class));
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de deleção com endereço existente deve deletar sem lançar exceção")
    void deletar_EnderecoExistente_DeveDeletarSemLancarExcecao() {
        // Arrange
        Long id = 1L;
        when(enderecoRepository.existsById(id)).thenReturn(true);
        doNothing().when(enderecoRepository).deleteById(id);

        // Act & Assert
        assertDoesNotThrow(() -> enderecoService.deletar(id));
        verify(enderecoRepository, times(1)).existsById(id);
        verify(enderecoRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de deleção com endereço inexistente deve lançar exceção")
    void deletar_EnderecoInexistente_DeveLancarExcecao() {
        // Arrange
        Long id = 999L;
        when(enderecoRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> enderecoService.deletar(id));
        
        assertTrue(exception.getMessage().contains("Não é possível deletar endereço inexistente"));
        verify(enderecoRepository, times(1)).existsById(id);
        verify(enderecoRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de busca por cidade deve retornar endereços correspondentes")
    void buscarPorCidade_DeveRetornarEnderecosCorrespondentes() {
        // Arrange
        String cidade = "São Paulo";
        List<Endereco> enderecosEsperados = Arrays.asList(endereco);
        when(enderecoRepository.findByCidadeIgnoreCaseContaining(cidade)).thenReturn(enderecosEsperados);

        // Act
        List<Endereco> enderecosRetornados = enderecoService.buscarPorCidade(cidade);

        // Assert
        assertNotNull(enderecosRetornados);
        assertEquals(1, enderecosRetornados.size());
        verify(enderecoRepository, times(1)).findByCidadeIgnoreCaseContaining(cidade);
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de busca por estado deve retornar endereços correspondentes")
    void buscarPorEstado_DeveRetornarEnderecosCorrespondentes() {
        // Arrange
        String estado = "SP";
        List<Endereco> enderecosEsperados = Arrays.asList(endereco);
        when(enderecoRepository.findByEstado(estado)).thenReturn(enderecosEsperados);

        // Act
        List<Endereco> enderecosRetornados = enderecoService.buscarPorEstado(estado);

        // Assert
        assertNotNull(enderecosRetornados);
        assertEquals(1, enderecosRetornados.size());
        verify(enderecoRepository, times(1)).findByEstado(estado);
    }
}

