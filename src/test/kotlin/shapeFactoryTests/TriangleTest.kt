package shapeFactoryTests

import org.junit.Assert
import org.junit.Test
import shapeFactory.Triangle
import kotlin.math.sqrt

class TriangleTest {

    @Test
    fun test_triangle_area_calculation() {
        val rectangle = Triangle(2.0, 3.0, 4.0)
        val halfPerimeter = 4.5
        val area = sqrt(halfPerimeter * (halfPerimeter - 2.0) * (halfPerimeter - 3.0) * (halfPerimeter - 4.0))
        Assert.assertEquals(area, rectangle.calcArea(), PRECISION)
    }

    @Test
    fun test_triangle_perimeter_calculation() {
        val triangle = Triangle(5.0, 3.0, 6.0)
        Assert.assertEquals(14.0, triangle.calcPerimeter(), PRECISION)
    }

    @Test
    fun test_triangle_invalid_side_init() {
        try {
            Triangle(-1.0, 1.0, 2.0)
        } catch (e: IllegalArgumentException) {
            Assert.assertEquals("Sides of the triangle must be positive", e.message)
        }
    }

    @Test
    fun test_triangle_cannot_exist_init() {
        try {
            Triangle(1.0, 2.0, 5.0)
        } catch (e: IllegalArgumentException) {
            Assert.assertEquals("Triangle with sides={1.0, 2.0, 5.0} cannot exist", e.message)
        }
    }
}