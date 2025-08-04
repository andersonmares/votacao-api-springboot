import io.gatling.core.Predef._
import io.gatling.http.Predef._

class VotacaoSimulation extends Simulation {
  val httpProtocol = http.baseUrl("http://localhost:8080")

  val scn = scenario("Cenario de Votacao")
    .exec(
      http("Abrir Sessao")
        .post("/api/v1/sessoes")
        .body(StringBody("{ \"pautaId\": 1 }"))
        .asJson()
    )
    .pause(1)
    .exec(
      http("Votar SIM")
        .post("/api/v1/votos")
        .body(StringBody("{ \"pautaId\": 1, \"associadoId\": 1, \"escolha\": \"SIM\" }"))
        .asJson()
    )

  setUp(
    scn.inject(atOnceUsers(1000))
  ).protocols(httpProtocol)
}
