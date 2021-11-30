package shapeCollector

import shapeFactory.Circle
import shapeFactory.Shape

object ShapeComparators {
    val byAreaAsc = compareBy<Shape> { it.calcArea() }
    val byAreaDesc = compareByDescending<Shape> { it.calcArea() }
    val byPerimeterAsc = compareBy<Shape> { it.calcPerimeter() }
    val byPerimeterDesc = compareByDescending<Shape> { it.calcPerimeter() }
    val byRadiusAsc = compareBy<Circle> { it.radius }
    val byRadiusDesc = compareByDescending<Circle> { it.radius }
}
