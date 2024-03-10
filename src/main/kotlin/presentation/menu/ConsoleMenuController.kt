package presentation.menu

import di.DI

class ConsoleMenuController {
    val mainMenu = DI.consoleMenu
    fun run() {
        println("Welcome to the restaurant!")
        try {
            mainMenu.run()
        } catch (e: Exception) {
            println("Unexpected error occurred: ${e.message}")
        }
    }
}
