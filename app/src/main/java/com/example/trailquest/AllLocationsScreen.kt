package com.example.trailquest

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllLocationsScreen(navController: NavController, onBackClick: () -> Unit) {
    val firestore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid
    var locations by remember { mutableStateOf<List<Location>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    // Fetch the locations for the logged-in user
    LaunchedEffect(Unit) {
        if (userId != null) {
            firestore.collection("locations")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { documents ->
                    locations = documents.mapNotNull { doc ->
                        val geoPoint = doc.getGeoPoint("location")
                        val timestamp = doc.getTimestamp("timestamp")
                        if (geoPoint != null && timestamp != null) {
                            Location(geoPoint, timestamp, doc.id) // Include document ID
                        } else {
                            null
                        }
                    }
                    isLoading = false
                }
                .addOnFailureListener { e ->
                    Log.e("AllLocationsScreen", "Error fetching locations", e)
                    errorMessage = "Failed to load locations"
                    isLoading = false
                }
        } else {
            isLoading = false
            errorMessage = "User not authenticated"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = Color(0xFFF9EDDB)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = {
                Text(
                    "My Locations",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontWeight = MaterialTheme.typography.bodyMedium.fontWeight
                    ),
                    color = Color(0xFF034620)
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = Color(0xFF034620),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFF9EDDB)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            locations.forEach { location ->
                LocationCard(
                    location = location,
                    onOpenClick = {
                        openLocation(navController, location.geoPoint, location.documentId)
                    },
                    onDelete = {
                        deleteLocation(navController, location.documentId) {
                            // Update the list after deletion
                            locations = locations.filter { it.documentId != location.documentId }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}




// Data class for Location
data class Location(
    val geoPoint: GeoPoint,
    val timestamp: Timestamp,
    val documentId: String // Include document ID
)

// Function to open location on map
fun openLocation(navController: NavController, geoPoint: GeoPoint, documentId: String) {
    navController.navigate("locationDetail/$documentId/${geoPoint.latitude}/${geoPoint.longitude}")
}

// Function to delete location
fun deleteLocation(navController: NavController, documentId: String, onSuccess: () -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("locations")
        .document(documentId)
        .delete()
        .addOnSuccessListener {
            Log.d("AllLocationsScreen", "Location deleted successfully")
            onSuccess()
        }
        .addOnFailureListener { e ->
            Log.e("AllLocationsScreen", "Error deleting location", e)
        }
}



@Composable
fun LocationCard(location: Location, onOpenClick: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFFBACD92)),
        ) {
            Text(
                text = "Location", // Default identifier since there's no name field
                modifier = Modifier
                    .padding(start = 16.dp)
                    .padding(top = 16.dp),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF034620)
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .padding(bottom = 7.dp)
                    .padding(top = 16.dp),
                text = "Saved at: ${location.timestamp.toDate()}",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF034620)
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Button(
                    onClick = onOpenClick,
                    modifier = Modifier
                        .width(155.dp)
                        .height(50.dp)
                        .padding(end = 8.dp)
                        .padding(start = 16.dp)
                        .padding(bottom = 16.dp)
                        .background(color = Color(0xFF034620), shape = RoundedCornerShape(15.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF034620))
                ) {
                    Text("Open")
                }
                Button(
                    onClick = onDelete,
                    modifier = Modifier
                        .width(140.dp)
                        .height(50.dp)
                        .padding(bottom = 16.dp)
                        .padding(start = 8.dp)
                        .background(color = Color(0xFF034620), shape = RoundedCornerShape(15.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF034620))
                ) {
                    Text("Delete")
                }
            }
        }
    }
}





