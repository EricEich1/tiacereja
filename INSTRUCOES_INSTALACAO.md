# 🚀 Instruções para Executar o Projeto Spring Boot

## ❌ Problema Identificado
O erro no PowerShell está acontecendo porque o **Java** e o **Maven** não estão instalados no seu sistema.

## ✅ Soluções Disponíveis

### Opção 1: Instalação Rápida com Chocolatey (Recomendado)

1. **Abra o PowerShell como Administrador:**
   - Clique com botão direito no ícone do PowerShell
   - Selecione "Executar como administrador"

2. **Execute os comandos:**
   ```powershell
   choco install openjdk17 maven -y
   ```

3. **Reinicie o PowerShell** e execute:
   ```powershell
   cd C:\Users\jp250\tiacereja\tiacereja
   mvn spring-boot:run
   ```

### Opção 2: Instalação Manual

#### Instalar Java 17:
1. Acesse: https://adoptium.net/temurin/releases/?version=17
2. Baixe o **Windows x64** (JDK)
3. Execute o instalador
4. **IMPORTANTE:** Marque a opção "Set JAVA_HOME variable" e "Add to PATH"

#### Instalar Maven:
1. Acesse: https://maven.apache.org/download.cgi
2. Baixe o **Binary zip archive**
3. Extraia para `C:\Program Files\Apache\maven`
4. Adicione `C:\Program Files\Apache\maven\bin` ao PATH do sistema

#### Configurar PATH:
1. Pressione `Win + R`, digite `sysdm.cpl` e pressione Enter
2. Clique em "Variáveis de Ambiente"
3. Em "Variáveis do sistema", encontre "Path" e clique em "Editar"
4. Clique em "Novo" e adicione:
   - `C:\Program Files\Eclipse Adoptium\jdk-17.x.x-hotspot\bin`
   - `C:\Program Files\Apache\maven\bin`
5. Clique em "OK" em todas as janelas
6. **Reinicie o PowerShell**

### Opção 3: Usar o Script Automatizado

Eu criei um script que detecta automaticamente os problemas:

```powershell
# Execute este comando no diretório do projeto
powershell -ExecutionPolicy Bypass -File run.ps1
```

## 🧪 Testando a Instalação

Após instalar o Java e Maven, teste com:

```powershell
# Verificar Java
java -version

# Verificar Maven
mvn --version

# Executar o projeto
mvn spring-boot:run
```

## 📁 Arquivos Criados

- `mvnw.cmd` - Maven Wrapper para Windows
- `run.ps1` - Script de execução automatizado
- `.mvn/wrapper/maven-wrapper.properties` - Configurações do Maven Wrapper

## 🎯 Resultado Esperado

Quando tudo estiver funcionando, você verá:
```
Started FestasApplication in X.XXX seconds (JVM running for X.XXX)
```

E a API estará disponível em: http://localhost:8080

## 🆘 Ainda com Problemas?

Se continuar com erro, execute:
```powershell
# Verificar variáveis de ambiente
echo $env:JAVA_HOME
echo $env:PATH

# Limpar cache do Maven
mvn clean
```

---
**Dica:** A Opção 1 (Chocolatey) é a mais rápida e confiável! 🚀
