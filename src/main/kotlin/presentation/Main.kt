package presentation


import presentation.menu.ConsoleMenuController

fun main() {
    try {
        val consoleMenuController = ConsoleMenuController()
        consoleMenuController.run()

    } catch (e: Exception) {
        println("Unexpected error occurred: ${e.message}")
    }
}