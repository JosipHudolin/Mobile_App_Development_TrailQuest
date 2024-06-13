package com.example.trailquest

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import android.util.Log

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                val errorMessage = task.exception?.message ?: "Unknown error occurred"
                Log.e("AuthViewModel", "Login error: $errorMessage")
                onError(errorMessage)
            }
        }
    }

    fun register(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                val errorMessage = task.exception?.message ?: "Unknown error occurred"
                Log.e("AuthViewModel", "Register error: $errorMessage")
                onError(errorMessage)
            }
        }
    }
    fun logout(onSuccess: () -> Unit) {
        auth.signOut()
        onSuccess()
    }
}