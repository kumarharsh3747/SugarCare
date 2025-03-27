package com.example.sugarfree

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun HealthCareHome(navController: NavController) {
    val items = listOf(
        "BMI" to R.drawable.ic_bmi,
        "Waist to Height ratio" to R.drawable.ic_waist_height,
        "Lean Body Mass" to R.drawable.ic_lean_mass,
        "Daily Calories" to R.drawable.ic_calories,
        "Energy Expenditure" to R.drawable.ic_energy,
        "Weight Loss" to R.drawable.ic_weight_loss,
        "Calories Burned" to R.drawable.ic_calories_burned,
        "Body Water" to R.drawable.ic_body_water,
        "Blood Volume" to R.drawable.ic_blood_volume,
        "Blood Pressure" to R.drawable.ic_blood_pressure,
        "Blood Alcohol" to R.drawable.ic_blood_alcohol,
        "Smoking Cost" to R.drawable.ic_smoking,
        "Water Requirement" to R.drawable.ic_water,
        "Body Fat (%)" to R.drawable.ic_body_fat
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Health Care" , fontSize = 40.sp, fontWeight = FontWeight.Bold,color = MaterialTheme.colorScheme.onSecondary) },

                navigationIcon = {
                    IconButton(onClick = { /* Open Drawer or Navigation */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                backgroundColor = Color(0x7C0F0F3B) // Set TopAppBar background color (Teal)
                , contentColor = Color(0xFFC1CCCC)
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8F6F6))
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(16.dp)
            ) {
                items(items) { (title, icon) ->
                    val route = when (title) {
                        "BMI", "Waist to Height ratio", "Lean Body Mass", "Daily Calories"
                            , "Energy Expenditure", "Weight Loss", "Calories Burned"
                                 , "Body Water", "Blood Volume", "Blood Pressure", "Blood Alcohol",
                                        "Smoking Cost", "Water Requirement", "Body Fat (%)"-> "calculator/$title"
                        else -> "home"
                    }
                    HealthCard(title, icon) {
                        navController.navigate(route)
                    }
                }
            }

        }
    }
}

@Composable
fun HealthCard(title: String, icon: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick) // ✅ Clickable to navigate
            .background(Color.White),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = title,
                tint = Color.Unspecified,
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, fontSize = 14.sp, color = Color.Black)
        }
    }
}
//
//@Composable
//fun BottomNavigationBar(navController: NavController) {
//    BottomNavigation(
//        modifier = Modifier.height(48.dp), // Reduced height
//        backgroundColor = Color(0xFF008080), // Set BottomNavigation background color (Teal)
//        contentColor = Color(0xFFDEEAEA) // Set text and icon color
//    ) {
//        BottomNavigationItem(
//            icon = { Icon(painterResource(id = R.drawable.ic_calculator), contentDescription = "Calculate") },
//            label = { Text("Calculate", fontSize = 12.sp) }, // Reduced text size
//            selected = true,
//            onClick = {}
//        )
//        BottomNavigationItem(
//            icon = { Icon(painterResource(id = R.drawable.ic_fitness), contentDescription = "Fitness") },
//            label = { Text("Fitness", fontSize = 12.sp) }, // Reduced text size
//            selected = false,
//            onClick = {}
//        )
//        BottomNavigationItem(
//            icon = { Icon(painterResource(id = R.drawable.ic_profile), contentDescription = "Profile") },
//            label = { Text("Profile", fontSize = 12.sp) }, // Reduced text size
//            selected = false,
//            onClick = {}
//        )
//    }
//}
//
