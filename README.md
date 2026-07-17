# Blog API

API REST para um sistema de blog, com suporte a usuários, posts e comentários, desenvolvida em Java com Spring Boot e persistência em MongoDB.

## Tecnologias

- **Java 21**
- **Spring Boot**
- **Spring Web**, para expor os endpoints REST
- **Spring Data MongoDB**, para persistência
- **MongoDB**
- **Spring Boot Test**, para a camada de testes
- **Maven**
- **Git**

## Modelo de domínio

- **User**: representa um usuário do blog, com nome e e-mail, podendo ser autor de posts e comentários.
- **Post**: representa uma publicação, contendo título, corpo, data, autor (`User`) e uma lista de comentários.
- **Comment**: representa um comentário associado a um post, contendo texto, data e autor (`User`).

## Estrutura do projeto

```
src/main/java/com/gspadaro/blogapi
├── config          # Configurações e carga inicial de dados (perfil de teste)
├── controller      # Controladores REST
├── domain          # Entidades de domínio (documentos MongoDB)
├── dto             # Objetos de transferência de dados (DTOs)
├── exception       # Exceções customizadas e tratamento global de erros
├── repository      # Repositórios Spring Data MongoDB
└── service         # Regras de negócio
```

## Tratamento de exceções

O projeto centraliza o tratamento de erros através de um `@RestControllerAdvice` (`GlobalHandlerException`), que intercepta exceções de negócio (como `ObjectNotFoundException`) e retorna uma resposta padronizada (`StandardError`), contendo:

- Data e hora do erro
- Status HTTP
- Descrição do erro
- Mensagem detalhada
- Caminho da requisição

## Endpoints disponíveis

### Usuários (`/users`)

| Método | Endpoint            | Descrição                            |
|--------|---------------------|----------------------------------------|
| POST   | `/users`             | Cria um novo usuário                  |
| GET    | `/users`             | Lista todos os usuários               |
| GET    | `/users/{id}`        | Busca um usuário pelo ID              |
| PUT    | `/users/{id}`        | Atualiza um usuário existente         |
| DELETE | `/users/{id}`        | Remove um usuário                     |
| GET    | `/users/{id}/posts`  | Lista os posts publicados por um usuário |

### Posts (`/posts`)

| Método | Endpoint      | Descrição               |
|--------|---------------|--------------------------|
| GET    | `/posts`      | Lista todos os posts    |
| GET    | `/posts/{id}` | Busca um post pelo ID   |
| PUT    | `/posts/{id}` | Atualiza um post existente (título e corpo) |

> Observação: por enquanto não há endpoint de criação (`POST`) nem remoção (`DELETE`) de posts, e comentários ainda não possuem endpoints próprios — os posts existentes são populados via `Instantiation` no perfil `test`.

## Roadmap

Funcionalidades planejadas para as próximas versões:

- [ ] `POST /posts` — criação de novos posts
- [ ] `DELETE /posts/{id}` — remoção de posts
- [ ] Endpoints de comentários (`POST`, `GET`, `DELETE`)

## Executando o projeto

### Pré-requisitos

- Java 21
- Maven
- MongoDB em execução (local ou remoto)

### Configuração

Configure a conexão com o MongoDB no arquivo `application.properties`.

### Rodando a aplicação

```bash
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`.

## Dados de teste

O projeto conta com uma classe `Instantiation`, ativada apenas no perfil `test`, que popula o banco com usuários e posts de exemplo ao iniciar a aplicação. Para utilizá-la, execute a aplicação com o perfil `test` ativo:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=test
```