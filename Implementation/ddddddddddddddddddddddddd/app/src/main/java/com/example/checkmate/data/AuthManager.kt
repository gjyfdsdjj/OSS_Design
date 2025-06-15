package com.example.checkmate.data

import com.google.firebase.auth.FirebaseAuth

object AuthManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task -> onResult(task.isSuccessful) }
    }

    fun register(email: String, password: String, onResult: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task -> onResult(task.isSuccessful) }
    }

    fun logout() {
        auth.signOut()
    }

    fun currentUserId(): String? = auth.currentUser?.uid
}