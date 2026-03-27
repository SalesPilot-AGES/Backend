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
2. **Configure as Variáveis de Ambiente:** Na raiz do projeto, crie um arquivo chamado `.env` e adicione as credenciais do banco de dados:
   ```env
   DB_NAME=
   DB_USER=
   DB_PASSWORD=
3. **Suba os Contêineres:** Abra o terminal na raiz do projeto e execute o comando abaixo para construir a aplicação e iniciar os serviços:
   ```text
    docker-compose up -d --build
   ```
4. **Verifique a Execução:** A API estará rodando em http://localhost:8080.
   - O banco de dados PostgreSQL estará disponível na porta 5432.
   - SonarQube estará acessível em http://localhost:9000.
   - (Para parar a execução, utilize o comando docker-compose down).
---

## Endpoints Disponíveis

| Método | Caminho                  | Descrição                        |
|--------|--------------------------|----------------------------------|
| GET    | `/api/v1/system/ping`    | Verifica o status do sistema     |
