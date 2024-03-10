package domain.service.visitor

import data.dao.MenuDao
import data.dao.OrdersDao
import domain.entity.MenuItem
import domain.entity.Order
import domain.entity.OrderStatus
import presentation.model.OutputModel
import java.util.*

class VisitorServiceImpl(private val menuDao: MenuDao,
                         private val orderDao: OrdersDao)
    : VisitorService {
    override fun viewMenu() {
        println(OutputModel("===Midnight restaurant menu==="))
        val menuItems = menuDao.getAllItems()
        if (menuItems.isEmpty()) {
            println(OutputModel("Menu is empty! Restaurant is not available. Please come back later."))
            return
        }
        for (menuItem in menuItems) {
            println(menuItem)
        }

    }
    override fun cancelOrder(orderId: Int) {
        TODO("Not yet implemented")
    }

    override fun placeOrder(userId: UUID, order: Order) {
        val order = Order(order.items, userId)
        orderDao.addOrder(order)
    }


    override fun payOrder(orderId: UUID) {
        val order = orderDao.getOrderById(orderId)
        if (order != null) {
            orderDao.payForOrder(order)
        }
    }
    override fun viewOrderStatus(orderId: UUID) :OrderStatus {
        val order = orderDao.getOrderById(orderId)
        return order?.status ?: OrderStatus.NOT_FOUND
    }

    override fun leaveReview(orderId: UUID, mark: Int, review: String) {
        orderDao.makeReview(orderId, mark, review)
    }

    override fun addItemToOrder(orderId: UUID, menuItem: String) {
        val order = orderDao.getOrderById(orderId)
        if (order != null) {
            val item = menuDao.getMenuItemByName(menuItem)
            if (item != null) {
                val updatedOrder = Order(order.items + item, order.orderId)
                orderDao.updateOrder(updatedOrder)
            }
        }
    }

    /*override fun generateOrderNum(): Int {
        return (Math.random() * 100000).toInt()
    }*/

    override fun getOrderItems(orderId: UUID): List<MenuItem> {
        val order = orderDao.getOrderById(orderId)
        return order?.items ?: emptyList()
    }

}