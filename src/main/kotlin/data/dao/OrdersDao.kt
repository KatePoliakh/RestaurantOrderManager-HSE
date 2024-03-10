package data.dao

import domain.entity.Order
import domain.entity.OrderStatus
import java.util.*

interface OrdersDao {
    fun addOrder(order: Order)
    fun removeOrder(order: Order)
    fun updateOrder(order: Order)
    fun getAllOrders(): List<Order>
    fun getOrderById(orderId: UUID): Order?
    fun changeOrderStatus(orderId: UUID, newStatus: OrderStatus)
    fun changeOrderPaidStatus(order: Order)
    fun makeReview(orderId: UUID, mark: Int, review: String)
    fun getOrdersByStatus(status: OrderStatus): List<Order>
    fun payForOrder(order: Order)
    fun getOrderByNum(orderNum: Int): Order?


}
