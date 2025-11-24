# Core - Sistema de Gerenciamento de Transporte de Grãos

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-green)
![SQLite](https://img.shields.io/badge/SQLite-3.45.0-blue)
![Gradle](https://img.shields.io/badge/Gradle-Build-blue)

Sistema desenvolvido para gerenciar o transporte e pesagem de grãos, oferecendo controle completo do processo desde a demanda de transporte até a pesagem final estabilizada.

## 📋 Funcionalidades

### Principais Módulos
- **Gestão de Filiais**: Cadastro e controle de unidades operacionais
- **Gestão de Caminhões**: Controle da frota com informações de tara
- **Gestão de Grãos**: Cadastro de produtos com preços de compra
- **Gestão de Docas**: Controle das estruturas de carregamento
- **Sistema de Balanças**: Equipamentos de pesagem vinculados às docas
- **Demandas de Transporte**: Solicitações de transporte com status
- **Pesagem Automatizada**: Sistema inteligente de estabilização de peso

### Funcionalidades Avançadas
- **Estabilização Automática**: Processamento em tempo real das medições para determinar peso estabilizado
- **APIs RESTful**: Interface completa para integração com sistemas externos
- **Documentação Swagger**: Interface interativa para teste e documentação das APIs
- **Auditoria**: Controle de criação e atualização com timestamps e usuários
- **Processamento Assíncrono**: Worker para processamento de pesagens em background

## 🛠 Tecnologias Utilizadas

| Tecnologia | Versão | Uso |
|------------|--------|-----|
| **Java** | 21 | Linguagem principal |
| **Spring Boot** | 3.5.7 | Framework principal |
| **Spring Data JPA** | Integrado | Persistência de dados |
| **JOOQ** | 8.2 | Query builder tipo-safe |
| **SQLite** | 3.45.0 | Banco de dados |
| **Hibernate** | 6.5.0 | ORM |
| **SpringDoc OpenAPI** | 2.6.0 | Documentação da API |
| **Lombok** | 1.18.34 | Redução de boilerplate |
| **Gradle** | - | Gerenciamento de dependências |

## 🚀 Como Executar

### Pré-requisitos
- **Java 21** ou superior
- **Gradle** (ou usar o wrapper incluído)

### Executando a Aplicação

1. **Clone o repositório**
```bash
git clone <url-do-repositorio>
cd core
```

2. **Execute a aplicação**
```bash
# Usando Gradle Wrapper (recomendado)
./gradlew bootRun

# Ou se tiver Gradle instalado
gradle bootRun
```

3. **Acesse a aplicação**
- **API**: http://localhost:8080
- **Documentação Swagger**: http://localhost:8080/swagger-ui.html

### Build para Produção
```bash
./gradlew build
java -jar build/libs/core-0.0.1-SNAPSHOT.jar
```

## 📁 Estrutura do Projeto

```
src/main/java/com/serasa/core/
├── CoreApplication.java              # Classe principal
├── balanca/                          # Módulo de balanças
│   ├── BalancaController.java        # API REST para balanças
│   ├── BalancaService.java          # Lógica de negócio
│   ├── BalancaEntity.java           # Entidade JPA
│   ├── BalancaRepository.java       # Repositório de dados
│   └── MedicaoBalanca*.java         # Gestão de medições
├── caminhao/                        # Módulo de caminhões
├── doca/                           # Módulo de docas
├── filial/                         # Módulo de filiais
├── grao/                           # Módulo de grãos
├── demandatransporte/              # Módulo de demandas
└── pesagem/                        # Módulo de pesagem
    ├── PesagemEntity.java          # Pesagens consolidadas
    ├── PesagemRepository.java      # Repositório de pesagens
    └── EstabilizacaoWorker.java    # Worker de estabilização
```

## 🔗 APIs Disponíveis

### Principais Endpoints

| Módulo | Endpoint | Métodos | Descrição |
|--------|----------|---------|-----------|
| **Filiais** | `/api/filiais` | GET, POST | Gestão de filiais |
| **Caminhões** | `/api/caminhoes` | GET, POST | Gestão de caminhões |
| **Grãos** | `/api/graos` | GET, POST | Gestão de grãos |
| **Docas** | `/api/docas` | GET, POST | Gestão de docas |
| **Balanças** | `/api/balancas` | GET, POST | Gestão de balanças |
| **Medições** | `/api/balancas/medicao` | POST | Registro de medições |

### Exemplo de Uso

**Criar uma filial:**
```bash
curl -X POST http://localhost:8080/api/filiais \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Filial Centro",
    "cnpj": "12.345.678/0001-90",
    "createdBy": "admin"
  }'
```

**Registrar medição de balança:**
```bash
curl -X POST http://localhost:8080/api/balancas/medicao \
  -H "Content-Type: application/json" \
  -d '{
    "idBalanca": 1,
    "placa": "ABC1234",
    "peso": 15420.75,
    "createdBy": "sistema"
  }'
```

## 🗄 Banco de Dados

### Modelo de Dados
O sistema utiliza SQLite com as seguintes tabelas principais:

- **filial**: Unidades operacionais
- **caminhao**: Frota de veículos
- **grao**: Produtos transportados
- **doca**: Estruturas de carregamento
- **balanca**: Equipamentos de pesagem
- **demanda_transporte**: Solicitações de transporte
- **medicao_balanca**: Medições brutas das balanças
- **pesagem**: Pesagens consolidadas e estabilizadas

### Inicialização
O banco é criado automaticamente na primeira execução usando o arquivo [`schema.sql`](src/main/resources/database/schema.sql).

## ⚙ Sistema de Estabilização

O sistema possui um algoritmo inteligente de estabilização de peso que:

1. **Coleta medições** em tempo real (100 medições nos últimos 10 segundos)
2. **Calcula desvio padrão** das medições
3. **Verifica estabilização** (desvio < 5kg)
4. **Consolida pesagem** quando estabilizada
5. **Finaliza demanda** automaticamente

### Configurações de Estabilização
- **Limiar de desvio**: 5.0 kg
- **Janela temporal**: 10 segundos
- **Mínimo de medições**: 100

## 📖 Documentação da API

Com a aplicação rodando, acesse:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs
