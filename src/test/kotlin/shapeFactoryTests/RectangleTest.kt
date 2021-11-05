package shapeFactoryTests

import org.junit.Assert
import org.junit.Test
import shapeFactory.Rectangle

class RectangleTest {

    @Test
    fun test_rectangle_area_calculation() {
        val rectangle = Rectangle(2.0, 3.0)
        Assert.assertEquals(6.0, rectangle.calcArea(), PRECISION)
    }

    @Test
    fun test_rectangle_perimeter_calculation() {
        val rectangle = Rectangle(5.0, 3.0)
        Assert.assertEquals(16.0, rectangle.calcPerimeter(), PRECISION)
    }

    @Test
    fun test_rectangle_invalid_side_init() {
        try {
            Rectangle(-1.0, 1.0)
        } catch (e: IllegalArgumentException) {
            Assert.assertEquals("Sides of the rectangle must be positive", e.message)
        }
    }

    @Test
    fun test_square_invalid_side_set() {
        val rectangle = Rectangle()
        try {
            rectangle.a = -2.0
        }
        catch (e: IllegalArgumentException){
            Assert.assertEquals("Sides of the rectangle must be positive", e.message)
        }
    }
}