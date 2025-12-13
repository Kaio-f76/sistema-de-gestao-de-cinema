# Sistema de Gestão de Cinema

 O projeto desenvolvido é um sistema para gerenciar as operações de um cinema, incluindo cadastro de filmes, controle de sessões, vendas de ingressos, reservas, e administração de salas. O sistema visa facilitar o planejamento da programação e o atendimento ao público, otimizando a gestão de recursos e a experiência dos clientes. O sistema se divide entre administrador e cliente, cada um com seu nível de acesso, o administrador pode cadastrar filmes, controlar as sessões, controlar a venda de ingressos, as reservas de cada sessão e a administração geral e tomadas de decisões através de dados e informações geradas e a geração de relatórios, tendo uma tela separada só para essas funções onde o cliente não possua acesso. O cliente tem a acesso apenas a tela da aplicação diferente do administrador que possui uma tela própria, cliente pode, visualizar os filmes em cartaz, filtrar os filmes por gênero, consultar os horários das sessões, verificar a disponibilidade de acesso entre as sessões, selecionar o filme, sala e assento desejado e efetuar a compra de ingressos. O projeto em geral funcionará na interação de cliente para administrador, onde o cliente terá as totais interações com o sistema, criando dados que poderão ser transformadas em informações para a tomada de decisões por parte do administrador, melhorando a qualidade e a experiência do cliente e a administração do sistema.


---

## Como Rodar o Projeto

### Pré-requisitos

* Docker e Docker Compose instalados ([Tutorial Docker](docs/docker_tutorial.md))
* Git configurado ([Tutorial Git](docs/git_tutorial.md))

### Passos para execução

1. Clonar o repositório:

```bash
git clone sistema_de_gestao_de_cinema
cd sistema_de_gestao_de_cinema
````

2. Subir os containers com Docker Compose:
> **dessa maneira os containers ficando rodando em segundo plano**

````bash
docker compose up -d
````
> **dessa outra maneira eles rodando pelo terminal até que o mesmo seja fechado**

````bash
docker compose up 
````

3. Verificar containers em execução:

````bash
docker ps
````

4. Acessar a aplicação no navegador (exemplo):

````
http://localhost:8080
````

### Parar os containers

````bash
docker compose down
````

---

## Documentações Extras

* [Tutorial Docker](docs/docker_tutorial.md) – Guia completo de instalação, configuração e uso do Docker neste projeto
* [Tutorial Git](docs/git_tutorial.md) – Guia de fluxo de trabalho, criação de branches, pull requests e padrão de commits

---

## Observações

* Para desenvolvimento, siga as instruções do tutorial Git para criar sua branch e abrir pull requests
* Utilize o padrão de commits descrito no tutorial Git para manter o histórico organizado
