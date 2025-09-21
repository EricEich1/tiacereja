package com.example.festas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // Sua lógica de autenticação aqui
        return ResponseEntity.ok().body(Map.of(
            "token", "jwt-token-aqui",
            "usuario", Map.of(
                "id", 1,
                "email", request.getEmail(),
                "nome", "Usuário Teste"
            )
        ));
    }
    
    // Classe interna para o request
    public static class LoginRequest {
        private String email;
        private String password;
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
        
        public String getPassword() {
            return password;
        }
        
        public void setPassword(String password) {
            this.password = password;
        }
    }
}