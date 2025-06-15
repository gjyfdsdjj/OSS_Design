package com.example.checkmate.model

enum class Priority(val label: String) {
    HIGH("HIGH"),
    MEDIUM("MEDIUM"),
    LOW("LOW");

    companion object {
        fun fromString(value: String?): Priority {
            return entries.find { it.name == value } ?: MEDIUM
        }
    }
}