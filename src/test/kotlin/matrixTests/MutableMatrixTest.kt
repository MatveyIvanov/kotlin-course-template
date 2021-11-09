package matrixTests

import matrix.MutableMatrix
import org.junit.Assert
import org.junit.Test
import java.lang.IndexOutOfBoundsException

class MutableMatrixTest {
    private val matrixList = listOf(
        doubleArrayOf(1.0, 2.0, 3.0),
        doubleArrayOf(4.0, 5.0, 6.0),
        doubleArrayOf(7.0, 8.0, 9.0)
    )
    private val extraMatrixList = listOf(
        doubleArrayOf(9.0, 8.0, 7.0),
        doubleArrayOf(6.0, 5.0, 4.0),
        doubleArrayOf(3.0, 2.0, 1.0)
    )

    @Test
    fun test_plus_assign() {
        val matrix = MutableMatrix(matrixList)
        val extraMatrix = MutableMatrix(extraMatrixList)
        matrix += extraMatrix
        Assert.assertEquals("\n10.0 10.0 10.0\n" +
                "10.0 10.0 10.0\n" +
                "10.0 10.0 10.0", matrix.toString())
    }

    @Test
    fun test_plus_assign_different_size_exception() {
        try {
            val matrix = MutableMatrix(matrixList)
            matrix += MutableMatrix(listOf(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0)))
        } catch (e: IllegalArgumentException) {
            Assert.assertEquals("Matrices must have equal size", e.message)
        }
    }

    @Test
    fun test_minus_assign() {
        val matrix = MutableMatrix(matrixList)
        val extraMatrix = MutableMatrix(extraMatrixList)
        matrix -= extraMatrix
        Assert.assertEquals("\n-8.0 -6.0 -4.0\n" +
                "-2.0 0.0 2.0\n" +
                "4.0 6.0 8.0", matrix.toString())
    }

    @Test
    fun test_minus_assign_different_size_exception() {
        try {
            val matrix = MutableMatrix(matrixList)
            matrix -= MutableMatrix(listOf(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0)))
        } catch (e: IllegalArgumentException) {
            Assert.assertEquals("Matrices must have equal size", e.message)
        }
    }

    @Test
    fun test_times_assign() {
        val matrix = MutableMatrix(matrixList)
        val extraMatrix = MutableMatrix(extraMatrixList)
        matrix *= extraMatrix
        Assert.assertEquals("\n30.0 24.0 18.0\n" +
                "84.0 69.0 54.0\n" +
                "138.0 114.0 90.0", matrix.toString())
    }

    @Test
    fun test_times_assign_size() {
        val matrix1 = MutableMatrix(listOf(
            doubleArrayOf(3.0, 5.0, 9.0),
            doubleArrayOf(-1.0, 4.0, 11.0)
        ))
        val matrix2 = MutableMatrix(listOf(
            doubleArrayOf(1.0, -1.0),
            doubleArrayOf(4.0, -2.0),
            doubleArrayOf(-4.0, -7.0)
        ))
        matrix1 *= matrix2
        Assert.assertEquals(Pair(2, 2), matrix1.size)
    }

    @Test
    fun test_times_assign_different_size_exception() {
        try {
            val matrix = MutableMatrix(matrixList)
            matrix *= MutableMatrix(listOf(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0)))
        } catch (e: IllegalArgumentException) {
            Assert.assertEquals("Number of rows in first matrix must be equal to number of columns in second matrix", e.message)
        }
    }

    @Test
    fun test_times_assign_scalar() {
        val matrix = MutableMatrix(matrixList)
        matrix *= 2.0
        Assert.assertEquals("\n2.0 4.0 6.0\n" +
                "8.0 10.0 12.0\n" +
                "14.0 16.0 18.0", matrix.toString())
    }

    @Test
    fun test_div_assign_scalar() {
        val matrix = MutableMatrix(matrixList)
        matrix /= 2.0
        Assert.assertEquals("\n0.5 1.0 1.5\n" +
                "2.0 2.5 3.0\n" +
                "3.5 4.0 4.5", matrix.toString())
    }

    @Test
    fun test_set() {
        val matrix = MutableMatrix(matrixList)
        matrix[1, 1] = 130.0
        Assert.assertEquals(130.0, matrix[1, 1], PRECISION)
    }

    @Test
    fun test_set_wrong_row_number_exception() {
        try {
            val matrix = MutableMatrix(matrixList)
            matrix[4, 0]
        } catch (e: IndexOutOfBoundsException) {
            Assert.assertEquals("Row number is out of range", e.message)
        }
    }

    @Test
    fun test_set_wrong_column_number_exception() {
        try {
            val matrix = MutableMatrix(matrixList)
            matrix[1, 4]
        } catch (e: IndexOutOfBoundsException) {
            Assert.assertEquals("Column number is out of range", e.message)
        }
    }
}