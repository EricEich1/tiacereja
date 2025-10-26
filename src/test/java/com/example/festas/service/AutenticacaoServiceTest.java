package com.example.festas.service;

import com.example.festas.entity.Usuario;
import com.example.festas.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutenticacaoServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AutenticacaoService autenticacaoService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setLogin("usuario@teste.com");
        usuario.setSenha("senhaCriptografada");
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de carregamento de usuário existente deve retornar UserDetails")
    void loadUserByUsername_UsuarioExistente_DeveRetornarUserDetails() {
        // Arrange
        String login = "usuario@teste.com";
        when(usuarioRepository.findByLogin(login)).thenReturn(usuario);

        // Act
        UserDetails userDetails = autenticacaoService.loadUserByUsername(login);

        // Assert
        assertNotNull(userDetails);
        assertEquals(login, userDetails.getUsername());
        assertEquals(usuario.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().size() > 0);
        verify(usuarioRepository, times(1)).findByLogin(login);
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de carregamento de usuário inexistente deve retornar null")
    void loadUserByUsername_UsuarioInexistente_DeveRetornarNull() {
        // Arrange
        String login = "usuario@inexistente.com";
        when(usuarioRepository.findByLogin(login)).thenReturn(null);

        // Act & Assert
        // O método apenas retorna o resultado do repository, que será null
        UserDetails userDetails = autenticacaoService.loadUserByUsername(login);
        
        assertNull(userDetails);
        verify(usuarioRepository, times(1)).findByLogin(login);
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de verificação de métodos UserDetails deve retornar true")
    void userDetails_DeveRetornarValoresCorretos() {
        // Arrange
        String login = "usuario@teste.com";
        when(usuarioRepository.findByLogin(login)).thenReturn(usuario);

        // Act
        UserDetails userDetails = autenticacaoService.loadUserByUsername(login);

        // Assert
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
        verify(usuarioRepository, times(1)).findByLogin(login);
    }
}

