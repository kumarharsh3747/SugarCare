package com.example.sugarfree

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DetoxTimerPage(navController: NavController) {
    var timePassed by remember { mutableStateOf(0L) } // Start from 0
    var timerRunning by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val totalDayTime = 86400000L // 24 hours in milliseconds

    // Timer logic using coroutine for indefinite counting
    LaunchedEffect(timerRunning) {
        if (timerRunning) {
            coroutineScope.launch {
                while (timerRunning) {
                    delay(1000L) // 1 second delay
                    timePassed += 1000L
                }
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController) // Calling the BottomNavigationBar function here
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB))
                        )
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                // Circular timer display
                CircularTimer(timePassed)

                Spacer(modifier = Modifier.height(16.dp))

                // Linear Progress bar (Upside-down day progress)
                LinearProgressIndicator(
                    progress = 1f - (timePassed.toFloat() / totalDayTime.toFloat()), // Decreasing progress
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .padding(horizontal = 16.dp),
                    color = Color(0xFF2196F3)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Start/Stop Button
                Button(onClick = { timerRunning = !timerRunning }) {
                    Text(text = if (timerRunning) "Stop" else "Start")
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Detox Timeline
                DetoxTimeline()
            }
        }
    )
}

// Rest of your functions remain unchanged
@Composable
fun CircularTimer(timePassed: Long) {
    val minutes = (timePassed / 1000 / 60).toString().padStart(2, '0')
    val seconds = ((timePassed / 1000) % 60).toString().padStart(2, '0')

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(200.dp)
    ) {
        CircularProgressIndicator(
            progress = (timePassed % 60000L) / 60000f, // Circle resets every minute
            strokeWidth = 12.dp,
            color = Color(0xFF2196F3),
            modifier = Modifier.fillMaxSize()
        )
        Text(
            text = "$minutes:$seconds",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Composable
fun DetoxTimeline() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Detox Timeline",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        val detoxDays = listOf(
            "Day 1: Blood glucose levels stabilize.",
            "Day 2: Energy levels improve.",
            "Day 3: Cravings reduce significantly.",
            "Day 4: Sleep quality improves.",
            "Day 5: Digestive system feels lighter.",
            "Day 6: Skin starts to clear.",
            "Day 7: Mental clarity enhances."
        )

        detoxDays.forEachIndexed { index, day ->
            DetoxDayItem(day, index + 1)
        }
    }
}

@Composable
fun DetoxDayItem(text: String, dayNumber: Int) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(Color(0xFF2196F3), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = dayNumber.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            color = Color.Black
        )
    }
}

