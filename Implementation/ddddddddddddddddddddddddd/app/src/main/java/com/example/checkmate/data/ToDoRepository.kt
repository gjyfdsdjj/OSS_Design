package com.example.checkmate.data

import com.example.checkmate.model.ToDoItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object ToDoRepository {

    private val db: FirebaseFirestore
        get() = FirebaseFirestore.getInstance()

    // 할 일 추가
    fun addToDo(userId: String, todo: ToDoItem, onResult: (Boolean) -> Unit) {
        db.collection("users").document(userId)
            .collection("todos")
            .add(todo)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    // 실시간 할 일 가져오기
    fun listenToToDos(userId: String, onData: (List<ToDoItem>) -> Unit): ListenerRegistration {
        return db.collection("users").document(userId)
            .collection("todos")
            .addSnapshotListener { snapshot, _ ->
                val todos = snapshot?.documents?.mapNotNull {
                    it.toObject(ToDoItem::class.java)?.copy(id = it.id)
                } ?: emptyList()
                onData(todos)
            }
    }

    // 수정
    fun updateToDo(userId: String, todo: ToDoItem) {
        db.collection("users").document(userId)
            .collection("todos").document(todo.id)
            .set(todo)
    }

    // 삭제
    fun deleteToDo(userId: String, todoId: String) {
        db.collection("users").document(userId)
            .collection("todos").document(todoId)
            .delete()
    }
}