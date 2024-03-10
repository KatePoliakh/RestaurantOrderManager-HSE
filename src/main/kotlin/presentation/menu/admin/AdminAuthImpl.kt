package presentation.menu.admin

import data.dao.MenuDao
import data.dao.OrdersDao
import data.dao.SystemStateDao
import data.dao.UserDao
import domain.entity.Role
import domain.service.InputValidator
import domain.service.admin.AdminService
import domain.service.auth.AuthService
import presentation.model.OutputModel
import presentation.model.OutputStatus

class AdminAuthImpl(
    private val authService: AuthService,
    private val userDao: UserDao,
    private val adminService: AdminService,
    private val menuDao: MenuDao,
    private val inputValidator: InputValidator,
    private val systemStateDao: SystemStateDao,
    private val ordersDao: OrdersDao
) : AdminAuth {
    private val adminPassword = "admin123" // Replace with your actual admin password
    override fun run() {
        var choice: Int
            handleAdminAuth()
        do {
            displayMenu()
            choice = inputValidator.readInt()
            when (choice) {
                1 -> handleAdminRegistration()
                2 -> handleAdminAuthentication()
                3 -> return
               // 4 -> System.exit(0)
                else -> println("Invalid choice. Please enter a number between 1 and 4.")
            }
        } while (choice != 4)
    }

    private fun displayMenu() {
        println("===Admin menu===")
        println("1. Admin registration")
        println("2. Admin authentication")
        println("3. Back to main menu")
        println("4. Exit")
    }
    override fun handleAdminAuth() {
        println(OutputModel("===Admin access confirmation==="))
        var input: String
        do {
            println(OutputModel("Enter admin password or type 'back' to return to the menu:"))
            input = inputValidator.readString()
            if (input == "back") {
                return
            }
            try {
                if (input == adminPassword) {
                    println(OutputModel("Admin access granted!"))
                    break
                } else {
                    println(OutputModel("Incorrect admin password. Please try again."))
                }
            } catch (e: Exception) {
                println(OutputModel("An error occurred: ${e.message}. Please try again."))
            }
        } while (true)
    }


    override fun handleAdminRegistration(){
        println("===Admin registration===")
        println("Enter username:")
        val username = inputValidator.readString()
        println("Enter password:")
        val password = inputValidator.readString()

        val result = authService.register(username, password, Role.ADMIN)
        if (result) {
            val checkResult = checkAuthStatus(username)
            if (checkResult.status == OutputStatus.Error) {
                println("Registration error: ${checkResult.message}")
            } else {
                println("Registration successful!")
                println("You can now authenticate as ADMIN.")
            }
        } else {
            println("Registration failed!")
        }
    }

    override fun handleAdminAuthentication() {
        println("===Admin authentication===")
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
                val adminMenu = AdminViewMenuImpl(adminService, menuDao, inputValidator, systemStateDao, ordersDao)
                adminMenu.run()
            }
        } else {
            println("Authentication failed!")
        }
    }

    override fun checkAuthStatus(username: String): OutputModel {
        val users = userDao.getAllUsers()
        val user = users.find { it.name == username }
        return when (user) {
            null -> OutputModel("User not found", OutputStatus.Error)
            else -> OutputModel("User found", OutputStatus.Success)
        }
    }
}