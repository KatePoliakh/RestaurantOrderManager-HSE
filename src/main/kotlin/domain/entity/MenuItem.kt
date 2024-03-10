package domain.entity

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class MenuItem(
    val name: String,
    var quantity: Int,
    val price: Double,
    val preparationTime: Int
) {
    @Serializable(with = UUIDSerializer::class)
    val id = UUID.randomUUID()
    override fun toString(): String {
        return "name='$name', quantity=$quantity, price=$price, preparationTime=$preparationTime"
    }
}
