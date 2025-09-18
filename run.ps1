# Script para executar o projeto Spring Boot
# Este script verifica se o Java está instalado e executa o projeto

Write-Host "=== Executando Projeto Spring Boot ===" -ForegroundColor Green

# Verificar se o Java está instalado
try {
    $javaVersion = java -version 2>&1
    Write-Host "Java encontrado:" -ForegroundColor Green
    Write-Host $javaVersion[0] -ForegroundColor Yellow
} catch {
    Write-Host "ERRO: Java não encontrado!" -ForegroundColor Red
    Write-Host "Por favor, instale o Java 17 ou superior:" -ForegroundColor Yellow
    Write-Host "1. Baixe em: https://adoptium.net/temurin/releases/?version=17" -ForegroundColor Cyan
    Write-Host "2. Instale e adicione ao PATH do sistema" -ForegroundColor Cyan
    Write-Host "3. Reinicie o PowerShell e execute novamente" -ForegroundColor Cyan
    exit 1
}

# Verificar se o Maven Wrapper existe
if (Test-Path "mvnw.cmd") {
    Write-Host "Executando com Maven Wrapper..." -ForegroundColor Green
    .\mvnw.cmd spring-boot:run
} else {
    Write-Host "Maven Wrapper não encontrado. Tentando com Maven global..." -ForegroundColor Yellow
    try {
        mvn spring-boot:run
    } catch {
        Write-Host "ERRO: Maven não encontrado!" -ForegroundColor Red
        Write-Host "Por favor, instale o Maven ou use o Maven Wrapper" -ForegroundColor Yellow
        exit 1
    }
}

Write-Host "=== Projeto executado com sucesso! ===" -ForegroundColor Green
