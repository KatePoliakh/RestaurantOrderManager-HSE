package domain.service.visitor

import domain.entity.MenuItem
import domain.entity.Order
import domain.entity.OrderStatus
import java.util.*

interface VisitorService {
    fun viewMenu()
    fun placeOrder(userId: UUID, order: Order)
    fun cancelOrder(orderId: Int)
    fun payOrder(orderId: UUID)
    fun viewOrderStatus(orderId: UUID) : OrderStatus
    fun leaveReview(orderId: UUID, mark: Int, review: String)
    fun addItemToOrder(orderId: UUID, menuItem: String)
    //fun generateOrderNum(): Int
    fun getOrderItems(orderId: UUID): List<MenuItem>

}