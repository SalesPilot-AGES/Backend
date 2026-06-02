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

Para facilitar a configuração do ambiente, utilizamos o Docker Compose. Ele subirá automaticamente a nossa API e o banco de dados PostgreSQL. Os serviços de métricas (Prometheus, Grafana e SonarQube) só sobem quando o profile `metrics` estiver ativo.

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
4. **(Opcional) Subir também os serviços de métricas:** Use o profile `metrics` para iniciar Prometheus, Grafana e SonarQube.
   ```text
    docker-compose --profile metrics up -d --build
   ```
5. **(Opcional) Rodar a API localmente com o Postgres do Docker:** Como o projeto usa `spring.config.import=optional:file:.env[.properties]`, ao manter o `.env` na raiz você pode iniciar a API local sem exportar variáveis manualmente.
   ```text
    mvn -pl bootstrap spring-boot:run
   ```
6. **Verifique a Execução:** A API estará rodando em http://localhost:8080.
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
| PUT | `/api/collaborators/managers/{id}` | Atualiza colaborador com role de gestor |
| GET | `/api/collaborators/managers/{id}` | Busca gestor por ID |
| GET | `/api/collaborators/managers` | Lista gestores com filtros opcionais e paginação |

### Corpos de request (Postman)

- `POST /api/companies`
  - Body JSON:

  ```json
  {
    "name": "Digital Sales Ltda",
    "tax_id": "12.345.678/0001-90",
    "plan": "BASIC",
    "active": true
  }
  ```

  - `plan`: `BASIC`, `PRO`, `ENTERPRISE`

- `GET /api/companies`
  - Query params opcionais: `name`, `taxId`, `plan`, `active`, `page`, `size`, `sort`
  - Limite de paginação: `size` máximo de `100`
  - Body: não possui

- `GET /api/companies/{id}`
  - Body: não possui

- `PUT /api/companies/{id}`
  - Body JSON:

  ```json
  {
    "name": "Digital Sales Ltda",
    "plan": "PRO",
    "active": true
  }
  ```

   - `plan`: `BASIC`, `PRO`, `ENTERPRISE`

- `POST /api/collaborators/managers`
  - Body JSON:

  ```json
  {
    "company_id": "b1c2d3e4-f5a6-7890-2345-67890abcdef1",
    "name": "Gabriel Ribeiro",
    "email": "gabriel@digitalsales.com",
    "active": true,
    "preferences": {
      "theme": "light",
      "default_model": "gpt-4o"
    }
  }
  ```

- `PUT /api/collaborators/managers/{id}`
  - Body JSON:

  ```json
  {
    "company_id": "b1c2d3e4-f5a6-7890-2345-67890abcdef1",
    "name": "Gabriel Ribeiro",
    "email": "gabriel@digitalsales.com",
    "active": true,
    "preferences": {
      "theme": "light",
      "default_model": "gpt-4o"
    }
  }
  ```

- `GET /api/collaborators/managers/{id}`
  - Body: não possui

- `GET /api/collaborators/managers`
  - Query params opcionais: `name`, `email`, `companyId`, `active`, `page`, `size`, `sort`
  - Body: não possui

- `GET /api/v1/system/ping`
  - Body: não possui
