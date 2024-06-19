package com.example.trailquest

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllLocationsScreen(onBackClick: () -> Unit) {


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
            .background(color = Color(0xFFF9EDDB)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = {
                Text(
                    "My Location",
                    style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace, fontSize = MaterialTheme.typography.bodyMedium.fontSize, fontWeight = MaterialTheme.typography.bodyMedium.fontWeight),
                    color = Color(0xFF034620)
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

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(color = Color(0xFF034620))
        } else if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace)
            )
        } else {
            // Display location cards
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                locations.forEach { location ->
                    LocationCard(location)
                    Spacer(modifier = Modifier.height(8.dp))
                }
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
fun openLocation(geoPoint: GeoPoint) {
    // Implement opening location on map or perform any other action based on geoPoint
}

// Function to delete location
fun deleteLocation(documentId: String) {
    val db = FirebaseFirestore.getInstance()
    db.collection("locations")
        .document(documentId)
        .delete()
        .addOnSuccessListener {
            Log.d("AllLocationsScreen", "Location deleted successfully")
        }
        .addOnFailureListener { e ->
            Log.e("AllLocationsScreen", "Error deleting location", e)
        }
}


@Composable
fun LocationCard(location: Location) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Location", // Default identifier since there's no name field
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = FontFamily.Monospace,
                    color = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Saved at: ${location.timestamp.toDate()}",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = FontFamily.Monospace,
                    color = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Button(
                    onClick = { openLocation(location.geoPoint) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                ) {
                    Text("Open")
                }
                Button(
                    onClick = { deleteLocation(location.documentId) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                ) {
                    Text("Delete")
                }
            }
        }
    }
}
