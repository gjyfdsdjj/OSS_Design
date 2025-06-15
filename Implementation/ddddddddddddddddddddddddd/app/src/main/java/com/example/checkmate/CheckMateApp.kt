package com.example.checkmate

import android.app.Application
import com.google.firebase.FirebaseApp

class CheckMateApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this) // Firebase 초기화
    }
}
