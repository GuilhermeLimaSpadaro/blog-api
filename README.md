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

- **User**: usuário do blog, com `id`, `name`, `email`, `phone` e `password`. Pode ser autor de posts e comentários.
- **Post**: publicação com `id`, `date`, `title`, `body` e autor (`User`, referenciado via `@DBRef`).
- **Comment**: comentário com `id`, `text`, `date`, autor (`User`) e o `Post` ao qual pertence, ambos referenciados via `@DBRef`. Possui coleção própria (`comment`), repositório e controller dedicados.

Todas as entidades validam seus campos obrigatórios no construtor, lançando `IllegalArgumentException` quando algum valor essencial é `null`.

## Estrutura do projeto

```
src/main/java/com/gspadaro/blogapi
├── config          # Configuração de carga inicial de dados (perfil "test")
├── controller      # Controladores REST (User, Post, Comment)
├── domain          # Entidades de domínio (documentos MongoDB)
├── dto             # DTOs de request/response (records)
├── exception       # Exceções customizadas e tratamento global de erros
├── mapper          # Conversão entre entidades e DTOs
├── repository      # Repositórios Spring Data MongoDB
└── service         # Regras de negócio
```

As DTOs são `records` — as entidades nunca são expostas diretamente pela API. Os DTOs de resposta de post/comentário usam `UserDetailsDTO` (apenas `id` e `name`) para expor o autor sem vazar dados sensíveis como `email`, `phone` ou `password`.

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

| Método | Endpoint       | Descrição                     |
|--------|----------------|--------------------------------|
| POST   | `/users`       | Cria um novo usuário           |
| GET    | `/users`       | Lista todos os usuários        |
| GET    | `/users/{id}`  | Busca um usuário pelo ID       |
| PUT    | `/users/{id}`  | Atualiza um usuário existente  |
| DELETE | `/users/{id}`  | Remove um usuário              |

### Posts (`/posts`)

| Método | Endpoint              | Descrição                                             |
|--------|------------------------|--------------------------------------------------------|
| POST   | `/posts`               | Cria um novo post                                      |
| GET    | `/posts/{id}`          | Busca um post pelo ID                                  |
| PUT    | `/posts/{id}`          | Atualiza um post existente                              |
| DELETE | `/posts/{id}`          | Remove um post                                          |
| GET    | `/posts/users/{id}`    | Lista os posts publicados por um usuário                |
| GET    | `/posts/comment/{id}`  | Busca um post pelo ID junto com seus comentários        |

### Comentários (`/comments`)

| Método | Endpoint          | Descrição                    |
|--------|--------------------|-------------------------------|
| POST   | `/comments`        | Cria um novo comentário       |
| GET    | `/comments`        | Lista todos os comentários    |
| PUT    | `/comments/{id}`   | Atualiza um comentário existente |
| DELETE | `/comments/{id}`   | Remove um comentário          |

## Roadmap

- [x] CRUD de posts e usuários
- [x] Relacionamento entre posts e autor
- [x] Tratamento global de exceções
- [x] CRUD de comentários (endpoints próprios, com autor e post referenciados)
- [ ] Validação de entrada (Bean Validation)
- [ ] Testes automatizados
- [ ] Autenticação e autorização (Spring Security + JWT)
- [ ] Documentação da API (Swagger/OpenAPI)
- [ ] Paginação e ordenação nos endpoints de listagem
- [ ] Deploy

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

Como o perfil `test` já vem ativo por padrão, a classe `Instantiation` roda automaticamente ao iniciar a aplicação: ela limpa as coleções de usuários, posts e comentários e as repopula com dados de exemplo.

## Testes

```bash
./mvnw test
```

O projeto conta com testes unitários da camada de serviço usando JUnit 5 e Mockito (ex.: `UserServiceTest`).
