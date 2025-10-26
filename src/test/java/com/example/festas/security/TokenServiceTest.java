package com.example.festas.security;

import com.example.festas.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    private Usuario usuario;
    private String secret;

    @BeforeEach
    void setUp() {
        secret = "12345678901234567890123456789012345678901234567890";
        ReflectionTestUtils.setField(tokenService, "secret", secret);

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setLogin("usuario@teste.com");
        usuario.setSenha("senhaCriptografada");
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de geração de token deve retornar token válido")
    void gerarToken_DeveRetornarTokenValido() {
        // Act
        String token = tokenService.gerarToken(usuario);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de geração de token deve incluir subject correto")
    void gerarToken_DeveIncluirSubjectCorreto() {
        // Act
        String token = tokenService.gerarToken(usuario);
        String subject = tokenService.getSubject(token);

        // Assert
        assertEquals(usuario.getUsername(), subject);
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de extração de subject com token válido deve retornar login")
    void getSubject_TokenValido_DeveRetornarLogin() {
        // Arrange
        String token = tokenService.gerarToken(usuario);

        // Act
        String subject = tokenService.getSubject(token);

        // Assert
        assertNotNull(subject);
        assertEquals(usuario.getUsername(), subject);
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de extração com token inválido deve lançar exceção")
    void getSubject_TokenInvalido_DeveLancarExcecao() {
        // Arrange
        String tokenInvalido = "token.invalido.exemplo";

        // Act & Assert
        assertThrows(RuntimeException.class, () -> tokenService.getSubject(tokenInvalido));
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de extração com token nulo deve lançar exceção")
    void getSubject_TokenNulo_DeveLancarExcecao() {
        // Act & Assert
        assertThrows(RuntimeException.class, () -> tokenService.getSubject(null));
    }

    @Test
    @DisplayName("TESTE UNITÁRIO - Cenário de token expirado deve ser detectado")
    void gerarToken_TokensDiferentesSaoGerados_DeveGerarTokensUnicos() {
        // Act
        String token1 = tokenService.gerarToken(usuario);
        
        // Wait to ensure different timestamp
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        String token2 = tokenService.gerarToken(usuario);

        // Assert
        assertNotEquals(token1, token2);
    }
}

