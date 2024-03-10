package domain.entity

import java.util.*

object CurrentUser {
    lateinit var id: UUID
    lateinit var name: String
    lateinit var password: String
    lateinit var salt: ByteArray
    lateinit var role: Role
}