package data.dao

import domain.entity.SystemState

open class SystemStateDao {
    open fun getSystemState(): SystemState { return SystemState()}
    open fun saveSystemState(systemState: SystemState) {}

    fun addRevenue(amount: Double) {
        val systemState = getSystemState()
        systemState.revenue += amount
        saveSystemState(systemState)
    }

}