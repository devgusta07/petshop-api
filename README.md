# Petshop API

API REST baseada em um cenario de uma clínica veterinária, desenvolvida com Java e Spring Boot.

O sistema permite cadastrar tutores e animais e gerenciar agendamentos de serviços veterinários.

## Tecnologias Utilizadas

- Java
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Docker
- Lombok
- Bean Validation
- Swagger / OpenAPI

## Funcionalidades

- Cadastro, consulta, atualização e remoção de tutores , Animais e Agendamentos
- Associação entre tutores e animais
- Validação dos dados recebidos pela API
- Verificação de conflitos de horarios de agendamentos
- Exceções personalizadas
- Tratamento global de exceções
- Documentação dos endpoints com Swagger

## Arquitetura

O projeto utiliza uma arquitetura em camadas, com separação de responsabilidades entre os pacotes:

```text
controller
service
repository
model
dto
exceptions
infra
```

- `controller`: recebe as requisições HTTP e define os endpoints da API.
- `service`: concentra as regras de negócio.
- `repository`: responsável pelo acesso aos dados através do Spring Data JPA.
- `model`: contém as entidades utilizadas na persistência.
- `dto`: objetos utilizados para entrada e saída de dados da API.
- `exceptions`: contém as exceções personalizadas e o tratamento de erros.
- `infra`: contém configurações e componentes relacionados à infraestrutura da aplicação.

## Relacionamentos

```text
Tutor 1 ─── N Animal
Animal 1 ─── N Agendamento
```

Um tutor pode possuir vários animais, e um animal pode possuir vários agendamentos.

## Como executar

### Pré-requisitos

* Java 17+
* IntelliJ IDEA (ou outra de sua preferência)
* Docker Desktop
* Git

O PostgreSQL é executado através do Docker Compose. Não é necessário instalar o PostgreSQL diretamente na máquina.

### 1. Clonar o projeto

```bash
git clone https://github.com/devgusta07/petshop-api.git
```

Abra a pasta clonada no IntelliJ IDEA.

### 2. Iniciar o banco de dados

Certifique-se de que o Docker Desktop esteja em execução.

Abra o terminal integrado do IntelliJ IDEA usando `Alt + F12` e execute:

```bash
docker compose up -d
```

O comando irá criar e iniciar o container do PostgreSQL configurado no projeto.

### 3. Executar a aplicação

A aplicação pode ser executada diretamente pelo IntelliJ IDEA através da classe `PetshopApiApplication`.

### 4. Acessar o Swagger

Com a aplicação em execução:

```text
http://localhost:8080/swagger-ui/index.html
```

O Swagger permite visualizar e testar os endpoints disponíveis na API.

## Estrutura do projeto

```text
src/main/java/com/petshop/petshop_api
├── controller
├── service
├── repository
├── model
├── dto
├── exceptions
└── infra
```
