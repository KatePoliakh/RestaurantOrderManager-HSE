    package presentation.menu.admin
    import data.dao.MenuDao
    import data.dao.OrdersDao
    import data.dao.SystemStateDao
    import domain.entity.MenuItem
import domain.service.InputValidator
import domain.service.admin.AdminService
    import kotlin.system.exitProcess

    class AdminViewMenuImpl(
        private val adminService: AdminService,
        private val menuDao: MenuDao,
        private val inputValidator: InputValidator,
        private val systemStateDao: SystemStateDao,
        private val ordersDao: OrdersDao
    ) : AdminViewMenu {

        override fun run() {
            var choice: Int
            do {
                println("===Admin menu===")
                handleMenu(
                    listOf("View Menu", "Manage Menu", "View Statistics", "Back to main menu(not available yet)", "Exit"),
                    listOf({ handleViewMenu() }, { handleManageMenu() }, { handleViewStatistics() },{ return@listOf }, { exitProcess(0) })
                )
                choice = inputValidator.readInt()
            } while (choice != 5)
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

        override fun handleViewMenu() {
            adminService.viewMenu()
        }

        override fun handleManageMenu() {
            println("===Admin menu===")
            handleMenu(
                listOf("View Menu", "Add dish", "Remove dish", "Update dish", "Back to main menu"),
                listOf({ adminService.viewMenu() }, { handleDishOperation { adminService.addItem(it) } }, { handleRemoveDish() }, { handleDishOperation { adminService.updateItem(it) } } , { return@listOf})
            )
        }
        private fun handleViewStatistics() {
            val revenue = systemStateDao.getSystemState().revenue
            println("Total revenue: $revenue")

            val orders = ordersDao.getAllOrders()
            //val startDate =
            //val endDate =
           // val ordersInPeriod = orders.filter { it.date in startDate..endDate }
            if (orders.isEmpty()) {
                println("No orders in the period")
                return
            }
            val popularDishes = orders
                .flatMap { it.items }
                .groupingBy { it }
                .eachCount()
                .maxByOrNull { it.value }
            println("Most popular dishes: ${popularDishes?.key}")

            val averageRating = orders.map { it.reviewMark }.average()
            println("Average order rating: $averageRating")
            val numberOfOrders = orders.size
            println("Number of orders in the period: $numberOfOrders")
        }
        private fun handleRemoveDish() {
            println("Enter the name of the dish you want to remove:")
            val dishName = inputValidator.readString()
            val menuItem = menuDao.getMenuItemByName(dishName)
            if (menuItem != null) {
                adminService.removeItem(menuItem)
            }
        }
        private fun handleDishOperation(operation: (MenuItem) -> Unit) {
            println("Enter dish name:")
            val dishName = inputValidator.readString()
            println("Enter dish quantity:")
            val dishQuantity = inputValidator.readInt()
            println("Enter dish price:")
            val dishPrice = inputValidator.readDouble()
            println("Enter dish complexity:")
            val dishPreparationTime = inputValidator.readInt()
            val menuItem = MenuItem(dishName, dishQuantity, dishPrice, dishPreparationTime)
            operation(menuItem)
        }
    }