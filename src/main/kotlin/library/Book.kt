package library

import java.time.Year

enum class Genre {
    DETECTIVE,
    FANTASY,
    HORROR,
    NOVEL
}

class Book(val name: String,
           val authors: List<Author> = arrayListOf(),
           val year: Year? = null,
           val genre: Genre? = null,
           var status: Status = Status.Available) {

    override fun toString(): String {
        var authorsString = ""
        var yearString = ""
        var genreString = ""
        if (authors.isNotEmpty())
            authorsString = authors.toString().replace("[", "").replace("]", "") + ", "
        if (year != null)
            yearString = "$year, "
        if (genre != null)
            genreString = "$genre, "
        return "$name, $authorsString$yearString$genreString$status"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) // Same object
            return true
        if (this.javaClass != other?.javaClass) // Different classes
            return false

        other as Book

        if (name == other.name && authors == other.authors &&
            year == other.year && genre == other.genre && status == other.status) // All fields must be equal
            return true
        return false
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + authors.hashCode()
        result = 31 * result + (year?.hashCode() ?: 0)
        result = 31 * result + (genre?.hashCode() ?: 0)
        result = 31 * result + status.hashCode()
        return result
    }
}