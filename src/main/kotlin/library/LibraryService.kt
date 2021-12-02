package library

import java.time.Year


sealed class Status {

    object Available : Status()
    data class UsedBy(val user: User) : Status() {
        override fun toString(): String = super.toString()
    }

    object ComingSoon : Status()
    object Restoration : Status()

    override fun toString(): String = when (this) {
        is Available -> "Available"
        is UsedBy -> "Used by ${this.user}"
        is ComingSoon -> "Coming soon"
        is Restoration -> "Restoration"
    }
}

interface LibraryServiceInterface {
    /**
     * @return list of books with the given [substring] in the [Book.name]
     */
    fun findBooks(substring: String): List<Book>

    /**
     * @return list of books with the given [author]
     */
    fun findBooks(author: Author): List<Book>

    /**
     * @return list of books with the given [year]
     */
    fun findBooks(year: Year): List<Book>

    /**
     * @return list of books with the given [genre]
     */
    fun findBooks(genre: Genre): List<Book>

    /**
     * @return list of books with the given filters
     */
    fun findBooks(
        substring: String? = null,
        author: Author? = null,
        year: Year? = null,
        genre: Genre? = null
    ): List<Book>

    /**
     * @return list of all books in the library
     */
    fun getAllBooks(): List<Book>

    /**
     * @return list of all available books in the library
     */
    fun getAllAvailableBooks(): List<Book>

    /**
     * @return status of the [book]
     */
    fun getBookStatus(book: Book): Status

    /**
     * @return map with [Book] as key and [Status] as value
     */
    fun getAllBookStatuses(): Map<Book, Status>

    /**
     * Set status of the [book]
     */
    fun setBookStatus(book: Book, status: Status)

    /**
     * Add [book] with [status] to the library
     */
    fun addBook(book: Book, status: Status = Status.Available)

    /**
     * Register [user]
     */
    fun registerUser(user: User)

    /**
     * Unregister user
     */
    fun unregisterUser(user: User)

    /**
     * Take [book] from library and give it to the [user]
     */
    fun takeBook(user: User, book: Book)

    /**
     * Return [book] to the library
     */
    fun returnBook(book: Book)

    /**
     * Send [book] to restoration
     */
    fun bookRestoration(book: Book)

    /**
     * @return list of books that will be available soon
     */
    fun availableSoonBooks(): List<Book>
}

class LibraryService(
    private val books: MutableSet<Book> = mutableSetOf(),
    private val users: MutableSet<User> = mutableSetOf(),
    private val statuses: MutableMap<Book, Status> = mutableMapOf(),
    private val userBooks: MutableMap<User, MutableSet<Book>> = mutableMapOf(),
) : LibraryServiceInterface {
    override fun findBooks(substring: String): List<Book> = books.filter { book -> book.name.contains(substring) }
    override fun findBooks(author: Author): List<Book> = books.filter { book -> book.authors.contains(author) }
    override fun findBooks(year: Year): List<Book> = books.filter { book -> book.year == year }
    override fun findBooks(genre: Genre): List<Book> = books.filter { book -> book.genre == genre }

    override fun findBooks(substring: String?, author: Author?, year: Year?, genre: Genre?): List<Book> {
        // 'filters' list is a list of conditions to filter the book list
        val filters = listOf<(Book) -> Boolean>(
            { substring == null || it.name.contains(substring) },
            { author == null || it.authors.contains(author) },
            { year == null || it.year == year },
            { genre == null || it.genre == genre }
        )
        return books.filter { book -> filters.all { filter -> filter(book) } }
    }

    override fun getAllBooks(): List<Book> = books.toList()
    override fun getAllAvailableBooks(): List<Book> = books.filter { book -> statuses[book] == Status.Available }

    override fun getBookStatus(book: Book): Status {
        val libraryBook = books.find { item -> item.name == book.name }
        libraryBook
            ?: throw NoSuchElementException("This book is not in the library") // If passed book is not found in book list

        return statuses[libraryBook]!!
    }

    override fun getAllBookStatuses(): Map<Book, Status> = books.associateWith { statuses[it]!! }

    override fun setBookStatus(book: Book, status: Status) {
        val libraryBook = books.find { item -> item.name == book.name }
        libraryBook
            ?: throw NoSuchElementException("This book is not in the library") // If passed book is not found in book list

        statuses[libraryBook] = status
    }

    override fun addBook(book: Book, status: Status) {
        val libraryBook = books.find { item -> item == book } // Check if book is already in book list
        if (libraryBook != null)
            throw RuntimeException("This book is already in the library")

        books.add(book)
        statuses[book] = status
    }

    override fun registerUser(user: User) {
        val libraryUser = users.find { item -> item == user } // Check if user is already in user list
        if (libraryUser != null)
            throw RuntimeException("User is already registered")

        users.add(user)
        userBooks[user] = mutableSetOf()
    }

    override fun unregisterUser(user: User) {
        val libraryUser = users.find { item -> item == user }
        libraryUser
            ?: throw NoSuchElementException("This user is not registered") // If passed user is not found in user list

        if (userBooks[libraryUser]!!.size > 0) // Check if user has books to return
            throw RuntimeException(
                "User has books to return:\n${
                    userBooks[libraryUser]!!.toString().replace("[", "").replace("]", "")
                }"
            )

        users.remove(user)
    }

    override fun takeBook(user: User, book: Book) {
        // Book check
        val libraryBook = books.find { item -> item == book }
        libraryBook
            ?: throw NoSuchElementException("This book is not in the library") // If passed book is not found in book list
        if (statuses[libraryBook] != Status.Available) // Not available book cannot be taken
            throw RuntimeException("Book is unavailable. Come back later")

        // User check
        val libraryUser = users.find { item -> item == user }
        libraryUser
            ?: throw NoSuchElementException("This user is not registered") // If passed user is not found in user list
        if (userBooks[libraryUser]!!.contains(libraryBook))
            throw RuntimeException("User already has that book")
        if (userBooks[libraryUser]!!.size == 3) // User cannot have more than 3 books
            throw RuntimeException("User cannot have more than 3 books at the same time")

        statuses[libraryBook] = Status.UsedBy(libraryUser)
        userBooks[libraryUser]!!.add(libraryBook)
    }

    override fun returnBook(book: Book) {
        val libraryBook = books.find { item -> item == book }
        libraryBook
            ?: throw NoSuchElementException("This book is not in the library") // If passed book is not found in book list

        var usedBy: User? = null
        run loop@{
            userBooks.forEach { (user, books) ->
                if (books.contains(libraryBook)) {
                    usedBy = user
                    return@loop // break
                }
            }
        }
        /*
        * Below is an alternative way to get book user, but it may be unsafe/unstable (not sure about this)
        *   'val usedBy = statuses[libraryBook] as Status.UsedBy'
        * So the condition would be:
        *   'item == usedBy.user'
        */
        val bookHolder = users.find { item -> item == usedBy }

        /*
            * There is no check if bookHolder is registered because:
            *   - Only registered user is allowed to take a book
            *   - User is not allowed to unregister until he returns all the books he has taken
            */
        userBooks[bookHolder]!!.remove(libraryBook) // Delete book from user's book set
        statuses[libraryBook] = Status.Available
    }

    override fun bookRestoration(book: Book) {
        val libraryBook = books.find { item -> item == book }
        libraryBook
            ?: throw NoSuchElementException("This book is not in the library") // If passed book is not in library

        if (statuses[libraryBook] == Status.Restoration)
            throw RuntimeException("Book is already on restoration")

        statuses[libraryBook] = Status.Restoration
    }

    override fun availableSoonBooks(): List<Book> =
        books.filter { book -> statuses[book] == Status.ComingSoon || statuses[book] == Status.Restoration }
}