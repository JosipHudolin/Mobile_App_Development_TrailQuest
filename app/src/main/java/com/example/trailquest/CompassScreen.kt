package com.example.trailquest

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompassScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    var azimuth by remember { mutableStateOf(0f) }

    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    val listener = remember {
        object : SensorEventListener {
            private val gravity = FloatArray(3)
            private val geomagnetic = FloatArray(3)
            private val R = FloatArray(9)
            private val I = FloatArray(9)

            override fun onSensorChanged(event: SensorEvent?) {
                if (event == null) return

                when (event.sensor.type) {
                    Sensor.TYPE_ACCELEROMETER -> {
                        System.arraycopy(event.values, 0, gravity, 0, gravity.size)
                    }
                    Sensor.TYPE_MAGNETIC_FIELD -> {
                        System.arraycopy(event.values, 0, geomagnetic, 0, geomagnetic.size)
                    }
                }

                if (SensorManager.getRotationMatrix(R, I, gravity, geomagnetic)) {
                    val orientation = FloatArray(3)
                    SensorManager.getOrientation(R, orientation)
                    azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    DisposableEffect(Unit) {
        sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(listener, magnetometer, SensorManager.SENSOR_DELAY_UI)
        onDispose {
            sensorManager.unregisterListener(listener)
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
                    "Compass",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = FontFamily.Monospace, // Roboto Mono
                        color = Color(0xFF034620)
                    )
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

        CompassDisplay(azimuth)
    }
}

@Composable
fun CompassDisplay(azimuth: Float) {
    val colorPrimary = Color(0xFF034620)
    val backgroundColor = Color(0xFFF9EDDB)
    val circleSize = 300.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.size(circleSize)
        ) {
            drawCircle(
                color = colorPrimary,
                style = Stroke(width = 8f)
            )
            drawCircle(
                color = Color.White,
                radius = size.minDimension / 2 - 8f,
                style = Stroke(width = 8f)
            )

            val arrowLength = size.minDimension / 2 - 30f
            val angleInRadians = Math.toRadians(azimuth.toDouble())

            val arrowX = center.x + arrowLength * Math.sin(angleInRadians).toFloat()
            val arrowY = center.y - arrowLength * Math.cos(angleInRadians).toFloat()

            drawLine(
                color = colorPrimary,
                start = center,
                end = Offset(arrowX, arrowY),
                strokeWidth = 12f
            )

            drawRoundRect(
                color = colorPrimary,
                topLeft = Offset(arrowX - 10f, arrowY - 10f),
                size = androidx.compose.ui.geometry.Size(20f, 20f),
                cornerRadius = CornerRadius(5f, 5f)
            )

            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    "N",
                    center.x - 15f,
                    60f,
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.parseColor("#FF034620")
                        textSize = 60f
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                )
            }
        }
    }
}
