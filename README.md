# Baozi Store – API REST

Trabalho da disciplina **Desenvolvimento Web Back-End** (UNINTER).
API REST simples para a Baozi Store, loja de pão chinês (baozi), com controle de **clientes**, **produtos** e **pedidos**.

Aluno: **Antonio – RU 5185553**

## Tecnologias

- Java 17
- Spring Boot 3.3 (Spring Web, arquitetura MVC)
- Spring Data JPA / Hibernate
- Banco relacional H2 (padrão, em arquivo) ou MySQL (perfil `mysql`)
- JSON em todos os endpoints
- Postman para os testes

## Estrutura (MVC)

```
src/main/java/br/com/baozistore
├── BaoziStoreApplication.java    # classe principal
├── model/                        # entidades JPA
│   ├── Cliente.java
│   ├── Produto.java
│   └── Pedido.java
├── repository/                   # interfaces Spring Data JPA
│   ├── ClienteRepository.java
│   ├── ProdutoRepository.java
│   └── PedidoRepository.java
└── controller/                   # endpoints REST
    ├── ClienteController.java
    ├── ProdutoController.java
    └── PedidoController.java
```

## Entidades

| Entidade | Campos |
|---|---|
| Cliente | `id` (Long), `nome` (String), `clienteDesde` (LocalDate) |
| Produto | `id` (Long), `nome` (String), `preco` (BigDecimal), `estoque` (Boolean) |
| Pedido  | `id` (Long), `clienteId` (Long), `produtoId` (Long), `quantidade` (Integer) |

## Endpoints

| Método | Rota | Descrição |
|---|---|---|
| POST | `/clientes` | Cria cliente |
| GET | `/clientes` | Lista todos os clientes |
| GET | `/clientes/{id}` | Consulta cliente por ID |
| PUT | `/clientes/{id}` | Atualiza cliente (opcional) |
| DELETE | `/clientes/{id}` | Apaga cliente |
| POST | `/produtos` | Cria produto |
| GET | `/produtos` | Lista todos os produtos |
| GET | `/produtos/{id}` | Consulta produto por ID |
| PUT | `/produtos/{id}` | Atualiza produto (opcional) |
| DELETE | `/produtos/{id}` | Apaga produto |
| POST | `/pedidos` | Registra pedido (valida se cliente e produto existem) |
| GET | `/pedidos` | Lista todos os pedidos |
| GET | `/pedidos/{id}` | Consulta pedido por ID |
| PUT | `/pedidos/{id}` | Atualiza pedido (opcional) |
| DELETE | `/pedidos/{id}` | Apaga pedido |

Códigos de resposta: `201 Created` (POST), `200 OK` (GET/PUT), `204 No Content` (DELETE), `404 Not Found` (ID inexistente), `400 Bad Request` (pedido com cliente/produto inexistente ou quantidade inválida).

## Como executar

Pré-requisito: JDK 17+ (o Maven não precisa ser instalado, o projeto usa o Maven Wrapper).

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows (Prompt de Comando ou PowerShell)
mvnw.cmd spring-boot:run
```

A API sobe em `http://localhost:8080`. O console do H2 fica em `http://localhost:8080/h2-console`
(JDBC URL: `jdbc:h2:file:./data/baozidb`, usuário `sa`, senha vazia).

### Usando MySQL (opcional)

Ajuste usuário/senha em `src/main/resources/application-mysql.properties` e execute:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=mysql
```

O banco `baozi_store` é criado automaticamente.

## Testes no Postman

Importe `postman/BaoziStore.postman_collection.json` no Postman. Execute na ordem:
POST Cliente → POST Produto → POST Pedido → GETs → DELETE.

Exemplos de corpo JSON:

```json
POST /clientes
{ "nome": "Antonio5185553", "clienteDesde": "2026-11-16" }

POST /produtos
{ "nome": "Baozi Vegetariano", "preco": 7.50, "estoque": true }

POST /pedidos
{ "clienteId": 1, "produtoId": 1, "quantidade": 6 }
```
