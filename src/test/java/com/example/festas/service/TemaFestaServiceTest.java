package com.example.festas.service;

import com.example.festas.entity.TemaFesta;
import com.example.festas.repository.TemaFestaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TemaFestaServiceTest {

    @Mock
    private TemaFestaRepository temaRepository;

    @InjectMocks
    private TemaFestaService temaFestaService;

    private TemaFesta tema;

    @BeforeEach
    void setUp() {
        tema = new TemaFesta();
        tema.setId(1L);
        tema.setNome("Aniversário");
        tema.setDescricao("Tema para aniversário infantil");
        tema.setPrecoBase(new BigDecimal("500.00"));
        tema.setAtivo(true);
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de busca de todos os temas deve retornar lista não vazia")
    void buscarTodos_DeveRetornarListaNaoVazia() {
        // Arrange
        List<TemaFesta> temasEsperados = Arrays.asList(tema);
        when(temaRepository.findAll()).thenReturn(temasEsperados);

        // Act
        List<TemaFesta> temasRetornados = temaFestaService.buscarTodos();

        // Assert
        assertNotNull(temasRetornados);
        assertFalse(temasRetornados.isEmpty());
        assertEquals(1, temasRetornados.size());
        verify(temaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de busca por ID com tema existente deve retornar tema")
    void buscarPorId_TemaExistente_DeveRetornarTema() {
        // Arrange
        Long id = 1L;
        when(temaRepository.findById(id)).thenReturn(Optional.of(tema));

        // Act
        Optional<TemaFesta> temaRetornado = temaFestaService.buscarPorId(id);

        // Assert
        assertTrue(temaRetornado.isPresent());
        assertEquals(tema.getNome(), temaRetornado.get().getNome());
        verify(temaRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de busca por ID com tema inexistente deve retornar vazio")
    void buscarPorId_TemaInexistente_DeveRetornarVazio() {
        // Arrange
        Long id = 999L;
        when(temaRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<TemaFesta> temaRetornado = temaFestaService.buscarPorId(id);

        // Assert
        assertTrue(temaRetornado.isEmpty());
        verify(temaRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de salvamento de tema com ativo nulo deve definir como true")
    void salvar_TemaComAtivoNulo_DeveDefinirComoTrue() {
        // Arrange
        TemaFesta novoTema = new TemaFesta();
        novoTema.setNome("Novo Tema");
        novoTema.setDescricao("Descrição");
        novoTema.setAtivo(null);

        when(temaRepository.save(any(TemaFesta.class))).thenReturn(novoTema);

        // Act
        TemaFesta temaSalvo = temaFestaService.salvar(novoTema);

        // Assert
        assertNotNull(temaSalvo.getAtivo());
        assertTrue(temaSalvo.getAtivo());
        verify(temaRepository, times(1)).save(novoTema);
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de atualização com tema existente deve retornar tema atualizado")
    void atualizar_TemaExistente_DeveRetornarTemaAtualizado() {
        // Arrange
        Long id = 1L;
        TemaFesta temaAtualizado = new TemaFesta();
        temaAtualizado.setNome("Aniversário Atualizado");

        when(temaRepository.findById(id)).thenReturn(Optional.of(tema));
        when(temaRepository.save(any(TemaFesta.class))).thenReturn(tema);

        // Act
        TemaFesta result = temaFestaService.atualizar(id, temaAtualizado);

        // Assert
        assertNotNull(result);
        verify(temaRepository, times(1)).findById(id);
        verify(temaRepository, times(1)).save(any(TemaFesta.class));
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de atualização com tema inexistente deve lançar exceção")
    void atualizar_TemaInexistente_DeveLancarExcecao() {
        // Arrange
        Long id = 999L;
        TemaFesta temaAtualizado = new TemaFesta();
        temaAtualizado.setNome("Tema Inexistente");

        when(temaRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> temaFestaService.atualizar(id, temaAtualizado));
        
        assertTrue(exception.getMessage().contains("Tema não encontrado"));
        verify(temaRepository, times(1)).findById(id);
        verify(temaRepository, never()).save(any(TemaFesta.class));
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de deleção com tema existente deve deletar sem lançar exceção")
    void deletar_TemaExistente_DeveDeletarSemLancarExcecao() {
        // Arrange
        Long id = 1L;
        when(temaRepository.existsById(id)).thenReturn(true);
        doNothing().when(temaRepository).deleteById(id);

        // Act & Assert
        assertDoesNotThrow(() -> temaFestaService.deletar(id));
        verify(temaRepository, times(1)).existsById(id);
        verify(temaRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de deleção com tema inexistente deve lançar exceção")
    void deletar_TemaInexistente_DeveLancarExcecao() {
        // Arrange
        Long id = 999L;
        when(temaRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> temaFestaService.deletar(id));
        
        assertTrue(exception.getMessage().contains("Não é possível deletar tema inexistente"));
        verify(temaRepository, times(1)).existsById(id);
        verify(temaRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de busca por nome deve retornar temas correspondentes")
    void buscarPorNome_DeveRetornarTemasCorrespondentes() {
        // Arrange
        String nome = "Aniversário";
        List<TemaFesta> temasEsperados = Arrays.asList(tema);
        when(temaRepository.findByNomeIgnoreCaseContaining(nome)).thenReturn(temasEsperados);

        // Act
        List<TemaFesta> temasRetornados = temaFestaService.buscarPorNome(nome);

        // Assert
        assertNotNull(temasRetornados);
        assertEquals(1, temasRetornados.size());
        verify(temaRepository, times(1)).findByNomeIgnoreCaseContaining(nome);
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de busca por temas ativos deve retornar apenas temas ativos")
    void buscarAtivos_DeveRetornarApenasTemasAtivos() {
        // Arrange
        List<TemaFesta> temasAtivos = Arrays.asList(tema);
        when(temaRepository.findByAtivo(true)).thenReturn(temasAtivos);

        // Act
        List<TemaFesta> temasRetornados = temaFestaService.buscarAtivos();

        // Assert
        assertNotNull(temasRetornados);
        assertEquals(1, temasRetornados.size());
        assertTrue(temasRetornados.get(0).getAtivo());
        verify(temaRepository, times(1)).findByAtivo(true);
    }
}

