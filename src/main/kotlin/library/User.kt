package library

import java.time.LocalDate

class User(val firstName: String, val lastName: String, val birthDate: LocalDate) {
    val books: MutableList<Book> = arrayListOf()

    override fun toString(): String = "$firstName $lastName (user) ${birthDate.dayOfMonth}.${birthDate.monthValue}.${birthDate.year}"

    override fun equals(other: Any?): Boolean {
        if (this === other) // Same object
            return true
        if (this.javaClass != other?.javaClass) // Different classes
            return false

        other as User

        if (firstName == other.firstName && lastName == other.lastName && birthDate == other.birthDate) // All fields must be equal
            return true
        return false
    }

    override fun hashCode(): Int {
        var result = firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        return result
    }
}