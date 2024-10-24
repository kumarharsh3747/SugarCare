package com.example.sugarfree

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FoodBank
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.jakewharton.threetenabp.AndroidThreeTen
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun MainScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it) // Adjust the padding to ensure the bottom bar is accounted for
                .background(Color(0xFFF5E6C9)),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            DynamicCalendarSection()
            Spacer(modifier = Modifier.height(25.dp))
            MiddleBoxSection(navController)
            Spacer(modifier = Modifier.height(60.dp))
            WaterTrackerSection()
        }
    }
}

@Composable
fun DynamicCalendarSection() {
    val calendar = Calendar.getInstance()
    var selectedDate by remember { mutableStateOf(calendar.time) }

    // Create a list of dates for the next 6 days
    val days = (0..5).map { dayOffset ->
        (calendar.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, dayOffset) }
    }

    // Get the current month for display
    val monthFormat = SimpleDateFormat("MMMM", Locale.getDefault())
    val currentMonth = monthFormat.format(calendar.time)

    val dateFormat = SimpleDateFormat("dd", Locale.getDefault()) // Day format
    val dayNameFormat = SimpleDateFormat("EEE", Locale.getDefault()) // Day of the week format

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Month Bar
        Text(
            text = currentMonth,
            fontSize = 24.sp,
            color = Color(0xFF8E44AD),
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center
        )

        // Dates Row
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(days) { cal ->
                val date = cal.time
                val formattedDate = dateFormat.format(date)
                val dayName = dayNameFormat.format(date)
                val isSelected = date == selectedDate
                val isCurrentDay = date.day == Calendar.getInstance().get(Calendar.DAY_OF_MONTH) &&
                        date.month == Calendar.getInstance().get(Calendar.MONTH) &&
                        date.year == Calendar.getInstance().get(Calendar.YEAR)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            selectedDate = date
                        }
                        .background(
                            color = if (isSelected) Color(0xFF8E44AD) else if (isCurrentDay) Color(0xFFB39BC5) else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp)
                ) {
                    Text(
                        text = formattedDate,
                        fontSize = 20.sp,
                        color = if (isSelected) Color.White else if (isCurrentDay) Color.White else Color.Black,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = dayName,
                        fontSize = 14.sp,
                        color = if (isSelected) Color.White else if (isCurrentDay) Color.White else Color.Black,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun MiddleBoxSection(navController: NavController) {
    Box(
        modifier = Modifier
            .size(350.dp)
            .padding(10.dp)
            .background(Color(0xFF211A3C), shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(25.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                CheckInButton(navController)
                AddFoodsButton(navController)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text("Feeling 😀 Today", color = Color.White)
            Button(
                onClick = { navController.navigate("challanges") },
                modifier = Modifier.padding(top = 20.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF48426D))
            ) {
                Text(text = "VIEW LOG", color = Color.White)
            }
        }
    }
}

@Composable
fun AddFoodsButton(navController: NavController) {
    Button(
        onClick = { navController.navigate("fruitlist") },
        modifier = Modifier
            .padding(start = 8.dp)
            .size(120.dp, 70.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF48426D))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.FoodBank,
                contentDescription = "See fruit sugar",
                tint = Color.Green,
                modifier = Modifier.size(24.dp)
            )
            Text("See fruit sugar", color = Color.White)
        }
    }
}

@Composable
fun CheckInButton(navController: NavController) {
    Button(
        onClick = { navController.navigate("healthMonitor") },
        modifier = Modifier
            .padding(end = 8.dp)
            .size(120.dp, 70.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF48426D))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Checked in",
                tint = Color.Green,
                modifier = Modifier.size(24.dp)
            )
            Text("Checked in", color = Color.White)
        }
    }
}

@Composable
fun WaterTrackerSection() {
    var waterCount by remember { mutableStateOf(0) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Water Tracker", fontSize = 20.sp, color = Color(0xFF62467C))
        Spacer(modifier = Modifier.height(16.dp))

        // Water tracker controls
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = { if (waterCount > 0) waterCount-- },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFF62467C), shape = CircleShape)
            ) {
                Text("-", color = Color.White, fontSize = 24.sp)
            }

            Text(
                text = "$waterCount",
                fontSize = 32.sp,
                color = Color(0xFF62467C),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            IconButton(
                onClick = { waterCount++ },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFF62467C), shape = CircleShape)
            ) {
                Text("+", color = Color.White, fontSize = 24.sp)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    androidx.compose.material3.BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primary,
        content = {
            Spacer(modifier = Modifier.weight(1f, true))

            androidx.compose.material3.IconButton(onClick = { navController.navigate("foodScanner") }) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Food Scanner"
                )
            }

            Spacer(modifier = Modifier.weight(1f, true))

            androidx.compose.material3.IconButton(onClick = { navController.navigate("ecommerce") }) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Rounded.ShoppingCart,
                    contentDescription = "eCommerce"
                )
            }

            Spacer(modifier = Modifier.weight(1f, true))

            androidx.compose.material3.IconButton(onClick = { navController.navigate("home") }) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Rounded.Home,
                    contentDescription = "Home"
                )
            }

            Spacer(modifier = Modifier.weight(1f, true))

            androidx.compose.material3.IconButton(onClick = { navController.navigate("profile") }) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = "Profile"
                )
            }

            Spacer(modifier = Modifier.weight(1f, true))
        }
    )
}
