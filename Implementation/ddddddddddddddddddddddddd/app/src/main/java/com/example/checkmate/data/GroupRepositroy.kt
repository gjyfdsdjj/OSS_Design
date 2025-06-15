package com.example.checkmate.data

import com.example.checkmate.model.Group
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration


object GroupRepository {
    private val db = FirebaseFirestore.getInstance()

    fun createGroup(
        userId: String,
        name: String,
        description: String,
        onResult: (Boolean) -> Unit
    ) {
        val group = Group(
            name = name,
            description = description,
            createdBy = userId,
            createdAt = Timestamp.now(),
            members = listOf(userId)
        )
        val groupRef = db.collection("groups").document()
        val groupWithId = group.copy(id = groupRef.id)

        groupRef.set(groupWithId)
            .addOnSuccessListener {
                onResult(true)
            }
            .addOnFailureListener { onResult(false) }
    }

    // 내가 멤버로 포함된 그룹 전체 조회
    fun getGroupsForUser(userId: String, onResult: (List<Group>) -> Unit) {
        db.collection("groups")
            .whereArrayContains("members", userId)
            .get()
            .addOnSuccessListener { snapshot ->
                val groups = snapshot.documents.mapNotNull { it.toObject(Group::class.java) }
                onResult(groups)
            }
    }

    fun listenGroups(onUpdate: (List<Group>) -> Unit): ListenerRegistration {
        return db.collection("groups")
            .whereArrayContains("members", AuthManager.currentUserId() ?: "")
            .addSnapshotListener { snapshot, _ ->
                val groups = snapshot?.documents?.mapNotNull { it.toObject(Group::class.java) } ?: emptyList()
                onUpdate(groups)
            }
    }

}
