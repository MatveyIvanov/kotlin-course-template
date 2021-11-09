package matrix

import java.lang.ArithmeticException

open class Matrix(protected var matrix: List<DoubleArray>) {

    /*
    * This class represents two-dimensional immutable matrix.
    * Once matrix is initialized cannot be changed, but can be used in other matrix calculations
    */

    var size: Pair<Int, Int>
        protected set // Can be set only from methods of this/child class

    init {
        if (matrix.isEmpty())
            throw IllegalArgumentException("Matrix cannot be empty")
        val firstRowSize = matrix[0].size
        matrix.forEach { row ->
            if (row.size != firstRowSize)
                throw IllegalArgumentException("Matrix rows must have equal size")
        }
        size = Pair(matrix.size, firstRowSize)
    }

    operator fun plus(other: Matrix): Matrix {
        if (size != other.size)
            throw IllegalArgumentException("Matrices must have equal size")
        val newMatrix: List<DoubleArray> = List(size.first) { DoubleArray(size.second) {0.0} }
        for (i in 0 until size.first) {
            for (j in 0 until size.second) {
                newMatrix[i][j] = matrix[i][j] + other[i, j]
            }
        }
        return Matrix(newMatrix)
    }

    operator fun minus(other: Matrix): Matrix {
        if (size != other.size)
            throw IllegalArgumentException("Matrices must have equal size")
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
}