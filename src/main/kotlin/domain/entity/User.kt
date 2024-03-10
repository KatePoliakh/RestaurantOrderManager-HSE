package domain.entity
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class User (
    val name: String,
    val password: String,
    val salt: ByteArray,
    val role: Role
) {
    @Serializable(with = UUIDSerializer::class)
    val userId = UUID.randomUUID()
}