package com.saudeconnectapp

import android.app.Application
import com.google.firebase.FirebaseApp

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Inicialize o Firebase
        FirebaseApp.initializeApp(this)
    }
}