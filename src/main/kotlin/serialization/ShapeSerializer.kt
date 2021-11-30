package serialization

import kotlinx.serialization.*
import shapeFactory.*
import kotlinx.serialization.modules.*
import kotlinx.serialization.json.Json


object ShapeSerializer {
    val json = Json {
        prettyPrint = true

        serializersModule = SerializersModule {
            polymorphic(Shape::class) {
                subclass(Square::class)
                subclass(Rectangle::class)
                subclass(Circle::class)
                subclass(Triangle::class)
            }
        }
    }

    inline fun <reified T> encode(value: T) : String = json.encodeToString(value)

    inline fun <reified T> decode(string: String) : T = json.decodeFromString(string)
}