# Blog API

Uma API REST para um blog, construída com Spring Boot e MongoDB. Este projeto nasceu como um espaço para aprofundar, na prática, conceitos de backend que eu vinha estudando — modelagem de domínio, relacionamento entre documentos, padrões de arquitetura em camadas e boas práticas de versionamento.

## Sobre o projeto

A Blog API é o back-end de uma plataforma de blog: usuários se cadastram, publicam posts e comentam nas publicações uns dos outros. A proposta é oferecer uma base sólida para esse tipo de aplicação — com um domínio bem definido de usuários, posts e comentários, e espaço para crescer com autenticação, paginação, busca e outras funcionalidades que um blog de verdade precisa conforme o projeto amadurece.

A API expõe seus recursos por endpoints REST, separando claramente o que é entidade de domínio do que é exposto para quem consome a API, e mantendo a lógica de negócio isolada em uma camada de serviço.

## Tecnologias utilizadas

- **Java 21**
- **Spring Boot 3.5**
- **Spring Web**, para expor os endpoints REST
- **Spring Data MongoDB**, para persistência
- **MongoDB**
- **Spring Boot Test**, para a camada de testes
- **Maven**
- **Git**

## O que esse projeto me ensinou

Construir a Blog API foi, antes de tudo, um exercício de tirar dúvidas na prática em vez de só ler sobre elas. Foi aqui que entendi de verdade a diferença entre modelar relacionamentos em um banco relacional e em um banco de documentos, que fixei o padrão DTO com `record`, e que aprendi a manter um histórico de commits que realmente conta a história do desenvolvimento do projeto — não só uma sequência de "fix" e "update".

## Autor

Guilherme Spadaro — desenvolvedor backend em transição de carreira, com foco em Java e Spring.

[GitHub](https://github.com/GuilhermeLimaSpadaro)