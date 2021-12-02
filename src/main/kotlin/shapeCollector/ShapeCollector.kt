package shapeCollector

import shapeFactory.*

class ShapeCollector<T : Shape> {
    private val allShapes = mutableListOf<T>()

    fun add(new: T) {
        allShapes.add(new)
    }

    fun addAll(new: Collection<T>) {
        allShapes.addAll(new)
    }

    fun getAll(): List<T> = allShapes

    fun getAllSorted(comparator: Comparator<in T>): List<T> = allShapes.sortedWith(comparator)

    fun getAllByClass(classname: Class<out T>): List<T> = allShapes.filter { shape -> shape::class.java == classname }
}
