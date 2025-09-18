# Script para baixar drivers MySQL via PowerShell
# Execute: powershell -ExecutionPolicy Bypass -File download-drivers.ps1

Write-Host "=== Baixando Drivers MySQL ===" -ForegroundColor Green

# Criar diretorio drivers se nao existir
if (!(Test-Path "drivers")) {
    New-Item -ItemType Directory -Name "drivers"
    Write-Host "Diretorio 'drivers' criado" -ForegroundColor Yellow
}

# URLs dos drivers MySQL
$drivers = @(
    @{
        Name = "mysql-connector-j-8.2.0.jar"
        Url = "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.2.0/mysql-connector-j-8.2.0.jar"
    },
    @{
        Name = "mysql-connector-j-8.3.0.jar"
        Url = "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.3.0/mysql-connector-j-8.3.0.jar"
    },
    @{
        Name = "mysql-connector-j-8.1.0.jar"
        Url = "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.1.0/mysql-connector-j-8.1.0.jar"
    }
)

# Baixar cada driver
foreach ($driver in $drivers) {
    $filePath = "drivers\$($driver.Name)"
    
    if (Test-Path $filePath) {
        Write-Host "Driver $($driver.Name) ja existe, pulando..." -ForegroundColor Yellow
        continue
    }
    
    try {
        Write-Host "Baixando $($driver.Name)..." -ForegroundColor Cyan
        Invoke-WebRequest -Uri $driver.Url -OutFile $filePath
        Write-Host "SUCESSO: $($driver.Name) baixado!" -ForegroundColor Green
    }
    catch {
        Write-Host "ERRO ao baixar $($driver.Name): $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Listar arquivos baixados
Write-Host "`n=== Drivers Disponiveis ===" -ForegroundColor Green
Get-ChildItem -Path "drivers" -Filter "*.jar" | ForEach-Object {
    $size = [math]::Round($_.Length / 1MB, 2)
    Write-Host "Arquivo: $($_.Name) - $size MB" -ForegroundColor White
}

Write-Host "`n=== Instrucoes para DBeaver ===" -ForegroundColor Yellow
Write-Host "1. Abra o DBeaver" -ForegroundColor White
Write-Host "2. Va em: Database -> Driver Manager" -ForegroundColor White
Write-Host "3. Selecione: MySQL -> Edit" -ForegroundColor White
Write-Host "4. Na aba Libraries: Add File - Selecione um dos drivers baixados" -ForegroundColor White
Write-Host "5. Salve e teste a conexao" -ForegroundColor White

Write-Host "`nDownload concluido!" -ForegroundColor Green