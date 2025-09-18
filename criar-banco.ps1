# Script para criar banco de dados festasdb
# Execute: powershell -ExecutionPolicy Bypass -File criar-banco.ps1

Write-Host "=== Criando Banco de Dados festasdb ===" -ForegroundColor Green

# Verificar se MySQL está rodando
Write-Host "Verificando se MySQL está rodando..." -ForegroundColor Yellow
$mysqlRunning = netstat -an | findstr :3306

if ($mysqlRunning) {
    Write-Host "✅ MySQL está rodando na porta 3306" -ForegroundColor Green
} else {
    Write-Host "❌ MySQL não está rodando!" -ForegroundColor Red
    Write-Host "Por favor, inicie o MySQL primeiro" -ForegroundColor Yellow
    exit 1
}

# Tentar encontrar o MySQL no sistema
$mysqlPaths = @(
    "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe",
    "C:\Program Files\MySQL\MySQL Server 8.1\bin\mysql.exe",
    "C:\Program Files\MySQL\MySQL Server 8.2\bin\mysql.exe",
    "C:\Program Files\MySQL\MySQL Server 8.3\bin\mysql.exe",
    "C:\xampp\mysql\bin\mysql.exe",
    "C:\wamp64\bin\mysql\mysql8.0.31\bin\mysql.exe"
)

$mysqlExe = $null
foreach ($path in $mysqlPaths) {
    if (Test-Path $path) {
        $mysqlExe = $path
        break
    }
}

if ($mysqlExe) {
    Write-Host "✅ MySQL encontrado em: $mysqlExe" -ForegroundColor Green
    
    # Criar banco de dados
    Write-Host "Criando banco de dados festasdb..." -ForegroundColor Cyan
    try {
        & $mysqlExe -u root -proot -e "CREATE DATABASE IF NOT EXISTS festasdb;"
        Write-Host "✅ Banco 'festasdb' criado com sucesso!" -ForegroundColor Green
        
        # Verificar se foi criado
        & $mysqlExe -u root -proot -e "SHOW DATABASES;" | findstr festasdb
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✅ Banco confirmado no MySQL" -ForegroundColor Green
        }
    }
    catch {
        Write-Host "❌ Erro ao criar banco: $($_.Exception.Message)" -ForegroundColor Red
    }
} else {
    Write-Host "❌ MySQL não encontrado no sistema" -ForegroundColor Red
    Write-Host "Você pode criar o banco manualmente via DBeaver" -ForegroundColor Yellow
}

Write-Host "`n=== Instruções para DBeaver ===" -ForegroundColor Yellow
Write-Host "1. Abra o DBeaver" -ForegroundColor White
Write-Host "2. Conecte no MySQL (sem especificar database)" -ForegroundColor White
Write-Host "3. Execute: CREATE DATABASE festasdb;" -ForegroundColor White
Write-Host "4. Reconecte especificando o banco festasdb" -ForegroundColor White

Write-Host "`n=== SQL para Executar no DBeaver ===" -ForegroundColor Cyan
Write-Host "CREATE DATABASE IF NOT EXISTS festasdb;" -ForegroundColor White
Write-Host "USE festasdb;" -ForegroundColor White
Write-Host "SHOW DATABASES;" -ForegroundColor White
