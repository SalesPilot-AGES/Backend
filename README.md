# Sales Pilot - Backend

Bem-vindo ao backend do Sales Pilot! Este projeto é construído utilizando os princípios da **Clean Architecture** combinados com os padrões de design **SOLID**.

O nosso principal objetivo é manter uma base de código altamente desacoplada, testável e de fácil manutenção, onde a lógica de negócios seja completamente isolada dos detalhes de implementação técnica (como banco de dados, frameworks ou APIs externas).

---

## Guia de Arquitetura

O projeto utiliza uma estrutura **multimódulo Maven**, onde cada camada da arquitetura é um módulo independente com o seu próprio `pom.xml` (divididos em `domain`, `application`, `infrastructure`, `presentation` e `bootstrap`).

**Para entender o fluxo de dados, a Regra de Dependência e a responsabilidade de cada camada, leia o nosso [Guia de Arquitetura](docs/ArchitectureGuide.md).**

---

## Padrões de Commit

Usaremos o Conventional Commits para manter o histórico do repositório mais organizado e legível, além de melhorar a comunicação interna.

1. Todo commit deve seguir o formato: `<type>: <short description>`.
   * **type**: Indica o propósito da mudança.
   * **short description**: Deve ser clara, objetiva, escrita em inglês e no imperativo (ex.: "add", "fix", "remove").
2. Escreva as mensagens de commit em inglês.
3. Seja claro e objetivo, isso é, invés de 1 único commit com todas as mudanças, faça commit a cada trecho significativo de código.

### Types Disponíveis
* `feat`: Nova funcionalidade
* `fix`: Correção de bug
* `refactor`: Refatoração de código
* `style`: Estilo / legibilidade (sem mudança de lógica)
* `test`: Testes automatizados
* `build`: Build e dependências
* `perf`: Melhoria de performance
* `ci`: Integração contínua (CI/CD)
* `revert`: Reversão de commit anterior

---

## Como Executar o Projeto

Para facilitar a configuração do ambiente, utilizamos o Docker Compose. Ele subirá automaticamente a nossa API, o banco de dados PostgreSQL e o SonarQube.

### Passo a Passo

1. **Instale o Docker:** Certifique-se de ter o [Docker Desktop](https://www.docker.com/products/docker-desktop/) instalado e rodando na sua máquina.
2. **Configure as Variáveis de Ambiente:** Na raiz do projeto, copie `.env.example` para `.env` e ajuste as credenciais do banco de dados:
   ```env
   DB_NAME=salespilot
   DB_USER=postgres
   DB_PASSWORD=postgres
3. **Suba os Contêineres:** Abra o terminal na raiz do projeto e execute o comando abaixo para construir a aplicação e iniciar os serviços:
   ```text
    docker-compose up -d --build
   ```
4. **(Opcional) Rodar a API localmente com o Postgres do Docker:** Como o projeto usa `spring.config.import=optional:file:.env[.properties]`, ao manter o `.env` na raiz você pode iniciar a API local sem exportar variáveis manualmente.
   ```text
    mvn -pl bootstrap spring-boot:run
   ```
5. **Verifique a Execução:** A API estará rodando em http://localhost:8080.
   - O banco de dados PostgreSQL estará disponível na porta 5432.
   - SonarQube estará acessível em http://localhost:9000.
   - Grafana estará acessível em http://localhost:3000
   - (Para parar a execução, utilize o comando docker-compose down).
---

## Endpoints Disponíveis

Base URL local: `http://localhost:8080`

| Método | Caminho | Descrição |
|--------|---------|-----------|
| GET | `/api/v1/system/ping` | Verifica o status do sistema |
| POST | `/api/companies` | Cria uma nova empresa |
| GET | `/api/companies` | Lista empresas com filtros opcionais e paginação |
| GET | `/api/companies/{id}` | Busca empresa por ID |
| PUT | `/api/companies/{id}` | Atualiza `name`, `plan` e `active` de uma empresa |
| POST | `/api/collaborators/managers` | Cria colaborador com role de gestor |

### Detalhes rápidos

- `POST /api/companies`
  - Body: `name`, `taxId`, `plan`, `active`
  - `plan`: `BASIC`, `PRO`, `ENTERPRISE`

- `GET /api/companies`
  - Query params opcionais: `name`, `taxId`, `plan`, `active`, `page`, `size`, `sort`
  - Limite de paginação: `size` máximo de `100`

- `PUT /api/companies/{id}`
  - Body: `name`, `plan`, `active`
  - `plan` segue enum `CompanyPlan`

- `POST /api/collaborators/managers`
  - Body: `companyId`, `name`, `email`, `active`, `preferences`
