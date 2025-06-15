package com.example.checkmate.model

import com.google.firebase.Timestamp

data class ToDoItem(
    val id: String = "",
    val title: String = "",
    val completed: Boolean = false,
    val dueDate: Timestamp? = null,
    val priority: String = Priority.MEDIUM.name
) {
    fun priorityEnum(): Priority = Priority.fromString(priority)
}
