package library

import java.lang.Exception
import java.time.LocalDate
import java.time.Year


sealed class Status {
    val bookUser: User? // null if book status is not 'UsedBy', User object otherwise
        get() = when(this) {
            is UsedBy -> { this.user }
            else -> { null }
        }

    object Available : Status()
    data class UsedBy(val user: User) : Status() {
        override fun toString(): String = super.toString()
    }
    object ComingSoon : Status()
    object Restoration : Status()

    override fun toString(): String = when(this) {
        is Available -> "Available"
        is UsedBy -> "Used by $bookUser"
        is ComingSoon -> "Coming soon"
        is Restoration -> "Restoration"
    }
}

interface LibraryServiceInterface {
    fun findBooks(substring: String): List<Book> // Get list of books with passed name
    fun findBooks(author: Author): List<Book> // Get list of books with passed author
    fun findBooks(year: Year): List<Book> // Get list of books with passed year
    fun findBooks(genre: Genre): List<Book> // Get list of books with passed genre
    fun findBooks(substring: String? = null, author: Author? = null, year: Year? = null, genre: Genre? = null): List<Book> // Get list of books which fields match every of passed filters

    fun getAllBooks(): List<Book> // Get list of all books
    fun getAllAvailableBooks(): List<Book> // Get list of all available books

    fun getBookStatus(book: Book): Status // Get book status
    fun getAllBookStatuses(): Map<Book, Status> // Get all books status

    fun setBookStatus(book: Book, status: Status) // Set book status

    fun addBook(book: Book, status: Status = book.status) // Add book to the library

    fun registerUser(user: User) // Register user by User object
    fun registerUser(firstName: String, lastName: String, birthDate: LocalDate) // Register user by firstname, lastname and birthdate
    fun unregisterUser(user: User) // Unregister user

    fun takeBook(user: User, book: Book) // Take book from library
    fun returnBook(book: Book) // Return book to library

    fun bookRestoration(book: Book) // Take book to restoration
    fun availableSoonBooks(): List<Book> // Get all the books coming soon
}

class LibraryService(
    private val bookList: MutableList<Book> = arrayListOf(),
    private val userList: MutableList<User> = arrayListOf()
): LibraryServiceInterface {
    override fun findBooks(substring: String): List<Book> = bookList.filter { book -> book.name.contains(substring) }
    override fun findBooks(author: Author): List<Book> = bookList.filter { book -> book.authors.contains(author) }
    override fun findBooks(year: Year): List<Book> = bookList.filter { book -> book.year == year }
    override fun findBooks(genre: Genre): List<Book> = bookList.filter { book -> book.genre == genre }

    override fun findBooks(substring: String?, author: Author?, year: Year?, genre: Genre?): List<Book> {
        // 'filters' list is a list of conditions to filter the book list
        val filters = listOf<(Book) -> Boolean>(
            { substring == null || it.name.contains(substring) },
            { author == null || it.authors.contains(author) },
            { year == null || it.year == year },
            { genre == null || it.genre == genre }
        )
        return bookList.filter { book -> filters.all { filter -> filter(book) } }
    }

    override fun getAllBooks(): List<Book> = bookList
    override fun getAllAvailableBooks(): List<Book> = bookList.filter { book -> book.status == Status.Available }

    override fun getBookStatus(book: Book): Status {
        val libraryBook = bookList.find { item -> item.name == book.name }
        libraryBook ?: throw Exception("This book is not in the library") // If passed book is not found in book list

        return libraryBook.status
    }

    override fun getAllBookStatuses(): Map<Book, Status> = bookList.associateWith { it.status }

    override fun setBookStatus(book: Book, status: Status) {
        val libraryBook = bookList.find { item -> item.name == book.name }
        libraryBook ?: throw Exception("This book is not in the library") // If passed book is not found in book list

        libraryBook.status = status
    }

    override fun addBook(book: Book, status: Status) {
        val libraryBook = bookList.find { item -> item == book } // Check if book is already in book list
        if (libraryBook != null)
            throw Exception("This book is already in the library")

        bookList.add(book)
    }

    override fun registerUser(user: User) {
        val libraryUser = userList.find { item -> item == user } // Check if user is already in user list
        if (libraryUser != null)
            throw Exception("User is already registered")

        userList.add(user)
    }

    override fun registerUser(firstName: String, lastName: String, birthDate: LocalDate) {
        val libraryUser = userList.find { item -> item.firstName == firstName && item.lastName == lastName && item.birthDate == birthDate } // Check if user is already in user list
        if (libraryUser != null)
            throw Exception("User is already registered")

        userList.add(User(firstName, lastName, birthDate))
    }

    override fun unregisterUser(user: User) {
        val libraryUser = userList.find { item -> item == user }
        libraryUser ?: throw Exception("This user is not registered") // If passed user is not found in user list

        if (libraryUser.books.size > 0) // Check if user has books to return
            throw Exception("User has books to return:\n${libraryUser.books.toString().replace("[", "").replace("]", "")}")

        userList.remove(user)
    }

    override fun takeBook(user: User, book: Book) {
        // Book check
        val libraryBook = bookList.find { item -> item == book }
        libraryBook ?: throw Exception("This book is not in the library") // If passed book is not found in book list
        if (libraryBook.status != Status.Available) // Not available book cannot be taken
            throw Exception("Book is unavailable. Come back later")

        // User check
        val libraryUser = userList.find { item -> item == user }
        libraryUser ?: throw Exception("This user is not registered") // If passed user is not found in user list
        if (libraryBook.status.bookUser == libraryUser)
            throw Exception("User already has that book")
        if (libraryUser.books.size == 3) // User cannot have more than 3 books
            throw Exception("User cannot have more than 3 books at the same time")

        libraryBook.status = Status.UsedBy(libraryUser)
        libraryUser.books.add(libraryBook)
    }
    override fun returnBook(book: Book) {
        val libraryBook = bookList.find { item -> item == book }
        libraryBook ?: throw Exception("This book is not in the library") // If passed book is not found in book list

        val bookHolder = userList.find { item -> item == libraryBook.status.bookUser }

        /*
        * There is no check if bookHolder is registered because:
        *   - Only registered user is allowed to take a book
        *   - User is not allowed to unregister until he returns all the books he has taken
        */
        bookHolder?.books?.remove(libraryBook) // Delete book from user's book list
        libraryBook.status = Status.Available // Change book status to Available
    }

    override fun bookRestoration(book: Book) {
        val libraryBook = bookList.find { item -> item == book }
        libraryBook ?: throw Exception("This book is not in the library") // If passed book is not in library

        if (libraryBook.status == Status.Restoration)
            throw Exception("Book is already on restoration")

        libraryBook.status = Status.Restoration
    }

    override fun availableSoonBooks(): List<Book> = bookList.filter { book -> book.status == Status.ComingSoon || book.status == Status.Restoration }
}