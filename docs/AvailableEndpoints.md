# Sales Pilot - Endpoints Disponíveis

Este documento lista todos os endpoints disponíveis na API do Sales Pilot, incluindo descrições, parâmetros esperados e exemplos de uso.

A API também conta com uma documentação interativa via **Swagger**, acessível em:
`http://localhost:8080/swagger-ui.html`

A URL base para desenvolvimento local é: `http://localhost:8080`

---

## Autenticação (`/api/auth`)

Endpoints relacionados à autenticação de usuários e gerenciamento de tokens.

| Método | Caminho | Resumo | Descrição |
|--------|---------|--------|-----------|
| POST | `/api/auth/login` | Login | Autentica um usuário e retorna os tokens de acesso e refresh. |
| POST | `/api/auth/refresh` | Atualizar token | Gera um novo token de acesso usando um refresh token válido. |
| GET | `/api/auth/me` | Perfil atual | Retorna os dados do perfil do usuário autenticado. |

---

## Empresas (`/api/companies`)

Gerenciamento de empresas na plataforma. Requer a permissão `SYSTEM_ADMIN`.

| Método | Caminho | Resumo | Descrição |
|--------|---------|--------|-----------|
| POST | `/api/companies` | Cadastrar empresa | Cria uma nova empresa. |
| GET | `/api/companies` | Listar empresas | Retorna uma lista paginada de empresas com filtros opcionais (`name`, `taxId`, `plan`, `active`). |
| GET | `/api/companies/{id}` | Buscar empresa por ID | Retorna os dados detalhados de uma empresa pelo seu UUID. |
| PUT | `/api/companies/{id}` | Atualizar empresa | Atualiza nome, plano e status de ativação de uma empresa. |

---

## Colaboradores (`/api/collaborators`)

Gerenciamento de gestores e vendedores.

| Método | Caminho | Resumo | Descrição | Permissão |
|--------|---------|--------|-----------|-----------|
| POST | `/api/collaborators/managers` | Cadastrar gestor | Cria um novo colaborador com papel de MANAGER. | `SYSTEM_ADMIN` |
| PUT | `/api/collaborators/managers/{id}` | Editar gestor | Atualiza os dados de um MANAGER. | `SYSTEM_ADMIN` |
| GET | `/api/collaborators/managers/{id}` | Buscar gestor por ID | Retorna os dados de um MANAGER pelo UUID. | `SYSTEM_ADMIN`, `MANAGER` |
| GET | `/api/collaborators/managers` | Listar gestores | Retorna uma lista paginada de gestores. | `SYSTEM_ADMIN` |
| POST | `/api/collaborators/sellers` | Cadastrar vendedor | Cria um novo colaborador com papel de SELLER. | `SYSTEM_ADMIN`, `MANAGER` |
| PUT | `/api/collaborators/sellers/{id}` | Editar vendedor | Atualiza os dados de um SELLER. | `SYSTEM_ADMIN`, `MANAGER` |
| GET | `/api/collaborators/sellers/{id}` | Buscar vendedor por ID | Retorna os dados de um SELLER, incluindo suas reuniões. | `SYSTEM_ADMIN`, `MANAGER` |
| GET | `/api/collaborators/sellers` | Buscar vendedores | Retorna uma lista paginada de vendedores com filtros. | `SYSTEM_ADMIN`, `MANAGER` |
| PATCH | `/api/collaborators/{id}/password` | Definir senha | Define ou atualiza a senha de um colaborador. | Próprio/Admin |

---

## Reuniões (`/api/meetings`)

Gerenciamento e análise de reuniões de vendas.

| Método | Caminho | Resumo | Descrição |
|--------|---------|--------|-----------|
| GET | `/api/meetings` | Listar reuniões | Retorna uma lista paginada de reuniões com métricas gerais (total, duração, taxa de sucesso). |
| GET | `/api/meetings/{id}` | Buscar reunião por ID | Retorna o contexto completo e metadados (pré-análise, vendedor, cliente). |
| GET | `/api/meetings/{id}/post-analysis` | Buscar pós-análise | Retorna o resumo da reunião, itens de ação e análise de sentimento. |
| GET | `/api/meetings/{id}/insights` | Buscar insights | Retorna insights gerados automaticamente (pontos chave, itens de ação). |

---

## Dashboard (`/api/dashboard`)

Endpoints para visualização de dados e métricas.

| Método | Caminho | Resumo | Descrição |
|--------|---------|--------|-----------|
| GET | `/api/dashboard/meetings-by-month` | Reuniões por mês | Retorna a contagem de reuniões por mês para um dado período (30d, 90d, custom). |
| GET | `/api/dashboard/meetings-by-company` | Top 5 empresas | Retorna as 5 empresas com maior volume de reuniões. |
| GET | `/api/dashboard/companies-status` | Status das empresas | Retorna a distribuição de empresas ativas e inativas. |
| GET | `/api/dashboard/metrics` | Métricas dos cards | Retorna métricas gerais para os cards do dashboard. |
| GET | `/api/dashboard/avg-duration` | Média de duração | Retorna a média de duração das reuniões por mês. |

---

## Sistema

Endpoints de utilidade geral e verificação de saúde.

| Método | Caminho | Resumo | Descrição |
|--------|---------|--------|-----------|
| GET | `/api/v1/system/ping` | Health check | Retorna o status da API e o horário da verificação. |
