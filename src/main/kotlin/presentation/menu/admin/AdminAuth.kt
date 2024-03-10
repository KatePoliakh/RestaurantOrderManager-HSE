package presentation.menu.admin

import presentation.model.OutputModel

interface AdminAuth {
    fun run()
    fun handleAdminAuth()
    fun handleAdminRegistration()
    fun handleAdminAuthentication()
    fun checkAuthStatus(username: String): OutputModel
}