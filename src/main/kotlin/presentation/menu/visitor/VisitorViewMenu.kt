package presentation.menu.visitor

interface VisitorViewMenu {
         fun run()
        fun handleViewMenu()
        fun handlePlaceOrder()
        fun handleCancelOrder()
        fun handlePayForOrder()
        fun handleLeaveReview()
        fun handleAddItemsToOrder()
}