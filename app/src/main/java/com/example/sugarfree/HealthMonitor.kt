
package com.example.sugarfree

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthMonitor(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Health Monitor",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0x7C0F0F3B)
                )
            )
        }
    ) { paddingValues ->
        // Box with lavender background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE3D4EA)) // Lavender background
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Image at the top
                Image(
                    painter = painterResource(id = R.drawable.health_image),
                    contentDescription = "Health Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(bottom = 16.dp),
                    contentScale = ContentScale.Crop
                )
            }

            // Buttons aligned vertically at the bottom
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 10.dp)
            ) {
                // Check Button
                Button(
                    onClick = { navController.navigate("blood_sugar_checker") },
                    modifier = Modifier
                        .fillMaxWidth() // Make the button full width
                        .height(80.dp) // Set a fixed height
                        .padding(bottom = 8.dp), // Space between buttons
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0x7C0F0F3B))
                ) {
                    Text(text = "Health Track", fontSize = 20.sp, color = Color.White)
                }

                // Symptom Button
                Button(
                    onClick = { navController.navigate("symptoms") },
                    modifier = Modifier
                        .fillMaxWidth() // Make the button full width
                        .height(80.dp) // Set a fixed height
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0x7C0F0F3B))
                ) {
                    Text(text = "Symptom", fontSize = 20.sp, color = Color.White)
                }

                // Diet Button
                Button(
                    onClick = { navController.navigate("diet") },
                    modifier = Modifier
                        .fillMaxWidth() // Make the button full width
                        .height(80.dp), // Set a fixed height
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0x7C0F0F3B))
                ) {
                    Text(text = "Diet", fontSize = 20.sp, color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HealthMonitorPreview() {
    HealthMonitor(navController = rememberNavController())
}