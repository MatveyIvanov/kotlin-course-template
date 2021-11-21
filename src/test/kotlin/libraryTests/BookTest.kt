package libraryTests

import library.*
import org.junit.Assert
import org.junit.Test
import java.time.Year

class BookTest {
    private val book1 = Book("Harry Potter 1",
        arrayListOf(Author("Joanne", "Rowling")),
        Year.parse("1997"),
        Genre.NOVEL
    )

    private val book2 = Book("Harry Potter 2",
        arrayListOf(Author("Joanne", "Rowling")),
        Year.parse("1998"),
        Genre.NOVEL
    )

    @Test
    fun test_init() {
        Assert.assertEquals("Book(name=Harry Potter 1, authors=[Author(firstName=Joanne, lastName=Rowling)], year=1997, genre=NOVEL)", book1.toString())
    }

    @Test
    fun test_equals() {
        val copy = Book("Harry Potter 1",
            arrayListOf(Author("Joanne", "Rowling")),
            Year.parse("1997"),
            Genre.NOVEL
        )
        Assert.assertTrue(book1 == copy)
        Assert.assertFalse(book1 == book2)
    }

}