package sky.micro

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.typesafe.config.ConfigFactory
import sky.micro.AkkaRoutes._

import scala.io.StdIn

object Hello {
  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem(Behaviors.empty, "my-system")
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.executionContext

    val config = ConfigFactory.load()
    import sky.micro.Endpoints.swaggerRoute

    val routes: Route = swaggerRoute ~ route ~ getBooksRoute ~ testRoute
    val bindingFuture = Http()
      .newServerAt(config.getString("http.interface"), config.getInt("http.port"))
      .bind(routes)

    println(s"Server now online. Please navigate to http://localhost:${config.getInt("http.port")}/hello\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
