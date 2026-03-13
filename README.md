# Sales Pilot - Guia de Arquitetura do Backend

Bem-vindo ao backend do Sales Pilot! Este projeto é construído utilizando os princípios da **Clean Architecture** (Arquitetura Limpa) combinados com os padrões de design **SOLID**.

Nosso principal objetivo é manter uma base de código altamente desacoplada, testável e de fácil manutenção, onde a lógica de negócios seja completamente isolada dos detalhes de implementação técnica (como banco de dados, frameworks ou APIs externas).

## Fluxo de Dependências

A regra mais crítica dessa arquitetura é a **Regra de Dependência**: as dependências do código-fonte devem apontar *apenas* para dentro, em direção ao Domínio (Domain).

Camadas internas não sabem **nada** sobre as camadas externas. Camadas externas são mecanismos; camadas internas são políticas/regras.

```text
┌───────────────────┐       ┌────────────────────────┐
│   Presentation    │       │     Infrastructure     │
│  (Controllers)    │       │  (JPA, External APIs)  │
└────────┬──────────┘       └──────────┬─────────────┘
         │                             │
         ↓                             ↓
┌───────────────────────────────────────────────┐
│       Application (Use Cases, DTOs)           │
└───────────────────────┬───────────────────────┘
                        │
                        ↓
┌───────────────────────────────────────────────┐
│     Domain (Entities, Repository Interfaces)  │
└───────────────────────────────────────────────┘
```

## Responsabilidades das Camadas

### 1. Domain (`domain`)
Este é o núcleo absoluto da aplicação. Contém as regras de negócio da empresa e não possui **nenhuma dependência** de frameworks ou camadas externas.
* **Entities (Entidades):** POJOs (Plain Old Java Objects) puros que representam nossos conceitos centrais de negócio (ex: `Client`, `Order`). Sem anotações do Spring, sem JPA (`@Entity`, `@Table`), sem Jackson.
* **Services (Serviços de Domínio):** Lógica de negócios pura que envolve múltiplas entidades ou que não se encaixa naturalmente dentro de uma única entidade.
* **Repositories (Interfaces):** Definições de interfaces para acesso a dados. O domínio dita *o que* precisa ser salvo ou buscado, mas não *como*.

### 2. Application (`application`)
Esta camada contém as regras de negócio específicas da aplicação. Ela orquestra o fluxo de dados, mas não contém lógica de negócios pura em si.
* **Use Cases (Casos de Uso):** Classes altamente coesas focadas em uma única responsabilidade ou ação do usuário (ex: `CreateOrderUseCase`). Eles coordenam tarefas: buscam entidades do Domínio, acionam comportamentos do Domínio e salvam os resultados.
* **DTOs (Data Transfer Objects):** Objetos simples usados para passar dados para dentro e para fora da camada de aplicação sem expor nossas entidades internas do Domínio para o mundo externo.
* **Dependências:** Depende *apenas* da camada de `Domain`. **Nenhuma importação do Spring (`@RestController`) ou JPA aqui.**

### 3. Presentation (`presentation`)
Este é o mecanismo de entrega (como o mundo exterior interage com nossa aplicação).
* **Controllers:** Classes `@RestController` do Spring. Eles recebem requisições HTTP, traduzem os payloads JSON em DTOs da Aplicação, os passam para os Casos de Uso (Use Cases) e formatam a resposta HTTP.
* **Dependências:** Aponta para dentro, para a camada de `Application`.

### 4. Infrastructure (`infrastructure`)
É aqui que vivem todos os detalhes técnicos.
* **Persistence (JPA):** Contém as interfaces `@Repository` do Spring Data, classes `@Entity` específicas do JPA (que mapeiam para as tabelas do banco de dados) e as classes concretas que *implementam* as interfaces de repositório do `Domain`.
* **Config:** Configurações específicas de frameworks (Spring Beans, Security, CORS, etc.).
* **Dependências:** Aponta para dentro, para as camadas de `Application` e `Domain`, para implementar seus contratos.

## Como o Fluxo Funciona (Inversão de Dependência)
Para manter o banco de dados na camada externa de Infraestrutura e ainda permitir que a camada interna de Aplicação o utilize, usamos o Princípio da Inversão de Dependência (Dependency Inversion):

1. O **Domain** define uma interface: `UserRepository`.
2. O **Use Case (Application)** depende dessa interface para fazer o seu trabalho.
3. A camada de **Infrastructure** fornece uma classe concreta `JpaUserRepositoryImpl` que implementa `UserRepository` usando o Spring Data.
4. Em tempo de execução (runtime), o Spring injeta a implementação da Infraestrutura na camada de Aplicação.