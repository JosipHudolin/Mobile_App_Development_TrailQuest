package com.example.trailquest

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit, onLoginClick: () -> Unit, navController: NavController) {
    val viewModel: AuthViewModel = viewModel()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var showErrorMessage by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF9EDDB)), // Background color F9EDDB
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo Image Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .padding(top = 40.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp), // Adjust size as needed
                contentScale = ContentScale.Fit
            )
        }

        // First Name TextField
        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First name", color = Color.Black.copy(alpha = 0.5f)) }, // Black with 50% transparency
            textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace, color = Color.Black), // Roboto Mono
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color(0xFF034620),
                unfocusedIndicatorColor = Color(0xFF034620),
                containerColor = Color(0xFFF9EDDB)
            ),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Last Name TextField
        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last name", color = Color.Black.copy(alpha = 0.5f)) }, // Black with 50% transparency
            textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace, color = Color.Black), // Roboto Mono
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color(0xFF034620),
                unfocusedIndicatorColor = Color(0xFF034620),
                containerColor = Color(0xFFF9EDDB)
            ),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Email TextField
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = Color.Black.copy(alpha = 0.5f)) }, // Black with 50% transparency
            textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace, color = Color.Black), // Roboto Mono
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color(0xFF034620),
                unfocusedIndicatorColor = Color(0xFF034620),
                containerColor = Color(0xFFF9EDDB)
            ),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Password TextField
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", color = Color.Black.copy(alpha = 0.5f)) }, // Black with 50% transparency
            textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace, color = Color.Black), // Roboto Mono
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color(0xFF034620),
                unfocusedIndicatorColor = Color(0xFF034620),
                containerColor = Color(0xFFF9EDDB)
            ),
            modifier = Modifier.padding(vertical = 8.dp),
            visualTransformation = PasswordVisualTransformation()
        )

        // Spacer
        Spacer(modifier = Modifier.height(8.dp))

        // Register Button
        Button(
            onClick = {
                viewModel.register(
                    email = email,
                    password = password,
                    firstName = firstName,
                    lastName = lastName,
                    onSuccess = {
                        onRegisterSuccess()
                    },
                    onEmailExists = {
                        showErrorMessage = true
                        errorMessage = "Email is already in use. Please use a different email."
                    },
                    onError = {
                        showErrorMessage = true
                        errorMessage = it
                    }
                )
            },
            modifier = Modifier
                .width(200.dp)
                .height(50.dp)
                .background(color = Color(0xFF034620), shape = RoundedCornerShape(15.dp)), // Background color 305303, radius 15
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF034620)) // Override button colors
        ) {
            Text(
                "Register",
                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace), // Roboto Mono
                color = Color.White
            )
        }

        // Error message if present
        if (showErrorMessage && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace) // Roboto Mono
            )
        }

        // Spacer
        Spacer(modifier = Modifier.height(8.dp))

        // Login Button
        TextButton(onClick = onLoginClick) {
            Text(
                "Already have an account? Login",
                color = Color(0xFF034620), // Text color 305303
                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace) // Roboto Mono
            )
        }
    }
}