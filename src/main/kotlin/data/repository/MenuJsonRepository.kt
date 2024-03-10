package data.repository

//import kotlinx.serialization.json.Json
import data.dao.MenuDao
import domain.entity.MenuItem
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MenuJsonRepository(private val menuJsonRepositoryPath: String) : JsonRepository<MenuItem>(), MenuDao {

    override fun addItem(menuItem: MenuItem) {
        val textFromFile = readFileOrCreateEmpty(menuJsonRepositoryPath)
        val items: List<MenuItem> = if (textFromFile.isBlank()) listOf() else Json.decodeFromString(textFromFile)
        val updatedUsers = items.toMutableList()
        updatedUsers.add(menuItem)
        val serializedUpdatedStorage = Json.encodeToString(updatedUsers.toList())
        writeTextToFile(menuJsonRepositoryPath, serializedUpdatedStorage)
    }

    override fun removeItem(menuItem: MenuItem) {
        val items: List<MenuItem> = getAllItems()
        val updatedItems = items.toMutableList()

        updatedItems.removeIf { oldItem -> oldItem.name == menuItem.name }
        val serializedUpdatedStorage = Json.encodeToString(updatedItems.toList())
        writeTextToFile(menuJsonRepositoryPath, serializedUpdatedStorage)

    }

    override fun updateItem(menuItem: MenuItem) {
        val items: List<MenuItem> = getAllItems()
        if(items.find { item -> item.name == menuItem.name } == null)
            return

        val updatedItems = items.toMutableList()
        updatedItems.removeIf { oldItem -> oldItem.name == menuItem.name }
        updatedItems.add(menuItem)

        val serializedUpdatedStorage = Json.encodeToString(updatedItems.toList())
        writeTextToFile(menuJsonRepositoryPath, serializedUpdatedStorage)

    }

    override fun getMenuItemByName(menuItemName: String): MenuItem? {
        val items: List<MenuItem> = getAllItems()
        return items.find { it.name == menuItemName }

    }

    override fun getAllItems(): List<MenuItem> {
        val textFromFile = readFileOrCreateEmpty(menuJsonRepositoryPath)

        return if (textFromFile.isBlank())
            listOf() else Json.decodeFromString<List<MenuItem>>(textFromFile)

    }


    override fun decreaseCountOfItems(items: List<MenuItem>): MenuDao.Result {
        val menu = getAllItems()
        val updatedMenu = menu.toMutableList()
        for (item in items) {
            val menuItem = updatedMenu.find { it.name == item.name } ?: return MenuDao.Error
            if (menuItem.quantity < item.quantity) return MenuDao.Error
            menuItem.quantity -= item.quantity
        }
        val serializedUpdatedStorage = Json.encodeToString(updatedMenu.toList())
        writeTextToFile(menuJsonRepositoryPath, serializedUpdatedStorage)
        return MenuDao.Success
    }

    override fun increaseCountOfItems(items: List<MenuItem>) {
        val menu = getAllItems()
        val updatedMenu = menu.toMutableList()
        for (item in items) {
            val menuItem = updatedMenu.find { it.name == item.name } ?: return
            menuItem.quantity += item.quantity
        }
        val serializedUpdatedStorage = Json.encodeToString(updatedMenu.toList())
        writeTextToFile(menuJsonRepositoryPath, serializedUpdatedStorage)
    }

}