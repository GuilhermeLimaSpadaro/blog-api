# Blog API

API REST para um sistema de blog — usuários, posts e comentários — feita em Java com Spring Boot e persistência em MongoDB.

Comecei esse projeto para sair da teoria e treinar decisões reais de arquitetura backend: modelagem de relacionamentos num banco não relacional, validação de entrada, organização em camadas sem exagerar na complexidade. Ele ainda está em construção, então o código muda com alguma frequência conforme vou revisando decisões antigas.

## Tecnologias

- **Java 21**
- **Spring Boot 4.0.7**
- **Spring Web**, para expor os endpoints REST
- **Spring Data MongoDB**, para persistência
- **Bean Validation** (Jakarta Validation), para validar os DTOs de entrada
- **MongoDB**
- **Spring Boot Test** + **Mockito**, para a camada de testes
- **Maven**

## Modelo de domínio

- **User**: usuário do blog, com `id`, `name`, `email`, `phone` e `password`. Pode ser autor de posts e comentários.
- **Post**: publicação com `id`, `date`, `title`, `body` e `authorId`.
- **Comment**: comentário com `id`, `text`, `date`, `authorId` e `postId`. Possui coleção própria (`comment`), repositório e controller dedicados.

Uma mudança recente: Post e Comment guardavam o autor com `@DBRef`, trazendo o objeto `User` inteiro embutido. Troquei isso por guardar só o `authorId` (e o `postId`, no caso do Comment) como `String`, e resolver esses relacionamentos na camada de serviço — `PostService` e `CommentService` agora pedem os dados do autor para o `UserService` quando precisam. Ficou mais barato nas consultas e evita o risco de vazar dado sensível do usuário (como `password`) sem querer.

Todas as entidades continuam validando seus campos obrigatórios no construtor, lançando `IllegalArgumentException` quando algum valor essencial é `null`.

## Estrutura do projeto

```
src/main/java/com/gspadaro/blogapi
├── config          # Configuração de carga inicial de dados (perfil "dev")
├── controller      # Controladores REST (User, Post, Comment)
├── domain          # Entidades de domínio (documentos MongoDB)
├── dto             # DTOs de request/response (records)
├── exception       # Exceções customizadas e tratamento global de erros
├── mapper          # Conversão entre entidades e DTOs
├── repository      # Repositórios Spring Data MongoDB
└── service         # Regras de negócio
```

As DTOs são `records` — as entidades nunca são expostas diretamente pela API. Os DTOs de resposta de post/comentário usam `UserDetailsDTO` (apenas `id` e `name`) para expor o autor sem vazar dados sensíveis como `email`, `phone` ou `password`.

## Validação de entrada

Os DTOs de request agora têm Bean Validation:

- `UserRequestDTO`: `@NotBlank` em `name` e `phone`, `@Email` no `email`, `@Size(min = 8)` na `password`
- `PostRequestDTO` e `CommentRequestDTO`: `@NotBlank` nos campos obrigatórios

Os controllers acionam essa validação com `@Valid` nos endpoints de criação e atualização.

## Tratamento de exceções

O projeto centraliza o tratamento de erros com um `@RestControllerAdvice` (`GlobalHandlerException`), que intercepta:

- `ResourceNotFoundException` → `404 Not Found`
- `IllegalArgumentException` → `400 Bad Request`
- `NullPointerException` → `404 Not Found`

Em todos os casos, a resposta segue o formato padronizado `StandardError`, contendo:

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
- [x] Validação de entrada (Bean Validation)
- [ ] Mais cobertura de testes automatizados
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

A aplicação sobe com o perfil `dev` ativo por padrão (definido em `application.properties`), que aponta para `mongodb://localhost:27017/blog_db` (arquivo `application-dev.properties`). Ajuste essa URI se o seu MongoDB estiver em outro endereço.

### Rodando a aplicação

```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`.

### Dados de teste

Com o perfil `dev` ativo por padrão, a classe `Instantiation` roda automaticamente ao iniciar a aplicação: ela limpa as coleções de usuários, posts e comentários e as repopula com dados de exemplo.

## Testes

```bash
./mvnw test
```

O projeto conta com testes unitários da camada de serviço usando JUnit 5 e Mockito (ex.: `UserServiceTest`, `PostServiceTest`, `CommentServiceTest`).