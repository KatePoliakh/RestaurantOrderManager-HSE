package data.repository

import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileNotFoundException

abstract class JsonRepository<T : Any> {
    private val json = Json { prettyPrint = true }

    fun serialize(data: List<T>): String {
        return try {
            Json.encodeToString(data)
        } catch (e: SerializationException) {
            throw SerializationException("Serialization error: ${e.message}")
        }
    }

    fun deserialize(data: String): List<T> {
        return try {
            Json.decodeFromString<List<T>>(data)
        } catch (e: SerializationException) {
            throw SerializationException("Deserialization error: ${e.message}")
        }
    }

    fun saveToFile(data: List<T>, path: String) {
        val file = File(path)
        val jsonData = serialize(data)
        file.writeText(jsonData)
    }

    fun loadFromFile(path: String): List<T> {
        val file = File(path)
        return try {
            val data = file.readText()
            if (data.isBlank()) {
                emptyList()
            } else {
                deserialize(data)
            }
        } catch (exception: FileNotFoundException) {
            file.createNewFile()
            emptyList()
        }
    }

    fun readFileOrCreateEmpty(filePath: String): String {
        val file = File(filePath)
        return try {
            file.readText()
        } catch (exception: FileNotFoundException) {
            file.createNewFile()
            ""
        }
    }

    fun writeTextToFile(filePath: String, text: String) {
        val file = File(filePath)
        file.writeText(text)
    }
}