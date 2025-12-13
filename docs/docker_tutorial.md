# Documentação Docker

**Instalação, Configuração e Uso no Linux e Windows**

## 1. Visão Geral

Docker é uma plataforma de containerização que permite empacotar aplicações e suas dependências em containers padronizados, garantindo consistência entre ambientes de desenvolvimento, teste e produção, independentemente do sistema operacional do desenvolvedor.

Esta documentação descreve:

* Instalação do Docker no Linux (Linux Mint como base)
* Instalação do Docker no Windows
* Configuração inicial
* Execução de containers usando Dockerfile e docker-compose
* Considerações sobre o uso de WSL no Windows
* Solução de problemas comuns

---

## 2. Requisitos Mínimos

### 2.1 Linux

* Distribuição baseada em Ubuntu (Linux Mint, Ubuntu, Pop!_OS)
* Kernel Linux 5.x ou superior
* Acesso sudo

### 2.2 Windows

* Windows 10 ou 11 (64 bits)
* Virtualização habilitada na BIOS
* WSL 2 (Windows Subsystem for Linux)
* Conta com privilégios de administrador

> **Nota:** O uso de WSL 2 é obrigatório para execução de containers Linux, que representam o padrão adotado pela indústria.

---

## 3. Instalação do Docker no Linux (Linux Mint)

### 3.1 Atualizar o sistema

```bash
sudo apt update
sudo apt upgrade -y
```

### 3.2 Instalar o Docker Engine

```bash
sudo apt install -y docker.io
```

### 3.3 Iniciar e habilitar o serviço

```bash
sudo systemctl start docker
sudo systemctl enable docker
```

### 3.4 Verificar instalação

```bash
docker --version
```

---

## 4. Configuração do Docker no Linux

### 4.1 Executar Docker sem sudo (recomendado)

```bash
sudo usermod -aG docker $USER
```

É necessário efetuar logout/login ou reiniciar a sessão para que a alteração tenha efeito.

### 4.2 Teste de funcionamento

```bash
docker run hello-world
```

---

## 5. Instalação do Docker no Windows

### 5.1 Instalar WSL 2

No PowerShell (como Administrador):

```powershell
wsl --install
```

Após a instalação, reinicie o sistema.

Verificar versão:

```powershell
wsl --list --verbose
```

---

### 5.2 Instalar Docker Desktop

1. Baixar o Docker Desktop no site oficial
2. Executar o instalador
3. Selecionar a opção **Use WSL 2 based engine**
4. Concluir a instalação e reiniciar

### 5.3 Verificar instalação

No PowerShell ou terminal WSL:

```bash
docker --version
```

---

## 6. Configuração do Docker no Windows

### 6.1 Integração com WSL

* Abrir Docker Desktop
* Settings → Resources → WSL Integration
* Habilitar a distribuição Linux desejada

### 6.2 Teste de funcionamento

```bash
docker run hello-world
```

---

## 7. Considerações sobre Docker no Windows sem WSL

### 7.1 Windows Containers

É tecnicamente possível executar Docker no Windows sem WSL utilizando **Windows Containers**.

Características:

* Utilizam o kernel do Windows
* Exigem imagens baseadas em Windows
* Dependem de Hyper-V
* Disponíveis apenas em versões Pro, Enterprise ou Education

Exemplo de imagem compatível:

```powershell
docker run mcr.microsoft.com/windows/nanoserver
```

### 7.2 Limitações dos Windows Containers

* Não suportam imagens Linux
* Incompatíveis com a maioria dos projetos modernos
* Imagens maiores e builds mais lentos
* Pouco suporte da comunidade open-source

### 7.3 Padrão adotado pela indústria

A maioria absoluta dos projetos utiliza **Linux Containers**, incluindo:

* Backends (Node.js, Python, Java, Go)
* Bancos de dados (PostgreSQL, MySQL, Redis)
* Ambientes de CI/CD
* Kubernetes

Por este motivo, o uso de **WSL 2 no Windows é considerado obrigatório** para manter compatibilidade com ambientes Linux de produção.

---

## 8. Execução de Containers

### 8.1 Rodar um container a partir de uma imagem

```bash
docker run nginx
```

Em modo detached:

```bash
docker run -d nginx
```

Com mapeamento de portas:

```bash
docker run -d -p 8080:80 nginx
```

---

## 9. Trabalhando com Dockerfile

### 9.1 Build da imagem

Na raiz do projeto:

```bash
docker build -t minha_imagem .
```

### 9.2 Execução do container

```bash
docker run minha_imagem
```

Com portas:

```bash
docker run -d -p 3000:3000 minha_imagem
```

---

## 10. Trabalhando com docker-compose

### 10.1 Instalação do Docker Compose (Linux)

```bash
sudo apt install docker-compose-plugin
```

Verificar:

```bash
docker compose version
```

---

### 10.2 Subir os serviços

```bash
docker compose up
```

Em segundo plano:

```bash
docker compose up -d
```

> **Atenção sobre portas MySQL:**
> Caso adicione um serviço MySQL no `docker-compose.yml`, o mapeamento padrão da porta é `3306:3306`. Se o desenvolvedor já tiver MySQL rodando localmente, esta porta estará ocupada, e o container não iniciará.
> **Soluções:**
>
> * Alterar a porta mapeada no `docker-compose.yml`, por exemplo:
>
> ```yaml
> ports:
>   - "3307:3306"
> ```
>
> * Parar a instância local do MySQL enquanto o container estiver rodando.

---

### 10.3 Parar os serviços

```bash
docker compose down
```

---

## 11. Comandos Úteis

Listar containers em execução:

```bash
docker ps
```

Listar todos os containers:

```bash
docker ps -a
```

Listar imagens:

```bash
docker images
```

Parar container:

```bash
docker stop <container>
```

Remover container:

```bash
docker rm <container>
```

---

## 12. Problemas Comuns e Soluções

### 12.1 Erro: Permission denied (Linux)

**Causa:** Usuário não pertence ao grupo docker
**Solução:**

```bash
sudo usermod -aG docker $USER
```

---

### 12.2 Erro: Docker daemon is not running

**Linux:**

```bash
sudo systemctl start docker
```

**Windows:**
Verificar se o Docker Desktop está em execução.

---

### 12.3 Erro: Port is already allocated

**Causa:** Porta já está em uso
**Solução:**

```bash
docker ps
```

Alterar o mapeamento de portas:

```bash
-p 8081:80
```

---

### 12.4 Erro: Cannot connect to the Docker daemon (Windows)

**Causa:** WSL não integrado ou Docker Desktop parado
**Solução:**

* Reiniciar Docker Desktop
* Verificar integração com WSL
* Executar:

```powershell
wsl --shutdown
```

---

### 12.5 Containers não refletem alterações no código

**Causa:** Imagem desatualizada
**Solução:**

```bash
docker compose up --build
```

---

### 12.6 Erro: Porta 3306 já está em uso (MySQL)

**Causa:** Porta padrão do MySQL já ocupada por instância local

**Soluções:**

1. Alterar mapeamento da porta no `docker-compose.yml`:

```yaml
ports:
  - "3307:3306"
```

2. Parar o MySQL local enquanto utiliza o container:

```bash
# Linux
sudo systemctl stop mysql

# Windows (PowerShell)
net stop MySQL
```

---

## 13. Boas Práticas

* Versionar Dockerfile e docker-compose.yml
* Utilizar `.dockerignore`
* Não versionar arquivos `.env`
* Usar imagens oficiais sempre que possível
* Manter imagens pequenas e com poucas camadas

---

## 14. Conclusão

Docker garante padronização entre ambientes Linux, Windows e macOS. Para projetos modernos baseados em containers Linux, o uso de WSL 2 no Windows é essencial para garantir compatibilidade com ambientes de produção. Ao compartilhar apenas arquivos de definição (Dockerfile e docker-compose.yml), equipes conseguem reproduzir ambientes idênticos independentemente do sistema operacional do desenvolvedor, reduzindo erros e inconsistências.


