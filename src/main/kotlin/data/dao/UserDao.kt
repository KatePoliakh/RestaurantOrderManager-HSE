package data.dao

import domain.entity.CurrentUser
import domain.entity.User
import java.util.*

interface UserDao {
    fun saveUser(user: User)
    fun getUserByName(username: String): User?
    fun getAllUsers(): List<User>

    fun setCurrentUser(login : String, id : UUID, hashPass : String, salt : ByteArray)

    fun getCurrentUser() : CurrentUser

}