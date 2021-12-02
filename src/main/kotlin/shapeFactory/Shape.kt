package shapeFactory

import kotlinx.serialization.Serializable
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.random.nextInt

interface Shape {
    fun calcArea(): Double // Area calculation
    fun calcPerimeter(): Double // Perimeter calculation
    fun propertyValidation(value: Double)
}

@Serializable
class Circle(val radius: Double = 1.0) : Shape {

    init {
        propertyValidation(radius)
    }

    override fun calcArea(): Double = Math.PI * radius.pow(2)
    override fun calcPerimeter(): Double = 2 * Math.PI * radius
    override fun toString(): String = "Circle with radius=$radius"

    override fun propertyValidation(value: Double) {
        if (value < 0)
            throw IllegalArgumentException("Radius of the circle must be positive")
    }
}

@Serializable
class Square(val side: Double = 1.0) : Shape {

    init {
        propertyValidation(side)
    }

    override fun calcArea(): Double = side.pow(2)
    override fun calcPerimeter(): Double = 4 * side
    override fun toString(): String = "Square with side=$side"

    override fun propertyValidation(value: Double) {
        if (value < 0)
            throw IllegalArgumentException("Side of the square must be positive")
    }
}

@Serializable
class Rectangle(val side1: Double = 1.0, val side2: Double = 1.0) : Shape {

    init {
        propertyValidation(side1)
        propertyValidation(side2)
    }

    override fun calcArea(): Double = side1 * side2
    override fun calcPerimeter(): Double = 2 * (side1 + side2)
    override fun toString(): String = "Rectangle with sides={$side1, $side2}"

    override fun propertyValidation(value: Double) {
        if (value < 0)
            throw IllegalArgumentException("Sides of the rectangle must be positive")
    }
}

@Serializable
class Triangle(val side1: Double = 1.0, val side2: Double = 1.0, val side3: Double = 1.0) : Shape {

    init {
        propertyValidation(side1)
        propertyValidation(side2)
        propertyValidation(side3)
        triangleValidation()
    }

    override fun calcArea(): Double {
        val halfPerimeter = (side1 + side2 + side3) / 2
        return sqrt(halfPerimeter * (halfPerimeter - side1) * (halfPerimeter - side2) * (halfPerimeter - side3))
    }

    override fun calcPerimeter(): Double = side1 + side2 + side3
    override fun toString(): String = "Triangle with sides={$side1, $side2, $side3}"

    override fun propertyValidation(value: Double) {
        if (value < 0)
            throw IllegalArgumentException("Sides of the triangle must be positive")
    }

    private fun triangleValidation() {
        /*
        *   Method that checks if triangle can exist.
        *   Triangle cannot exist if any of its sides is equal or greater than sum of other two sides
        */
        if (side1 >= side2 + side3 || side2 >= side1 + side3 || side3 >= side1 + side2)
            throw IllegalArgumentException("Triangle with sides={$side1, $side2, $side3} cannot exist")
    }
}

interface ShapeFactory {
    fun createCircle(radius: Double): Circle
    fun createSquare(a: Double): Square
    fun createRectangle(a: Double, b: Double): Rectangle
    fun createTriangle(a: Double, b: Double, c: Double): Triangle

    fun createRandomCircle(): Circle
    fun createRandomSquare(): Square
    fun createRandomRectangle(): Rectangle
    fun createRandomTriangle(): Triangle

    fun createRandomShape(): Shape
}

class ShapeFactoryImpl : ShapeFactory {
    override fun createCircle(radius: Double): Circle = Circle(radius)
    override fun createSquare(a: Double): Square = Square(a)
    override fun createRectangle(a: Double, b: Double): Rectangle = Rectangle(a, b)
    override fun createTriangle(a: Double, b: Double, c: Double): Triangle = Triangle(a, b, c)

    /*
    *   All methods for creation a random shape use square root of the Double.MAX_VALUE
    *   to prevent situations, where side(s)/radius is too big so the calculation of area
    *   or perimeter causes the result equal to infinity.
    */
    override fun createRandomCircle(): Circle = Circle(Random.nextDouble(0.0, sqrt(Double.MAX_VALUE) / Math.PI))
    override fun createRandomSquare(): Square = Square(Random.nextDouble(0.0, sqrt(Double.MAX_VALUE)))
    override fun createRandomRectangle(): Rectangle = Rectangle(
        Random.nextDouble(0.0, sqrt(Double.MAX_VALUE)),
        Random.nextDouble(0.0, sqrt(Double.MAX_VALUE))
    )

    override fun createRandomTriangle(): Triangle {
        val a = Random.nextDouble(0.0, sqrt(Double.MAX_VALUE))
        val b = Random.nextDouble(0.0, sqrt(Double.MAX_VALUE))
        /*
        *   The last side must be bigger than abs(a-b) and less than (a + b)
        *   so the triangle can exist
        */
        val c = Random.nextDouble(abs(a - b) + 0.0001, a + b - 0.0001)
        return Triangle(a, b, c)
    }

    override fun createRandomShape(): Shape {
        return when (Random.nextInt(1..4)) {
            1 -> createRandomCircle()
            2 -> createRandomSquare()
            3 -> createRandomRectangle()
            else -> createRandomTriangle()
        }
    }
}
