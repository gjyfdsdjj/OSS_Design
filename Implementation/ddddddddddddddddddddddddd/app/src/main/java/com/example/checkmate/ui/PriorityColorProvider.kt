package com.example.checkmate.ui

import androidx.compose.ui.graphics.Color
import com.example.checkmate.model.Priority

object PriorityColorProvider {
    fun colorFor(priority: Priority): Color = when (priority) {
        Priority.HIGH -> Color.Red
        Priority.MEDIUM -> Color(0xFFFFA000) // Orange
        Priority.LOW -> Color.Green
    }
}
