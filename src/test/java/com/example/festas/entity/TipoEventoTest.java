package com.example.festas.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TipoEventoTest {

    private TipoEvento tipoEvento;

    @BeforeEach
    void setUp() {
        tipoEvento = new TipoEvento();
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de criação de tipo de evento deve criar objeto válido")
    void criarTipoEvento_DeveCriarObjetoValido() {
        // Arrange & Act
        tipoEvento.setId(1L);
        tipoEvento.setNome("Aniversário Infantil");
        tipoEvento.setDescricao("Festa para crianças");
        tipoEvento.setCapacidadeMinima(10);
        tipoEvento.setCapacidadeMaxima(50);

        // Assert
        assertNotNull(tipoEvento);
        assertEquals(1L, tipoEvento.getId());
        assertEquals("Aniversário Infantil", tipoEvento.getNome());
        assertEquals("Festa para crianças", tipoEvento.getDescricao());
        assertEquals(10, tipoEvento.getCapacidadeMinima());
        assertEquals(50, tipoEvento.getCapacidadeMaxima());
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de setter e getter de nome deve funcionar corretamente")
    void settersEGetters_Nome_DeveFuncionarCorretamente() {
        // Act
        tipoEvento.setNome("Casamento");
        
        // Assert
        assertEquals("Casamento", tipoEvento.getNome());
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de setter e getter de capacidade mínima deve funcionar corretamente")
    void settersEGetters_CapacidadeMinima_DeveFuncionarCorretamente() {
        // Act
        tipoEvento.setCapacidadeMinima(20);
        
        // Assert
        assertEquals(20, tipoEvento.getCapacidadeMinima());
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de setter e getter de capacidade máxima deve funcionar corretamente")
    void settersEGetters_CapacidadeMaxima_DeveFuncionarCorretamente() {
        // Act
        tipoEvento.setCapacidadeMaxima(100);
        
        // Assert
        assertEquals(100, tipoEvento.getCapacidadeMaxima());
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de setter e getter de ID deve funcionar corretamente")
    void settersEGetters_Id_DeveFuncionarCorretamente() {
        // Act
        tipoEvento.setId(10L);
        
        // Assert
        assertEquals(10L, tipoEvento.getId());
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de tipo de evento com capacidade nula deve ser permitido")
    void criarTipoEvento_CapacidadeNulla_DeveSerPermitido() {
        // Act
        tipoEvento.setCapacidadeMinima(null);
        tipoEvento.setCapacidadeMaxima(null);
        
        // Assert
        assertNull(tipoEvento.getCapacidadeMinima());
        assertNull(tipoEvento.getCapacidadeMaxima());
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de capacidade mínima maior que máxima deve ser permitido (validação de negócio)")
    void criarTipoEvento_CapacidadeMinimaMaiorQueMaxima_DeveSerPermitido() {
        // Act
        tipoEvento.setCapacidadeMinima(100);
        tipoEvento.setCapacidadeMaxima(50);
        
        // Assert
        assertEquals(100, tipoEvento.getCapacidadeMinima());
        assertEquals(50, tipoEvento.getCapacidadeMaxima());
    }
}

