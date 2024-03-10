package data.repository

//import domain.entity.Order

//import kotlinx.serialization.json.Json
import data.dao.UserDao
import domain.entity.CurrentUser
import domain.entity.User
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

class UserJsonRepository(private val userJsonRepositoryPath: String) : JsonRepository<User>(), UserDao {

    override fun saveUser(user: User) {
        val textFromFile = readFileOrCreateEmpty(userJsonRepositoryPath)
        val users: List<User> = if (textFromFile.isBlank())
            listOf() else Json.decodeFromString(textFromFile)
        val updatedUsers = users.toMutableList()
        updatedUsers.add(user)
        val serializedUpdatedStorage = Json.encodeToString(updatedUsers.toList())
        writeTextToFile(userJsonRepositoryPath, serializedUpdatedStorage)

    }

    override fun getUserByName(username: String): User? {
        val textFromFile = readFileOrCreateEmpty(userJsonRepositoryPath)
        val users: List<User> = if (textFromFile.isBlank())
            listOf() else Json.decodeFromString(textFromFile)
        return users.find { it.name == username }
    }

    override fun getAllUsers(): List<User> {
        val textFromFile = readFileOrCreateEmpty(userJsonRepositoryPath)

        return if (textFromFile.isBlank())
            listOf() else Json.decodeFromString<List<User>>(textFromFile)
    }

    override fun setCurrentUser(login: String, id: UUID, hashPass: String, salt: ByteArray) {
        CurrentUser.id = id
        CurrentUser.name = login
        CurrentUser.password = hashPass
        CurrentUser.salt = salt
    }

    override fun getCurrentUser(): CurrentUser {
        return CurrentUser
    }

    /* override fun updateUser(username: String, password: String, role: String): Boolean {
         val users = loadFromFile(userJsonRepositoryPath).toMutableList()
         val user = users.find { it.name == username }
         if (user != null) {
             user.password = password
             user.role = role
             saveToFile(users, userJsonRepositoryPath)
             return true
         }
         return false
     }*/
}