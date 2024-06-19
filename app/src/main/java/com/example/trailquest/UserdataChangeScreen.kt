package com.example.trailquest

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserdataChangeScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isChangingPassword by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val user = auth.currentUser

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF9EDDB)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = {
                Text(
                    "Change User Data",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = FontFamily.Monospace, // Roboto Mono
                        color = Color(0xFF034620)
                    )
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    // Wrapping the icon in a circular Box
                    Box(
                        modifier = Modifier
                            .size(40.dp) // Adjust size as needed
                            .background(
                                color = Color(0xFF034620), // Circle background color
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center // Center the icon within the circle
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White // Arrow icon color
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFF9EDDB) // TopAppBar background color
            )
        )

        Spacer(modifier = Modifier.height(70.dp))

        // Change Password Section
        Text("Change Password", style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace, color = Color(0xFF034620)))
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("New Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFF9EDDB),
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Color(0xFF034620),
                focusedIndicatorColor = Color(0xFF034620),
                focusedLabelColor = Color(0xFF034620),
                unfocusedLabelColor = Color(0xFF034620)
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFF9EDDB),
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Color(0xFF034620),
                focusedIndicatorColor = Color(0xFF034620),
                focusedLabelColor = Color(0xFF034620),
                unfocusedLabelColor = Color(0xFF034620)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (user != null) {
                    user.updatePassword(newPassword)
                        .addOnSuccessListener {
                            // Password updated successfully
                            Toast.makeText(context, "Password updated successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { exception->
                            // Handle the error
                            val errorMessage = exception.message ?: "Failed to update password"
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                            // Log the exception for debugging purposes
                        }
                } else {
                    // User is not logged in, handle accordingly (e.g., redirect to login)
                }
                newPassword = ""
                confirmPassword = ""
                isChangingPassword = false
            },
            modifier = Modifier
                .width(200.dp)
                .height(50.dp)
                .background(color = Color(0xFF034620), shape = RoundedCornerShape(15.dp)), // Background color, radius 15
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF034620)) // Override button colors
        ) {
            if (isChangingPassword) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Update Password", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace)
            )
        }
    }
}