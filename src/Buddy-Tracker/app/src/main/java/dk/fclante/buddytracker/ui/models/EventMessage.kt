package dk.fclante.buddytracker.ui.models

abstract class EventMessage<T>(
    val guid: String,
    val title: String,
    val description: String,
    val payload: T,
    val date: String,
    val time: String,
)
