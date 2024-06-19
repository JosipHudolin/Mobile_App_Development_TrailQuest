package com.example.trailquest

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.maps.android.compose.*
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MissingPermission")
@Composable
fun MyLocationScreen(
    onBackClick: () -> Unit,
    onSaveLocation: (GeoPoint) -> Unit
) {
    val context = LocalContext.current
    val fusedLocationClient: FusedLocationProviderClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    // Obtain current user ID
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid ?: ""

    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    val cameraPositionState = rememberCameraPositionState()

    // Permission launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        val latLng = LatLng(it.latitude, it.longitude)
                        currentLocation = latLng
                        cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 15f)
                    }
                }
            }
        }
    )

    // Request location permission if not granted
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    currentLocation = latLng
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 15f)
                }
            }
        }
    }

    Column(modifier = Modifier
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

        val properties by remember {
            mutableStateOf(
                MapProperties(
                    mapType = MapType.HYBRID
                )
            )
        }

        currentLocation?.let { location ->
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                cameraPositionState = cameraPositionState,
                properties = properties
            ) {
                Marker(
                    state = MarkerState(position = location),
                    title = "Current Location"
                )
            }
        } ?: run {
            Text("Fetching location...", modifier = Modifier.padding(16.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                currentLocation?.let {
                    val geoPoint = GeoPoint(it.latitude, it.longitude)
                    saveLocationToFirestore(userId, geoPoint, {
                        onSaveLocation(geoPoint)
                    }, { errorMessage ->
                        // Handle error
                        Log.e("MyLocationScreen", "Failed to save location: $errorMessage")
                    })
                }
            },
            modifier = Modifier
                .width(200.dp)
                .height(50.dp)
                .background(color = Color(0xFF034620), shape = RoundedCornerShape(15.dp)), // Background color 305303, radius 15
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF034620)) // Override button colors
        ) {
            Text("Save My Location")
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}


// Function to save location to Firestore
fun saveLocationToFirestore(
    userId: String, // Add userId parameter
    geoPoint: GeoPoint,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val locationData = hashMapOf(
        "userId" to userId, // Include userId in the data
        "location" to geoPoint,
        "timestamp" to Timestamp.now()
    )

    db.collection("locations")
        .add(locationData)
        .addOnSuccessListener {
            onSuccess()
        }
        .addOnFailureListener { exception ->
            onError(exception.message ?: "Unknown error occurred")
        }
}



