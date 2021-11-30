package shapeFactoryTests

import org.junit.Assert
import org.junit.Test
import shapeFactory.Circle


const val PRECISION = 0.0001

class CircleTest {
    
    @Test
    fun test_circle_area_calculation() {
        val circle = Circle(2.0)
        val area = Math.PI * 2.0 * 2.0
        Assert.assertEquals(area, circle.calcArea(), PRECISION)
    }

    @Test
    fun test_circle_perimeter_calculation() {
        val circle = Circle(5.0)
        val perimeter = 2 * Math.PI * 5.0
        Assert.assertEquals(perimeter, circle.calcPerimeter(), PRECISION)
    }

    @Test
    fun test_circle_invalid_radius_init() {
        try {
            Circle(-1.0)
        } catch (e: IllegalArgumentException) {
            Assert.assertEquals("Radius of the circle must be positive", e.message)
        }
    }
}