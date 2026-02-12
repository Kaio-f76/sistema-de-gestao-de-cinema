# Documentação Git

**Fluxo de Trabalho, Padrões de Commit e Uso no Projeto**

## 1. Visão Geral

Git é um sistema de controle de versão distribuído que permite rastrear alterações no código, colaborar com equipes e gerenciar diferentes versões de um projeto de forma eficiente.

Esta documentação descreve:

* Instalação do Git no Linux e Windows
* Configuração inicial do Git
* Fluxo de trabalho com branches no projeto
* Uso de pull requests para integração de código
* Padrões de commits (Conventional Commits)
* Solução de problemas comuns

---

## 2. Requisitos Mínimos

### 2.1 Linux

* Distribuição baseada em Ubuntu (Linux Mint, Ubuntu, Pop!_OS)
* Git versão 2.x ou superior
* Acesso sudo

### 2.2 Windows

* Windows 10 ou 11 (64 bits)
* Git for Windows instalado
* Conta com privilégios de administrador

---

## 3. Instalação do Git

### 3.1 Linux

```bash
sudo apt update
sudo apt install -y git
```

Verificar versão:

```bash
git --version
```

### 3.2 Windows

1. Baixar Git for Windows no site oficial: [https://git-scm.com/download/win](https://git-scm.com/download/win)
2. Executar instalador e manter as configurações padrão
3. Verificar instalação:

```powershell
git --version
```

---

## 4. Configuração Inicial do Git

### 4.1 Configurar usuário global

> **Importante:** O e-mail configurado deve ser **o mesmo utilizado na sua conta do GitHub**, para que seus commits sejam corretamente associados.

```bash
git config --global user.name "Seu Nome"
git config --global user.email "seu.email@dominio.com"
```

### 4.2 Configurar editor padrão

```bash
git config --global core.editor "nano"  # ou "code --wait" para VS Code
```

### 4.3 Verificar configuração

```bash
git config --list
```

---

## 5. Fluxo de Trabalho no Projeto

O projeto utiliza **branches separadas para cada desenvolvedor**, garantindo isolamento e revisão de código antes da integração na main.

### 5.1 Estrutura de Branches

* `main`: branch principal; apenas avaliadores têm permissão para merge
* `feature/<nome-da-feature>`: para desenvolvimento de funcionalidades isoladas
* `hotfix/<descricao>`: correção rápida em produção (para avaliadores)

### 5.2 Criação de Branch

```bash
# Partindo da main
git checkout main
git pull origin main

# Criar branch da feature
git checkout -b feature/<nome-da-feature>`
```

### 5.3 Commit de Alterações

1. Adicionar arquivos alterados:

```bash
git add .
```

2. Criar commit seguindo **Conventional Commits** (detalhado abaixo):

```bash
git commit -m "feat: adicionar endpoint de login"
```

### 5.4 Envio da Branch para o Repositório Remoto

```bash
git push origin dev/<seu-nome>
```

### 5.5 Pull Request (PR)

* Abrir PR da sua branch `dev/<seu-nome>` para `main`
* Revisores irão validar e aprovar a PR
* Após aprovação, avaliadores fazem merge na branch `main`
* Branch do desenvolvedor pode ser atualizada com:

```bash
git fetch origin
git rebase origin/main
```

> **Nota:** Desenvolvedores sem acesso direto à main devem sempre usar PRs para integrar suas alterações.

---

## 6. Padrões de Commit (Conventional Commits)

O projeto utiliza **Conventional Commits** para padronizar mensagens de commit, facilitando rastreabilidade e geração de changelogs automáticos.

| Tipo     | Descrição                                                   | Exemplo de Commit                          |
| -------- | ----------------------------------------------------------- | ------------------------------------------ |
| feat     | Nova funcionalidade                                         | `feat: adicionar endpoint de login`        |
| fix      | Correção de bug                                             | `fix: corrigir erro de validação de email` |
| docs     | Alterações em documentação                                  | `docs: atualizar README.md`                |
| style    | Formatação, espaços, ponto e vírgula, sem mudança de lógica | `style: ajustar indentação do código`      |
| refactor | Refatoração de código sem mudança de funcionalidade         | `refactor: reorganizar classes de serviço` |
| perf     | Melhorias de performance                                    | `perf: otimizar consulta ao banco`         |
| test     | Adição ou alteração de testes                               | `test: criar testes para service X`        |
| chore    | Alterações de build, configuração, scripts                  | `chore: atualizar dependências do Maven`   |

> **Dica:** Sempre escreva commits no **imperativo presente**, por exemplo: “adicionar”, “corrigir”, “remover”.
> **Atenção:** Verifique se o e-mail configurado no Git é o mesmo da sua conta GitHub antes de commitar, para evitar problemas de associação de commits.

---

## 7. Fluxo de Trabalho Resumido

1. Atualizar branch local main:

```bash
git checkout main
git pull origin main
```

2. Criar branch do dev:

```bash
git checkout -b dev/<seu-nome>
```

3. Desenvolver código e realizar commits seguindo o padrão:

```bash
git add .
git commit -m "tipo: descrição do commit"
```

4. Enviar branch para repositório remoto:

```bash
git push origin dev/<seu-nome>
```

5. Abrir Pull Request para `main`

6. Resolver feedbacks dos revisores e atualizar branch se necessário:

```bash
git fetch origin
git rebase origin/main
```

7. Após aprovação, a branch será integrada na main pelos avaliadores

---

## 8. Problemas Comuns e Soluções

### 8.1 Conflito de Merge

**Causa:** Alterações divergentes entre sua branch e a main

**Solução:**

```bash
git fetch origin
git rebase origin/main
# Resolver conflitos
git add .
git rebase --continue
git push origin dev/<seu-nome> --force
```

---

### 8.2 Commit fora do padrão

**Causa:** Mensagem de commit não segue Conventional Commits

**Solução:** Reescrever o commit:

```bash
git commit --amend -m "feat: descrição correta do commit"
```

---

### 8.3 Push negado

**Causa:** Branch remota foi atualizada

**Solução:**

```bash
git fetch origin
git rebase origin/dev/<seu-nome>
git push origin dev/<seu-nome> --force
```

---

### 8.4 Pull Request rejeitado

**Causa:** Código não segue padrões ou apresenta erros

**Solução:**

* Ajustar conforme feedback do revisor
* Commitar alterações seguindo o padrão
* Atualizar PR

---

## 9. Boas Práticas

* Sempre atualizar branch antes de iniciar desenvolvimento
* Commits pequenos e claros
* Seguir estritamente os tipos do **Conventional Commits**
* Abrir PRs frequentes para facilitar revisão
* Resolver conflitos rapidamente
* Não realizar merge direto na main sem aprovação
* Certificar que o e-mail configurado no Git seja o mesmo do GitHub antes de cada commit

---

## 10. Conclusão

O uso correto do Git e do fluxo de branches isoladas garante organização, rastreabilidade e colaboração eficiente entre desenvolvedores. O padrão de **Conventional Commits** facilita comunicação, revisão de código e geração de histórico do projeto. Seguindo esta documentação e utilizando o mesmo e-mail do GitHub, a equipe consegue integrar alterações de forma segura e padronizada, evitando conflitos e mantendo a main estável.


