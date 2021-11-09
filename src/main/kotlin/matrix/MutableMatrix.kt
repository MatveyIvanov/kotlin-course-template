package matrix

import java.lang.ArithmeticException

class MutableMatrix(matrix: List<DoubleArray>): Matrix(matrix) {

    /*
    *  This class represents two-dimensional mutable matrix.
    *  It inherits all methods of immutable matrix, but also has methods for changing matrix
    */

    operator fun plusAssign(other: Matrix) {
        if (size != other.size)
            throw IllegalArgumentException("Matrices must have equal size")
        for (i in 0 until size.first) {
            for (j in 0 until size.second) {
                matrix[i][j] += other[i, j]
            }
        }
    }

    operator fun minusAssign(other: Matrix) {
        if (size != other.size)
            throw IllegalArgumentException("Matrices must have equal size")
        for (i in 0 until size.first) {
            for (j in 0 until size.second) {
                matrix[i][j] -= other[i, j]
            }
        }
    }

    operator fun timesAssign(other: Matrix) {
        if (size.first != other.size.second)
            throw IllegalArgumentException("Number of rows in first matrix must be equal to number of columns in second matrix")
        val newMatrix: List<DoubleArray> = List(size.first) { DoubleArray(other.size.second) {0.0} }
        for (i in 0 until size.first) {
            for (j in 0 until other.size.second) {
                for (k in 0 until size.second) {
                    newMatrix[i][j] += matrix[i][k] * other[k, j]
                }
            }
        }

        size = Pair(newMatrix.size, newMatrix[0].size)
        matrix = newMatrix
    }

    operator fun timesAssign(scalar: Double) {
        for (i in 0 until size.first) {
            for (j in 0 until size.second) {
                matrix[i][j] *= scalar
            }
        }
    }

    operator  fun divAssign(scalar: Double) {
        if (scalar == 0.0)
            throw ArithmeticException("Division by zero is not allowed")
        for (i in 0 until size.first) {
            for (j in 0 until size.second) {
                matrix[i][j] /= scalar
            }
        }
    }

    operator fun set(i: Int, j: Int, value: Double) {
        if (i >= size.first)
            throw IndexOutOfBoundsException("Row number is out of range")
        if (j >= size.second)
            throw IndexOutOfBoundsException("Column number is out of range")
        matrix[i][j] = value
    }
}