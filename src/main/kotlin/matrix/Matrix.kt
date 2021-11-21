package matrix

import java.lang.ArithmeticException

open class Matrix(m: List<DoubleArray>) {
    protected var matrix = listOf(doubleArrayOf())
    /*
    * This class represents two-dimensional immutable matrix.
    * Once matrix is initialized cannot be changed, but can be used in other matrix calculations
    */

    val size: Pair<Int, Int>
        get() {
            return Pair(matrix.size, matrix[0].size)
        }

    init {
        if (m.isEmpty())
            throw IllegalArgumentException("Matrix cannot be empty")
        val firstRowSize = m[0].size
        m.forEach { row ->
            if (row.size != firstRowSize)
                throw IllegalArgumentException("Matrix rows must have equal size")
        }
        matrix = m.toList() // toList() is used to create a new object of matrix
    }

    operator fun plus(other: Matrix): Matrix {
        sizeEqualTo(other.size) // If sizes are not equal then exception is called

        val newMatrix: List<DoubleArray> = List(size.first) { DoubleArray(size.second) {0.0} }
        for (i in 0 until size.first) {
            for (j in 0 until size.second) {
                newMatrix[i][j] = matrix[i][j] + other[i, j]
            }
        }
        return Matrix(newMatrix)
    }

    operator fun minus(other: Matrix): Matrix {
        sizeEqualTo(other.size) // If sizes are not equal then exception is called

        val newMatrix: List<DoubleArray> = List(size.first) { DoubleArray(size.second) {0.0} }
        for (i in 0 until size.first) {
            for (j in 0 until size.second) {
                newMatrix[i][j] = matrix[i][j] - other[i, j]
            }
        }
        return Matrix(newMatrix)
    }

    operator fun times(other: Matrix): Matrix {
        if (size.second != other.size.first)
            throw IllegalArgumentException("Number of columns in first matrix must be equal to number of rows in second matrix")
        val newMatrix: List<DoubleArray> = List(size.first) { DoubleArray(other.size.second) {0.0} }
        for (i in 0 until size.first) {
            for (j in 0 until other.size.second) {
                for (k in 0 until size.second) {
                    newMatrix[i][j] += matrix[i][k] * other[k, j]
                }
            }
        }
        return Matrix(newMatrix)
    }

    operator fun times(scalar: Double): Matrix {
        val newMatrix: List<DoubleArray> = List(size.first) { DoubleArray(size.second) {0.0} }
        for (i in 0 until size.first) {
            for (j in 0 until size.second) {
                newMatrix[i][j] = matrix[i][j] * scalar
            }
        }
        return Matrix(newMatrix)
    }

    operator fun div(scalar: Double): Matrix {
        if (scalar == 0.0)
            throw ArithmeticException("Division by zero is not allowed")
        val newMatrix: List<DoubleArray> = List(size.first) { DoubleArray(size.second) {0.0} }
        for (i in 0 until size.first) {
            for (j in 0 until size.second) {
                newMatrix[i][j] = matrix[i][j] / scalar
            }
        }
        return Matrix(newMatrix)
    }

    operator fun get(i: Int, j: Int): Double {
        if (i >= size.first)
            throw IndexOutOfBoundsException("Row number is out of range")
        if (j >= size.second)
            throw IndexOutOfBoundsException("Column number is out of range")
        return matrix[i][j]
    }

    operator fun unaryMinus(): Matrix {
        val newMatrix: List<DoubleArray> = List(size.first) { DoubleArray(size.second) {0.0} }
        for(i in 0 until size.first) {
            for (j in 0 until size.second) {
                newMatrix[i][j] = -matrix[i][j]
            }
        }
        return Matrix(newMatrix)
    }

    operator fun unaryPlus(): Matrix = this

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (other?.javaClass != javaClass)
            return false

        other as Matrix

        if (size != other.size)
            return false
        for (i in 0 until size.first) {
            for (j in 0 until size.second) {
                if (matrix[i][j] != other[i, j])
                    return false
            }
        }
        return true
    }

    override fun toString(): String {
        var representation = ""
        for (i in 0 until size.first) {
            representation += "\n"
            for (j in 0 until size.second) {
                representation += if (j == size.second - 1) "${matrix[i][j]}" else "${matrix[i][j]} "
            }
        }
        return representation
    }

    override fun hashCode(): Int {
        return matrix.hashCode()
    }

    protected fun sizeEqualTo(s: Pair<Int, Int>): Boolean {
        if (size != s)
            throw IllegalArgumentException("Matrices must have equal size")
        return true
    }
}