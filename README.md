# Sales Pilot - Guia de Arquitetura do Backend

Bem-vindo ao backend do Sales Pilot! Este projeto é construído utilizando os princípios da **Clean Architecture** (Arquitetura Limpa) combinados com os padrões de design **SOLID**.

Nosso principal objetivo é manter uma base de código altamente desacoplada, testável e de fácil manutenção, onde a lógica de negócios seja completamente isolada dos detalhes de implementação técnica (como banco de dados, frameworks ou APIs externas).

## Estrutura do Projeto

O projeto utiliza uma estrutura **multi-módulo Maven**, onde cada camada da arquitetura é um módulo independente com seu próprio `pom.xml`:

```
backend/
├── pom.xml               ← POM raiz (agregador)
│
├── domain/               ← Entidades e interfaces de repositório
├── application/          ← Casos de uso e DTOs
├── infrastructure/       ← Implementações JPA, configurações Spring
├── presentation/         ← Controllers REST
└── bootstrap/            ← Ponto de entrada da aplicação
```

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

Bootstrap depende de todos os módulos acima para montar a aplicação.
```

## Responsabilidades dos Módulos

### 1. `domain`
Este é o núcleo absoluto da aplicação. Contém as regras de negócio da empresa e não possui **nenhuma dependência** de frameworks ou camadas externas.
* **Entities (Entidades):** POJOs puros que representam os conceitos centrais de negócio. Sem anotações do Spring, sem JPA (`@Entity`), sem Jackson.
* **Repositories (Interfaces):** Contratos de acesso a dados. O domínio dita *o que* precisa ser buscado, mas não *como*.
* **Services (Serviços de Domínio):** Lógica de negócios pura envolvendo múltiplas entidades.

### 2. `application`
Contém as regras de negócio específicas da aplicação. Orquestra o fluxo de dados sem conter lógica de negócios pura em si.
* **Use Cases (Casos de Uso):** Classes focadas em uma única responsabilidade (ex: `GetSystemStatusUseCase`). Coordenam entidades do Domínio e retornam resultados.
* **DTOs (Data Transfer Objects):** Objetos (Java records) para transferência de dados sem expor as entidades internas do Domínio.
* **Dependências:** Apenas o módulo `domain`.

### 3. `infrastructure`
É aqui que vivem todos os detalhes técnicos.
* **Persistence:** Implementações concretas das interfaces de repositório do `domain` (ex: `SystemStatusRepositoryImpl`).
* **Config:** Configurações do Spring (`@Configuration`), injeção de dependências dos casos de uso.
* **Dependências:** Módulos `domain` e `application`.

### 4. `presentation`
O mecanismo de entrega HTTP da aplicação.
* **Controllers:** Classes `@RestController` que recebem requisições HTTP, invocam os casos de uso e formatam as respostas.
* **Dependências:** Apenas o módulo `application`.

### 5. `bootstrap`
Ponto de entrada e montagem da aplicação.
* Contém a classe `ApiApplication` (`@SpringBootApplication`) e o `application.properties`.
* É o único módulo que referencia todos os outros, responsável por montar o contexto Spring completo.
* É aqui que o `spring-boot-maven-plugin` gera o JAR executável.
* **Dependências:** Todos os outros módulos.

## Como o Fluxo Funciona (Inversão de Dependência)

Para manter o banco de dados na camada externa de Infraestrutura e ainda permitir que a camada interna de Aplicação o utilize, usamos o Princípio da Inversão de Dependência:

1. O **`domain`** define uma interface: `SystemStatusRepository`.
2. O **Use Case (`application`)** depende dessa interface para fazer o seu trabalho.
3. O **`infrastructure`** fornece a implementação concreta `SystemStatusRepositoryImpl` que implementa a interface.
4. O **`bootstrap`**, via configuração Spring no `infrastructure` (`UseCaseConfig`), injeta a implementação correta em tempo de execução.

## Como Executar

### Com Maven
```bash
./mvnw clean package -DskipTests
java -jar bootstrap/target/bootstrap-0.0.1-SNAPSHOT.jar
```

### Com Docker
```bash
docker build -t salespilot-api .
docker run -p 8080:8080 salespilot-api
```

## Endpoints Disponíveis

| Método | Caminho                  | Descrição                        |
|--------|--------------------------|----------------------------------|
| GET    | `/api/v1/system/ping`    | Verifica o status do sistema     |
