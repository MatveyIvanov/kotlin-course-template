package libraryTests

import library.*
import org.junit.Assert
import org.junit.Test
import java.time.LocalDate

class UserTest {
    private val user1 = User("Matvey", "Ivanov", LocalDate.of(2001, 3, 25))
    private val user2 = User("Vasiliy", "Petrov", LocalDate.now())

    @Test
    fun test_init() {
        Assert.assertEquals("Matvey Ivanov (user) 25.3.2001", user1.toString())
    }

    @Test
    fun test_equals() {
        val copy = User("Matvey", "Ivanov", LocalDate.of(2001, 3, 25))
        Assert.assertTrue(user1 == copy)
        Assert.assertFalse(user1 == user2)
    }
}