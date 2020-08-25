package example

import example.ExampleMethods._
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder

class ExampleSimulation extends Simulation with  ExampleInjects {
  def scenarioOne: ScenarioBuilder = scenario("ScenarioOne").exec(firstChain)
  def scenarioTwo: ScenarioBuilder = scenario("ScenarioTwo").exec(secondChain)
  def scenarioThree: ScenarioBuilder = scenario("ScenarioThree").exec(thirdChain)
  def scenarioFour: ScenarioBuilder = scenario("ScenarioFour").exec(fourthChain)

  val test = 3

  loader(test, scenarioOne, scenarioTwo, scenarioThree, scenarioFour)

}