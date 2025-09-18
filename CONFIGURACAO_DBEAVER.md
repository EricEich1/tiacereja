# 🗄️ Configuração do DBeaver para MySQL

## 📋 Credenciais do Banco de Dados

- **Host:** `localhost`
- **Porta:** `3306`
- **Database:** `festasdb`
- **Username:** `root`
- **Password:** `root`

## 🔧 Passo a Passo para Configurar DBeaver

### 1. Abrir DBeaver
- Inicie o DBeaver

### 2. Criar Nova Conexão
- Clique em **"New Database Connection"** (ícone de plug)
- Ou vá em: `Database` → `New Database Connection`

### 3. Selecionar Driver
- Escolha **"MySQL"** na lista de drivers
- Clique em **"Next"**

### 4. Configurar Driver (se necessário)
Se o driver MySQL não estiver configurado:

1. **Vá em:** `Database` → `Driver Manager`
2. **Selecione:** `MySQL` → `Edit`
3. **Na aba "Libraries":**
   - Clique em `Add File`
   - Selecione: `drivers\mysql-connector-j-8.2.0.jar`
4. **Clique em "OK"**

### 5. Configurar Conexão
Preencha os campos:

```
Server Host: localhost
Port: 3306
Database: festasdb
Username: root
Password: root
```

### 6. Testar Conexão
- Clique em **"Test Connection"**
- Deve aparecer: ✅ **"Connected"**

### 7. Salvar Conexão
- Clique em **"Finish"**
- Dê um nome para a conexão: `FestasDB`

## 🧪 Testando a Conexão

Após conectar, execute estas queries para verificar:

```sql
-- Verificar versão do MySQL
SELECT VERSION();

-- Verificar se o banco existe
SHOW DATABASES;

-- Usar o banco festasdb
USE festasdb;

-- Verificar tabelas criadas
SHOW TABLES;

-- Verificar estrutura da tabela clientes
DESCRIBE cliente;
```

## 📊 Tabelas do Sistema

O sistema criará automaticamente estas tabelas:

- **`cliente`** - Dados dos clientes
- **`endereco`** - Endereços dos clientes
- **`tema_festa`** - Temas de festa disponíveis
- **`tipo_evento`** - Tipos de eventos
- **`solicitacao_orcamento`** - Solicitações de orçamento

## 🚨 Solução de Problemas

### Erro: "Driver not found"
- Verifique se o driver MySQL está configurado
- Use o arquivo: `drivers\mysql-connector-j-8.2.0.jar`

### Erro: "Access denied"
- Verifique se o MySQL está rodando
- Confirme as credenciais: `root` / `root`

### Erro: "Database does not exist"
- Crie o banco manualmente:
```sql
CREATE DATABASE festasdb;
```

## ✅ Configuração Concluída

Quando tudo estiver funcionando, você verá:
- Conexão ativa no DBeaver
- Tabelas do sistema visíveis
- Possibilidade de executar queries SQL

---
**Dica:** Use as credenciais `root` / `root` para facilitar o desenvolvimento! 🚀
