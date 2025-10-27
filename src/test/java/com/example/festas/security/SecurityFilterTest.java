package com.example.festas.security;

import com.example.festas.entity.Usuario;
import com.example.festas.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach; // O import de limpeza
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows; // O import para exceção
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityFilterTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private SecurityFilter securityFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @AfterEach
    void tearDown() {
        // A CORREÇÃO ESTÁ AQUI: Limpa o usuário logado entre os testes
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Deve autenticar usuário se o token for válido")
    void doFilterInternal_TokenValido_DeveAutenticarUsuario() throws ServletException, IOException {
        // Arrange
        String token = "mock.jwt.token";
        String login = "usuario@teste.com";
        Usuario usuario = new Usuario();
        usuario.setLogin(login);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(tokenService.getSubject(token)).thenReturn(login);
        when(usuarioRepository.findByLogin(login)).thenReturn(usuario);

        // Act
        securityFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Não deve autenticar se não houver token na requisição")
    void doFilterInternal_SemToken_NaoDeveAutenticar() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        securityFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Não deve autenticar se o token for válido (usuário não encontrado)")
    void doFilterInternal_TokenValidoUsuarioInexistente_NaoDeveAutenticar() throws ServletException, IOException {
        // Arrange
        String token = "mock.jwt.token";
        String login = "usuario@fantasma.com";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(tokenService.getSubject(token)).thenReturn(login);
        when(usuarioRepository.findByLogin(login)).thenReturn(null); // Usuário não existe

        // Act
        securityFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Não deve autenticar se o token falhar na validação")
    void doFilterInternal_TokenInvalido_NaoDeveAutenticar() throws ServletException, IOException {
        // Arrange
        String token = "token.invalido";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        // Simula o TokenService lançando a exceção que vimos no log
        when(tokenService.getSubject(token)).thenThrow(new RuntimeException("Token JWT inválido ou expirado!"));

        // Act & Assert
        // A CORREÇÃO ESTÁ AQUI: Nós afirmamos que a exceção VAI acontecer
        assertThrows(RuntimeException.class, () -> {
            securityFilter.doFilterInternal(request, response, filterChain);
        });

        // Verificamos também que o usuário NÃO foi autenticado
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        // E que o filtro parou e não continuou a cadeia
        verify(filterChain, never()).doFilter(request, response);
    }
}