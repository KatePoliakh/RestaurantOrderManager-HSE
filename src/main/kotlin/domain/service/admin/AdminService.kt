package domain.service.admin

import domain.entity.MenuItem

interface AdminService {
    fun viewMenu()
    fun addItem(menuItem: MenuItem)
    fun removeItem(menuItem: MenuItem)
    fun updateItem(menuItem: MenuItem)

    fun getRevenue(): Double
    fun getStatistics(): String
}