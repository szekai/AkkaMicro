package sky.micro

import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import io.circe.generic.auto._
import akka.stream.scaladsl.Source
import akka.util.ByteString
import sky.micro.Model._
import sttp.model.{MediaType, StatusCode}
import sttp.tapir.Codec.{PlainCodec, cookies}
import sttp.tapir._
import sttp.tapir.Endpoint
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._

import java.util.UUID
import scala.concurrent.Future

object Endpoints {
  type AuthToken = String

  case class ErrorInfo(error: String)

  implicit val yearCodec: PlainCodec[Year] = implicitly[PlainCodec[Int]].map(new Year(_))(_.year)

  val baseEndpoint: Endpoint[Unit, Unit, (StatusCode, ErrorInfo), Unit, Any] = endpoint
    .in("api" / "v1.0")
    .errorOut(statusCode.and(jsonBody[ErrorInfo]))

  val booksQueryInput: EndpointInput[BooksQuery] = query[Option[Year]]("year").and(query[Option[Int]]("limit")).mapTo[BooksQuery]

  val getBooks: Endpoint[Unit, BooksQuery, (StatusCode, ErrorInfo), List[Book], Any] = baseEndpoint.get
    .in("book")
    .in(booksQueryInput)
    .out(jsonBody[List[Book]].example(List(Database.books.head)))

//  val getBookCover: Endpoint[UUID, (StatusCode, ErrorInfo), Source[ByteString, Any], Source[ByteString, Any]] = baseEndpoint.get
//    .in("book" / path[UUID]("bookId") / "cover")
//    .out(streamBody[Source[ByteString, Any]](schemaFor[Array[Byte]], MediaType.ApplicationOctetStream))
//
//  val addBook: Endpoint[Unit, (AuthToken, NewBook), (StatusCode, ErrorInfo), Unit, Any] = baseEndpoint.post
//    .securityIn(auth.bearer[String])
//    .in("book")
//    .in(multipartBody[NewBook])

  val test = baseEndpoint.get.in("echo" / path[String]("input")).out(stringBody)

  // first interpret as swagger ui endpoints, backend by the appropriate yaml
  val swaggerEndpoints = SwaggerInterpreter().fromEndpoints[Future](List(getBooks, test), "My App", "1.0")

  // add to your akka routes
  val swaggerRoute = AkkaHttpServerInterpreter().toRoute(swaggerEndpoints)
}
