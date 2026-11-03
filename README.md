# Blog API

API REST para gerenciamento de um blog, permitindo o cadastro e manipulação de **usuários**, **posts** e **comentários**.

Desenvolvida com **Java 21** e **Spring Boot 4**, utilizando **MongoDB** como banco de dados NoSQL.

---

## Tecnologias

- **Java 21**
- **Spring Boot 4.1.2**
- **Spring Data MongoDB**
- **MapStruct 1.6.3**
- **Jakarta Validation**
- **JUnit 5 + Mockito**

---

## Funcionalidades

### Usuários
- Cadastro, consulta, atualização e remoção
- Validação de nome, e-mail e telefone
- Senha não é retornada pela API

### Posts
- Cadastro, consulta, atualização e remoção
- Vinculação ao usuário autor via `userId`
- Listagem de posts por usuário

### Comentários
- Cadastro, consulta, atualização e remoção
- Vinculação ao usuário autor e ao post via `userId` + `postId`
- Verificação de existência do usuário e do post antes de criar/atualizar

### Geral
- Consulta de post com todos os comentários em um único endpoint
- Validação de entrada com Bean Validation
- Tratamento centralizado de erros
- Testes unitários da camada de serviço

---

## Arquitetura

```
Client → Controller → Service → Repository → MongoDB
```

### Camadas
- **Controller** — endpoints REST
- **Service** — lógica de negócio
- **Repository** — acesso ao MongoDB via Spring Data
- **Mapper** — conversão entre DTOs e entidades (MapStruct)
- **DTO** — records para entrada e saída de dados
- **Exception** — tratamento centralizado com `@RestControllerAdvice`

### Modelo de domínio

```
User (id, name, email, phone, password)
  ├── Posts (referenciados por userId)
  └── Comments (referenciados por userId)

Post (id, date, title, body, userId)
  ├── userId → referência ao autor
  └── Comments (referenciados por postId)

Comment (id, text, date, userId, postId)
  ├── userId → referência ao autor
  └── postId → referência ao post
```

---

## Endpoints

### Users

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/v1/users` | Criar usuário |
| GET | `/api/v1/users/{id}` | Buscar por ID |
| PUT | `/api/v1/users/{id}` | Atualizar |
| DELETE | `/api/v1/users/{id}` | Remover |

### Posts

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/v1/posts` | Criar post |
| GET | `/api/v1/posts/{id}` | Buscar por ID |
| GET | `/api/v1/posts/users/{id}` | Listar por usuário |
| GET | `/api/v1/posts/{id}/comments` | Post com comentários |
| PUT | `/api/v1/posts/{id}` | Atualizar |
| DELETE | `/api/v1/posts/{id}` | Remover |

### Comments

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/v1/comments` | Criar comentário |
| GET | `/api/v1/comments/{id}` | Buscar por ID |
| PUT | `/api/v1/comments/{id}` | Atualizar |
| DELETE | `/api/v1/comments/{id}` | Remover |

---

## Validação

| Entidade | Regras |
|----------|--------|
| **User** | `name` obrigatório, `email` obrigatório e válido, `phone` obrigatório (máx. 11) |
| **Post** | `title` obrigatório (máx. 200), `body` obrigatório, `userId` obrigatório |
| **Comment** | `text` obrigatório (máx. 200), `userId` obrigatório, `postId` obrigatório |

> **Nota:** O campo `password` não está no `UserRequestDTO`. A autenticação com **Spring Security + JWT** está planejada para uma próxima versão.

---

## Tratamento de Erros

Exceções tratadas via `@RestControllerAdvice`:

| Exceção | Status |
|---------|--------|
| `ResourceNotFoundException` | 404 |
| `IllegalArgumentException` | 400 |
| `NullPointerException` | 500 |

Resposta padronizada:

```json
{
  "timestamp": "2025-01-01T00:00:00Z",
  "status": 404,
  "error": "Resource not found",
  "message": "User not found",
  "path": "/api/v1/users/abc123"
}
```

---

## Como Executar

### Pré-requisitos
- **Java 21**
- **Maven 3.8+**
- **MongoDB** em `localhost:27017`

### Configuração

Configure as credenciais do MongoDB via variáveis de ambiente:

```bash
export MONGODB_USERNAME=seu_usuario
export MONGODB_PASSWORD=sua_senha
```

### Executar

```bash
./mvnw spring-boot:run
```

A API fica disponível em `http://localhost:8080`.

### Testes

```bash
./mvnw test
```

### Build

```bash
./mvnw clean package
```

---

## Próximos Passos

- [ ] Autenticação e autorização com **Spring Security + JWT**
- [ ] Campo `password` com criptografia
- [ ] Testes de integração com **Testcontainers**
- [ ] Documentação de API com **SpringDoc + OpenAPI 3**
- [ ] Pipeline de CI/CD

---

## Autor

**Guilherme Spadaro**