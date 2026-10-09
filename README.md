# Blog API

API REST de blog para gerenciar usuários, posts e comentários, desenvolvida com Java e Spring Boot e persistência em
MongoDB.

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

* Cadastro, consulta, atualização e remoção de usuários
* Cadastro, consulta, atualização e remoção de posts
* Cadastro, consulta, atualização e remoção de comentários
* Posts vinculados ao usuário autor
* Comentários vinculados ao usuário autor e ao post comentado
* Consulta de um post junto com todos os seus comentários
* Listagem dos posts de um usuário
* Verificação de que o usuário e o post existem ao criar ou atualizar um comentário
* Validação dos dados de entrada
* Respostas de erro padronizadas
* Dados sensíveis protegidos: a senha do usuário nunca é retornada pela API
* Logs das principais operações
* Testes unitários dos serviços

## Modelo de domínio

```text
User
 ├── Posts
 └── Comments

Post
 ├── userId
 └── Comments

Comment
 ├── userId
 └── postId
```

Os relacionamentos são guardados por IDs (`userId` e `postId`). Nas respostas, o post traz o `id` e o `name` do autor.

## Estrutura

```text
src/main/java/br/com/gspadaro/blogapi
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

## Endpoints

### Users

| Método | Endpoint             | Descrição           |
|--------|----------------------|---------------------|
| POST   | `/api/v1/users`      | Cria um usuário     |
| GET    | `/api/v1/users/{id}` | Busca um usuário    |
| PUT    | `/api/v1/users/{id}` | Atualiza um usuário |
| DELETE | `/api/v1/users/{id}` | Remove um usuário   |

### Posts

| Método | Endpoint                      | Descrição                                               |
|--------|-------------------------------|---------------------------------------------------------|
| POST   | `/api/v1/posts`               | Cria um post                                            |
| GET    | `/api/v1/posts/{id}`          | Busca um post                                           |
| GET    | `/api/v1/posts/{id}/comments` | Busca um post com seus comentários                      |
| GET    | `/api/v1/posts/users/{id}`    | Lista os posts de um usuário (`{id}` é o ID do usuário) |
| PUT    | `/api/v1/posts/{id}`          | Atualiza um post                                        |
| DELETE | `/api/v1/posts/{id}`          | Remove um post                                          |

### Comments

| Método | Endpoint                | Descrição              |
|--------|-------------------------|------------------------|
| POST   | `/api/v1/comments`      | Cria um comentário     |
| GET    | `/api/v1/comments/{id}` | Busca um comentário    |
| PUT    | `/api/v1/comments/{id}` | Atualiza um comentário |
| DELETE | `/api/v1/comments/{id}` | Remove um comentário   |

## Validação

Os dados de entrada são validados com Bean Validation.

* Usuário: nome, e-mail, telefone e senha obrigatórios; e-mail válido e senha com no mínimo 8 caracteres.
* Post: título, corpo e `userId` obrigatórios.
* Comentário: texto, `userId` e `postId` obrigatórios. O usuário e o post informados precisam existir.

## Tratamento de erros

As exceções são tratadas de forma centralizada com `@RestControllerAdvice`.

Principais respostas:

* `404 Not Found` para recurso inexistente (usuário, post ou comentário)
* `400 Bad Request` para argumentos inválidos
* `500 Internal Server Error` para `NullPointerException`
* Resposta padronizada com timestamp, status, erro, mensagem e caminho da requisição

## Como executar

### Pré-requisitos

* Java 21
* Maven
* MongoDB em execução em `localhost:27017`

### Configuração

O perfil `local` vem ativo por padrão (`application-local.yml`) e conecta em `mongodb://localhost:27017/blog_db`. As
credenciais são lidas de variáveis de ambiente:

```bash
export MONGODB_USERNAME=seu_usuario
export MONGODB_PASSWORD=sua_senha
```

No Windows (PowerShell):

```powershell
$env:MONGODB_USERNAME="seu_usuario"
$env:MONGODB_PASSWORD="sua_senha"
```

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

Os testes unitários cobrem a camada de serviço (`UserService`, `PostService`, `PostQueryService` e `CommentService`) com
JUnit 5 e Mockito, usando os mappers reais. O teste `contextLoads` sobe a aplicação completa e precisa do MongoDB
disponível.

## Próximos passos

* Aumentar a cobertura de testes
* Implementar autenticação e autorização com Spring Security/JWT
* Adicionar documentação OpenAPI/Swagger
* Implementar paginação e ordenação
* Realizar deploy

## Autor

**Guilherme Spadaro**