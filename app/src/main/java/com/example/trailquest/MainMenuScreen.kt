package com.example.trailquest

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun MainMenuScreen(
    onCurrentLocationClick: () -> Unit,
    onAllLocationsClick: () -> Unit,
    onCompassClick: () -> Unit,
    onUserDataChangeClick: () -> Unit,
    navController: NavController
) {
    val viewModel: AuthViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF9EDDB))
            .padding(16.dp),
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
                modifier = Modifier.size(150.dp), // Adjust size as needed
                contentScale = ContentScale.Fit
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            navController.navigate("my_location_screen")
            onCurrentLocationClick()
        },
            modifier = Modifier
                .width(200.dp)
                .height(50.dp)
                .background(color = Color(0xFF034620), shape = RoundedCornerShape(15.dp)), // Background color 305303, radius 15
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF034620))) {
            Text("Current Location")
        }
        Spacer(modifier = Modifier.height(15.dp))
        Button(onClick = {
                onAllLocationsClick()
        },
            modifier = Modifier
                .width(200.dp)
                .height(50.dp)
                .background(color = Color(0xFF034620), shape = RoundedCornerShape(15.dp)), // Background color 305303, radius 15
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF034620)))
        {
            Text("All Locations")
        }
        Spacer(modifier = Modifier.height(15.dp))
        Button(onClick = {
            navController.navigate("compass_screen")
            onCompassClick()
        },
            modifier = Modifier
                .width(200.dp)
                .height(50.dp)
                .background(color = Color(0xFF034620), shape = RoundedCornerShape(15.dp)), // Background color 305303, radius 15
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF034620))) {
            Text("Compass")
        }
        Spacer(modifier = Modifier.height(15.dp))
        Button(onClick = {
            navController.navigate("userdata_change_screen")
            onUserDataChangeClick()
        },
            modifier = Modifier
                .width(200.dp)
                .height(50.dp)
                .background(color = Color(0xFF034620), shape = RoundedCornerShape(15.dp)), // Background color 305303, radius 15
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF034620)))
        {
            Text("Change User Data")
        }
        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = {
            viewModel.logout {
                navController.navigate("login_screen") {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            }
        },
            modifier = Modifier
                .width(150.dp)
                .height(50.dp)
                .background(color = Color(0xFF034620), shape = RoundedCornerShape(15.dp)), // Background color 305303, radius 15
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF034620)))
        {
            Text("Logout")
        }
    }
}