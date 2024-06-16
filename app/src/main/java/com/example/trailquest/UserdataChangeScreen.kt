package com.example.trailquest

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserdataChangeScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    var newEmail by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isChangingEmail by remember { mutableStateOf(false) }
    var isChangingPassword by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

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

        Spacer(modifier = Modifier.height(20.dp))

        // Change Email Section
        Text("Change Email", style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace, color = Color(0xFF034620)))
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = newEmail,
            onValueChange = { newEmail = it },
            label = { Text("New Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Color(0xFF034620),
                focusedIndicatorColor = Color(0xFF034620)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (newEmail.isNotEmpty()) {
                    isChangingEmail = true
                    auth.currentUser?.updateEmail(newEmail)
                        ?.addOnCompleteListener { task ->
                            isChangingEmail = false
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Email updated successfully", Toast.LENGTH_SHORT).show()
                            } else {
                                errorMessage = task.exception?.message ?: "Failed to update email"
                            }
                        }
                } else {
                    errorMessage = "Please enter a new email"
                }
            },
            modifier = Modifier
                .width(200.dp)
                .height(50.dp)
                .background(color = Color(0xFF034620), shape = RoundedCornerShape(15.dp)), // Background color, radius 15
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF034620)) // Override button colors
        ) {
            if (isChangingEmail) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Update Email", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

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
                containerColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Color(0xFF034620),
                focusedIndicatorColor = Color(0xFF034620)
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
                containerColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Color(0xFF034620),
                focusedIndicatorColor = Color(0xFF034620)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (newPassword == confirmPassword && newPassword.isNotEmpty()) {
                    isChangingPassword = true
                    auth.currentUser?.updatePassword(newPassword)
                        ?.addOnCompleteListener { task ->
                            isChangingPassword = false
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Password updated successfully", Toast.LENGTH_SHORT).show()
                            } else {
                                errorMessage = task.exception?.message ?: "Failed to update password"
                            }
                        }
                } else {
                    errorMessage = "Passwords do not match or are empty"
                }
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