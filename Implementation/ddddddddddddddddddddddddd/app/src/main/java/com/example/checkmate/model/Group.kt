package com.example.checkmate.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Group(
    @DocumentId val id: String = "",
    val name: String = "",
    val description: String = "", // ✅ 추가
    val createdBy: String = "",
    val createdAt: Timestamp? = null,
    val members: List<String> = emptyList()
)

