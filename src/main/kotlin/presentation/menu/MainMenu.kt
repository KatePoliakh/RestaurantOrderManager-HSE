package presentation.menu

interface MainMenu {
    fun run()
    fun handleRegistration()
    fun handleAuthentication()
    fun handleViewMenu()
    fun handleVisitorAuth()
    fun handleAdminAuth()
}