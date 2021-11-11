package library

class Author(val firstName: String, val lastName: String) {

    override fun toString(): String = "$firstName $lastName (author)"

    override fun equals(other: Any?): Boolean {
        if (this === other) // Same object
            return true
        if (this.javaClass != other?.javaClass) // Different classes
            return false

        other as Author
        if (firstName == other.firstName && lastName == other.lastName) // All fields must be equal
            return true
        return false
    }

    override fun hashCode(): Int {
        var result = firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        return result
    }
}