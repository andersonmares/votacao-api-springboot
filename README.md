
# Votação API Spring Boot

API REST para gerenciar assembleias de cooperativismo, permitindo criar pautas, abrir sessões de votação, registrar votos e consultar resultados.

---

## 🛠️ Tecnologias

- Java 17
- Spring Boot 3.2
- Spring Data JPA (Hibernate 6)
- **H2 (perfil `dev`, arquivo local)** e PostgreSQL (perfil `docker`)
- Spring Actuator (health, info)
- Springdoc OpenAPI (Swagger UI)
- Lombok
- **Apache Kafka** (publicação de eventos de voto) — opcional
- Docker & Docker Compose

---

## ⚡ Integração com Kafka (nova)

A publicação de eventos de votos foi isolada por uma **interface** e controlada por *feature flag*:

- `VotoEventPublisher` (interface)
- `KafkaVotoEventPublisher` (implementação real via `KafkaTemplate`)
- `NoopVotoEventPublisher` (implementação “vazia”, não envia nada)

A escolha é feita pela propriedade `app.kafka.enabled` (boolean):
- `false` → usa `NoopVotoEventPublisher` (padrão no **dev**)
- `true`  → usa `KafkaVotoEventPublisher`

O **evento** publicado contém: `pautaId`, `associadoId`, `voto` e `dataHora` (UTC do app).  
O **tópico** padrão é `votos` (configurável por `app.kafka.topic.votos`).

### ✨ Onde é publicado?
A publicação ocorre no **Aspect** `VotoProducerAspect` **após** o `VotoService.salvar(...)` retornar com sucesso. Assim, a regra de negócio não depende de Kafka e seus testes ficam simples.

### ✅ Como habilitar Kafka no `dev`
1) Suba um broker local (ex.: com Docker Compose abaixo).  
2) Ligue a flag e aponte o bootstrap:
```yaml
# application-dev.yml
app:
  kafka:
    enabled: true
spring:
  kafka:
    bootstrap-servers: localhost:9092
```
> Se preferir, use variáveis de ambiente: `APP_KAFKA_ENABLED=true` e `SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092`.

### ✅ Como habilitar Kafka no `docker` (containers)
No `docker-compose.yml` já há um serviço `kafka`. Garanta que o app use `kafka:9092` dentro da rede Docker:

```yaml
# application-docker.yml (exemplo)
app:
  kafka:
    enabled: true
spring:
  kafka:
    bootstrap-servers: kafka:9092
```

> Se quiser, sobrescreva via env do serviço `app` no `docker-compose.yml`:
```yaml
environment:
  SPRING_PROFILES_ACTIVE: docker
  SPRING_KAFKA_BOOTSTRAP_SERVERS: kafka:9092
  APP_KAFKA_ENABLED: "true"
```

### 🔧 Propriedades relevantes
```yaml
app:
  kafka:
    enabled: false              # dev padrão
  # tópico com default 'votos' (se não definido)
  kafka.topic.votos: votos

spring:
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
```

### 🧩 Contrato da publicação
```java
// package com.anderson.votacao.kafka;

public interface VotoEventPublisher {
    void publicar(com.anderson.votacao.kafka.dto.VotoEvent event);
}
```

Implementações:
- **KafkaVotoEventPublisher**: envia para o tópico com `KafkaTemplate<String, Object>`; usa `@ConditionalOnProperty(name="app.kafka.enabled", havingValue="true")`.
- **NoopVotoEventPublisher**: faz nada; usa `@ConditionalOnProperty(name="app.kafka.enabled", havingValue="false", matchIfMissing=true")`.

> Observação: se `app.kafka.enabled=true` e o broker estiver indisponível, você verá avisos/erros de conexão nos logs do Kafka client. Se `false`, nenhuma tentativa de conexão é feita.

---

## 🚀 Como executar

### Pré-requisitos
- JDK 17
- Maven (opcional, apenas para modo dev)
- Docker & Docker Compose (para modo docker e/ou Kafka)

---

### 1) Modo **dev** (H2 **em arquivo**, sem Docker)

1. Clone o repositório:
```bash
git clone https://github.com/andersonmares/votacao-api-springboot.git
cd votacao-api-springboot
```
2. Execute com Maven (perfil **dev** é padrão):
```bash
mvn clean spring-boot:run
```
3. A aplicação: `http://localhost:8080`

4. Endpoints principais:
- **POST /api/v1/pautas** — criar nova pauta
- **GET /api/v1/pautas** — listar pautas
- **POST /api/v1/sessoes** — abrir sessão (`{ "pautaId":1, "duracaoMinutos":5 }`)
- **GET /api/v1/sessoes** — listar sessões
- **POST /api/v1/votos** — registrar voto (`{ "cpf":"…","pautaId":1,"voto":true,"associadoId":1 }`)
- **GET /api/v1/pautas/{id}/resultado** — resultado da votação
- **Actuator**:
  - **GET /actuator/health**
  - **GET /actuator/info**

5. H2 Console (dev):  
`http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:file:./data/votacao-db;MODE=LEGACY`
- Usuário: `sa`
- Senha: _(vazio)_

> **Kafka no dev**: vem **desativado** (`app.kafka.enabled=false`). Habilite conforme seção Kafka.

---

### 2) Modo **docker** (PostgreSQL + app + Kafka)

Suba tudo com um comando (na raiz do projeto):
```bash
docker-compose up --build -d
```
Isso cria:
- **kafka** (porta 9092)
- **db** (PostgreSQL, porta 5432, db `votacao`, user/pass `votacao`)
- **app** (porta 8080, perfil `docker`)

Aplicação: `http://localhost:8080`  
Swagger UI: `http://localhost:8080/swagger-ui.html`

Parar:
```bash
docker-compose down
```

**Dica:** para testar o tópico `votos`, utilize o console do Kafka (ex.: imagens Bitnami) ou um consumidor simples em outra aplicação.

---

## 📁 Estrutura do Projeto (atualizada)

```text
votacao-api-springboot/
├─ src/main/java/com/anderson/votacao/
│  ├─ controller/                 ← REST Controllers
│  ├─ dto/                        ← DTOs
│  ├─ entity/                     ← Entidades JPA
│  ├─ exception/                  ← Exceções e handlers
│  ├─ kafka/                      ← Integração Kafka
│  │  ├─ VotoEventPublisher.java          (interface)
│  │  ├─ KafkaVotoEventPublisher.java     (impl real, condicional)
│  │  ├─ NoopVotoEventPublisher.java      (impl no-op, condicional)
│  │  ├─ VotoProducerAspect.java          (publica após VotoService.salvar)
│  │  └─ dto/VotoEvent.java               (payload do evento)
│  ├─ mapper/                     ← MapStruct mappers
│  ├─ repository/                 ← Spring Data JPA
│  ├─ service/                    ← Regras de negócio (agnósticas de Kafka)
│  └─ service/validator/          ← Validadores (ex.: SessaoAbertaValidator)
│
├─ src/main/resources/
│  ├─ application.yml
│  ├─ application-dev.yml
│  ├─ application-docker.yml
│  └─ db/migration/               ← Flyway (ex.: V1__init.sql)
│
├─ docker-compose.yml
├─ Dockerfile
├─ pom.xml
└─ README.md
```

---

## 🧪 Exemplos de chamadas

Criar pauta:
```bash
curl -X POST http://localhost:8080/api/v1/pautas   -H "Content-Type: application/json"   -d '{ "titulo":"Nova Pauta", "descricao":"Descrição..." }'
```

Abrir sessão (5 min):
```bash
curl -X POST http://localhost:8080/api/v1/sessoes   -H "Content-Type: application/json"   -d '{ "pautaId": 1, "duracaoMinutos": 5 }'
```

Votar (SIM):
```bash
curl -X POST http://localhost:8080/api/v1/votos   -H "Content-Type: application/json"   -d '{ "cpf":"12345678900","pautaId":1,"voto":true,"associadoId":42 }'
```

Consultar resultado:
```bash
curl http://localhost:8080/api/v1/pautas/1/resultado
```

---

## 📝 Notas

- **Dev (H2)**: banco persiste em arquivo `./data/votacao-db`.
- **Docker (PostgreSQL)**: credenciais e DB definidos no `docker-compose.yml`.
- **Kafka opcional**: desligado por padrão no dev. Ligue `app.kafka.enabled=true` para publicar eventos.
- **Tópico**: altere via `app.kafka.topic.votos` (default `votos`).

---

## 📄 Licença
MIT
