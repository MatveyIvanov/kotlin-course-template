package serialization

import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Paths

val DEFAULT_PATH = Paths.get("").toAbsolutePath().toString()

object FileIO {
    fun writeToFile(data: String, path: String = "$DEFAULT_PATH\\data.txt") {
        FileWriter(path).buffered().use { writer ->
            writer.write(data)
        }
    }

    fun readFromFile(path: String): String {
        var text: String
        FileReader(path).buffered().use { reader ->
            text = reader.readText()
        }
        return text
    }
}