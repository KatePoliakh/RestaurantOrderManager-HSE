package presentation.menu.visitor

import data.dao.MenuDao
import data.dao.OrdersDao
import data.dao.UserDao
import domain.entity.Role
import domain.service.InputValidator
import domain.service.auth.AuthService
import domain.service.order.OrderProcessingService
import domain.service.visitor.VisitorService
import presentation.model.OutputModel
import presentation.model.OutputStatus

class VisitorAuthImpl(
    private val authService: AuthService,
    private val usersDao: UserDao,
    private val visitorService: VisitorService,
    private val menuDao: MenuDao,
    private val ordersDao: OrdersDao,
    private val inputValidator: InputValidator,
    private val orderProcessingService: OrderProcessingService
) : VisitorAuth {

    override fun run() {
        var choice: Int
        do {
            displayMenu()
            choice = inputValidator.readInt()
            when (choice) {
                1 -> handleVisitorRegistration()
                2 -> handleVisitorAuthentication()
                3 -> return
                4 -> System.exit(0)
                else -> println("Invalid choice. Please enter a number between 1 and 4.")
            }
        } while (choice != 4)
    }

    private fun displayMenu() {
        println("===Visitor menu===")
        println("1. Visitor registration")
        println("2. Visitor authentication")
        println("3. Back to main menu")
        println("4. Exit")
    }

    override fun handleVisitorRegistration() {
        println("===Visitor registration===")
        println("Enter username:")
        val username = inputValidator.readString()
        println("Enter password:")
        val password = inputValidator.readString()

        val result = authService.register(username, password, Role.VISITOR)
        if (result) {
            val checkResult = checkAuthStatus(username)
            if (checkResult.status == OutputStatus.Error) {
                println("Registration error: ${checkResult.message}")
            } else {
                println("Registration successful!")
                println("You can now authenticate with your username and password.")
            }
        } else {
            println("Registration failed!")
        }
    }

    override fun handleVisitorAuthentication() {
        println("===Visitor authentication===")
        println("Enter username:")
        val username = inputValidator.readString()
        println("Enter password:")
        val password = inputValidator.readString()

        val user = authService.login(username, password)
        if (user != null) {
            val checkResult = checkAuthStatus(username)
            if (checkResult.status == OutputStatus.Error) {
                println("Authentication error: ${checkResult.message}")
            } else {
                println("Authentication successful!")
                usersDao.setCurrentUser(user.name, user.userId, user.password, user.salt)
                val visitorMenu = VisitorMenuImpl(menuDao, visitorService, ordersDao, inputValidator, usersDao, orderProcessingService)
                visitorMenu.run()
            }
        } else {
            println("Authentication failed!")
        }
    }
    override fun checkAuthStatus(username: String): OutputModel {
        val users = usersDao.getAllUsers()
        val user = users.find { it.name == username }
        return when (user) {
            null -> OutputModel("User not found", OutputStatus.Error)
            else -> OutputModel("User found", OutputStatus.Success)
        }
    }

}