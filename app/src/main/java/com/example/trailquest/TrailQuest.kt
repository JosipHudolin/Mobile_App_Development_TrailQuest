package com.example.trailquest

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun TrailQuest(navController: NavHostController) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    NavHost(navController = navController, startDestination = if (currentUser !=null) "main_menu_screen" else "login_screen") {
        composable("main_menu_screen") {
            MainMenuScreen(
                onCurrentLocationClick = { navController.navigate("my_location_screen") },
                onAllLocationsClick = { navController.navigate("all_locations_screen") },
                onCompassClick = { navController.navigate("compass_screen") },
                onUserDataChangeClick = { navController.navigate("userdata_change_screen") },
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
        composable("all_locations_screen") {
            AllLocationsScreen(onBackClick = { navController.navigate("main_menu_screen") })
        }
        composable("compass_screen") {
            CompassScreen(onBackClick = { navController.navigate("main_menu_screen") })
        }
        composable("userdata_change_screen") {
            UserdataChangeScreen(onBackClick = { navController.navigate("main_menu_screen") })
        }
        // Additional composable functions...
    }
}