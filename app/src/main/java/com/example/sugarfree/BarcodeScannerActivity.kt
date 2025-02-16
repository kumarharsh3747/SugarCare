package com.example.sugarfree

import androidx.annotation.OptIn
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

@Composable
fun BarcodeScannerScreen(onBarcodeScanned: (String) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    var barcodeValue by remember { mutableStateOf<String?>(null) }
    var scanError by remember { mutableStateOf<String?>(null) }
    val isScanning by remember { derivedStateOf { barcodeValue == null } }

    val barcodeScanner = remember {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            .build()
        BarcodeScanning.getClient(options)
    }

    LaunchedEffect(barcodeValue) {
        barcodeValue?.let {
            onBarcodeScanned(it)
            scanError = null
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Camera Preview
        CameraPreview(
            modifier = Modifier.fillMaxSize(),
            cameraProviderFuture = cameraProviderFuture,
            lifecycleOwner = lifecycleOwner,
            onBarcodeDetected = { barcode ->
                if (barcodeValue == null) {
                    barcodeValue = barcode
                }
            },
            onScanError = { error ->
                scanError = error
            }
        )

        // Scanner overlay
        ScannerOverlay(modifier = Modifier.fillMaxSize(), isScanning = isScanning)

        // Scan status/result panel
        ScanStatus(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            barcodeValue = barcodeValue,
            errorMessage = scanError,
            onRescan = { barcodeValue = null }
        )

        // Error handling
        scanError?.let { error ->
            Snackbar(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp),
                action = {
                    TextButton(onClick = { scanError = null }) {
                        Text("Dismiss")
                    }
                }
            ) {
                Text(error)
            }
        }
    }
}

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
    lifecycleOwner: LifecycleOwner,
    onBarcodeDetected: (String) -> Unit,
    onScanError: (String) -> Unit
) {
    val context = LocalContext.current

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            PreviewView(ctx).apply {
                cameraProviderFuture.addListener({
                    try {
                        val cameraProvider = cameraProviderFuture.get()

                        // Preview setup
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(surfaceProvider)
                        }

                        // Image analysis setup
                        val executor = Executors.newSingleThreadExecutor()
                        val imageAnalysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .also { analysis ->
                                analysis.setAnalyzer(executor) { imageProxy ->
                                    processBarcodeImage(
                                        imageProxy = imageProxy,
                                        onBarcodeDetected = onBarcodeDetected,
                                        onError = onScanError
                                    )
                                }
                            }

                        // Camera selection
                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview,
                                imageAnalysis
                            )
                        } catch (exc: Exception) {
                            onScanError("Camera initialization failed: ${exc.localizedMessage}")
                        }
                    } catch (e: Exception) {
                        onScanError("Failed to get camera provider: ${e.localizedMessage}")
                    }
                }, ContextCompat.getMainExecutor(ctx))
            }
        }
    )
}

@Composable
private fun ScannerOverlay(modifier: Modifier = Modifier, isScanning: Boolean) {
    val animatedProgress by animateFloatAsState(
        targetValue = if (isScanning) 1f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier = modifier) {
        // Draw overlay background
        drawRect(color = Color.Black.copy(alpha = 0.6f))

        // Calculate frame dimensions
        val frameWidth = size.width * 0.8f
        val frameHeight = frameWidth * 0.6f
        val frameTop = (size.height - frameHeight) / 2

        // Clear area for scanning frame
        drawRect(
            color = Color.Transparent,
            topLeft = Offset((size.width - frameWidth) / 2, frameTop),
            size = Size(frameWidth, frameHeight)
        )

        // Draw animated border
        val borderPath = androidx.compose.ui.graphics.Path().apply {
            addRoundRect(
                RoundRect(
                    rect = Rect(
                        offset = Offset((size.width - frameWidth) / 2, frameTop),
                        size = Size(frameWidth, frameHeight)
                    ),
                    cornerRadius = CornerRadius(16.dp.toPx())
                )
            )
        }

        drawPath(
            path = borderPath,
            color = Color(0xFFFF5722),
            style = Stroke(width = 4.dp.toPx()),
        )

        // Draw animated scanning line
        if (isScanning) {
            val lineY = frameTop + frameHeight * animatedProgress
            drawLine(
                color = Color(0xFFFF5722),
                start = Offset((size.width - frameWidth) / 2 + 8.dp.toPx(), lineY),
                end = Offset((size.width + frameWidth) / 2 - 8.dp.toPx(), lineY),
                strokeWidth = 4.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
private fun ScanStatus(
    modifier: Modifier = Modifier,
    barcodeValue: String?,
    errorMessage: String?,
    onRescan: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                errorMessage != null -> {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Scan Error",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }

                barcodeValue != null -> {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Product Scanned",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = barcodeValue,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onRescan) {
                        Text("Scan Another Product")
                    }
                }

                else -> {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Scanning...",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Align barcode within the frame",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalGetImage::class)
private fun processBarcodeImage(
    imageProxy: ImageProxy,
    onBarcodeDetected: (String) -> Unit,
    onError: (String) -> Unit
) {
    val scanner = BarcodeScanning.getClient()
    val mediaImage = imageProxy.image
    mediaImage?.let {
        val inputImage = InputImage.fromMediaImage(it, imageProxy.imageInfo.rotationDegrees)
        scanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                barcodes.firstOrNull()?.displayValue?.let { value ->
                    onBarcodeDetected(value)
                }
            }
            .addOnFailureListener { exception ->
                onError("Barcode detection failed: ${exception.localizedMessage}")
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    } ?: imageProxy.close()
}