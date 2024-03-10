package domain.service.admin
import data.dao.MenuDao
import data.dao.SystemStateDao
import domain.entity.MenuItem
import presentation.model.OutputModel


class AdminServiceImpl(private val menuDao: MenuDao,
    private val systemStateDao: SystemStateDao) : AdminService {
    override fun viewMenu() {
        println(OutputModel("===Midnight restaurant menu==="))
        val menuItems = menuDao.getAllItems()
        if (menuItems.isEmpty()) {
            println("Menu is empty! Add some items first.")
            return
        }
        for (menuItem in menuItems) {
            println(menuItem)
        }
    }
    override fun addItem(menuItem: MenuItem) {
        menuDao.addItem(menuItem)
    }

    override fun removeItem(menuItem: MenuItem) {
        menuDao.removeItem(menuItem)
    }

    override fun updateItem(menuItem: MenuItem) {
        menuDao.updateItem(menuItem)
    }

    override fun getRevenue(): Double {
        return systemStateDao.getSystemState().revenue
    }

    override fun getStatistics(): String {
        TODO("Not yet implemented")
    }
}