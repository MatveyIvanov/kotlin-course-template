import shapeFactory.*

fun main() {
    val factory = ShapeFactoryImpl()
    val figures = listOf(factory.createCircle(2.0),
                         factory.createRandomCircle(),
                         factory.createSquare(3.0),
                         factory.createRandomSquare(),
                         factory.createRectangle(1.0, 4.0),
                         factory.createRandomRectangle(),
                         factory.createTriangle(0.8, 1.2, 1.0),
                         factory.createRandomTriangle()
    )
    var sumArea = 0.0
    var sumPerimeter = 0.0
    var minArea: Shape? = null
    var maxArea: Shape? = null
    var minPerimeter: Shape? = null
    var maxPerimeter: Shape? = null
    println("Figures:")
    figures.forEach { shape ->
        println("${shape.toString()}, area=${shape.calcArea()}, perimeter=${shape.calcPerimeter()}")
        sumArea += shape.calcArea()
        sumPerimeter += shape.calcPerimeter()
        if (minArea == null || shape.calcArea() < minArea!!.calcArea())
            minArea = shape
        if (maxArea == null || shape.calcArea() > maxArea!!.calcArea())
            maxArea = shape
        if (minPerimeter == null || shape.calcPerimeter() < minPerimeter!!.calcPerimeter())
            minPerimeter = shape
        if (maxPerimeter == null || shape.calcPerimeter() > maxPerimeter!!.calcPerimeter())
            maxPerimeter = shape
    }
    println("\n\nTotal area of all figures: $sumArea")
    println("Total perimeter of all figures: $sumPerimeter")
    println("Figure with biggest area=${maxArea!!.calcArea()}: ${maxArea.toString()}")
    println("Figure with smallest area=${minArea!!.calcArea()}: ${minArea.toString()}")
    println("Figure with biggest perimeter=${maxPerimeter!!.calcPerimeter()}: ${maxPerimeter.toString()}")
    println("Figure with smallest perimeter=${minPerimeter!!.calcPerimeter()}: ${minPerimeter.toString()}")
}