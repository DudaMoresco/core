# Core - Sistema de Gerenciamento de Transporte de Grãos

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-green)
![SQLite](https://img.shields.io/badge/SQLite-3.45.0-blue)
![Gradle](https://img.shields.io/badge/Gradle-Build-blue)

Sistema desenvolvido para gerenciar o transporte e pesagem de grãos, oferecendo controle completo do processo desde a demanda de transporte até a pesagem final estabilizada.

---

## Funcionalidades

### Principais Módulos

* **Gestão de Filiais**
* **Gestão de Caminhões** (inclui tara)
* **Gestão de Grãos** (inclui preço de compra por tonelada)
* **Gestão de Docas**
* **Sistema de Balanças**
* **Demandas de Transporte**
* **Pesagem Automatizada com Estabilização**

### Funcionalidades Avançadas

* Estabilização automática das leituras de peso
* Processamento assíncrono via mensageria (RabbitMQ)
* APIs RESTful
* Documentação com Swagger/OpenAPI
* Auditoria automática

---

## Tecnologias Utilizadas

| Tecnologia        | Versão  | Uso                    |
| ----------------- | ------- | ---------------------- |
| Java              | 21      | Linguagem principal    |
| Spring Boot       | 3.5.7   | Framework principal    |
| Spring Data JPA   | -       | Persistência           |
| SQLite            | 3.45.0  | Banco de dados         |
| Hibernate         | 6.5.0   | ORM                    |
| SpringDoc OpenAPI | 2.6.0   | Documentação           |
| Lombok            | 1.18.34 | Redução de boilerplate |
| Gradle            | -       | Build                  |

---

## Como Executar

### Pré-requisitos

* Java 21+
* Gradle (ou usar o wrapper incluído)
* Docker

---

## Subindo o RabbitMQ (Obrigatório)

O sistema utiliza **RabbitMQ** para processar medições e eventos de pesagem.
Sem o RabbitMQ funcionando, nenhuma leitura de balança será processada.

### Executando via Docker

```bash
docker run -d --hostname rabbit --name rabbitmq \
  -p 5672:5672 -p 15672:15672 \
  rabbitmq:3-management
```

### Acesso ao painel administrativo

* URL: [http://localhost:15672](http://localhost:15672)
* Usuário: `guest`
* Senha: `guest`

---

## Inicializando a Aplicação

1. Clone o repositório:

```bash
git clone <url-do-repositorio>
cd core
```

2. Execute:

```bash
./gradlew bootRun
```

Ou:

```bash
gradle bootRun
```

3. Endpoints:

* API: [http://localhost:8080](http://localhost:8080)
* Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## Banco de Dados

O banco SQLite é criado automaticamente na primeira execução conforme o arquivo:

[`schema.sql`](src/main/resources/database/schema.sql)

### Modelo de Dados (ERD)

```mermaid
erDiagram
    FILIAL {
        INTEGER id PK
        VARCHAR nome
        VARCHAR cnpj
    }

    CAMINHAO {
        INTEGER id PK
        VARCHAR placa
        DECIMAL tara
    }

    GRAO {
        INTEGER id PK
        VARCHAR nome
        DECIMAL preco_compra_por_tonelada
    }

    DOCA {
        INTEGER id PK
    }

    BALANCA {
        INTEGER id PK
        INTEGER id_doca FK
        VARCHAR status
    }

    DEMANDA_TRANSPORTE {
        INTEGER id PK
        INTEGER id_caminhao FK
        INTEGER id_grao FK
        INTEGER id_filial FK
        VARCHAR status
    }

    PESAGEM {
        INTEGER id PK
        INTEGER id_demanda FK
        INTEGER id_balanca FK
        INTEGER id_caminhao FK
        INTEGER id_grao FK
        INTEGER id_filial FK
        INTEGER id_doca FK
        DECIMAL peso_bruto
        DECIMAL tara
        DECIMAL peso_liquido
        DECIMAL custo
    }

    ESTOQUE_DOCA_GRAO {
        INTEGER id PK
        INTEGER id_doca FK
        INTEGER id_grao FK
        DECIMAL qtd_max
        DECIMAL qtd_atual
    }

    DOCA ||--o{ BALANCA : possui
    FILIAL ||--o{ DEMANDA_TRANSPORTE : origina
    CAMINHAO ||--o{ DEMANDA_TRANSPORTE : recebe
    GRAO ||--o{ DEMANDA_TRANSPORTE : tipo

    CAMINHAO ||--o{ PESAGEM : registrado
    GRAO ||--o{ PESAGEM : tipo
    FILIAL ||--o{ PESAGEM : origina
    BALANCA ||--o{ PESAGEM : realiza
    DOCA ||--o{ PESAGEM : ocorre_em
    DEMANDA_TRANSPORTE ||--o{ PESAGEM : gera

    DOCA ||--o{ ESTOQUE_DOCA_GRAO : possui
    GRAO ||--o{ ESTOQUE_DOCA_GRAO : estoque
```

---

## Sistema de Estabilização e Arquitetura de Medição

Para detalhes completos sobre:

* Funcionamento do algoritmo de estabilização
* Processamento assíncrono (eventos)
* Critérios de estabilização
* Arquitetura do pipeline de leitura → estabilização → pesagem consolidada

Acesse:

[`docs/estabilizacao.md`](docs/Lógica%20de%20estabilização.md)
[`docs/arquitetura-eventos.md`](docs/Arquitetura%20de%20Medição.md)

---

## Documentação da API

Com a aplicação rodando:

* Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
* OpenAPI JSON: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
