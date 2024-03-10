package domain.service.order

import domain.entity.MenuItem
import domain.entity.Order
import java.util.*

interface OrderProcessingService {
    fun createOrder(userId: UUID, items: List<MenuItem>) : Int
    fun cancelOrder(order: Order)
    suspend fun processOrder(order: Order)
    fun payForOrder(order: Order)
    fun addItemsToOrder(orderId: UUID, items: List<MenuItem>)
    fun makeReview(order: Order, mark: Int, review: String)
}