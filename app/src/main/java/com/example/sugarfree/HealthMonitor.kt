package com.example.sugarfree

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthMonitor(navController: NavHostController) {
    var historyList by remember { mutableStateOf(mutableListOf<String>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Health Monitor", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { /* TODO: Show notifications */ }) {
                        Icon(Icons.Filled.Notifications, contentDescription = "Notifications")
                    }
                    IconButton(onClick = { /* TODO: Show info */ }) {
                        Icon(Icons.Filled.Info, contentDescription = "Info")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Main content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // History Log
                Text(text = "History Log", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    items(historyList) { entry ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = entry)
                                Spacer(modifier = Modifier.weight(1f))
                                IconButton(onClick = { historyList.remove(entry) }) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Delete Entry")
                                }
                            }
                        }
                    }
                }
            }

            // Floating buttons
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 5.dp)
            ) {
                // Symptom Button
                Button(
                    onClick = { navController.navigate("symptoms") },
                    modifier = Modifier
                        .size(150.dp)
                        .padding(end = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                ) {
                    Text(text = "Symptom", fontSize = 12.sp, color = Color.White)
                }

                // Diet Button
                Button(
                    onClick = { /* TODO: Navigate to Diet page */ },
                    modifier = Modifier
                        .size(150.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                ) {
                    Text(text = "Diet", fontSize = 12.sp, color = Color.White)
                }
            }
        }
    }
}
