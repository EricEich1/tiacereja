package com.example.festas.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnderecoTest {

    private Endereco endereco;

    @BeforeEach
    void setUp() {
        endereco = new Endereco();
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de criação de endereço deve criar objeto válido")
    void criarEndereco_DeveCriarObjetoValido() {
        // Arrange & Act
        endereco.setId(1L);
        endereco.setRua("Rua A");
        endereco.setNumero("123");
        endereco.setComplemento("Apto 101");
        endereco.setBairro("Centro");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        endereco.setCep("01310-100");

        // Assert
        assertNotNull(endereco);
        assertEquals(1L, endereco.getId());
        assertEquals("Rua A", endereco.getRua());
        assertEquals("123", endereco.getNumero());
        assertEquals("Apto 101", endereco.getComplemento());
        assertEquals("Centro", endereco.getBairro());
        assertEquals("São Paulo", endereco.getCidade());
        assertEquals("SP", endereco.getEstado());
        assertEquals("01310-100", endereco.getCep());
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de setter e getter de rua deve funcionar corretamente")
    void settersEGetters_Rua_DeveFuncionarCorretamente() {
        // Act
        endereco.setRua("Av. Paulista");
        
        // Assert
        assertEquals("Av. Paulista", endereco.getRua());
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de setter e getter de número deve funcionar corretamente")
    void settersEGetters_Numero_DeveFuncionarCorretamente() {
        // Act
        endereco.setNumero("456");
        
        // Assert
        assertEquals("456", endereco.getNumero());
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de setter e getter de cidade deve funcionar corretamente")
    void settersEGetters_Cidade_DeveFuncionarCorretamente() {
        // Act
        endereco.setCidade("Rio de Janeiro");
        
        // Assert
        assertEquals("Rio de Janeiro", endereco.getCidade());
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de setter e getter de estado deve funcionar corretamente")
    void settersEGetters_Estado_DeveFuncionarCorretamente() {
        // Act
        endereco.setEstado("RJ");
        
        // Assert
        assertEquals("RJ", endereco.getEstado());
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de endereço sem complemento deve ser permitido")
    void criarEndereco_SemComplemento_DeveSerPermitido() {
        // Act
        endereco.setComplemento(null);
        
        // Assert
        assertNull(endereco.getComplemento());
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de setter e getter de ID deve funcionar corretamente")
    void settersEGetters_Id_DeveFuncionarCorretamente() {
        // Act
        endereco.setId(10L);
        
        // Assert
        assertEquals(10L, endereco.getId());
    }
}

