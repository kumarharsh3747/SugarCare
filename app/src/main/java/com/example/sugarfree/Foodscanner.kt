package com.example.sugarfree

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Eco
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun FoodScannerUI(navController: NavController) {
    var recognizedText by remember { mutableStateOf("") }
    var hiddenSugars by remember { mutableStateOf<List<String>>(emptyList()) }
    var isScanning by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val imageCapture = remember { ImageCapture.Builder().build() }
    val permissionState = rememberPermissionState(Manifest.permission.CAMERA)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Sugar Detective",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        bottomBar = { FoodScannerBottomNavigationBar(navController) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (permissionState.status.isGranted) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Camera Preview Section
                    CameraPreview(
                        modifier = Modifier
                            .size(250.dp)
                            .padding(top = 70.dp)
                            .background(Color.Gray),
                        imageCapture = imageCapture
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Recognized Text Section
                    Text(text = "Recognized Text:", modifier = Modifier.padding(vertical = 8.dp))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                            .background(Color(0xFFE6E6FA)) // Lavender background for the text area
                            .padding(16.dp)
                    ) {
                        if (recognizedText.isNotEmpty()) {
                            val sugarKeywords = listOf("glucose", "sugar", "fructose", "sucrose", "maltose", "dextrose", "syrup")
                            Text(
                                text = highlightSugars(recognizedText, sugarKeywords),
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Justify,
                                color = Color.Black,
                                lineHeight = 54.sp
                            )
                        } else {
                            Text(
                                text = "No text recognized yet",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                color = Color.Black
                            )
                        }
                    }

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
                    Button(onClick = {
                        isScanning = true
                        captureAndRecognizeText(context, imageCapture) { text ->
                            recognizedText = text
                            hiddenSugars = findHiddenSugars(text)
                            isScanning = false
                        }
                    }) {
                        Text(text = "Scan Food Label")
                    }
                }
            } else {
                RequestCameraPermission(permissionState)
            }

        }
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestCameraPermission(onGranted: PermissionState) {
    val permissionState = rememberPermissionState(Manifest.permission.CAMERA)

    if (permissionState.status.isGranted) {
        onGranted
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
private fun FoodScannerBottomNavigationBar(navController: NavController) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("home") },
            icon = { Icon(Icons.Rounded.Home, "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = true,
            onClick = { navController.navigate("foodscanner") },
            icon = { Icon(Icons.Rounded.CameraAlt, "Scanner") },
            label = { Text("Scanner") }
        )
//        NavigationBarItem(
//            selected = false,
//            onClick = { navController.navigate("ecommerce") },
//            icon = { Icon(Icons.Rounded.ShoppingCart, "Shop") },
//            label = { Text("Shop") }
//        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("fruitlist") },
            icon = { Icon(Icons.Rounded.Eco, "Info") },
            label = { Text("Info") }
        )
    }
}

