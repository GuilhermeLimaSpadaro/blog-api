# Blog API

API REST para gerenciamento de usuários, posts e comentários, desenvolvida com Java e Spring Boot e persistência em MongoDB.

O projeto aplica uma arquitetura em camadas e utiliza DTOs para separar o modelo de domínio da representação da API.

## Tecnologias

* Java 21
* Spring Boot 4.1.1
* Spring Web
* Spring Data MongoDB
* MongoDB
* Bean Validation / Jakarta Validation
* MapStruct
* JUnit 5
* Mockito
* Maven

## Funcionalidades

* Criação, consulta, atualização e remoção de usuários
* Criação, consulta, atualização e remoção de posts
* Criação, consulta, atualização e remoção de comentários
* Associação de posts a autores
* Associação de comentários a autores e posts
* Validação dos dados de entrada
* DTOs de request e response
* Mapeamento entre entidades e DTOs
* Tratamento global de exceções
* Testes unitários da camada de serviço

## Modelo de domínio

```text
User
 ├── Posts
 └── Comments

Post
 ├── authorId
 └── Comments

Comment
 ├── authorId
 └── postId
```

Os relacionamentos de autor e post são representados por IDs. Os DTOs de resposta utilizam `UserDetailsDTO` para retornar somente os dados necessários do autor.

## Estrutura

```text
src/main/java/com/gspadaro/blogapi
├── controller
├── dto
│   ├── comment
│   ├── post
│   └── user
├── exception
├── mapper
│   └── custom
├── model
├── repository
└── service
```

* `controller` — endpoints REST
* `service` — regras de negócio
* `repository` — acesso ao MongoDB
* `model` — documentos MongoDB (`User`, `Post`, `Comment`)
* `dto` — objetos de entrada e saída da API
* `mapper` — conversão entre modelo e DTOs
* `exception` — exceções e tratamento global

## Endpoints

### Users

| Método | Endpoint             | Descrição           |
| ------ | -------------------- | ------------------- |
| POST   | `/api/v1/users`      | Cria um usuário     |
| GET    | `/api/v1/users/{id}` | Busca um usuário    |
| PUT    | `/api/v1/users/{id}` | Atualiza um usuário |
| DELETE | `/api/v1/users/{id}` | Remove um usuário   |

### Posts

| Método | Endpoint                      | Descrição                                                  |
| ------ | ----------------------------- | ---------------------------------------------------------- |
| POST   | `/api/v1/posts`               | Cria um post                                               |
| GET    | `/api/v1/posts/{id}`          | Busca um post                                              |
| GET    | `/api/v1/posts/{id}/comments` | Busca um post com seus comentários                         |
| GET    | `/api/v1/posts/{id}/author`   | Lista os posts de um autor (`{id}` é o ID do autor)        |
| PUT    | `/api/v1/posts/{id}`          | Atualiza um post                                           |
| DELETE | `/api/v1/posts/{id}`          | Remove um post                                             |

### Comments

| Método | Endpoint                | Descrição              |
| ------ | ----------------------- | ---------------------- |
| POST   | `/api/v1/comments`      | Cria um comentário     |
| GET    | `/api/v1/comments/{id}` | Busca um comentário    |
| PUT    | `/api/v1/comments/{id}` | Atualiza um comentário |
| DELETE | `/api/v1/comments/{id}` | Remove um comentário   |

## Validação

Os DTOs de entrada utilizam Bean Validation.

* `UserRequestDTO`: nome, e-mail e telefone obrigatórios, e-mail válido e senha com no mínimo 8 caracteres.
* `PostRequestDTO`: título, corpo e `authorId` obrigatórios.
* `CommentRequestDTO`: texto, `authorId` e `postId` obrigatórios.

## Tratamento de erros

O projeto utiliza `@RestControllerAdvice` para centralizar o tratamento de exceções.

Principais respostas:

* `404 Not Found` para recurso inexistente
* `400 Bad Request` para argumentos inválidos
* Resposta padronizada com timestamp, status, erro, mensagem e caminho da requisição

## Como executar

### Pré-requisitos

* Java 21
* Maven
* MongoDB em execução em `localhost:27017`

### Executar

```bash
./mvnw spring-boot:run
```

A aplicação fica disponível em:

```text
http://localhost:8080
```

### Testes

```bash
./mvnw test
```

Os testes existentes cobrem principalmente a camada de serviço com JUnit 5 e Mockito.

## Próximos passos

* Aumentar a cobertura de testes
* Implementar autenticação e autorização com Spring Security/JWT
* Adicionar documentação OpenAPI/Swagger
* Implementar paginação e ordenação
* Realizar deploy

### Correções planejadas

* Corrigir a configuração do MongoDB no `application.yml` (usar `spring.data.mongodb.uri`)
* Corrigir o handler de `NullPointerException` no tratamento global de exceções

## Autor

**Guilherme Spadaro**
