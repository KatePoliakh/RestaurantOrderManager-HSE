package domain.service.auth

import data.dao.UserDao
import domain.entity.Role
import domain.entity.User
import presentation.model.OutputModel
import presentation.model.OutputStatus
import java.security.MessageDigest
import kotlin.random.Random

class AuthServiceImpl(private val userDao: UserDao) : AuthService {
    override fun register(username: String, password: String, role: Role) : Boolean {
        val salt = ByteArray(16)
        Random.nextBytes(salt)
        val passwordHash = hashPassword(password, salt)
        userDao.saveUser(User(username, passwordHash, salt, role))
        OutputModel("The user has been successfully registered.", OutputStatus.Success)
        return true
    }

    override fun login(name: String, password: String): User? {
        val user = userDao.getUserByName(name)
        if (user != null) {
            val salt = user.salt
            val passwordHash = hashPassword(password, salt)
            if (user.password == passwordHash) {
                return user
            }
        }
        return null
    }
    override fun hashPassword(password: String, salt: ByteArray): String {
        val md = MessageDigest.getInstance("SHA-256")
        md.update(salt)
        val hashedPassword = md.digest(password.toByteArray())
        return salt.toHexString() + ":" + hashedPassword.toHexString()
    }

    override fun ByteArray.toHexString(): String {
        return joinToString("") { "%02x".format(it) }
    }
}