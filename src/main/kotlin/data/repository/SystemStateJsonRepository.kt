package data.repository

import data.dao.SystemStateDao
import domain.entity.SystemState
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class SystemStateJsonRepository(private val filePath: String) : SystemStateDao() {
    override fun getSystemState(): SystemState {
        val file = File(filePath)
        if (!file.exists()) {
            saveSystemState(SystemState())
        }
        val serializedState = file.readText()
        return Json.decodeFromString<SystemState>(serializedState)
    }

    override fun saveSystemState(systemState: SystemState) {
        val serializedState = Json.encodeToString(systemState)
        File(filePath).writeText(serializedState)
    }
}