package com.example.trailquest

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun TrailQuest(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login_screen") {
        composable("main_menu_screen") {
            MainMenuScreen(
                onCurrentLocationClick = { navController.navigate("my_location_screen") },
                onAllLocationsClick = { navController.navigate("all_locations_screen") },
                onCompassClick = { navController.navigate("compass_screen") },
                onUserDataChangeClick = { navController.navigate("user_data_change_screen") },
                navController = navController
            )
        }
        composable("login_screen") {
            LoginScreen(
                onLoginSuccess = { navController.navigate("main_menu_screen") },
                onRegisterClick = { navController.navigate("register_screen") },
                navController = navController
            )
        }
        composable("register_screen") {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate("main_menu_screen") },
                onLoginClick = { navController.navigate("login_screen") },
                navController = navController
            )
        }
        composable("my_location_screen") {
            MyLocationScreen(onBackClick = { navController.navigate("main_menu_screen") }) {
            }
        }
        // Additional composable functions...
    }
}