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
* `hotfix`: Correção crítica em ambiente de produção

---

## Padrões de Branch

Todas as branches devem seguir o formato: `<type>/<issue-number>/<short-description>`.

* **type**: Um dos tipos disponíveis listados acima.
* **issue-number**: Número da issue no Jira/GitHub ou `no-ref` se não aplicável.
* **short-description**: Descrição curta em kebab-case (ex.: `add-user-auth`, `fix-sidebar-bug`).

### Protected Branches

As seguintes branches são protegidas e não requerem validação de padrão:
* `main` - produção
* `development` - homologação/desenvolvimento

### Exemplos de Branches Válidas
```text
feat/1234/add-user-authentication
fix/5678/sidebar-alignment-issue
refactor/no-ref/simplify-form-logic
hotfix/9999/critical-database-bug
```

---

## Git Hooks (Lefthook)

O projeto utiliza **Lefthook** para automatizar validações de código e commits. Os hooks executam validadores em Java para garantir conformidade com os padrões acima.

### Instalação & Setup

#### Setup Automatizado

Após instalar o Lefthook, execute na raiz do backend:

```bash
lefthook install
```

**Requisitos:**
- `lefthook` instalado e disponível no PATH
- Java 11+ disponível no PATH (`java -version`)

#### Instalando Lefthook

**Opção 1: Via Scoop (Windows - Recomendado)**
```powershell
scoop install lefthook
```

**Opção 2: Via npm/pnpm**
```bash
npm install -g @evilmartians/lefthook
# ou
pnpm add -g @evilmartians/lefthook
```

**Opção 3: Via Chocolatey**
```powershell
choco install lefthook
```

---

### Hooks Configurados

#### Pre-Commit Hook (`pre-commit`)

Executado automaticamente antes de cada commit. Todos os comandos rodam **em paralelo** para agilidade.

1. **Branch Name Validation** (`--strict`)
   - Valida se a branch segue o padrão `<type>/<issue>/<description>`
   - Exit code 1 se inválida

2. **Spotless Format** (`spotless:apply`)
   - Glob: `**/*.java`
   - Formata automaticamente arquivos Java
   - Auto-stages arquivos corrigidos

3. **Unit Tests** (`mvn test`)
   - Glob: `**/*Test.java`
   - Pula durante merge commits
   - Falha bloqueia o commit

#### Commit Message Hook (`commit-msg`)

Valida o formato da mensagem de commit segundo Conventional Commits.

**Formato esperado:** `<type>: <description>`

**Tipos válidos:** feat, fix, refactor, style, test, build, perf, ci, revert, hotfix

**Executado:** Antes do commit ser finalizado

#### Post-Checkout Hook (`post-checkout`)

Valida a branch quando você muda de branch ou cria uma nova.

**Modo:** Warning (não bloqueia, apenas alerta)

**Comando:** `java scripts/ValidateBranchName.java`

#### Pre-Push Hook (`pre-push`)

Executa checks antes de fazer push. Todos os comandos rodam **em paralelo**.

1. **Branch Name Validation** (`--strict`)
   - Valida branch (mode strict = bloqueia se inválida)

2. **Spotless Check** (`spotless:check`)
   - Glob: `**/*.java`
   - Verifica formatação (não corrige)
   - Bloqueia push se houver problemas

3. **Unit Tests** (`mvn test`)
   - Glob: `**/*Test.java`
   - Executa testes
   - Bloqueia push se testes falharem

---

### Executar Hooks Manualmente

```bash
# Instalar hooks
lefthook install

# Remover hooks
lefthook uninstall

# Executar pre-commit checks
lefthook run pre-commit

# Executar post-checkout check
lefthook run post-checkout

# Executar commit-msg validation (com arquivo temporário)
echo "feat: test" > .tmp-msg.txt
lefthook run commit-msg .tmp-msg.txt
rm .tmp-msg.txt

# Executar pre-push checks
lefthook run pre-push
```

---

### Testar Validadores Localmente

#### Validar Commit Message

```bash
# Válido
echo "feat: add user authentication" > .tmp.txt
java scripts/ValidateCommitMsg.java .tmp.txt

# Inválido
echo "invalid message" > .tmp.txt
java scripts/ValidateCommitMsg.java .tmp.txt
```

#### Validar Branch Name

```bash
# Modo warning (post-checkout)
java scripts/ValidateBranchName.java

# Modo strict (pre-commit/pre-push)
java scripts/ValidateBranchName.java --strict

# Com branch específica
git checkout -b feat/1234/test-feature
java scripts/ValidateBranchName.java --strict
```

---

### Comportamento em Caso de Falha

Se alguma validação falhar:

* **Pre-commit**: Commit é **abortado**, você pode corrigir e tentar novamente
* **Commit-msg**: Commit é **abortado**, edite a mensagem e tente novamente
* **Pre-push**: Push é **bloqueado**, corrija os problemas e tente novamente

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
  - Body: `name`, `tax_id`, `plan`, `active`
  - `plan`: `BASIC`, `PRO`, `ENTERPRISE`

- `GET /api/companies`
  - Query params opcionais: `name`, `tax_id`, `plan`, `active`, `page`, `size`, `sort`
  - Limite de paginação: `size` máximo de `100`

- `PUT /api/companies/{id}`
  - Body: `name`, `plan`, `active`
  - `plan` segue enum `CompanyPlan`

- `POST /api/collaborators/managers`
  - Body: `company_id`, `name`, `email`, `active`, `preferences`
