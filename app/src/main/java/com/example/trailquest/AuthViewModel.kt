package com.example.trailquest

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuthUserCollisionException

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

    fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        onSuccess: () -> Unit,
        onEmailExists: () -> Unit,
        onError: (String) -> Unit
    ) {
        // Check if the email already exists
        auth.fetchSignInMethodsForEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val signInMethods = task.result?.signInMethods ?: emptyList<String>()

                if (signInMethods.isNotEmpty()) {
                    // Email already exists
                    onEmailExists()
                } else {
                    // Create new user
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { createTask ->
                        if (createTask.isSuccessful) {
                            // Get the authenticated user
                            val firebaseUser = auth.currentUser

                            // Create a user object with necessary data
                            val user = hashMapOf(
                                "userId" to firebaseUser?.uid,
                                "email" to email,
                                "firstName" to firstName,
                                "lastName" to lastName
                            )

                            // Reference to Firestore database
                            val db = FirebaseFirestore.getInstance()

                            // Add the user data to the "users" collection
                            if (firebaseUser != null) {
                                db.collection("users").document(firebaseUser.uid)
                                    .set(user)
                                    .addOnSuccessListener {
                                        onSuccess()
                                    }
                                    .addOnFailureListener { e ->
                                        onError(e.message ?: "Unknown error occurred")
                                    }
                            }
                        } else {
                            val errorMessage = createTask.exception?.message ?: "Unknown error occurred"
                            Log.e("AuthViewModel", "Register error: $errorMessage")
                            onError(errorMessage)
                        }
                    }
                }
            } else {
                val errorMessage = task.exception?.message ?: "Unknown error occurred"
                Log.e("AuthViewModel", "Email check error: $errorMessage")
                onError(errorMessage)
            }
        }
    }
    fun logout(onSuccess: () -> Unit) {
        auth.signOut()
        onSuccess()
    }
}