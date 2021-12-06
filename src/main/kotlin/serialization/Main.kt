package serialization

import shapeFactory.*

fun main() {
    val shapeFactory = ShapeFactoryImpl()
    val path = "$DEFAULT_PATH\\src\\main\\kotlin\\serialization"

    val decodedList = ShapeSerializer.decode<MutableList<Shape>>(FileIO.readFromFile("$path\\data.json"))

    println("Deserialized list of shapes from data.json:\n\n$decodedList")

    decodedList.addAll(
        listOf(
            shapeFactory.createRandomShape(),
            shapeFactory.createRandomShape(),
            shapeFactory.createRandomShape()
        )
    )

    println("\n\nUpdated list of shapes:\n\n$decodedList")

    FileIO.writeToFile(ShapeSerializer.encode(decodedList), "$path\\updated_data.json")
}