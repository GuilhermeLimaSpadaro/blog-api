<div align="center">

# 📝 Blog API

[![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Build](https://img.shields.io/badge/Build-Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![MongoDB](https://img.shields.io/badge/MongoDB-7.0-47A248?style=for-the-badge&logo=mongodb&logoColor=white)](https://www.mongodb.com/)

API REST para gerenciamento de um blog, permitindo o cadastro e manipulação de **usuários**, **posts** e **comentários**.

[Funcionalidades](#-funcionalidades) •
[Arquitetura](#-arquitetura) •
[Endpoints](#-endpoints) •
[Como Executar](#-como-executar) •
[Padrões Adotados](#-padr%C3%B5es-adotados)

</div>

---

## 📖 Sobre o Projeto

**Blog API** é uma API RESTful construída com **Java 21** e **Spring Boot 4.1.2** que gerencia o ciclo completo de um blog, desde o cadastro de usuários até a criação de posts e comentários.

O projeto segue uma arquitetura em camadas, separando claramente as responsabilidades entre controllers, services, repositories e mappers, com uso de DTOs para isolamento da camada de persistência e validação robusta dos dados de entrada. Utiliza **MongoDB** como banco de dados NoSQL, aproveitando a flexibilidade de schemas e o modelo de documentos orientado.

---

## 🛠 Tecnologias

| Categoria | Tecnologia | Versão |
|---|---|---|
| **Linguagem** | Java | 21 |
| **Framework** | Spring Boot | 4.1.2 |
| **Web** | Spring Web MVC | — |
| **Banco de Dados** | MongoDB | 7.0 |
| **Data Access** | Spring Data MongoDB | — |
| **Mapper** | MapStruct | 1.6.3 |
| **Validação** | Jakarta Validation | — |
| **Testes** | JUnit 5 + Mockito | — |
| **Build** | Maven | — |
| **Servidor** | Apache Tomcat | 11.0.27 |

---

## ✨ Funcionalidades

- ✅ **Usuários** — Cadastro, consulta, atualização e remoção
- ✅ **Posts** — CRUD completo com título, corpo, data e vinculação ao autor
- ✅ **Comentários** — CRUD completo com vinculação ao post e ao autor
- ✅ **Posts por Usuário** — Listagem de todos os posts de um usuário específico
- ✅ **Post com Comentários** — Consulta de um post com todos os seus comentários em um único endpoint
- ✅ **Validação** — Validação de dados com Jakarta Validation em todos os endpoints
- ✅ **Tratamento de Erros** — Handler global com respostas padronizadas em JSON
- ✅ **Testes Unitários** — Cobertura dos services com JUnit 5 e Mockito
- ✅ **Versionamento** — API versionada com prefixo `/api/v1`

---

## 🏗 Arquitetura

### Diagrama de Camadas

```
┌─────────────────────────────────────────────────────────────┐
│                     Client (HTTP)                           │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                  Controller Layer                           │
│  @RestController • @Valid • ResponseEntity                 │
│  UserController • PostController • CommentController       │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                    Service Layer                            │
│  @Service • Lógica de negócio                              │
│  UserService • PostService • PostQueryService              │
│  CommentService                                            │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                     Mapper Layer                            │
│  MapStruct • DTO ↔ Entity conversion                      │
│  UserMapper • PostMapper • CommentMapper                  │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                  Repository Layer                           │
│  Spring Data MongoDB                                       │
│  UserRepository • PostRepository • CommentRepository       │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                    Database (MongoDB)                       │
└─────────────────────────────────────────────────────────────┘
```

### Modelo de Domínio

```
┌──────────┐         ┌──────────┐         ┌──────────┐
│  User    │────1:N──│  Post    │────1:N──│ Comment  │
│          │         │          │         │          │
│  _id     │         │  _id     │         │  _id     │
│  name    │         │  date    │         │  text    │
│  email   │         │  title   │         │  date    │
│  phone   │         │  body    │         │  userId  │
│  password│         │  userId  │         │  postId  │
└──────────┘         └──────────┘         └──────────┘

Relacionamentos:
  User → Post (userId)
  Post → Comment (postId)
  User → Comment (userId)
```

### Relacionamentos

| Relação | Tipo | Descrição |
|---|---|---|
| `User` → `Post` | OneToMany | Um usuário pode ter vários posts (referenciado por `userId`) |
| `User` → `Comment` | OneToMany | Um usuário pode ter vários comentários (referenciado por `userId`) |
| `Post` → `Comment` | OneToMany | Um post pode ter vários comentários (referenciado por `postId`) |

---

## 📁 Estrutura do Projeto

```
src/main/java/br/com/gspadaro/blogapi/
├── controller/           # Endpoints REST
│   ├── UserController.java
│   ├── PostController.java
│   └── CommentController.java
├── dto/                  # Objetos de transferência (records)
│   ├── comment/
│   │   ├── CommentRequestDTO.java
│   │   └── CommentResponseDTO.java
│   ├── post/
│   │   ├── PostRequestDTO.java
│   │   └── PostResponseDTO.java
│   └── user/
│       ├── UserRequestDTO.java
│       └── UserResponseDTO.java
├── exception/            # Tratamento global de exceções
│   ├── GlobalHandlerException.java
│   ├── ResourceNotFoundException.java
│   └── StandardError.java
├── mapper/               # Conversão entre entidades e DTOs
│   ├── ObjectMapper.java          # Interface base genérica
│   └── custom/
│       ├── UserMapper.java
│       ├── PostMapper.java
│       └── CommentMapper.java
├── model/                # Entidades MongoDB
│   ├── User.java
│   ├── Post.java
│   └── Comment.java
├── repository/           # Persistência com Spring Data MongoDB
│   ├── UserRepository.java
│   ├── PostRepository.java
│   └── CommentRepository.java
├── service/              # Regras de negócio
│   ├── UserService.java
│   ├── PostService.java
│   ├── PostQueryService.java
│   └── CommentService.java
└── BlogApiApplication.java
```

---

## 🔌 Endpoints

> **Base URL:** `http://localhost:8080/api/v1`

### Users

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/v1/users` | Cria um usuário |
| `GET` | `/api/v1/users/{id}` | Busca um usuário por ID |
| `PUT` | `/api/v1/users/{id}` | Atualiza um usuário |
| `DELETE` | `/api/v1/users/{id}` | Remove um usuário |

### Posts

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/v1/posts` | Cria um post |
| `GET` | `/api/v1/posts/{id}` | Busca um post por ID |
| `GET` | `/api/v1/posts/users/{id}` | Lista posts de um usuário |
| `GET` | `/api/v1/posts/{id}/comments` | Busca post com comentários |
| `PUT` | `/api/v1/posts/{id}` | Atualiza um post |
| `DELETE` | `/api/v1/posts/{id}` | Remove um post |

### Comments

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/v1/comments` | Cria um comentário |
| `GET` | `/api/v1/comments/{id}` | Busca um comentário por ID |
| `PUT` | `/api/v1/comments/{id}` | Atualiza um comentário |
| `DELETE` | `/api/v1/comments/{id}` | Remove um comentário |

---

## 📝 Exemplos de Request

### Criar Usuário

```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Guilherme Spadaro",
    "email": "guilherme@email.com",
    "phone": "11999999999"
  }'
```

### Criar Post

```bash
curl -X POST http://localhost:8080/api/v1/posts \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Primeiro Post",
    "body": "Conteúdo do meu primeiro post no blog.",
    "userId": "64f1a2b3c4d5e6f7g8h9i0j1"
  }'
```

### Criar Comentário

```bash
curl -X POST http://localhost:8080/api/v1/comments \
  -H "Content-Type: application/json" \
  -d '{
    "text": "Ótimo post!",
    "userId": "64f1a2b3c4d5e6f7g8h9i0j1",
    "postId": "64f1a2b3c4d5e6f7g8h9i0j2"
  }'
```

---

## ✅ Validações

Os DTOs de request possuem validações com **Jakarta Validation**:

| DTO | Validações |
|---|---|
| `UserRequestDTO` | `name` (@NotBlank), `email` (@Email, @NotBlank), `phone` (@NotBlank, @Size max=11) |
| `PostRequestDTO` | `title` (@NotBlank, @Size max=200), `body` (@NotBlank), `userId` (@NotBlank) |
| `CommentRequestDTO` | `text` (@NotBlank, @Size max=200), `userId` (@NotBlank), `postId` (@NotBlank) |

---

## 🚨 Tratamento de Exceções

A API possui tratamento global de exceções com respostas padronizadas:

| Exceção | Status HTTP | Descrição |
|---|:---:|---|
| `ResourceNotFoundException` | 404 | Recurso não encontrado |
| `IllegalArgumentException` | 400 | Argumento inválido |
| `NullPointerException` | 500 | Erro interno do servidor |

### Exemplo de Resposta de Erro

```json
{
  "timestamp": "2026-01-01T00:00:00Z",
  "status": 404,
  "error": "Resource not found",
  "message": "User not found",
  "path": "/api/v1/users/abc123"
}
```

---

## ⚙️ Configuração

O projeto utiliza **application.yml** para configuração:

```yaml
spring:
  application:
    name: blog-api
  data:
    mongodb:
      uri: mongodb://${MONGODB_USERNAME}:${MONGODB_PASSWORD}@localhost:27017/blog_db
```

### Variáveis de Ambiente

| Variável | Descrição |
|---|---|
| `MONGODB_USERNAME` | Usuário do MongoDB |
| `MONGODB_PASSWORD` | Senha do MongoDB |

---

## 🚀 Como Executar

### Pré-requisitos

- **Java 21**
- **Maven**
- **MongoDB** rodando em `localhost:27017`

### Rodar a Aplicação

```bash
./mvnw spring-boot:run
```

A API fica disponível em:

```
http://localhost:8080
```

### Testes

```bash
./mvnw test
```

### Build

```bash
./mvnw clean package
```

---

## 📐 Padrões Adotados

Este projeto segue práticas e convenções amplamente adotadas no mercado:

### Arquitetura

- **Camadas separadas** — Controller → Service → Mapper → Repository
- **Injeção via construtor** — Imutabilidade e testabilidade
- **DTOs com records** — Objetos imutáveis e concisos para transferência de dados
- **MapStruct** — Mapeamento tipo-safe gerado em tempo de compilação
- **Spring Data MongoDB** — Acesso ao banco com repositórios tipo-safe

### Nomenclatura

- **Coleções MongoDB** — Snake_case (`users`, `posts`, `comments`)
- **Campos** — camelCase (`userId`, `postId`, `createdAt`)
- **Classes Java** — PascalCase (`PostService`, `UserRequestDTO`)
- **Variáveis** — camelCase (`postRepository`, `createdPost`)

### REST

- **Versionamento** — Prefixo `/api/v1` nos endpoints
- **HTTP verbs** — POST (criar), GET (ler), PUT (atualizar), DELETE (remover)
- **Status codes** — 200 (OK), 201 (Created), 204 (No Content), 400 (Bad Request), 404 (Not Found)
- **URI de criação** — `Location` header com URI do recurso criado

### Validação e Erros

- **Jakarta Validation** — Validação declarativa nos DTOs
- **Handler global** — Tratamento centralizado de exceções
- **Resposta padronizada** — Estrutura JSON consistente para erros

### Banco de Dados

- **IDs** — ObjectId nativo do MongoDB (`@Id`)
- **Relacionamentos** — Referências por ID (`userId`, `postId`) sem embed
- **Timestamp** — Campo `date` como `Instant` para posts e comentários

---

## 📅 Roadmap

- [ ] **Spring Security** — Autenticação e autorização com JWT
- [ ] **Criptografia de senhas** — BCrypt para armazenamento seguro
- [ ] **Testes de integração** — Endpoints com Testcontainers
- [ ] **OpenAPI/Swagger** — Documentação interativa da API
- [ ] **Docker** — Containerização da aplicação e do MongoDB
- [ ] **CI/CD** — Pipeline de integração contínua

---

## 👤 Autor

**Guilherme Spadaro**

---

<div align="center">

Feito com ❤️ usando Java 21 e Spring Boot 4.1.2

</div>