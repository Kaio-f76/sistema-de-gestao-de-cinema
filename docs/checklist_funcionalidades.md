# Checklist de Funcionalidades / Fluxos de Usuário

---

# UC_001 - Criar Conta

## Fluxo Principal
- [x] Usuário acessa a área de cadastro
- [x] Sistema exibe formulário de cadastro
- [x] Usuário informa nome, e-mail e senha
- [x] Sistema valida os dados informados
- [x] Sistema confirma cadastro realizado com sucesso
- [ ] Sistema envia e-mail de confirmação
- [x] Fluxo encerrado

## Fluxos Alternativos

### A1 - Cancelamento de Cadastro
- [x] Usuário cancela o cadastro
- [x] Sistema redireciona para página inicial
- [x] Fluxo encerrado

## Fluxos de Exceção

### E1 - Dados inválidos
- [x] Sistema identifica dados inválidos
- [x] Sistema exibe alerta informando erro
- [x] Sistema retorna para preenchimento dos dados

### E2 - E-mail já cadastrado
- [x] Sistema detecta e-mail existente
- [x] Sistema exibe mensagem informando duplicidade
- [x] Sistema solicita novo envio de dados

---

# UC_002 - Realizar Login

## Fluxo Principal
- [x] Usuário informa e-mail e senha
- [x] Sistema valida credenciais
- [x] Sistema autentica o usuário
- [ ] Sistema redireciona para tela inicial
- [x] Sistema lista filmes disponíveis
- [x] Fluxo encerrado

## Fluxos Alternativos

### A1 - Cancelar Login
- [x] Usuário cancela tentativa de login
- [x] Sistema retorna para tela inicial
- [x] Fluxo encerrado

## Fluxos de Exceção

### E1 - Dados inválidos
- [x] Sistema detecta credenciais inválidas
- [x] Sistema exibe alerta
- [x] Sistema solicita nova tentativa de login

---

# UC_003 - Listar Filmes

## Fluxo Principal
- [x] Usuário acessa o site
- [x] Sistema exibe lista de filmes disponíveis
- [x] Usuário seleciona um filme
- [x] Sistema exibe detalhes do filme
- [x] Fluxo encerrado

## Fluxos Alternativos

### A1 - Visualizar Detalhes
- [x] Usuário seleciona um filme
- [x] Sistema exibe informações detalhadas

### A2 - Voltar
- [x] Usuário clica em voltar
- [x] Sistema retorna à lista de filmes

## Fluxos de Exceção

### E1 - Nenhum filme cadastrado
- [x] Sistema detecta ausência de filmes
- [x] Sistema exibe mensagem "Nenhum filme cadastrado"

---

# UC_004 - Comprar Ingressos

## Fluxo Principal
- [x] Cliente seleciona um filme
- [x] Sistema exibe sessões disponíveis
- [x] Cliente seleciona uma sessão
- [x] Sistema exibe mapa de assentos
- [x] Cliente seleciona assentos
- [x] Sistema exibe resumo do pedido
- [ ] Cliente escolhe forma de pagamento
- [x] Sistema valida pagamento
- [x] Sistema gera ingressos
- [x] Fluxo encerrado

## Fluxos Alternativos

### A1 - Cancelar Compra
- [x] Cliente cancela compra no resumo
- [x] Sistema libera assentos reservados
- [x] Fluxo encerrado

## Fluxos de Exceção

### E1 - Nenhum assento selecionado
- [x] Sistema detecta ausência de assentos
- [x] Sistema impede avanço da compra
- [x] Sistema solicita seleção de assento

### E2 - Saldo insuficiente
- [x] Sistema verifica saldo
- [x] Sistema identifica saldo insuficiente
- [x] Sistema bloqueia compra
- [ ] Sistema retorna à tela inicial

### E3 - Assento indisponível
- [x] Sistema detecta conflito de assento
- [x] Sistema atualiza mapa de assentos
- [x] Sistema solicita nova escolha

### E4 - Falha no pagamento
- [ ] Sistema recebe erro da operadora
- [ ] Sistema exibe mensagem de erro
- [ ] Sistema permite nova tentativa ou cancelamento

---

# UC_005 - Gerenciar Salas

## Fluxo Principal
- [x] Administrador realiza login
- [x] Sistema autentica administrador
- [x] Administrador acessa gerenciamento de salas
- [x] Sistema exibe opções: listar, atualizar, excluir e cadastrar

## Fluxos Alternativos

### A1 - Listar Salas
- [x] Administrador acessa listagem
- [x] Sistema exibe todas as salas cadastradas

### A2 - Atualizar Sala
- [x] Administrador seleciona uma sala
- [x] Sistema exibe detalhes
- [x] Administrador edita dados
- [x] Sistema salva alterações
- [ ] Sistema exibe dados atualizados

### A3 - Excluir Sala
- [ ] Administrador seleciona sala
- [ ] Administrador confirma exclusão
- [ ] Sistema remove sala

### A4 - Criar Sala
- [x] Administrador seleciona criar sala
- [x] Administrador informa dados da sala
- [x] Sistema valida informações
- [x] Sistema registra sala

## Fluxos de Exceção

### E1 - Dados inválidos ou sala duplicada
- [x] Sistema identifica erro nos dados
- [x] Sistema exibe mensagem de erro

### E2 - Operação cancelada
- [x] Administrador cancela ação
- [x] Sistema retorna à página de salas

---

# UC_006 - Consultar Relatórios

## Fluxo Principal
- [x] Sistema lista dados coletados
- [x] Sistema exibe filtros de pesquisa
- [x] Administrador aplica filtros
- [x] Sistema exibe resultados
- [x] Administrador exporta relatório em PDF

## Fluxos Alternativos

### A1 - Exportar relatório filtrado
- [x] Administrador exporta resultados filtrados para PDF

## Fluxos de Exceção

### E1 - Dados obrigatórios não preenchidos
- [x] Sistema detecta ausência de dados
- [x] Sistema exibe mensagem de erro

---

# UC_007 - Gerenciar Filmes

## Fluxo Principal
- [x] Administrador realiza login
- [x] Sistema autentica administrador
- [x] Administrador acessa gerenciamento de filmes
- [x] Sistema exibe opções: listar, editar, excluir e cadastrar

## Fluxos Alternativos

### A1 - Listar Filmes
- [x] Sistema exibe lista de filmes cadastrados

### A2 - Editar Filme
- [x] Administrador acessa detalhes do filme
- [x] Administrador edita informações
- [x] Sistema salva alterações

### A3 - Excluir Filme
- [x] Administrador seleciona filme
- [x] Administrador confirma exclusão
- [x] Sistema remove filme

### A4 - Cadastrar Filme
- [x] Administrador acessa cadastro
- [x] Sistema exibe formulário
- [x] Administrador preenche dados
- [x] Sistema valida informações
- [x] Sistema registra novo filme

## Fluxos de Exceção

### E1 - Campos obrigatórios não preenchidos
- [ ] Sistema identifica campos vazios
- [x] Sistema exibe alerta
- [ ] Sistema solicita correção

---

# UC_008 - Gerenciar Contas

## Fluxo Principal
- [x] Usuário realiza login
- [x] Sistema valida credenciais
- [x] Sistema exibe dados da conta
- [x] Usuário seleciona opção de editar
- [x] Usuário altera dados
- [x] Sistema salva alterações

## Fluxos Alternativos

### A1 - Excluir Conta
- [x] Usuário solicita exclusão
- [ ] Sistema verifica pendências
- [ ] Sistema remove conta
- [ ] Sistema confirma exclusão

### A2 - Desativar Conta
- [ ] Usuário acessa detalhes da conta
- [ ] Usuário seleciona desativar conta
- [ ] Sistema solicita confirmação
- [ ] Usuário confirma com senha
- [ ] Sistema altera status da conta para inativa

## Fluxos de Exceção

### E1 - Exclusão não permitida
- [x] Sistema detecta dependências (ex: débito)
- [x] Sistema exibe mensagem de erro
- [x] Sistema impede exclusão

---

# UC_009 - Pesquisar Filmes

## Fluxo Principal
- [ ] Usuário acessa barra de pesquisa
- [x] Usuário digita nome ou seleciona gênero
- [x] Sistema processa busca
- [x] Sistema exibe resultados
- [x] Usuário seleciona filme para ver detalhes
- [x] Fluxo encerrado

## Fluxos Alternativos

### A1 - Visualizar detalhes
- [x] Usuário seleciona filme
- [x] Sistema exibe informações completas

### A2 - Retornar
- [x] Usuário volta à tela inicial
- [x] Sistema retorna à lista

## Fluxos de Exceção

### E1 - Nenhum resultado encontrado
- [x] Sistema não encontra filmes
- [x] Sistema exibe mensagem informativa
- [x] Sistema permite nova busca

---

# UC_010 - Gerenciar Sessões

## Fluxo Principal
- [x] Administrador acessa gestão de sessões
- [x] Sistema lista sessões cadastradas
- [x] Administrador cria nova sessão
- [x] Administrador seleciona filme
- [x] Administrador seleciona sala
- [x] Administrador define data e horário
- [ ] Sistema valida disponibilidade da sala
- [ ] Sistema calcula horário de término
- [x] Sistema registra sessão

## Fluxos Alternativos

### A1 - Editar Sessão
- [x] Administrador altera dados da sessão
- [x] Sistema valida conflitos
- [x] Sistema salva alterações

### A2 - Cancelar Sessão
- [x] Administrador solicita exclusão
- [x] Sistema verifica vendas associadas

## Fluxos de Exceção

### E1 - Conflito de horário
- [x] Sistema detecta sobreposição de sessões
- [ ] Sistema exibe erro de sala ocupada

### E2 - Sessão com ingressos vendidos
- [x] Sistema detecta vendas existentes
- [x] Sistema bloqueia exclusão
- [x] Sistema solicita estorno antes da remoção