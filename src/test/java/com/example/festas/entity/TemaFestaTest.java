package com.example.festas.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TemaFestaTest {

    private TemaFesta tema;

    @BeforeEach
    void setUp() {
        tema = new TemaFesta();
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de criação de tema deve criar objeto válido")
    void criarTema_DeveCriarObjetoValido() {
        // Arrange & Act
        tema.setId(1L);
        tema.setNome("Aniversário");
        tema.setDescricao("Tema para aniversário infantil");
        tema.setPrecoBase(new BigDecimal("500.00"));
        tema.setAtivo(true);

        // Assert
        assertNotNull(tema);
        assertEquals(1L, tema.getId());
        assertEquals("Aniversário", tema.getNome());
        assertEquals("Tema para aniversário infantil", tema.getDescricao());
        assertEquals(new BigDecimal("500.00"), tema.getPrecoBase());
        assertTrue(tema.getAtivo());
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de setter e getter de nome deve funcionar corretamente")
    void settersEGetters_Nome_DeveFuncionarCorretamente() {
        // Act
        tema.setNome("Casamento");
        
        // Assert
        assertEquals("Casamento", tema.getNome());
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de setter e getter de preço base deve funcionar corretamente")
    void settersEGetters_PrecoBase_DeveFuncionarCorretamente() {
        // Act
        tema.setPrecoBase(new BigDecimal("1000.00"));
        
        // Assert
        assertEquals(new BigDecimal("1000.00"), tema.getPrecoBase());
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de setter e getter de ativo deve funcionar corretamente")
    void settersEGetters_Ativo_DeveFuncionarCorretamente() {
        // Act
        tema.setAtivo(false);
        
        // Assert
        assertFalse(tema.getAtivo());
        
        // Act
        tema.setAtivo(true);
        
        // Assert
        assertTrue(tema.getAtivo());
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de setter e getter de ID deve funcionar corretamente")
    void settersEGetters_Id_DeveFuncionarCorretamente() {
        // Act
        tema.setId(10L);
        
        // Assert
        assertEquals(10L, tema.getId());
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de tema com preço nulo deve ser permitido")
    void criarTema_PrecoNullo_DeveSerPermitido() {
        // Act
        tema.setPrecoBase(null);
        
        // Assert
        assertNull(tema.getPrecoBase());
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de tema com ativo nulo deve ser permitido")
    void criarTema_AtivoNullo_DeveSerPermitido() {
        // Act
        tema.setAtivo(null);
        
        // Assert
        assertNull(tema.getAtivo());
    }
}

