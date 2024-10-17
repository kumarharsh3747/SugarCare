package com.example.sugarfree

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

//@OptIn(ExperimentalPermissionsApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodScannerSimpleUI(navController: NavController) {
    var recognizedText by remember { mutableStateOf("") }
    var hiddenSugars by remember { mutableStateOf<List<String>>(emptyList()) }

    val context = LocalContext.current
    val imageCapture = remember { ImageCapture.Builder().build() }

    RequestCameraPermission {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /*Text(
                text = "AI-Powered Food Scanner",

                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 50.dp)
            )*/
            // Top App Bar
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                    Text(
                        text = "AI-Powered Food Scanner",

                        color = Color.White
                    )}
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor =Color(0xFF734F96)),
                modifier = Modifier.height(50.dp)
            )


            CameraPreview(
                modifier = Modifier
                    .size(200.dp)
                    .padding(top = 80.dp)
                    .background(Color.Gray),
                imageCapture = imageCapture
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Recognized Text Section - Make it scrollable
            Text(text = "Recognized Text:",modifier = Modifier.padding(vertical = 60.dp))
            Box(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                Text(
                    text = if (recognizedText.isNotEmpty()) recognizedText else "No text recognized yet",
                    style = MaterialTheme.typography.bodyLarge,
                    //modifier = Modifier.padding(vertical = 1.dp)
                )
            }

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

            Button(onClick = {
                captureAndRecognizeText(context, imageCapture) { text ->
                    recognizedText = text
                    hiddenSugars = findHiddenSugars(text)
                }
            }) {
                Text(text = "Scan Food Label")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Bottom Navigation Bar
            FoodScannerBottomNavigationBar(navController)

        }
    }
}

// Request camera permission
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestCameraPermission(onGranted: @Composable () -> Unit) {
    val permissionState = rememberPermissionState(Manifest.permission.CAMERA)

    if (permissionState.status.isGranted) {
        onGranted()
    } else {
        LaunchedEffect(Unit) {
            permissionState.launchPermissionRequest()
        }
    }
}

// Camera Preview Implementation
@Composable
fun CameraPreview(modifier: Modifier = Modifier, imageCapture: ImageCapture) {
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
                cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(context))
    }
}

// Function to capture and recognize text from image
fun captureAndRecognizeText(
    context: android.content.Context,
    imageCapture: ImageCapture,
    onTextRecognized: (String) -> Unit
) {
    imageCapture.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(imageProxy: ImageProxy) {
                val bitmap = imageProxy.toBitmap()
                val inputImage = InputImage.fromBitmap(bitmap, 0)

                val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                recognizer.process(inputImage)
                    .addOnSuccessListener { visionText ->
                        onTextRecognized(visionText.text)
                    }
                    .addOnFailureListener { e ->
                        Log.e("TextRecognition", "Error: ${e.message}")
                    }
                    .addOnCompleteListener {
                        imageProxy.close()
                    }
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("Capture", "Error capturing image: ${exception.message}")
            }
        }
    )
}

// Extension function to convert ImageProxy to Bitmap
fun ImageProxy.toBitmap(): Bitmap {
    val buffer = planes[0].buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

// Function to find hidden sugars in recognized text
fun findHiddenSugars(text: String): List<String> {
    val sugarKeywords = listOf("glucose", "fructose", "sucrose", "maltose", "dextrose")
    return sugarKeywords.filter { text.contains(it, ignoreCase = true) }
}

// Bottom Navigation Bar Implementation
@Composable
fun FoodScannerBottomNavigationBar(navController: NavController) {
    BottomAppBar {
        IconButton(onClick = {
            navController.navigate("Home") // Replace with your home route
        }) {
            Icon(Icons.Default.Home, contentDescription = "Home")
        }
        IconButton(onClick = {
            navController.navigate("detox") // Replace with your profile route
        }) {
            Image(
                painter = painterResource(id = R.drawable.cart), // Replace with your image resource
                contentDescription = "Profile",
                modifier = Modifier.size(24.dp) // Adjust size as needed
            )
        }
        // Add more icons or buttons as necessary
    }
}
