package shapeFactory

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

class Circle(_radius: Double = 1.0): Shape {
    var radius: Double = _radius
        set(value) {
            propertyValidation(value)
            field = value
        }
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

class Square(_a: Double = 1.0): Shape {
    var a: Double = _a
        set(value) {
            propertyValidation(value)
            field = value
        }
    init {
        propertyValidation(a)
    }
    override fun calcArea(): Double = a.pow(2)
    override fun calcPerimeter(): Double = 4 * a
    override fun toString(): String = "Square with side=$a"

    override fun propertyValidation(value: Double) {
        if (value < 0)
            throw IllegalArgumentException("Side of the square must be positive")
    }
}
class Rectangle(_a: Double = 1.0, _b: Double = 1.0): Shape {
    var a: Double = _a
        set(value) {
            propertyValidation(value)
            field = value
        }
    var b: Double = _b
        set(value) {
            propertyValidation(value)
            field = value
        }
    init {
        propertyValidation(a)
        propertyValidation(b)
    }
    override fun calcArea(): Double = a * b
    override fun calcPerimeter(): Double = 2 * (a + b)
    override fun toString(): String = "Rectangle with sides={$a, $b}"

    override fun propertyValidation(value: Double) {
        if (value < 0)
            throw IllegalArgumentException("Sides of the rectangle must be positive")
    }
}

class Triangle(_a: Double = 1.0, _b: Double = 1.0, _c: Double = 1.0): Shape {
    var a: Double = _a
        set(value) {
            propertyValidation(value)
            field = value
            triangleValidation()
        }
    var b: Double = _b
        set(value) {
            propertyValidation(value)
            field = value
            triangleValidation()
        }
    var c: Double = _c
        set(value) {
            propertyValidation(value)
            field = value
            triangleValidation()
        }
    init {
        propertyValidation(a)
        propertyValidation(b)
        propertyValidation(c)
        triangleValidation()
    }
    override fun calcArea(): Double {
        val halfPerimeter = (a + b + c) / 2
        return sqrt(halfPerimeter * (halfPerimeter - a) * (halfPerimeter - b) * (halfPerimeter - c))
    }
    override fun calcPerimeter(): Double = a + b + c
    override fun toString(): String = "Triangle with sides={$a, $b, $c}"

    override fun propertyValidation(value: Double) {
        if (value < 0)
            throw IllegalArgumentException("Sides of the triangle must be positive")
    }

    private fun triangleValidation() {
        /*
        *   Method that checks if triangle can exist.
        *   Triangle cannot exist if any of its sides is equal or greater than sum of other two sides
        */
        if (a >= b + c || b >= a + c || c >= a + b)
            throw IllegalArgumentException("Triangle with sides={$a, $b, $c} cannot exist")
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
    override fun createRandomRectangle(): Rectangle = Rectangle(Random.nextDouble(0.0, sqrt(Double.MAX_VALUE)),
                                                                Random.nextDouble(0.0, sqrt(Double.MAX_VALUE)))
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
