package example

import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import io.gatling.core.Predef._
import io.gatling.core.session.Expression
import books.books.{BookList, Empty}
import com.github.phisgr.gatling.grpc.Predef.{$, _}
import com.github.phisgr.gatling.grpc.action.GrpcCallActionBuilder
import com.github.phisgr.gatling.pb._


object ExampleMethods {
  val index: ChainBuilder = exec(http("index")
    .get("/")
    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.135 Safari/537.36")
    .check(status.is(200))
  )

  val order: ChainBuilder = exec(http("order")
    .post("/api/orders")
    .header("Accept", "*/*")
    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.135 Safari/537.36")
    .body(RawFileBody("order.json")).asJson
    .check(status.is(200))
  )

  val callBook: Expression[Empty] = Empty()
    .updateExpr()

  val bookListCall: GrpcCallActionBuilder[Empty, BookList] = grpc("bookListCall")
    .rpc(books.books.BookServiceGrpc.METHOD_LIST)
    .payload(callBook)

  // Scenario 1
  val firstChain: ChainBuilder = group("firstChain") {
    exec(index)
  }

  // Scenario 2
  val secondChain: ChainBuilder = group("secondChain") {
    exec(order)
  }

  // Scenario 3
  val thirdChain: ChainBuilder = group("thirdChain") {
    exec(index).exec(order)
  }

  // Scenario 4 GRPC
  val fourthChain: ChainBuilder = group("fourthChain") {
    exec(bookListCall)
  }


}