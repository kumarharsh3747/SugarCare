package com.example.sugarfree

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            TopAppBar(
                title = { Text(text = "                  Food Scanner", color = Color.Black) },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFFE3D4EA)),
                modifier = Modifier.height(60.dp)
            )

            CameraPreview(
                modifier = Modifier
                    .size(200.dp)
                    .padding(top = 70.dp)
                    .background(Color.Gray),
                imageCapture = imageCapture
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Recognized Text:", modifier = Modifier.padding(vertical = 8.dp))

            // Box for Extracted Text with Dark Blue Background
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .background(Color(0xFFE3D4EA)) // Dark Blue Background
                    .padding(16.dp)
            ) {
                if (recognizedText.isNotEmpty()) {
                    val sugarKeywords = listOf("glucose", "sugar", "fructose", "sucrose", "maltose", "dextrose", "syrup")
                    Text(
                        text = highlightSugars(recognizedText, sugarKeywords),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Justify, // Justify text
                        color = Color.Black, // White text for readability
                        lineHeight = 54.sp // Adjust line height
                    )
                } else {
                    Text(
                        text = "             No text recognized yet",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }
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

            FoodScannerBottomNavigationBar(navController)
        }
    }
}

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

@Composable
fun CameraPreview(modifier: Modifier = Modifier, imageCapture: ImageCapture) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val previewView = remember { PreviewView(context) }
    val cameraExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }

    AndroidView(factory = { previewView }, modifier = modifier) { view ->
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

fun captureAndRecognizeText(
    context: Context,
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

fun ImageProxy.toBitmap(): Bitmap {
    val buffer = planes[0].buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

fun findHiddenSugars(text: String): List<String> {
    val sugarKeywords = listOf("glucose", "fructose", "sucrose", "maltose", "dextrose", "syrup", "honey", "lactose")
    val regex = Regex(sugarKeywords.joinToString("|"), RegexOption.IGNORE_CASE)
    return regex.findAll(text).map { it.value }.distinct().toList()
}

fun highlightSugars(text: String, keywords: List<String>): AnnotatedString {
    return buildAnnotatedString {
        val lowerCaseText = text.lowercase()
        var currentIndex = 0

        while (currentIndex < text.length) {
            val match = keywords.firstOrNull { lowerCaseText.indexOf(it, currentIndex) >= 0 }
            if (match != null) {
                val start = lowerCaseText.indexOf(match, currentIndex)
                append(text.substring(currentIndex, start))
                withStyle(style = SpanStyle(color = Color.Red)) {
                    append(text.substring(start, start + match.length))
                }
                currentIndex = start + match.length
            } else {
                append(text.substring(currentIndex))
                break
            }
        }
    }
}

@Composable
fun FoodScannerBottomNavigationBar(navController: NavController) {
    BottomAppBar {
        IconButton(onClick = { navController.navigate("Home") }) {
            Icon(Icons.Default.Home, contentDescription = "Home")
        }
        IconButton(onClick = { navController.navigate("detox") }) {
            Image(
                painter = painterResource(id = R.drawable.cart),
                contentDescription = "Profile",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
