package presentation.model

data class OutputModel (
    val message: String,
    val status: OutputStatus = OutputStatus.Success
    )
    {
        override fun toString(): String {
            return message
        }
}