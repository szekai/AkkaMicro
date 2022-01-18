package sky.micro

import java.nio.file.Path
import java.util.UUID

object Model {
  // model
  class Year(val year: Int) extends AnyVal

  case class Country(name: String)

  case class Author(name: String, country: Country)

  case class Book(id: UUID, title: String, year: Year, author: Author)

  case class NewBook(title: String, cover: Option[Path], year: Year, authorName: String, authorCountry: String)

  case class BooksQuery(year: Option[Year], limit: Option[Int])

  object Database {
    var books: List[Book] = List(
      Book(
        UUID.randomUUID(),
        "The Sorrows of Young Werther",
        new Year(1774),
        Author("Johann Wolfgang von Goethe", Country("Germany"))
      ),
      Book(UUID.randomUUID(), "Iliad", new Year(-8000), Author("Homer", Country("Greece"))),
      Book(UUID.randomUUID(), "Nad Niemnem", new Year(1888), Author("Eliza Orzeszkowa", Country("Poland"))),
      Book(UUID.randomUUID(), "The Colour of Magic", new Year(1983), Author("Terry Pratchett", Country("United Kingdom"))),
      Book(UUID.randomUUID(), "The Art of Computer Programming", new Year(1968), Author("Donald Knuth", Country("USA"))),
      Book(UUID.randomUUID(), "Pharaoh", new Year(1897), Author("Boleslaw Prus", Country("Poland")))
    )

    var bookCovers: Map[UUID, Path] = Map.empty
  }
}
