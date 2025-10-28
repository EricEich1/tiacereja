package com.example.festas.controller;

import com.example.festas.security.SecurityConfigurations;
import org.springframework.context.annotation.Import;

import com.example.festas.entity.Usuario;
import com.example.festas.repository.UsuarioRepository;
import com.example.festas.security.DadosAutenticacao;
import com.example.festas.security.ITokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf; // O import importante
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfigurations.class)
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager manager;

    @MockBean
    private ITokenService tokenService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private DadosAutenticacao dadosLogin;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        dadosLogin = new DadosAutenticacao("usuario@teste.com", "123456");
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setLogin("usuario@teste.com");
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve autenticar com sucesso e retornar token JWT com status 200")
    void efetuarLogin_CredenciaisValidas_DeveRetornarTokenComStatus200() throws Exception {
        // Arrange
        var authentication = new UsernamePasswordAuthenticationToken(usuario, null);
        when(manager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(tokenService.gerarToken(any(Usuario.class))).thenReturn("mock.jwt.token");

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .with(csrf()) // A CORREÇÃO ESTÁ AQUI
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosLogin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock.jwt.token"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve falhar ao autenticar com credenciais erradas e retornar status 401")
    void efetuarLogin_CredenciaisInvalidas_DeveRetornarStatus401() throws Exception {
        // Arrange
        when(manager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Falha na autenticação") {});

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .with(csrf()) // A CORREÇÃO ESTÁ AQUI
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosLogin)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve registrar usuário com sucesso e retornar status 200")
    void registrar_UsuarioNovo_DeveRetornarStatus200() throws Exception {
        // Arrange
        when(usuarioRepository.findByLogin(anyString())).thenReturn(null);
        when(passwordEncoder.encode(anyString())).thenReturn("senhaCriptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act & Assert
        mockMvc.perform(post("/api/auth/registrar")
                        .with(csrf()) // A CORREÇÃO ESTÁ AQUI
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosLogin)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO - Deve falhar ao registrar usuário existente e retornar status 400")
    void registrar_UsuarioExistente_DeveRetornarStatus400() throws Exception {
        // Arrange
        when(usuarioRepository.findByLogin(eq(dadosLogin.login()))).thenReturn(usuario);

        // Act & Assert
        mockMvc.perform(post("/api/auth/registrar")
                        .with(csrf()) // A CORREÇÃO ESTÁ AQUI
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosLogin)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Usuário já existente!"));
    }
}