package matrixTests

import matrix.Matrix
import org.junit.Assert
import org.junit.Test
import java.lang.ArithmeticException
import java.lang.IndexOutOfBoundsException

const val PRECISION = 0.00001

class MatrixTest {
    private val matrix = Matrix(
        listOf(
            doubleArrayOf(1.0, 2.0, 3.0),
            doubleArrayOf(4.0, 5.0, 6.0),
            doubleArrayOf(7.0, 8.0, 9.0)
        )
    )
    private val extraMatrix = Matrix(
        listOf(
            doubleArrayOf(9.0, 8.0, 7.0),
            doubleArrayOf(6.0, 5.0, 4.0),
            doubleArrayOf(3.0, 2.0, 1.0)
        )
    )

    @Test
    fun test_init_by_string_representation() {
        Assert.assertEquals(
            "\n1.0 2.0 3.0\n" +
                    "4.0 5.0 6.0\n" +
                    "7.0 8.0 9.0", matrix.toString()
        )
    }

    @Test
    fun test_init_by_size() {
        Assert.assertEquals(Pair(3, 3), matrix.size)
    }

    @Test
    fun test_init_empty_matrix_exception() {
        try {
            Matrix(emptyList())
        } catch (e: IllegalArgumentException) {
            Assert.assertEquals("Matrix cannot be empty", e.message)
        }
    }

    @Test
    fun test_init_different_row_size_exception() {
        try {
            Matrix(listOf(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0, 5.0)))
        } catch (e: IllegalArgumentException) {
            Assert.assertEquals("Matrix rows must have equal size", e.message)
        }
    }

    @Test
    fun test_plus() {
        Assert.assertEquals(
            "\n10.0 10.0 10.0\n" +
                    "10.0 10.0 10.0\n" +
                    "10.0 10.0 10.0", (matrix + extraMatrix).toString()
        )
    }

    @Test
    fun test_plus_by_reference() {
        Assert.assertNotEquals(matrix, matrix + extraMatrix)
    }

    @Test
    fun test_plus_different_size_exception() {
        try {
            Matrix(listOf(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0))) + matrix
        } catch (e: IllegalArgumentException) {
            Assert.assertEquals("Matrices must have equal size", e.message)
        }
    }

    @Test
    fun test_minus() {
        Assert.assertEquals(
            "\n-8.0 -6.0 -4.0\n" +
                    "-2.0 0.0 2.0\n" +
                    "4.0 6.0 8.0", (matrix - extraMatrix).toString()
        )
    }

    @Test
    fun test_minus_by_reference() {
        Assert.assertNotEquals(matrix, matrix - extraMatrix)
    }

    @Test
    fun test_minus_different_size_exception() {
        try {
            Matrix(listOf(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0))) - matrix
        } catch (e: IllegalArgumentException) {
            Assert.assertEquals("Matrices must have equal size", e.message)
        }
    }

    @Test
    fun test_times() {
        Assert.assertEquals(
            "\n30.0 24.0 18.0\n" +
                    "84.0 69.0 54.0\n" +
                    "138.0 114.0 90.0", (matrix * extraMatrix).toString()
        )
    }

    @Test
    fun test_times_by_reference() {
        Assert.assertNotEquals(matrix, matrix * extraMatrix)
    }

    @Test
    fun test_times_wrong_size_exception() {
        try {
            Matrix(listOf(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0))) * matrix
        } catch (e: IllegalArgumentException) {
            Assert.assertEquals(
                "Number of columns in first matrix must be equal to number of rows in second matrix",
                e.message
            )
        }
    }

    @Test
    fun test_times_scalar() {
        Assert.assertEquals(
            "\n2.0 4.0 6.0\n" +
                    "8.0 10.0 12.0\n" +
                    "14.0 16.0 18.0", (matrix * 2.0).toString()
        )
    }

    @Test
    fun test_times_scalar_by_reference() {
        val newMatrix = matrix * 2.0
        Assert.assertNotEquals(matrix, newMatrix)
    }

    @Test
    fun test_div_scalar() {
        Assert.assertEquals(
            "\n0.5 1.0 1.5\n" +
                    "2.0 2.5 3.0\n" +
                    "3.5 4.0 4.5", (matrix / 2.0).toString()
        )
    }

    @Test
    fun test_div_scalar_by_reference() {
        val newMatrix = matrix / 2.0
        Assert.assertNotEquals(matrix, newMatrix)
    }

    @Test
    fun test_div_scalar_division_by_zero_exception() {
        try {
            matrix / 0.0
        } catch (e: ArithmeticException) {
            Assert.assertEquals("Division by zero is not allowed", e.message)
        }
    }

    @Test
    fun test_get() {
        Assert.assertEquals(4.0, matrix[1, 0], PRECISION)
    }

    @Test
    fun test_get_wrong_row_number_exception() {
        try {
            matrix[4, 0]
        } catch (e: IndexOutOfBoundsException) {
            Assert.assertEquals("Row number is out of range", e.message)
        }
    }

    @Test
    fun test_get_wrong_column_number_exception() {
        try {
            matrix[1, 4]
        } catch (e: IndexOutOfBoundsException) {
            Assert.assertEquals("Column number is out of range", e.message)
        }
    }

    @Test
    fun test_equals_same_object() {
        Assert.assertTrue(matrix == matrix)
    }

    @Test
    fun test_equals_to_other_class() {
        Assert.assertFalse(matrix.equals(doubleArrayOf()))
    }

    @Test
    fun test_unary_minus() {
        Assert.assertEquals(
            "\n-1.0 -2.0 -3.0\n" +
                    "-4.0 -5.0 -6.0\n" +
                    "-7.0 -8.0 -9.0", (-matrix).toString()
        )
    }

    @Test
    fun test_unary_minus_by_reference() {
        Assert.assertFalse(matrix == -matrix)
    }

    @Test
    fun test_unary_plus() {
        Assert.assertEquals(
            "\n1.0 2.0 3.0\n" +
                    "4.0 5.0 6.0\n" +
                    "7.0 8.0 9.0", (+matrix).toString()
        )
    }

    @Test
    fun test_equals() {
        Assert.assertTrue(
            matrix == Matrix(
                listOf(
                    doubleArrayOf(1.0, 2.0, 3.0),
                    doubleArrayOf(4.0, 5.0, 6.0),
                    doubleArrayOf(7.0, 8.0, 9.0)
                )
            )
        )
    }

    @Test
    fun test_to_string() {
        Assert.assertEquals(
            "\n1.0 2.0 3.0\n" +
                    "4.0 5.0 6.0\n" +
                    "7.0 8.0 9.0", matrix.toString()
        )
    }
}