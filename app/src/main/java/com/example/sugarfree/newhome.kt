package com.example.sugarfree

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.jakewharton.threetenabp.AndroidThreeTen
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

private lateinit var auth: FirebaseAuth
@Composable
fun MainScreen(navController: NavController) {
    auth = Firebase.auth
    // Create a scroll state to manage the scroll position
    val scrollState = rememberScrollState()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it) // Adjust the padding to ensure the bottom bar is accounted for
                .background(Color(0xFFF5E6C9))
                .verticalScroll(scrollState), // Enable scrolling
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            // Dynamic Calendar Section (if applicable)
            DynamicCalendarSection()

            Spacer(modifier = Modifier.height(25.dp))

            // Middle Box Section (use your specific section code here)
            MiddleBoxSection(navController)

            Spacer(modifier = Modifier.height(60.dp))

            // Water Tracker Section
            WaterTrackerSection()

            Spacer(modifier = Modifier.height(60.dp))

            // Detox button section
            detoxsButton(navController)

            Spacer(modifier = Modifier.height(60.dp))

            // Add the graph image with animation
            GraphImageWithAnimation()

            Spacer(modifier = Modifier.height(30.dp))

            // 5 Tips to Succeed Section
            TipsToSucceedSection()

            Spacer(modifier = Modifier.height(30.dp))

            // Benefits and What to Do Section
            BenefitsAndWhatToDoSection()

            Spacer(modifier = Modifier.height(30.dp))

            // What Can I Eat Section
            WhatCanIEatSection()

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
fun TipsToSucceedSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "5 Tips to Succeed",
           // style = MaterialTheme.typography.h6,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Display tips with numbers and descriptions
        for (i in 1..5) {
            Text(
                text = "$i. ${getTipText(i)}",
               // style = MaterialTheme.typography.body1,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

fun getTipText(i: Int): String {
    return when (i) {
        1 -> "Drink Water. Six (12 ounce) glasses each day, or 2.2L. Try adding sliced berries, citrus slices, or mint leaves for flavor."
        2 -> "Eat Fruit. Fruit contains plenty of natural sugars and is also an important part of any healthy eating plan."
        3 -> "Get your Fiber! Vegetables, fruits, nuts, seeds, and legumes are great sources of fiber."
        4 -> "Make Home-Cooked Meals. Pre-packaged food is higher in salt, fat, and sugar. Preparing meals at home gives you control."
        5 -> "Explore Flavors. Try adding herbs and spices to your food to ignite your taste buds!"
        else -> ""
    }
}

@Composable
fun BenefitsAndWhatToDoSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display the "Benefits" text section
        Text(
            text = "Benefits",
           // style = MaterialTheme.typography.h6,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Display the benefits list
        val benefits = listOf(
            "🏋️ Fat loss",
            "🧠 Mental focus and better mood, thanks to decreased brain inflammation.",
            "🥗 Decreased hunger and food cravings.",
            "🚴 All-day energy with no crash, thanks to stable blood sugar."
        )
        benefits.forEach { benefit ->
            Text(
                text = benefit,
               // style = MaterialTheme.typography.body1,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Display the "What to Do" text section
        Text(
            text = "What to Do",
           // style = MaterialTheme.typography.h6,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Display the advice about cutting out sugar
        Text(
            text = "Avoid all refined sugars including white sugar, corn syrup, and brown sugar...",
            //style = MaterialTheme.typography.body1,
            color = Color.Black
        )
    }
}

@Composable
fun WhatCanIEatSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display "What Can I Eat" title
        Text(
            text = "What Can I Eat",
            //style = MaterialTheme.typography.h6,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display food items using icons and text
        val foodItems = listOf(

            "🐟 Wild Salmon",
            "🥓 Bacon",
            "🥚 Eggs",
            "🥑 Avocados",
            "🌰 Raw Nuts",
            "🫒 Olive Oil",
            "🥥 Coconut Oil",
            "🥦 Low-Carb Veggies"
        )

        foodItems.forEach { foodItem ->
            Text(
                text = foodItem,
                //style = MaterialTheme.typography.body1,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun GraphImageWithAnimation() {
    var isVisible by remember { mutableStateOf(false) }

    // Trigger visibility change after a delay to simulate animation
    LaunchedEffect(Unit) {
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = expandVertically(animationSpec = tween(durationMillis = 1000)) // Slide animation
    ) {
        Image(
            painter = painterResource(id = R.drawable.graph), // Replace with your graph image resource
            contentDescription = "Diabetes Projection Graph",
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth()
                .padding(16.dp),
            contentScale = ContentScale.Fit
        )
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
fun detoxsButton(navController: NavController) {
    Button(
        onClick = { navController.navigate("detox") },
        modifier = Modifier
//            .padding(start = 8.dp)
            .size(200.dp, 150.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFF5E6C9))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
            painter = painterResource(id = R.drawable.timer),
            contentDescription = "Box Image",
            modifier = Modifier
                .size(200.dp, 200.dp)

        )
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

            androidx.compose.material3.IconButton(onClick = { func1 (navController=navController) }) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = "Profile"
                )
            }

            Spacer(modifier = Modifier.weight(1f, true))
        }
    )
}



fun func1(navController:NavController): () -> Unit {
    val currentUser=auth.currentUser
    if (currentUser==null)
        navController.navigate(route = "auth")
    else
        navController.navigate(route = "profile")
       return { }
}