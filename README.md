# Votação API Spring Boot

API REST para gerenciar assembleias de cooperativismo, permitindo criar pautas, abrir sessões de votação, registrar votos e consultar resultados.

---

## 🛠️ Tecnologias

- Java 17
- Spring Boot 3.2
- Spring Data JPA
- H2 (perfil **dev**) e PostgreSQL (perfil **docker**)
- Spring Actuator (health, info)
- Springdoc OpenAPI (Swagger UI)
- Lombok
- Docker & Docker Compose

---

## 🚀 Como executar

### Pré-requisitos

- JDK 17 instalado
- Maven (opcional, somente para modo dev)
- Docker & Docker Compose (para modo Docker)

---

### 1) Modo **dev** (H2 em memória, sem Docker)

1. Clone o repositório:
   ```bash
   git clone https://github.com/andersonmares/votacao-api-springboot.git
   cd votacao-api-springboot
   ```
2. Compile e execute com Maven (perfil **dev** é padrão):
   ```bash
   mvn clean spring-boot:run
   ```
3. A aplicação estará disponível em  
   `http://localhost:8080`

4. Endpoints principais:
    - **POST /api/v1/pautas** — criar nova pauta
    - **GET /api/v1/pautas** — listar pautas
    - **POST /api/v1/sessoes** — abrir sessão (`{ "pautaId":1, "duracaoMinutos":5 }`)
    - **GET /api/v1/sessoes** — listar sessões
    - **POST /api/v1/votos** — registrar voto (`{ "cpf":"…","pautaId":1,"voto":true, "associadoId":1 }`)
    - **GET /api/v1/pautas/{id}/resultado** — resultado da votação
    - **Actuator**:
        - **GET /actuator/health**
        - **GET /actuator/info**

5. H2 Console (dev):  
   Acesse `http://localhost:8080/h2-console`
    - JDBC URL: `jdbc:h2:mem:testdb`
    - Usuário: `sa`
    - Senha: _(vazio)_

---

### 2) Modo **docker** (PostgreSQL + app em container)

Certifique-se de ter Docker e Docker Compose instalados.  
Na raiz do projeto, suba tudo em um único comando:
```bash
docker-compose up --build -d
```
Isso criará:
- **postgres** (porta 5432, db `votacao_db`, user/pass `votacao`)
- **app-votacao** (porta 8080, perfil `docker`)

Acesse a API em  
`http://localhost:8080`

Swagger UI:  
`http://localhost:8080/swagger-ui.html`

Logs:
- Container **app** exibe no console Docker
- Container **db** exibe logs do PostgreSQL

Para parar tudo:
```bash
docker-compose down
```

---

## 📁 Estrutura do Projeto

```text
votacao-api-springboot/
├─ src/
│  ├─ main/
│  │  ├─ java/com/anderson/votacao/
│  │  │  ├─ controller/           ← Controllers REST
│  │  │  ├─ dto/                  ← DTOs de entrada/saída
│  │  │  ├─ entity/               ← Entidades JPA (Lombok)
│  │  │  ├─ exception/            ← Exceções e handlers
│  │  │  ├─ repository/           ← Spring Data JPA
│  │  │  ├─ service/              ← Regras de negócio
│  │  │  └─ validation/           ← Validadores SOLID
│  │  │     ├─ Validador.java
│  │  │     ├─ CpfObrigatorioValidator.java
│  │  │     ├─ CpfStatusValidator.java
│  │  │     ├─ SessaoAbertaValidator.java
│  │  │     └─ JaVotouValidator.java
│  │  ├─ resources/
│  │  │  ├─ application.yml
│  │  │  ├─ application-dev.yml
│  │  │  ├─ application-docker.yml
│  │  │  └─ db/
│  │  │     └─ migration/         ← Scripts SQL para Flyway (ex: V1__init.sql)
│  ├─ test/
│  │  └─ java/com/anderson/votacao/
│  │     ├─ service/              ← Testes de serviço (ex: VotoServiceTest)
│  │     └─ validation/           ← Testes unitários dos validadores
│
├─ src/gatling/                 ← Testes de performance (Gatling)
│  └─ simulations/
│     └─ VotacaoSimulation.scala
│
├─ Dockerfile
├─ docker-compose.yml
├─ pom.xml
└─ README.md
               
```

---

## 📝 Notas

- **Perfil dev** (`dev`) usa H2 em memória e não requer banco externo.
- **Perfil docker** (`docker`) usa PostgreSQL via Docker Compose.
- `/actuator/info` retorna:

```json
{
  "app": {
    "name": "Votação API",
    "version": "1.0.0"
  }
}
```

- Adapte `application-docker.yml` para apontar a um Postgres remoto em produção, se desejar.  
