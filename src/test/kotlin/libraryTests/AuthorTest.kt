package libraryTests

import library.*
import org.junit.Assert
import org.junit.Test

class AuthorTest {

    private val author1 = Author("Joanne", "Rowling")
    private val author2 = Author("Andrzej", "Sapkowski")

    @Test
    fun test_init() {
        Assert.assertEquals("Joanne Rowling (author)", author1.toString())
    }

    @Test
    fun test_equals() {
        val copy = Author("Joanne", "Rowling")
        Assert.assertTrue(author1 == copy)
        Assert.assertFalse(author1 == author2)
    }
}