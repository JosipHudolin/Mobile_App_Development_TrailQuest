package com.example.trailquest

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = onCurrentLocationClick) {
            navController.navigate("my_location_screen")
            Text("Current Location")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onAllLocationsClick) {
            Text("All Locations")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onCompassClick) {
            Text("Compass")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onUserDataChangeClick) {
            Text("Change User Data")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            viewModel.logout {
                navController.navigate("login_screen") {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            }
        }) {
            Text("Logout")
        }
    }
}