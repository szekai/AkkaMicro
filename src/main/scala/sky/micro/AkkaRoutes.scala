package sky.micro

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{complete, get, path}
import akka.http.scaladsl.server.Route
import sky.micro.Model.Database.books
import sttp.model.StatusCode
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter

import scala.concurrent.Future

object AkkaRoutes {
  import Endpoints._

  val getBooksLogic = getBooks.serverLogic {
    booksQuery =>
      if (booksQuery.limit.getOrElse(0) < 0) {
        Future.successful(Left((StatusCode.BadRequest, ErrorInfo("Limit must be positive"))))
      } else {
        val filteredByYear = booksQuery.year.map(year => books.filter(_.year == year)).getOrElse(books)
        val limited = booksQuery.limit.map(limit => filteredByYear.take(limit)).getOrElse(filteredByYear)
        Future.successful(Right(limited))
      }
  }
  val getBooksRoute: Route = AkkaHttpServerInterpreter().toRoute(getBooksLogic)

//  val getBookCoverRoute: Route = Endpoints.getBookCover.toRoute { bookId =>
//    bookCovers.get(bookId) match {
//      case None                => Future.successful(Left((StatusCodes.NotFound, ErrorInfo("Book not found"))))
//      case Some(bookCoverPath) => Future.successful(Right(FileIO.fromPath(bookCoverPath)))
//    }
//  }
//
//  val addBookRoute: Route = Endpoints.addBook.toRoute {
//    case (authToken, newBook) =>
//      if (authToken == "secret") {
//        val book = Book(UUID.randomUUID(), newBook.title, newBook.year, Author(newBook.authorName, Country(newBook.authorCountry)))
//        books = books :+ book
//        newBook.cover.foreach { cover =>
//          bookCovers = bookCovers + (book.id -> cover)
//        }
//        Future.successful(Right(()))
//      } else {
//        Future.successful(Left((StatusCodes.Unauthorized, ErrorInfo("Incorrect auth token"))))
//      }
//  }

  val route =
    path("hello") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
      }
    }

  val testLogic = test.serverLogicSuccess { a =>
    Future.successful(s"$a")
  }
  val testRoute = AkkaHttpServerInterpreter().toRoute(testLogic)

  val routes = AkkaHttpServerInterpreter().toRoute(List(getBooksLogic, testLogic))

}
