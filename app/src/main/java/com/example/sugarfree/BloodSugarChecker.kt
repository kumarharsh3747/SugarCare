package com.example.sugarfree

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BloodSugarChecker(navController: NavHostController) {
    var sugarLevel by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    // BMR Calculator States
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var bmrResult by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Check Kar", fontSize = 40.sp, fontWeight = FontWeight.Bold,color = MaterialTheme.colorScheme.onSecondary) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0x7C0F0F3B)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Blood Sugar Checker Section Title
            Text("Blood Sugar Checker", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            // Blood Sugar Input
            Text("Enter your blood sugar level (mg/dL)", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = sugarLevel,
                onValueChange = { sugarLevel = it },
                label = { Text("Blood Sugar Level") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val level = sugarLevel.toFloatOrNull()
                    result = when {
                        level == null -> "Please enter a valid number"
                        level < 70 -> "Low: Consider eating a snack to raise your levels."
                        level in 70f..130f -> "Normal: Your blood sugar level is within the normal range."
                        level in 130f..180f -> "High: Consider consulting your healthcare provider."
                        else -> "Very High: Seek medical attention immediately."
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0x7C0F0F3B))
            ) {
                Text("Check", fontSize = 20.sp, color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (result.isNotEmpty()) {
                Text(text = result, fontSize = 18.sp)
            }

            // BMR Calculator Section Title
            Spacer(modifier = Modifier.height(32.dp))
            Text("BMR Calculator", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Weight (kg)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = height,
                onValueChange = { height = it },
                label = { Text("Height (cm)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Age (years)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Gender Selector
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                Text("Gender: ")
                RadioButton(
                    selected = gender == "Male",
                    onClick = { gender = "Male" }
                )
                Text("Male")
                RadioButton(
                    selected = gender == "Female",
                    onClick = { gender = "Female" }
                )
                Text("Female")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val w = weight.toFloatOrNull()
                    val h = height.toFloatOrNull()
                    val a = age.toIntOrNull()
                    if (w != null && h != null && a != null) {
                        bmrResult = if (gender == "Male") {
                            (66.5 + (13.75 * w) + (5.003 * h) - (6.755 * a)).toString()
                        } else {
                            (655.1 + (9.563 * w) + (1.850 * h) - (4.676 * a)).toString()
                        }
                    } else {
                        bmrResult = "Please enter valid values."
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0x7C0F0F3B))
            ) {
                Text("Calculate BMR", fontSize = 20.sp, color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (bmrResult.isNotEmpty()) {
                Text(text = "Your BMR is: $bmrResult kcal/day", fontSize = 18.sp)
            }
        }
    }
}
