# 📝 Blog API

API REST para gerenciamento de um blog, permitindo o cadastro e manipulação de **usuários**, **posts** e **comentários**. Desenvolvida com **Java 21** e **Spring Boot 4**, utilizando **MongoDB** como banco de dados NoSQL.

O projeto segue arquitetura em camadas (Controller → Service → Repository), padrão de mapeamento com **MapStruct**, tratamento centralizado de exceções e validação de dados com **Bean Validation**.

---

## 🛠️ Tecnologias

| Tecnologia | Versão |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.1 |
| Spring Web MVC | — |
| Spring Data MongoDB | — |
| MongoDB | 5.13.0 |
| MapStruct | 1.6.3 |
| Jakarta Validation | — |
| JUnit 5 + Mockito | — |
| Maven | — |
| SLF4J (Logging) | — |

---

## 📋 Funcionalidades

### Usuários
- Cadastro, consulta, atualização e remoção de usuários
- Validação de nome, e-mail e telefone
- Proteção de dados sensíveis: a senha **não** é retornada pela API

### Posts
- Cadastro, consulta, atualização e remoção de posts
- Vinculação de posts ao usuário autor (`userId`)
- Listagem de posts por usuário
- Validação de título (máx. 200 caracteres), corpo e autor

### Comentários
- Cadastro, consulta, atualização e remoção de comentários
- Vinculação de comentários ao usuário autor e ao post (`userId` + `postId`)
- Verificação de existência do usuário e do post antes de criar/atualizar
- Validação de texto (máx. 200 caracteres)

### Geral
- Consulta de post com todos os seus comentários em um único endpoint
- Validação dos dados de entrada com Bean Validation
- Tratamento centralizado de erros com respostas padronizadas
- Logs estruturados das principais operações com SLF4J
- Testes unitários da camada de serviço

---

## 🏗️ Arquitetura

O projeto segue a arquitetura em camadas clássica do Spring Boot:

```
Client → Controller → Service → Repository → MongoDB
                ↓
            Mapper (MapStruct)
                ↓
              DTOs (Records)
```

### Camadas
- **Controller**: expõe os endpoints REST e delega para os serviços
- **Service**: contém a lógica de negócio e orquestra as operações
- **Repository**: interface com o MongoDB via Spring Data
- **Mapper**: converte entre DTOs e entidades usando MapStruct
- **DTO**: registros (Records) para entrada e saída de dados
- **Exception**: tratamento centralizado com `@RestControllerAdvice`

### Modelo de domínio

```text
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

Os relacionamentos são mantidos por **IDs de referência** (`userId` e `postId`). Nas respostas dos posts, o autor é retornado com `id` e `name`.

### Estrutura do código

```text
src/main/java/br/com/gspadaro/blogapi
├── BlogApiApplication.java          # Classe principal
├── controller/                      # Controllers REST
│   ├── UserController.java
│   ├── PostController.java
│   └── CommentController.java
├── dto/                             # Data Transfer Objects (Records)
│   ├── comment/
│   │   ├── CommentRequestDTO.java
│   │   └── CommentResponseDTO.java
│   ├── post/
│   │   ├── PostRequestDTO.java
│   │   ├── PostResponseDTO.java
│   │   └── PostWithCommentsDTO.java
│   └── user/
│       ├── UserRequestDTO.java
│       ├── UserResponseDTO.java
│       └── UserDetailsDTO.java
├── exception/                       # Tratamento de exceções
│   ├── GlobalHandlerException.java
│   ├── ResourceNotFoundException.java
│   └── StandardError.java
├── mapper/                          # Mapeamento com MapStruct
│   ├── ObjectMapper.java            # Interface base genérica
│   └── custom/
│       ├── UserMapper.java
│       ├── PostMapper.java
│       └── CommentMapper.java
├── model/                           # Entidades MongoDB
│   ├── User.java
│   ├── Post.java
│   └── Comment.java
├── repository/                      # Repositórios Spring Data
│   ├── UserRepository.java
│   ├── PostRepository.java
│   └── CommentRepository.java
└── service/                         # Lógica de negócio
    ├── UserService.java
    ├── PostService.java
    ├── PostQueryService.java
    └── CommentService.java
```

---

## 🔌 Endpoints

### Users

| Método | Endpoint             | Descrição              | Status |
|--------|----------------------|------------------------|--------|
| POST   | `/api/v1/users`      | Criar usuário          | 201    |
| GET    | `/api/v1/users/{id}` | Buscar usuário por ID  | 200    |
| PUT    | `/api/v1/users/{id}` | Atualizar usuário      | 200    |
| DELETE | `/api/v1/users/{id}` | Remover usuário        | 204    |

### Posts

| Método | Endpoint                      | Descrição                                      | Status |
|--------|-------------------------------|------------------------------------------------|--------|
| POST   | `/api/v1/posts`               | Criar post                                     | 201    |
| GET    | `/api/v1/posts/{id}`          | Buscar post por ID                             | 200    |
| GET    | `/api/v1/posts/{id}/comments` | Buscar post com seus comentários               | 200    |
| GET    | `/api/v1/posts/users/{id}`    | Listar posts por usuário                       | 200    |
| PUT    | `/api/v1/posts/{id}`          | Atualizar post                                 | 200    |
| DELETE | `/api/v1/posts/{id}`          | Remover post                                   | 204    |

### Comments

| Método | Endpoint                | Descrição                | Status |
|--------|-------------------------|--------------------------|--------|
| POST   | `/api/v1/comments`      | Criar comentário         | 201    |
| GET    | `/api/v1/comments/{id}` | Buscar comentário por ID | 200    |
| PUT    | `/api/v1/comments/{id}` | Atualizar comentário     | 200    |
| DELETE | `/api/v1/comments/{id}` | Remover comentário       | 204    |

---

## ✅ Validação

Os dados de entrada são validados com **Bean Validation** (Jakarta Validation):

| Entidade | Regras |
|----------|--------|
| **User** | `name` obrigatório, `email` obrigatório e válido, `phone` obrigatório (máx. 11 caracteres) |
| **Post** | `title` obrigatório (máx. 200 caracteres), `body` obrigatório, `userId` obrigatório |
| **Comment** | `text` obrigatório (máx. 200 caracteres), `userId` obrigatório, `postId` obrigatório |

> **⚠️ Nota:** O campo `password` foi temporariamente removido do `UserRequestDTO`. Ele será reimplementado quando a autenticação e autorização com **Spring Security + JWT** forem adicionadas ao projeto.

Ao criar ou atualizar um comentário, o sistema verifica se o usuário e o post referenciados existem.

---

## 🚨 Tratamento de Erros

Exceções tratadas centralmente via `@RestControllerAdvice` (`GlobalHandlerException`):

| Exceção | Status | Descrição |
|---------|--------|-----------|
| `ResourceNotFoundException` | 404 | Recurso não encontrado (usuário, post ou comentário) |
| `IllegalArgumentException` | 400 | Argumento inválido |
| `NullPointerException` | 500 | Erro interno do servidor |

Todas as respostas de erro seguem o formato padronizado:

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

## 🚀 Como Executar

### Pré-requisitos

- **Java 21**
- **Maven 3.8+**
- **MongoDB** rodando em `localhost:27017`

### Configuração

O perfil ativo por padrão é `local` (definido em `application.yml`). Configure as credenciais do MongoDB via variáveis de ambiente:

**Linux / macOS:**
```bash
export MONGODB_USERNAME=seu_usuario
export MONGODB_PASSWORD=sua_senha
```

**Windows (PowerShell):**
```powershell
$env:MONGODB_USERNAME="seu_usuario"
$env:MONGODB_PASSWORD="sua_senha"
```

### Executar a aplicação

```bash
./mvnw spring-boot:run
```

A API estará disponível em:

```
http://localhost:8080
```

### Executar os testes

```bash
./mvnw test
```

Os testes unitários cobrem a camada de serviço (`UserService`, `PostService`, `PostQueryService` e `CommentService`) com **JUnit 5** e **Mockito**, utilizando os mappers reais do MapStruct. O teste `contextLoads` inicializa a aplicação completa e requer o MongoDB disponível.

---

## 📌 Próximos Passos

- [ ] Implementar autenticação e autorização com **Spring Security + JWT**
- [ ] Adicionar documentação interativa com **OpenAPI / Swagger**
- [ ] Implementar paginação e ordenação nos endpoints de listagem
- [ ] Aumentar a cobertura de testes (integração e controller)
- [ ] Configurar pipeline de CI/CD
- [ ] Preparar containerização com **Docker**

---

## 👤 Autor

**Guilherme Spadaro**