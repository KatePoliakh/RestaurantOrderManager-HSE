package domain.service.order

import data.dao.MenuDao
import data.dao.OrdersDao
import data.dao.UserDao
import domain.entity.MenuItem
import domain.entity.Order
import domain.entity.OrderStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.*

class OrderProcessingServiceImpl(
    private val ordersDao: OrdersDao,
    private val userDao: UserDao,
    private val menuDao: MenuDao,

    ) : OrderProcessingService {
    override fun createOrder(userId: UUID, items: List<MenuItem>): Int {
        val users = userDao.getAllUsers()
        users.find { it.userId == userId } ?: return 0
        val order = Order(items, userId)
        ordersDao.addOrder(order)
        return order.num
    }

    override fun cancelOrder(order: Order) {
        if (order.status != OrderStatus.COMPLETED) {
            ordersDao.changeOrderStatus(order.orderId, OrderStatus.CANCELLED)
            menuDao.increaseCountOfItems(order.items)
        }
    }
    private val mutex = Mutex()
    private val coroutineScope = CoroutineScope(Dispatchers.Default)


    override suspend fun processOrder(order: Order) {
        coroutineScope.launch {
            mutex.withLock {
                ordersDao.changeOrderStatus(order.orderId, OrderStatus.IN_PROGRESS)
                delay(order.preparationTime * 1000L)

                val orderUpdated = ordersDao.getOrderById(order.orderId) ?: return@launch

                var timeDelay = 0L
                if (orderUpdated.items.size != order.items.size) {
                    for (i in order.items.size..<orderUpdated.items.size) {
                        timeDelay += orderUpdated.items[i].preparationTime * 1000L
                    }
                }

                delay(timeDelay)

                if (orderUpdated.status != OrderStatus.CANCELLED) {
                    ordersDao.changeOrderStatus(orderUpdated.orderId, OrderStatus.COMPLETED)
                    OrderMessagesServiceImpl().addMessage(order)
                }
            }


        }
    }
    override fun payForOrder(order: Order) {
        ordersDao.payForOrder(order)
    }

    override fun addItemsToOrder(orderId: UUID, items: List<MenuItem>) {
        val orders = ordersDao.getAllOrders()

        val order = orders.find { it.orderId == orderId } ?: return
        order.items += items
        ordersDao.updateOrder(order)
    }

    override fun makeReview(order: Order, mark: Int, review: String) {
        ordersDao.makeReview(order.orderId, mark, review)
    }


}