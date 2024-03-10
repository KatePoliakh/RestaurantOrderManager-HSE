package data.repository

//import kotlinx.serialization.json.Json
//import kotlinx.serialization.SerializationException
import data.dao.OrdersDao
import data.dao.SystemStateDao
import data.dao.UserDao
import domain.entity.Order
import domain.entity.OrderStatus
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*


class OrdersJsonRepository(private val ordersJsonRepositoryPath: String, private val systemStateDao: SystemStateDao, private val userDao: UserDao) : JsonRepository<Order>(), OrdersDao {
    private val orders: List<Order> = getAllOrders()
    private var counter = if (orders.isEmpty()) {
        1
    } else {
        orders.last().num + 1
    }
    override fun addOrder(order: Order) {
        val textFromFile = readFileOrCreateEmpty(ordersJsonRepositoryPath)
        val items: List<Order> = if (textFromFile.isBlank()) listOf() else Json.decodeFromString(textFromFile)
        val updatedOrders = items.toMutableList()

        if (order.status == OrderStatus.ACCEPTED) {
            order.num = counter++
        }

        order.preparationTime = order.items.sumOf{ it.preparationTime }

        updatedOrders.add(order)
        val serializedUpdatedStorage = Json.encodeToString(updatedOrders.toList())
        writeTextToFile(ordersJsonRepositoryPath, serializedUpdatedStorage)
    }
    override fun removeOrder(order: Order){
        val items: List<Order> = getAllOrders()
        val updatedItems = items.toMutableList()

        updatedItems.removeIf { oldItem -> oldItem.orderId == order.orderId }
        val serializedUpdatedStorage = Json.encodeToString(updatedItems.toList())
        writeTextToFile(ordersJsonRepositoryPath, serializedUpdatedStorage)
    }
    override fun updateOrder(order: Order) {
        val textFromFile = readFileOrCreateEmpty(ordersJsonRepositoryPath)
        val items: List<Order> = if (textFromFile.isBlank()) listOf() else Json.decodeFromString(textFromFile)
        val updatedOrders = items.toMutableList()

        val existingOrder = updatedOrders.find { it.orderId == order.orderId } ?: return

        if (existingOrder.status != OrderStatus.IN_PROGRESS) {
            println("Cannot add dishes to the order because it is not being processed")
            return
        }

        val temp = existingOrder.items.toMutableList()
        for (item in order.items) {
            temp.add(item)
        }

        existingOrder.items = temp

        val serializedUpdatedStorage = Json.encodeToString(updatedOrders.toList())
        writeTextToFile(ordersJsonRepositoryPath, serializedUpdatedStorage)

    }
    override fun getAllOrders(): List<Order> {
        val textFromFile = readFileOrCreateEmpty(ordersJsonRepositoryPath)

        return if (textFromFile.isBlank())
            listOf() else Json.decodeFromString<List<Order>>(textFromFile)
    }

    override fun getOrderById(orderId: UUID): Order? {
        val items: List<Order> = getAllOrders()
        return items.find { it.orderId == orderId }

    }

    override fun changeOrderStatus(orderId: UUID, newStatus: OrderStatus) {
        val order = getOrderById(orderId)
        if (order != null) {
            order.status = newStatus
            updateOrder(order)
        }
    }

    override fun changeOrderPaidStatus(order: Order) {
        order.paid = true
        updateOrder(order)
    }

    override fun makeReview(orderId: UUID, mark: Int, review: String) {
        val order = getOrderById(orderId)
        if (order != null) {
            order.review = review
            order.reviewMark = mark
            updateOrder(order)
        }
    }

    override fun getOrdersByStatus(status: OrderStatus): List<Order> {
        val items: List<Order> = getAllOrders()
        return items.filter { it.status == status }
    }

    override fun payForOrder(order: Order) {
        val items: List<Order> = getAllOrders()
        val updatedItems = items.toMutableList()
        updatedItems.removeIf { oldItem -> oldItem.orderId == order.orderId }
        updatedItems.add(order)
        val serializedUpdatedStorage = Json.encodeToString(updatedItems.toList())
        writeTextToFile(ordersJsonRepositoryPath, serializedUpdatedStorage)
        val orderCost = calculateOrderCost(order)
        systemStateDao.addRevenue(orderCost)
        changeOrderPaidStatus(order)
    }

    override fun getOrderByNum(orderNum: Int): Order? {
        val items: List<Order> = getAllOrders()
        val userId = userDao.getCurrentUser().id
        return items.find { it.num == orderNum && it.userId == userId }
    }

    private fun calculateOrderCost(order: Order): Double {
        var cost = 0.0
        for (item in order.items) {
            cost += item.price
        }
        return cost
    }


}