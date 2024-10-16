package com.example.sugarfree

import android.Manifest
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FoodScannerSimpleUI(navController: NavController) {
    var recognizedText by remember { mutableStateOf("") }
    var hiddenSugars by remember { mutableStateOf<List<String>>(emptyList()) }

    // Request camera permission before showing the UI
    RequestCameraPermission {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = "AI-Powered Food Scanner",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Camera Preview Section
            CameraPreview(
                modifier = Modifier
                    .size(250.dp)
                    .background(Color.Gray)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Recognized Text Section
            Text(text = "Recognized Text:")
            Text(
                text = if (recognizedText.isNotEmpty()) recognizedText else "No text recognized yet",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Hidden Sugars Section
            Text(text = "Hidden Sugars:")
            if (hiddenSugars.isNotEmpty()) {
                Text(
                    text = hiddenSugars.joinToString(", "),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Red
                )
            } else {
                Text(text = "No hidden sugars found.", style = MaterialTheme.typography.bodyMedium)
            }

            // Scan Button
            Button(onClick = { /* Add scanning logic here */ }) {
                Text(text = "Scan Food Label")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Bottom Navigation Bar
            BottomNavigationBar(navController)
        }
    }
}

// Permission Handling
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestCameraPermission(onGranted: @Composable () -> Unit) {
    val permissionState = rememberPermissionState(Manifest.permission.CAMERA)

    if (permissionState.status.isGranted) {
        onGranted()
    } else {
        // Request the permission if not granted
        LaunchedEffect(Unit) {
            permissionState.launchPermissionRequest()
        }
    }
}

// Camera Preview Implementation
@Composable
fun CameraPreview(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val previewView = remember { PreviewView(context) }
    val cameraExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }

    AndroidView(
        factory = { previewView },
        modifier = modifier
    ) { view ->
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(view.surfaceProvider)
            }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(context))
    }
}
