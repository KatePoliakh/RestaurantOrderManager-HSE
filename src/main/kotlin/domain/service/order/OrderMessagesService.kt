package domain.service.order

import domain.entity.Order
import domain.entity.OrderStatus

interface OrderMessagesService {

    fun addMessage(order: Order)

    fun printMessage()


}

class OrderMessagesServiceImpl : OrderMessagesService {
    private var listOfFinishedNumbersOrders = listOf<Int>()

    override fun addMessage(order: Order) {
        if (order.status != OrderStatus.CANCELLED) {
            val temp = listOfFinishedNumbersOrders.toMutableList()
            temp.add(order.num)
            listOfFinishedNumbersOrders = temp
        }
    }

    override fun printMessage() {
        if (listOfFinishedNumbersOrders.isNotEmpty()) {
            for (num in listOfFinishedNumbersOrders) {
                println("\u001B[34mOrder ${num} is ready\u001B[0m")
                val temp = listOfFinishedNumbersOrders.toMutableList()
                temp.remove(num)
                listOfFinishedNumbersOrders = temp
            }
        }
    }

}