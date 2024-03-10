package presentation.menu.visitor

import data.dao.MenuDao
import data.dao.OrdersDao
import data.dao.UserDao
import domain.entity.MenuItem
import domain.entity.OrderStatus
import domain.service.InputValidator
import domain.service.order.OrderMessagesServiceImpl
import domain.service.order.OrderProcessingService
import domain.service.visitor.VisitorService
import kotlin.system.exitProcess


class VisitorMenuImpl(
    private val menuDao: MenuDao,
    private val visitorService: VisitorService,
    private val ordersDao: OrdersDao,
    private val inputValidator: InputValidator,
    private val userDao: UserDao,
    private val orderProcessingService: OrderProcessingService
)
    : VisitorViewMenu {
    override fun run() {
        OrderMessagesServiceImpl().printMessage()
        var choice: Int
        do {
            println("===Visitor menu===")
            handleMenu(
                listOf("View Menu", "Make an Order", "Add items to order (if order is not done yet)",
                    "Cancel order (if order is not done yet)", "Pay for order", "Leave a review", "Exit"),
                listOf({ handleViewMenu() },
                    { handlePlaceOrder()  },
                    { handleAddItemsToOrder() },
                    { handleCancelOrder() },
                    { handlePayForOrder() },
                    { handleLeaveReview() },
                    { exitProcess(0) })
            )
            choice = inputValidator.readInt()
        } while (choice != 7)
    }
    override fun handleViewMenu() {
        visitorService.viewMenu()
    }

    override fun handlePlaceOrder() {
        OrderMessagesServiceImpl().printMessage()
        val orderedItems = mutableListOf<MenuItem>()
        var continueOrdering = "yes"
        while (continueOrdering == "yes") {
            visitorService.viewMenu()
            println("Enter the name of the dish you want to order")
            val dishName = inputValidator.readString()
            val dish = menuDao.getMenuItemByName(dishName)
            if (dish != null) {
                orderedItems.add(dish)
            } else {
                println("Dish not found")
            }
            println("Do you want to continue ordering? (yes/no)")
            continueOrdering = inputValidator.readString()
        }
        if (orderedItems.isNotEmpty()) {
            val userId = userDao.getCurrentUser().id
            val orderNum = orderProcessingService.createOrder(userId, orderedItems)
            //orderProcessingService.processOrder(ordersDao.getAllOrders().last())
            //OrderMessagesServiceImpl().printMessage()
            println("Order with number $orderNum  has been accepted. Please remember your order number.")
        }
    }

    override fun handleCancelOrder() {
        println("Enter the order number of the order you want to cancel")
        val orderNum = inputValidator.readInt()
        val order = ordersDao.getOrderByNum(orderNum)
        if (order != null) {
            orderProcessingService.cancelOrder(order)
            println("Order cancelled successfully")
        } else {
            println("Order not found")
        }
    }

    override fun handlePayForOrder() {
        println("Enter the order number of the order you want to pay for")
        val orderNum = inputValidator.readInt()
        val order = ordersDao.getOrderByNum(orderNum)
        if (order != null) {
            if (order.status == OrderStatus.COMPLETED) {
                orderProcessingService.payForOrder(order)
            } else {
                println("Order is not ready yet")
            }
        } else {
            println("Order not found")
        }
    }

    override fun handleLeaveReview() {
        println("Enter the order number of the order you want to leave a review for")
        val orderNum = inputValidator.readInt()
        val order = ordersDao.getOrderByNum(orderNum)
        if (order != null) {
            println("Enter your review")
            val review = inputValidator.readString()
            println("Enter your mark (1-5)")
            val mark = inputValidator.readInt()
            orderProcessingService.makeReview(order, mark, review)
        } else {
            println("Order not found")
        }
    }

    override fun handleAddItemsToOrder() {
        println("Enter the order number of the order you want to add items to")
        val orderNum = inputValidator.readInt()
        val order = ordersDao.getOrderByNum(orderNum)
        if (order != null) {
            visitorService.viewMenu()
            println("Enter the name of the dish you want to add to the order")
            val dishName = inputValidator.readString()
            val dish = menuDao.getMenuItemByName(dishName)
            if (dish != null) {
                orderProcessingService.addItemsToOrder(order.orderId, listOf(dish))
            } else {
                println("Dish not found")
            }
        }
    }

    private fun displayMenu(options: List<String>) {
        options.forEachIndexed { index, option ->
            println("${index + 1}. $option")
        }
    }

    private fun handleMenu(options: List<String>, actions: List<() -> Unit>) {
        var choice: Int
        do {
            displayMenu(options)
            choice = inputValidator.readInt()
            actions.getOrNull(choice - 1)?.invoke()
        } while (choice != options.size)
    }


}