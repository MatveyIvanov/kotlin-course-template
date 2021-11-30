package shapeFactoryTests

import org.junit.Assert
import org.junit.Test
import shapeFactory.Square

class SquareTest {

    @Test
    fun test_square_area_calculation() {
        val square = Square(2.0)
        Assert.assertEquals(4.0, square.calcArea(), PRECISION)
    }

    @Test
    fun test_square_perimeter_calculation() {
        val square = Square(5.0)
        Assert.assertEquals(20.0, square.calcPerimeter(), PRECISION)
    }

    @Test
    fun test_square_invalid_side_init() {
        try {
            Square(-1.0)
        } catch (e: IllegalArgumentException) {
            Assert.assertEquals("Side of the square must be positive", e.message)
        }
    }
}