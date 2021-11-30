package serialization

import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Paths

val DEFAULT_PATH = Paths.get("").toAbsolutePath().toString()

object FileIO {
    fun writeToFile(data: String, path: String = "$DEFAULT_PATH\\data.txt") : Boolean {
        return try {
            FileWriter(path).buffered().use { writer ->
                writer.write(data)
            }
            true
        } catch (e: IOException) {
            false
        }
    }

    fun readFromFile(path: String) : String? {
        return try {
            val text: String?
            FileReader(path).buffered().use { reader ->
                text = reader.readText()
            }
            text
        } catch (e: IOException) {
            null
        }
    }
}