package domain.service
import java.util.*

class InputValidator {
    private val scanner = Scanner(System.`in`)

    fun readInt(): Int {
        while (true) {
            try {
                return scanner.nextInt()
            } catch (e: InputMismatchException) {
                println("Invalid input. Please enter an integer.")
                scanner.next() // Discard the invalid input
            }
        }
    }

    fun readString(): String {
        return scanner.next()
    }

    fun readDouble(): Double {
        while (true) {
            try {
                return scanner.nextDouble()
            } catch (e: InputMismatchException) {
                println("Invalid input. Please enter a double.")
                scanner.next() // Discard the invalid input
            }
        }
    }
}