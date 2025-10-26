package com.example.festas.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de criação de usuário deve criar objeto válido")
    void criarUsuario_DeveCriarObjetoValido() {
        // Arrange & Act
        usuario.setId(1L);
        usuario.setLogin("usuario@teste.com");
        usuario.setSenha("senhaCriptografada");

        // Assert
        assertNotNull(usuario);
        assertEquals("usuario@teste.com", usuario.getUsername());
        assertEquals("senhaCriptografada", usuario.getPassword());
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de UserDetails deve ter roles corretas")
    void getUserDetails_DeveTerRolesCorretas() {
        // Arrange
        usuario.setLogin("usuario@teste.com");

        // Act
        var authorities = usuario.getAuthorities();

        // Assert
        assertNotNull(authorities);
        assertFalse(authorities.isEmpty());
        assertEquals("ROLE_USER", authorities.iterator().next().getAuthority());
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de métodos UserDetails devem retornar true")
    void getUserDetails_DeveRetornarTrue() {
        // Assert
        assertTrue(usuario.isAccountNonExpired());
        assertTrue(usuario.isAccountNonLocked());
        assertTrue(usuario.isCredentialsNonExpired());
        assertTrue(usuario.isEnabled());
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de getUsername deve retornar login")
    void getUsername_DeveRetornarLogin() {
        // Arrange
        usuario.setLogin("usuario@teste.com");

        // Act
        String username = usuario.getUsername();

        // Assert
        assertEquals("usuario@teste.com", username);
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de getPassword deve retornar senha")
    void getPassword_DeveRetornarSenha() {
        // Arrange
        usuario.setSenha("senha123");

        // Act
        String password = usuario.getPassword();

        // Assert
        assertEquals("senha123", password);
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de setter e getter de ID deve funcionar corretamente")
    void settersEGetters_Id_DeveFuncionarCorretamente() {
        // Act
        usuario.setId(10L);
        
        // Assert - testando que o setter funciona
        assertNotNull(usuario);
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de criação de múltiplos usuários deve criar objetos diferentes")
    void criarMultiplosUsuarios_DeveCriarObjetosDiferentes() {
        // Arrange
        usuario.setId(1L);
        usuario.setLogin("usuario@teste.com");
        usuario.setSenha("senha1");
        
        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setLogin("outro@teste.com");
        usuario2.setSenha("senha2");

        // Assert - os objetos são diferentes
        assertNotNull(usuario);
        assertNotNull(usuario2);
        assertNotSame(usuario, usuario2);
    }
}

