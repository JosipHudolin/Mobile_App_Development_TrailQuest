package com.example.trailquest

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.maps.android.compose.*
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDetailScreen(
    location: GeoPoint,
    documentId: String,
    onBackClick: () -> Unit,
    onDeleteSuccess: () -> Unit
) {
    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(location.latitude, location.longitude), 15f
        )
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
                    "Location Details",
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

        val properties by remember {
            mutableStateOf(
                MapProperties(
                    mapType = MapType.HYBRID
                )
            )
        }

        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            cameraPositionState = cameraPositionState,
            properties = properties
        ) {
            Marker(
                state = MarkerState(position = LatLng(location.latitude, location.longitude)),
                title = "Selected Location"
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                // Brisanje lokacije iz Firestore baze
                deleteLocation(documentId) {
                    onDeleteSuccess()
                }
            },
            modifier = Modifier
                .width(200.dp)
                .height(50.dp)
                .background(
                    color = Color(0xFF034620),
                    shape = RoundedCornerShape(15.dp)
                ),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF034620))
        ) {
            Text("Delete Location")
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

fun deleteLocation(documentId: String, onDeleteSuccess: () -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("locations")
        .document(documentId)
        .delete()
        .addOnSuccessListener {
            Log.d("LocationDetailScreen", "Location deleted successfully")
            onDeleteSuccess()
        }
        .addOnFailureListener { e ->
            Log.e("LocationDetailScreen", "Error deleting location", e)
        }
}

