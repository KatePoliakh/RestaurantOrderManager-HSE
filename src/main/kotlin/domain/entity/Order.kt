package domain.entity

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class Order(
    var items: List<MenuItem>,
    @Serializable(with = UUIDSerializer::class)
    val userId : UUID,
    //calculate total price
    //calculate total cooking time
) {
    @Serializable(with = UUIDSerializer::class)
    val orderId = UUID.randomUUID()
    var preparationTime : Int = 0
    var status = OrderStatus.ACCEPTED
    var paid = false
    var reviewMark : Int = 0
    var review : String = ""
    var num : Int = 0
}