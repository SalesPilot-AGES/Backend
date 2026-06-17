# Sales Pilot - Backend

Bem-vindo ao backend do Sales Pilot! Este projeto é construído utilizando os princípios da **Clean Architecture** combinados com os padrões de design **SOLID**.

O nosso principal objetivo é manter uma base de código altamente desacoplada, testável e de fácil manutenção, onde a lógica de negócios seja completamente isolada dos detalhes de implementação técnica (como banco de dados, frameworks ou APIs externas).

---

## Guia de Arquitetura

O projeto utiliza uma estrutura **multimódulo Maven**, onde cada camada da arquitetura é um módulo independente com o seu próprio `pom.xml` (divididos em `domain`, `application`, `infrastructure`, `presentation` e `bootstrap`).

**Para entender o fluxo de dados, a Regra de Dependência e a responsabilidade de cada camada, leia o nosso [Guia de Arquitetura](docs/ArchitectureGuide.md).**

---

## Endpoints da API

A API do Sales Pilot é documentada utilizando o padrão OpenAPI (Swagger). Você pode explorar todos os recursos disponíveis, esquemas de request/response e testar os endpoints diretamente pela interface interativa.

**Para uma lista completa e detalhada de todos os recursos, consulte os [Endpoints Disponíveis](docs/AvailableEndpoints.md) ou acesse o Swagger local em `/swagger-ui.html`.**

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

1. **Instale o Docker:** Certifique-se de ter o [Docker Desktop](https://www.docker.com/products/docker-desktop/) instalado e rodando.
2. **Configure as Variáveis de Ambiente:** Na raiz do projeto, copie o arquivo `.env.example` para `.env`:
   ```bash
   cp .env.example .env
   ```
   *As configurações padrão já permitem a execução via Docker sem alterações.*
3. **Suba a infraestrutura e a API:**
   ```bash
   docker-compose up -d --build
   ```
4. **(Opcional) Serviços de Monitoramento:** Para subir Prometheus, Grafana e SonarQube, utilize o profile `metrics`:
   ```bash
   docker-compose --profile metrics up -d
   ```
5. **(Opcional) Execução Local (Desenvolvimento):** Caso queira rodar a API fora do Docker (usando apenas o banco de dados do container):
   - Certifique-se de que o container `salespilot-db` esteja rodando.
   - Verifique se a `DB_URL` no seu `.env` aponta para `localhost:5432`.
   - Execute via Maven:
     ```bash
     mvn -pl bootstrap spring-boot:run
     ```
6. **Verifique a Execução:** A API estará rodando em http://localhost:8080.
   - O banco de dados PostgreSQL estará disponível na porta 5432.
   - SonarQube estará acessível em http://localhost:9000.
   - Grafana estará acessível em http://localhost:3000
   - (Para parar a execução, utilize o comando docker-compose down).