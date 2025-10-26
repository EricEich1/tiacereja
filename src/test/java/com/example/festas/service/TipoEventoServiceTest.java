package com.example.festas.service;

import com.example.festas.entity.TipoEvento;
import com.example.festas.repository.TipoEventoRepository;
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
class TipoEventoServiceTest {

    @Mock
    private TipoEventoRepository tipoEventoRepository;

    @InjectMocks
    private TipoEventoService tipoEventoService;

    private TipoEvento tipoEvento;

    @BeforeEach
    void setUp() {
        tipoEvento = new TipoEvento();
        tipoEvento.setId(1L);
        tipoEvento.setNome("Aniversário Infantil");
        tipoEvento.setDescricao("Festa para crianças");
        tipoEvento.setCapacidadeMinima(10);
        tipoEvento.setCapacidadeMaxima(50);
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de busca de todos os tipos de evento deve retornar lista não vazia")
    void buscarTodos_DeveRetornarListaNaoVazia() {
        // Arrange
        List<TipoEvento> tiposEsperados = Arrays.asList(tipoEvento);
        when(tipoEventoRepository.findAll()).thenReturn(tiposEsperados);

        // Act
        List<TipoEvento> tiposRetornados = tipoEventoService.buscarTodos();

        // Assert
        assertNotNull(tiposRetornados);
        assertFalse(tiposRetornados.isEmpty());
        assertEquals(1, tiposRetornados.size());
        verify(tipoEventoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de busca por ID com tipo de evento existente deve retornar tipo")
    void buscarPorId_TipoExistente_DeveRetornarTipo() {
        // Arrange
        Long id = 1L;
        when(tipoEventoRepository.findById(id)).thenReturn(Optional.of(tipoEvento));

        // Act
        Optional<TipoEvento> tipoRetornado = tipoEventoService.buscarPorId(id);

        // Assert
        assertTrue(tipoRetornado.isPresent());
        assertEquals(tipoEvento.getNome(), tipoRetornado.get().getNome());
        verify(tipoEventoRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de busca por ID com tipo de evento inexistente deve retornar vazio")
    void buscarPorId_TipoInexistente_DeveRetornarVazio() {
        // Arrange
        Long id = 999L;
        when(tipoEventoRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<TipoEvento> tipoRetornado = tipoEventoService.buscarPorId(id);

        // Assert
        assertTrue(tipoRetornado.isEmpty());
        verify(tipoEventoRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de salvamento de tipo de evento deve retornar tipo salvo")
    void salvar_DeveRetornarTipoSalvo() {
        // Arrange
        TipoEvento novoTipo = new TipoEvento();
        novoTipo.setNome("Novo Tipo");
        novoTipo.setCapacidadeMinima(20);
        novoTipo.setCapacidadeMaxima(100);

        when(tipoEventoRepository.save(any(TipoEvento.class))).thenReturn(novoTipo);

        // Act
        TipoEvento tipoSalvo = tipoEventoService.salvar(novoTipo);

        // Assert
        assertNotNull(tipoSalvo);
        verify(tipoEventoRepository, times(1)).save(novoTipo);
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de atualização com tipo existente deve retornar tipo atualizado")
    void atualizar_TipoExistente_DeveRetornarTipoAtualizado() {
        // Arrange
        Long id = 1L;
        TipoEvento tipoAtualizado = new TipoEvento();
        tipoAtualizado.setNome("Aniversário Adulto");

        when(tipoEventoRepository.findById(id)).thenReturn(Optional.of(tipoEvento));
        when(tipoEventoRepository.save(any(TipoEvento.class))).thenReturn(tipoEvento);

        // Act
        TipoEvento result = tipoEventoService.atualizar(id, tipoAtualizado);

        // Assert
        assertNotNull(result);
        verify(tipoEventoRepository, times(1)).findById(id);
        verify(tipoEventoRepository, times(1)).save(any(TipoEvento.class));
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de atualização com tipo inexistente deve lançar exceção")
    void atualizar_TipoInexistente_DeveLancarExcecao() {
        // Arrange
        Long id = 999L;
        TipoEvento tipoAtualizado = new TipoEvento();
        tipoAtualizado.setNome("Tipo Inexistente");

        when(tipoEventoRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> tipoEventoService.atualizar(id, tipoAtualizado));
        
        assertTrue(exception.getMessage().contains("Tipo de evento não encontrado"));
        verify(tipoEventoRepository, times(1)).findById(id);
        verify(tipoEventoRepository, never()).save(any(TipoEvento.class));
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de deleção com tipo existente deve deletar sem lançar exceção")
    void deletar_TipoExistente_DeveDeletarSemLancarExcecao() {
        // Arrange
        Long id = 1L;
        when(tipoEventoRepository.existsById(id)).thenReturn(true);
        doNothing().when(tipoEventoRepository).deleteById(id);

        // Act & Assert
        assertDoesNotThrow(() -> tipoEventoService.deletar(id));
        verify(tipoEventoRepository, times(1)).existsById(id);
        verify(tipoEventoRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de deleção com tipo inexistente deve lançar exceção")
    void deletar_TipoInexistente_DeveLancarExcecao() {
        // Arrange
        Long id = 999L;
        when(tipoEventoRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> tipoEventoService.deletar(id));
        
        assertTrue(exception.getMessage().contains("Não é possível deletar tipo de evento inexistente"));
        verify(tipoEventoRepository, times(1)).existsById(id);
        verify(tipoEventoRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de busca por nome deve retornar tipos correspondentes")
    void buscarPorNome_DeveRetornarTiposCorrespondentes() {
        // Arrange
        String nome = "Aniversário";
        List<TipoEvento> tiposEsperados = Arrays.asList(tipoEvento);
        when(tipoEventoRepository.findByNomeIgnoreCaseContaining(nome)).thenReturn(tiposEsperados);

        // Act
        List<TipoEvento> tiposRetornados = tipoEventoService.buscarPorNome(nome);

        // Assert
        assertNotNull(tiposRetornados);
        assertEquals(1, tiposRetornados.size());
        verify(tipoEventoRepository, times(1)).findByNomeIgnoreCaseContaining(nome);
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de busca por capacidade deve retornar tipos com capacidade adequada")
    void buscarPorCapacidade_DeveRetornarTiposAdequados() {
        // Arrange
        Integer capacidade = 30;
        List<TipoEvento> tiposEsperados = Arrays.asList(tipoEvento);
        when(tipoEventoRepository.findByCapacidadeMinimaLessThanEqualAndCapacidadeMaximaGreaterThanEqual(capacidade, capacidade))
                .thenReturn(tiposEsperados);

        // Act
        List<TipoEvento> tiposRetornados = tipoEventoService.buscarPorCapacidade(capacidade);

        // Assert
        assertNotNull(tiposRetornados);
        assertEquals(1, tiposRetornados.size());
        verify(tipoEventoRepository, times(1))
                .findByCapacidadeMinimaLessThanEqualAndCapacidadeMaximaGreaterThanEqual(capacidade, capacidade);
    }
}

