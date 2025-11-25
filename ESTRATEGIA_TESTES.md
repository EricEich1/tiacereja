# Estratégia de Testes - Sistema de Gestão de Festas

## 📋 Objetivos

1. **Qualidade**: Validar que o código funciona corretamente
2. **Prevenir Regressões**: Detectar problemas em novas funcionalidades
3. **Documentar Comportamento**: Testes como documentação viva
4. **Robustez**: Testar situações extremas e de erro

---

## 🏗️ Organização

```
src/test/java/com/example/festas/
├── controller/        # Testes REST API
├── service/           # Testes de lógica de negócio
├── entity/            # Testes de entidades (mínimo)
└── security/          # Testes de autenticação
```

---

## 📊 Tipos de Testes

### 1. Testes Unitários
- **Uso**: Services com regras de negócio complexas
- **Ferramentas**: JUnit 5 + Mockito
- **Exemplo**: `ClienteServiceTest` testa regra de status COMPLETO/INCOMPLETO

### 2. Testes de Integração (Slice)
- **Uso**: Controllers e contratos HTTP
- **Ferramentas**: `@WebMvcTest` + MockMvc
- **Exemplo**: `ClienteControllerTest` valida status HTTP e formato JSON

### 3. Testes de Robustez
- **Uso**: Valores extremos e edge cases
- **Exemplo**: `RobustezLimitesTest` testa IDs zero/negativos, strings longas

### 4. Testes de Validação
- **Uso**: Exceções e mensagens de erro
- **Exemplo**: `ValidacaoFalhasTest` valida exceções com mensagens específicas

---

## 🛠️ Ferramentas

| Ferramenta | Uso |
|------------|-----|
| **JUnit 5** | Framework base |
| **Mockito** | Mocks para testes unitários |
| **JaCoCo** | Cobertura de código |

---

## 📐 Padrões

### Nomenclatura
`[Ação]_[Condição]_[ResultadoEsperado]`

```java
void atualizar_ClienteInexistente_DeveLancarExcecao()
```

### Estrutura AAA
```java
// Arrange - Preparação
// Act - Execução
// Assert - Validação
```

### Display Names
```java
@DisplayName("TESTE DE INTEGRAÇÃO - Cenário de busca por ID...")
```

---

## 🎯 Prioridades

1. **Regras de Negócio**: Definição de status, validações obrigatórias
2. **Contratos de API**: Status HTTP, formato JSON, validação de entrada
3. **Tratamento de Erros**: Exceções claras, valores extremos
4. **Segurança**: Autenticação JWT, validação de tokens

---

## 🚫 O que NÃO testamos

- **Getters/Setters simples**: Sem lógica de negócio
- **@SpringBootTest completo**: Priorizamos slice tests (@WebMvcTest) por velocidade

---

## 📊 Cobertura

- **Meta Geral**: 90% (JaCoCo)
- **Foco**: Services e Controllers (código crítico)
- **Total**: ~110+ testes

---

## 🎯 Conclusão

Estratégia focada em:
- Qualidade sobre quantidade
- Rapidez de execução
- Robustez e tratamento de erros
- Testes como documentação
