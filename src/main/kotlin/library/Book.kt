package library

import java.time.Year

enum class Genre {
    DETECTIVE,
    FANTASY,
    HORROR,
    NOVEL
}

data class Book(val name: String,
           val authors: List<Author> = arrayListOf(),
           val year: Year? = null,
           val genre: Genre? = null)