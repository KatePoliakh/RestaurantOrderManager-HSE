package domain.service.auth

import domain.entity.Role
import domain.entity.User

interface AuthService {
    fun register(username: String, password: String, role: Role) : Boolean
    fun login(name: String, password: String): User?
    fun hashPassword(password: String, salt: ByteArray): String
    fun ByteArray.toHexString(): String
}