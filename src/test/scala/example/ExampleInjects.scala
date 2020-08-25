package example

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.protocol.HttpProtocolBuilder
import io.grpc.ManagedChannelBuilder
import scala.concurrent.duration._
import com.github.phisgr.gatling.grpc.Predef.{$, _}

trait ExampleInjects extends Simulation {
  val baseUrl: HttpProtocolBuilder = http.baseUrl("http://185.233.0.230:3000/");

  def loader(test: Int,scenario: ScenarioBuilder, scenario2: ScenarioBuilder, scenario3: ScenarioBuilder, scenario4: ScenarioBuilder): SetUp = {
    val httpProtocol = baseUrl
    val grpcConf = grpc(ManagedChannelBuilder.forAddress("185.233.0.230", 50051).usePlaintext())

      if (test == 1) {
        setUp(
          // open index with rps = 1
          scenario.inject(constantUsersPerSec(1) during(1 hour)).protocols(httpProtocol),
          // send order with rps = 0.27 (1000 per hour)
          scenario2.inject(constantUsersPerSec(0.28) during(1 hour)).protocols(httpProtocol)
        )
      }
      else if (test == 2) {
        setUp(
        // performance test: increase RPM from 10 to 60 with step in 10 min
          scenario3.inject(
            incrementUsersPerSec(0.17) // Double
              .times(6)
              .eachLevelLasting(10 minutes)
              .startingFrom(0.1) // Double
          ).protocols(httpProtocol)
        )
      }
      else if (test == 3) {
        setUp(
          // performance test: increase RPS from 2 to 12 with step in 10 min
          scenario3.inject(
            incrementUsersPerSec(2) // Double
              .times(6)
              .eachLevelLasting(10 minutes)
              .startingFrom(2) // Double
          ).protocols(httpProtocol)
        )
      }
      else if (test == 4)
        setUp(
          scenario4.inject(
            constantUsersPerSec(50) during(15 minutes)
          ).protocols(grpcConf)
        )
      else {
        setUp(
        )
      }
  }

}