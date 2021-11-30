package serializationTests

import org.junit.Assert
import org.junit.Test
import serialization.ShapeSerializer
import shapeFactory.*

class ShapeSerializerTest {

    private val square = Square(2.0)
    private val rectangle = Rectangle(2.0, 4.0)
    private val circle = Circle(2.0)
    private val triangle = Triangle(4.0, 5.0, 6.0)
    private val shapes = listOf(square, rectangle, circle, triangle)

    @Test
    fun test_encode() {
        Assert.assertEquals("""
            {
                "side": 2.0
            }
        """.trimIndent(), ShapeSerializer.encode(square))
        Assert.assertEquals("""
            {
                "side1": 2.0,
                "side2": 4.0
            }
        """.trimIndent(), ShapeSerializer.encode(rectangle))
        Assert.assertEquals("""
            {
                "radius": 2.0
            }
        """.trimIndent(), ShapeSerializer.encode(circle))
        Assert.assertEquals("""
            {
                "side1": 4.0,
                "side2": 5.0,
                "side3": 6.0
            }
        """.trimIndent(), ShapeSerializer.encode(triangle))
    }

    @Test
    fun test_encode_list_of_shapes() {
        Assert.assertEquals("""
            [
                {
                    "type": "shapeFactory.Square",
                    "side": 2.0
                },
                {
                    "type": "shapeFactory.Rectangle",
                    "side1": 2.0,
                    "side2": 4.0
                },
                {
                    "type": "shapeFactory.Circle",
                    "radius": 2.0
                },
                {
                    "type": "shapeFactory.Triangle",
                    "side1": 4.0,
                    "side2": 5.0,
                    "side3": 6.0
                }
            ]
        """.trimIndent(), ShapeSerializer.encode(shapes))
    }

    @Test
    fun test_decode() {
        Assert.assertEquals("Square with side=2.0", ShapeSerializer.decode<Square>(ShapeSerializer.encode(square)).toString())
        Assert.assertEquals("Rectangle with sides={2.0, 4.0}", ShapeSerializer.decode<Rectangle>(ShapeSerializer.encode(rectangle)).toString())
        Assert.assertEquals("Circle with radius=2.0", ShapeSerializer.decode<Circle>(ShapeSerializer.encode(circle)).toString())
        Assert.assertEquals("Triangle with sides={4.0, 5.0, 6.0}", ShapeSerializer.decode<Triangle>(ShapeSerializer.encode(triangle)).toString())
    }

    @Test
    fun test_decode_to_list() {
        Assert.assertEquals("[Square with side=2.0, Rectangle with sides={2.0, 4.0}, Circle with radius=2.0, Triangle with sides={4.0, 5.0, 6.0}]", ShapeSerializer.decode<List<Shape>>(ShapeSerializer.encode(shapes)).toString())
    }
}