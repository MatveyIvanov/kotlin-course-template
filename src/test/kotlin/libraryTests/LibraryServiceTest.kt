package libraryTests

import library.*
import org.junit.Assert
import org.junit.Test
import java.lang.Exception
import java.time.LocalDate
import java.time.Year
import kotlin.RuntimeException

class LibraryServiceTest {
    private val libraryService = LibraryService()
    private val authorList = arrayListOf(
        Author("Joanne", "Rowling"),
        Author("Andrzej", "Sapkowski"),
        Author("Stephen", "King"),
        Author("Thomas", "Harris"),
        Author("John", "Tolkien")
    )
    private val bookList = arrayListOf(
        Book("Harry Potter and the Prisoner of Azkaban", arrayListOf(authorList[0]), Year.parse("1999"), Genre.NOVEL),
        Book("Harry Potter and the Deathly Hallows", arrayListOf(authorList[0]), Year.parse("2007"), Genre.NOVEL),
        Book("Lady of the Lake", arrayListOf(authorList[1]), Year.parse("1999"), Genre.NOVEL),
        Book("The Shining", arrayListOf(authorList[2]), Year.parse("1977"), Genre.NOVEL),
        Book("The Silence of the Lambs", arrayListOf(authorList[3]), Year.parse("1988"), Genre.DETECTIVE),
        Book("The Lord of the Rings", arrayListOf(authorList[4]), Year.parse("1954"), Genre.FANTASY),
        Book("It", arrayListOf(authorList[2]), Year.parse("1986"), Genre.HORROR)
    )
    private val userList = arrayListOf(
        User("Matvey", "Ivanov", LocalDate.of(2001, 3, 25)),
        User("Petr", "Petrov", LocalDate.of(2004, 12, 14)),
        User("Ivan", "Ivanov", LocalDate.of(1998, 1, 7))
    )
    private val testBook = Book("Test Book")
    private val testUser = User("Test", "User", LocalDate.now())

    init {
        bookList.forEach { book -> libraryService.addBook(book) }
        userList.forEach { user -> libraryService.registerUser(user) }
        libraryService.setBookStatus(bookList[1], Status.Restoration)
    }

    @Test
    fun test_find_books_by_name_substring() {
        Assert.assertEquals(arrayListOf(bookList[0], bookList[1]), libraryService.findBooks("Harry Potter"))
    }

    @Test
    fun test_find_books_by_author() {
        Assert.assertEquals(arrayListOf(bookList[3], bookList[6]), libraryService.findBooks(authorList[2]))
    }

    @Test
    fun test_find_books_by_year() {
        Assert.assertEquals(arrayListOf(bookList[0], bookList[2]), libraryService.findBooks(Year.parse("1999")))
    }

    @Test
    fun test_find_books_by_genre() {
        Assert.assertEquals(arrayListOf(bookList[0], bookList[1], bookList[2], bookList[3]), libraryService.findBooks(Genre.NOVEL))
    }

    @Test
    fun test_find_books_by_multiple_filters() {
        Assert.assertEquals(arrayListOf(bookList[3]), libraryService.findBooks("The", year = Year.parse("1977"), genre = Genre.NOVEL))
    }

    @Test
    fun test_get_all_books() {
        Assert.assertEquals(bookList, libraryService.getAllBooks())
    }

    @Test
    fun test_get_all_available_books() {
        Assert.assertEquals(bookList.filterNot { book -> book == bookList[1] }, libraryService.getAllAvailableBooks())
    }

    @Test
    fun test_get_book_status() {
        Assert.assertEquals(Status.Restoration, libraryService.getBookStatus(bookList[1]))
    }

    @Test
    fun test_get_book_status_exception() {
        try {
            libraryService.getBookStatus(testBook)
        } catch (e: NoSuchElementException) {
            Assert.assertEquals("This book is not in the library", e.message)
        }
    }

    @Test
    fun test_get_all_book_statuses() {
        Assert.assertEquals(bookList.associateWith { libraryService.getBookStatus(it) }, libraryService.getAllBookStatuses())
    }

    @Test
    fun test_set_book_status() {
        libraryService.setBookStatus(bookList[3], Status.Restoration)
        Assert.assertEquals(Status.Restoration, libraryService.getBookStatus(bookList[3]))
    }

    @Test
    fun test_set_book_status_exception() {
        try {
            libraryService.setBookStatus(testBook, Status.ComingSoon)
        } catch (e: NoSuchElementException) {
            Assert.assertEquals("This book is not in the library", e.message)
        }
    }

    @Test
    fun test_add_book() {
        val newBook = Book("The Witcher", arrayListOf(authorList[1]), Year.parse("1990"), Genre.NOVEL)
        libraryService.addBook(newBook)
        Assert.assertEquals(newBook, libraryService.findBooks("The Witcher")[0])
    }

    @Test
    fun test_add_book_exception() {
        try {
            libraryService.addBook(bookList[4])
        } catch (e: RuntimeException) {
            Assert.assertEquals("This book is already in the library", e.message)
        }
    }

    @Test
    fun test_register_user() {
        libraryService.registerUser(testUser)
        try {
            libraryService.registerUser(testUser)
        } catch (e: RuntimeException) {
            Assert.assertEquals("User is already registered", e.message)
        }
    }

    @Test
    fun test_unregister_user() {
        try {
            libraryService.unregisterUser(testUser)
        } catch (e: NoSuchElementException) {
            Assert.assertEquals("This user is not registered", e.message)
        }
    }

    @Test
    fun test_unregister_user_who_has_books() {
        libraryService.takeBook(userList[0], bookList[0])
        try {
            libraryService.unregisterUser(userList[0])
        } catch (e: RuntimeException) {
            Assert.assertEquals("User has books to return:\nBook(name=Harry Potter and the Prisoner of Azkaban, authors=Author(firstName=Joanne, lastName=Rowling), year=1999, genre=NOVEL)", e.message)
        }
    }

    @Test
    fun test_take_book() {
        libraryService.takeBook(userList[0], bookList[2])
        val usedBy = libraryService.getBookStatus(bookList[2]) as Status.UsedBy
        Assert.assertEquals(userList[0], usedBy.user)
    }

    @Test
    fun test_take_book_that_not_in_library_exception() {
        try {
            libraryService.takeBook(userList[0], testBook)
        } catch (e: NoSuchElementException) {
            Assert.assertEquals("This book is not in the library", e.message)
        }
    }

    @Test
    fun test_take_book_that_is_unavailable_exception() {
        try {
            libraryService.takeBook(userList[0], bookList[3])
        } catch (e: RuntimeException) {
            Assert.assertEquals("Book is unavailable. Come back later", e.message)
        }
    }

    @Test
    fun test_take_book_user_is_not_registered_exception() {
        try {
            libraryService.takeBook(testUser, bookList[4])
        } catch (e: Exception) {
            Assert.assertEquals("This user is not registered", e.message)
        }
    }

    @Test
    fun test_take_book_user_has_that_book_exception() {
        try {
            libraryService.takeBook(userList[0], bookList[2])
        } catch (e: RuntimeException) {
            Assert.assertEquals("User already has that book", e.message)
        }
    }

    @Test
    fun test_take_book_user_has_three_books_exception() {
        libraryService.takeBook(userList[0], bookList[0])
        libraryService.takeBook(userList[0], bookList[2])
        libraryService.takeBook(userList[0], bookList[3])
        try {
            libraryService.takeBook(userList[0], bookList[4])
        } catch (e: RuntimeException) {
            Assert.assertEquals("User cannot have more than 3 books at the same time", e.message)
        }
    }

    @Test
    fun test_return_book() {
        libraryService.takeBook(userList[0], bookList[0])
        libraryService.returnBook(bookList[0])
        Assert.assertEquals(Status.Available, libraryService.getBookStatus(bookList[0]))
    }

    @Test
    fun test_return_book_that_is_not_in_library_exception() {
        try {
            libraryService.returnBook(testBook)
        } catch (e: NoSuchElementException) {
            Assert.assertEquals("This book is not in the library", e.message)
        }
    }

    @Test
    fun test_book_restoration() {
        libraryService.bookRestoration(bookList[6])
        Assert.assertEquals(Status.Restoration, libraryService.getBookStatus(bookList[6]))
    }

    @Test
    fun test_book_restoration_that_not_in_library_exception() {
        try {
            libraryService.bookRestoration(testBook)
        } catch (e: NoSuchElementException) {
            Assert.assertEquals("This book is not in the library", e.message)
        }
    }

    @Test
    fun test_book_restoration_already_on_restoration_exception() {
        try {
            libraryService.bookRestoration(bookList[6])
        } catch (e: RuntimeException) {
            Assert.assertEquals("Book is already on restoration", e.message)
        }
    }

    @Test
    fun test_available_soon_books() {
        libraryService.setBookStatus(bookList[3], Status.ComingSoon)
        Assert.assertEquals(arrayListOf(bookList[1], bookList[3]), libraryService.availableSoonBooks())
    }
}