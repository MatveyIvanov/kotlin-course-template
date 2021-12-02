package shapeCollector

import shapeFactory.*

fun main() {
    /*
     *   Examples with ShapeCollector<Shape>
     */
    val shapeCollector = ShapeCollector<Shape>()

    shapeCollector.add(Square(2.0))
    shapeCollector.add(Rectangle(1.0, 4.0))
    shapeCollector.addAll(listOf(Circle(5.0), Triangle(2.0, 2.0, 2.0)))

    println("Shape collector:\n" + shapeCollector.getAll())

    println("\nSorted by area ascending:\n")
    shapeCollector.getAllSorted(ShapeComparators.byAreaAsc).forEach { shape -> println(shape.calcArea()) }

    println("\nSorted by area descending:\n")
    shapeCollector.getAllSorted(ShapeComparators.byAreaDesc).forEach { shape -> println(shape.calcArea()) }

    println("\nSorted by perimeter ascending:\n")
    shapeCollector.getAllSorted(ShapeComparators.byPerimeterAsc).forEach { shape -> println(shape.calcPerimeter()) }

    println("\nSorted by perimeter descending:\n")
    shapeCollector.getAllSorted(ShapeComparators.byPerimeterDesc).forEach { shape -> println(shape.calcPerimeter()) }

    println("\nShapes with class Rectangle:\n${shapeCollector.getAllByClass(Rectangle::class.java)}")

    /*
     *   Examples with ShapeCollector<Circle>
     */
    val circleCollector = ShapeCollector<Circle>()

    circleCollector.add(Circle(3.0))
    circleCollector.addAll(listOf(Circle(4.0), Circle(6.0)))

    println("\n\nCircle collector:\n" + circleCollector.getAll())

    println("\nSorted by area ascending:\n")
    circleCollector.getAllSorted(ShapeComparators.byAreaAsc).forEach { shape -> println(shape.calcArea()) }

    println("\nSorted by area descending:\n")
    circleCollector.getAllSorted(ShapeComparators.byAreaDesc).forEach { shape -> println(shape.calcArea()) }

    println("\nSorted by perimeter ascending:\n")
    circleCollector.getAllSorted(ShapeComparators.byPerimeterAsc).forEach { shape -> println(shape.calcPerimeter()) }

    println("\nSorted by perimeter descending:\n")
    circleCollector.getAllSorted(ShapeComparators.byPerimeterDesc).forEach { shape -> println(shape.calcPerimeter()) }

    println("\nSorted by radius ascending:\n")
    circleCollector.getAllSorted(ShapeComparators.byRadiusAsc).forEach { shape -> println(shape.radius) }

    println("\nSorted by radius descending:\n")
    circleCollector.getAllSorted(ShapeComparators.byRadiusDesc).forEach { shape -> println(shape.radius) }
}

