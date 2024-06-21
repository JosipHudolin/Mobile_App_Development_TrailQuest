package com.example.trailquest

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint

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
            AllLocationsScreen(onBackClick = { navController.navigate("main_menu_screen") }, navController = navController)
        }
        composable("compass_screen") {
            CompassScreen(onBackClick = { navController.navigate("main_menu_screen") })
        }
        composable("userdata_change_screen") {
            UserdataChangeScreen(onBackClick = { navController.navigate("main_menu_screen") })
        }
        composable(
            "locationDetail/{documentId}/{latitude}/{longitude}",
            arguments = listOf(
                navArgument("documentId") { type = NavType.StringType },
                navArgument("latitude") { type = NavType.FloatType },
                navArgument("longitude") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val documentId = backStackEntry.arguments?.getString("documentId")!!
            val latitude = backStackEntry.arguments?.getFloat("latitude")!!
            val longitude = backStackEntry.arguments?.getFloat("longitude")!!
            LocationDetailScreen(
                location = GeoPoint(latitude.toDouble(), longitude.toDouble()),
                documentId = documentId,
                onBackClick = { navController.navigateUp() },
                onDeleteSuccess = {
                    navController.popBackStack()
                }
            )
        }
        // Additional composable functions...
    }
}