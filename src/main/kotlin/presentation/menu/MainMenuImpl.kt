package presentation.menu

import data.dao.MenuDao
import data.dao.OrdersDao
import data.dao.SystemStateDao
import data.dao.UserDao
import domain.entity.Role
import domain.service.InputValidator
import domain.service.admin.AdminService
import domain.service.auth.AuthService
import domain.service.order.OrderProcessingService
import domain.service.visitor.VisitorService
import presentation.menu.admin.AdminAuthImpl
import presentation.menu.visitor.VisitorAuthImpl
import presentation.model.OutputModel
import kotlin.system.exitProcess

class MainMenuImpl(
    private val authService: AuthService,
    private val userDao: UserDao,
    private val menuDao: MenuDao,
    private val adminService: AdminService,
    private val visitorService: VisitorService,
    private val ordersDao: OrdersDao,
    private val inputValidator: InputValidator,
    private val orderProcessingService: OrderProcessingService,
    private val systemStateDao: SystemStateDao
) : MainMenu {

    override fun run() {
        val menu = ConsoleMenu()
        var choice: Int

        do {
            menu.displayMenu()
            choice = inputValidator.readInt()

            when (choice) {
                1 -> handleViewMenu()
                2 -> handleVisitorAuth()
                3 -> handleAdminAuth()
                4 -> exitProcess(0)
                else -> println("Invalid choice. Please enter a number between 1 and 4.")
            }
        } while (choice != 4)
    }

    override fun handleViewMenu() {
        val menuItems = menuDao.getAllItems()
        if (menuItems.isEmpty()) {
            println(OutputModel("Menu is empty"))
            return
        }
        for (menuItem in menuItems) {
            println(menuItem.toString())
        }
    }

    override fun handleVisitorAuth() {
        val visitorAuth = VisitorAuthImpl(authService, userDao, visitorService, menuDao, ordersDao, inputValidator, orderProcessingService)
        visitorAuth.run()

    }
    override fun handleAdminAuth() {
        val adminAuth = AdminAuthImpl(authService,userDao, adminService, menuDao, inputValidator, systemStateDao, ordersDao)
        adminAuth.run()
    }
    override fun handleRegistration() {
        var role: Role? = null
        while (role == null) {
            try {
                println("Enter role (VISITOR or ADMIN):")
                role = Role.valueOf(inputValidator.readString().uppercase())
            } catch (e: IllegalArgumentException) {
                println("Invalid role name. Please enter VISITOR or ADMIN. (uppercase or lowercase)")
            }
        }

        println("Enter username:")
        val username = inputValidator.readString()
        println("Enter password:")
        val password = inputValidator.readString()

        val result = authService.register(username, password, role)
        if (result) {
            println("Registration successful!")
        } else {
            println("Registration failed!")
        }
    }

    override fun handleAuthentication() {
        println("Enter username:")
        val username = inputValidator.readString()
        println("Enter password:")
        val password = inputValidator.readString()

        val user = authService.login(username, password)
        if (user != null) {
            println("Authentication successful!")
        } else {
            println("Authentication failed!")
        }
    }

}