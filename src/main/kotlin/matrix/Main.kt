package matrix

fun main() {
    val matrix1 = Matrix(listOf(
        doubleArrayOf(3.0, 5.0, 9.0),
        doubleArrayOf(-1.0, 4.0, 11.0)
    ))
    val matrix2 = Matrix(listOf(
        doubleArrayOf(-3.0, 1.0, 6.0),
        doubleArrayOf(-4.0, -4.0, 15.0)
    ))
    val matrix3 = Matrix(listOf(
        doubleArrayOf(1.0, -1.0),
        doubleArrayOf(4.0, -2.0),
        doubleArrayOf(-4.0, -7.0)
    ))
    println("Matrix (immutable):\n\nInitialized:\nMatrix1:$matrix1\n\n" +
            "Matrix2:$matrix2\n\n" +
            "Matrix3:$matrix3\n\n")
    println("Matrix1 + matrix2:${matrix1 + matrix2}\n\n")
    println("Matrix2 - matrix1:${matrix2 - matrix1}\n\n")
    println("Matrix1 * matrix3:${matrix1 * matrix3}\n\n")
    println("Matrix1 / 2:${matrix1 / 2.0}\n\n")
    println("-Matrix1:${-matrix1}\n")
    println("Matrix1[0][1]:\n${matrix1[0, 1]}\n\n")

    val mutableMatrix1 = MutableMatrix(listOf(
        doubleArrayOf(3.0, 5.0, 9.0),
        doubleArrayOf(-1.0, 4.0, 11.0)
    ))
    val mutableMatrix2 = MutableMatrix(listOf(
        doubleArrayOf(-3.0, 1.0, 6.0),
        doubleArrayOf(-4.0, -4.0, 15.0)
    ))
    val mutableMatrix3 = MutableMatrix(listOf(
        doubleArrayOf(1.0, -1.0),
        doubleArrayOf(4.0, -2.0),
        doubleArrayOf(-4.0, -7.0)
    ))

    println("-".repeat(40))
    println("All matrices has been changed to mutable ones")
    mutableMatrix1 += mutableMatrix2
    println("Matrix1 after addition with matrix2:$mutableMatrix1\n\n")
    mutableMatrix1 -= mutableMatrix2
    println("Matrix1 after subtraction by matrix2:$mutableMatrix1\n\n")
    mutableMatrix1 *= mutableMatrix3
    println("Matrix1 after multiplication by matrix3:$mutableMatrix1\n\n")
    mutableMatrix2 *= 2.0
    println("Matrix2 after multiplication by 2.0:$mutableMatrix2\n\n")
    mutableMatrix2 /= 2.0
    println("Matrix2 after division by 2.0:$mutableMatrix2\n\n")
    mutableMatrix2[1, 0] = 128.0
    println("Matrix2 after changing element[1][0] to 128:$mutableMatrix2\n\n")
}
