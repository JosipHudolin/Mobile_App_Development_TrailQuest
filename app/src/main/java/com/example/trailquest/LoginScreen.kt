package com.example.trailquest

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onRegisterClick: () -> Unit, navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val viewModel: AuthViewModel = viewModel()
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF9EDDB)), // Background color F9EDDB
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .padding(top = 40.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo), // Replace with your logo resource
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp), // Adjust size as needed
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
        // Email TextField
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = Color.Black.copy(alpha = 0.5f)) }, // Black with 50% transparency
            textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace, color = Color.Black), // Roboto Mono
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color(0xFF034620), // Underline color 305303
                unfocusedIndicatorColor = Color(0xFF034620), // Underline color 305303
                containerColor = Color(0xFFF9EDDB)
            ),
            modifier = Modifier.padding(vertical = 8.dp) // Space between fields 54.dp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Password TextField
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", color = Color.Black.copy(alpha = 0.5f)) }, // Black with 50% transparency
            visualTransformation = PasswordVisualTransformation(),
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontFamily = FontFamily.Monospace), // Roboto Mono
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color(0xFF034620), // Underline color 305303
                unfocusedIndicatorColor = Color(0xFF034620), // Underline color 305303
                containerColor = Color(0xFFF9EDDB)
            ),
            modifier = Modifier.padding(vertical = 8.dp) // Space between fields 54.dp
        )

        Spacer(modifier = Modifier.height(15.dp))

        // Login Button
        Button(
            onClick = {
                viewModel.login(email, password, onSuccess = {
                    onLoginSuccess()
                }, onError = {
                    errorMessage = it
                })
            },
            modifier = Modifier
                .width(200.dp)
                .height(50.dp)
                .background(color = Color(0xFF034620), shape = RoundedCornerShape(15.dp)), // Background color 305303, radius 15
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF034620)) // Override button colors
        ) {
            Text(
                "Login",
                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace), // Roboto Mono
                color = Color.White
            )
        }

        // Error message if present
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace) // Roboto Mono
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Register Button
        TextButton(onClick = onRegisterClick) {
            Text(
                "Don't have an account? Register",
                color = Color(0xFF034620), // Text color 305303
                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace) // Roboto Mono
            )
        }
    }
}