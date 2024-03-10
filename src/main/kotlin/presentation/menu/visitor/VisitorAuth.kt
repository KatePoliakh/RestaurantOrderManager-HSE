package presentation.menu.visitor

import presentation.model.OutputModel

interface VisitorAuth {
    fun run()
    fun handleVisitorRegistration()
    fun handleVisitorAuthentication()
    fun checkAuthStatus(username: String): OutputModel
}