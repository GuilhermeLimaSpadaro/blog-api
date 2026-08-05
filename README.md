# Blog API

API REST para um sistema de blog, com suporte a usuários, posts e comentários, desenvolvida em Java com Spring Boot e persistência em MongoDB.

## Tecnologias

- **Java 21**
- **Spring Boot 4.0.7**
- **Spring Web**, para expor os endpoints REST
- **Spring Data MongoDB**, para persistência
- **MongoDB**
- **Spring Boot Test** + **Mockito**, para a camada de testes
- **Maven**

## Modelo de domínio

- **User**: usuário do blog, com `id`, `name` e `email`. Pode ser autor de posts e comentários. Mantém a lista de `posts` como referência (`@DBRef`).
- **Post**: publicação com `id`, `date`, `title`, `body`, autor (`User`, referenciado via `@DBRef`) e uma lista de `comments`.
- **Comment**: comentário com `text`, `date` e autor (`User`). É armazenado **embutido** dentro do `Post` (não é uma coleção própria no MongoDB nem possui repositório ou controller dedicados).

Todas as entidades validam seus campos obrigatórios no construtor, lançando `IllegalArgumentException` quando algum valor essencial é `null`.

## Estrutura do projeto

```
src/main/java/com/gspadaro/blogapi
├── config          # Configuração de carga inicial de dados (perfil "test")
├── controller      # Controladores REST (User, Post)
├── domain          # Entidades de domínio (documentos MongoDB)
├── dto             # DTOs de request/response (records)
├── exception       # Exceções customizadas e tratamento global de erros
├── repository      # Repositórios Spring Data MongoDB
└── service         # Regras de negócio
```

As DTOs são `records` com métodos estáticos `from(...)` para converter entidades de domínio em DTOs de resposta — as entidades nunca são expostas diretamente pela API.

## Tratamento de exceções

O projeto centraliza o tratamento de erros com um `@RestControllerAdvice` (`GlobalHandlerException`), que intercepta:

- `ResourceNotFoundException` → `404 Not Found`
- `IllegalArgumentException` → `400 Bad Request`

Em ambos os casos, a resposta segue o formato padronizado `StandardError`, contendo:

- Data e hora do erro
- Status HTTP
- Descrição do erro
- Mensagem detalhada
- Caminho da requisição

## Endpoints disponíveis

### Usuários (`/users`)

| Método | Endpoint             | Descrição                                |
|--------|-----------------------|-------------------------------------------|
| POST   | `/users`               | Cria um novo usuário                      |
| GET    | `/users`               | Lista todos os usuários                   |
| GET    | `/users/{id}`          | Busca um usuário pelo ID                  |
| PUT    | `/users/{id}`          | Atualiza um usuário existente             |
| DELETE | `/users/{id}`          | Remove um usuário                         |
| GET    | `/users/{id}/posts`    | Lista os posts publicados por um usuário  |

### Posts (`/posts`)

| Método | Endpoint                | Descrição                                     |
|--------|---------------------------|-------------------------------------------------|
| POST   | `/posts`                   | Cria um novo post                              |
| GET    | `/posts`                   | Lista todos os posts                           |
| GET    | `/posts/{id}`               | Busca um post pelo ID                          |
| PUT    | `/posts/{id}`               | Atualiza um post existente (data, título e corpo) |
| DELETE | `/posts/{id}`               | Remove um post                                 |
| GET    | `/posts/title/{title}`     | Busca posts cujo título contém o termo informado (case-insensitive) |

> **Observação:** comentários ainda não possuem endpoints próprios. Eles só existem hoje via dados de exemplo carregados pela classe `Instantiation` no perfil `test`.

## Roadmap

Funcionalidades planejadas para as próximas versões:

- [ ] Endpoints de comentários (`POST`, `GET`, `DELETE`)
- [ ] Bean Validation nos DTOs de request
- [ ] Autenticação

## Executando o projeto

### Pré-requisitos

- Java 21
- Maven (ou o wrapper `./mvnw` incluso no projeto)
- MongoDB em execução (local ou remoto)

### Configuração

A aplicação já sobe com o perfil `test` ativo por padrão (definido em `application.properties`), que aponta para `mongodb://localhost:27017/blog_db` (arquivo `application-test.properties`). Ajuste essa URI se o seu MongoDB estiver em outro endereço.

### Rodando a aplicação

```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`.

### Dados de teste

Como o perfil `test` já vem ativo por padrão, a classe `Instantiation` roda automaticamente ao iniciar a aplicação: ela limpa as coleções de usuários e posts e as repopula com usuários, posts e comentários de exemplo.

## Testes

```bash
./mvnw test
```

O projeto conta com testes unitários da camada de serviço usando JUnit 5 e Mockito (ex.: `UserServiceTest`).