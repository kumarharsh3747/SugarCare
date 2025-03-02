package com.example.sugarfree

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.*

private lateinit var auth: FirebaseAuth

val AppGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFFF8F7FC), Color(0xFFE6E4F2)) // Soft lavender to white gradient
)
val PrimaryColor = Color(0xFF6C5B7B)       // Deep amethyst purple
val SecondaryColor = Color(0xFF8BD3C7)     // Soft mint teal
val AccentColor = Color(0xFFF67280)        // Coral pink

@Composable
fun MainScreen(navController: NavController) {
    auth = Firebase.auth
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SugarFree", color = Color.White) },
                backgroundColor = PrimaryColor,
                elevation = 4.dp
            )
        },
        bottomBar = { BottomNavigationBar(navController) },
        floatingActionButton = {
            AnimatedVisibility(
                visible = true,
                enter = scaleIn(animationSpec = tween(500))
            ) {
                FloatingActionButton(
                    onClick = { navController.navigate("quick_check") },
                    backgroundColor = PrimaryColor
                ) {
                    Icon(Icons.Default.Add, "Quick Check", tint = Color.White)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppGradient)
                .padding(padding)
        ) {
            HeaderSection(navController)
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                AnimatedCalendarSection()
                DailyStatsRow()
                MiddleBoxSection(navController)
                WaterTrackerSection()
                SymptomCheckSection()
                DetoxChallengeSection(navController)
                HealthGraphSection()
                NutritionTipsSection()
            }
        }
    }
}

@Composable
private fun HeaderSection(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Welcome Back!",
                fontSize = 24.sp,
                color = PrimaryColor,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Let's stay healthy today",
                fontSize = 14.sp,
                color = PrimaryColor.copy(alpha = 0.8f)
            )
        }
        IconButton(
            onClick = { func1(navController) },
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(SecondaryColor)
        ) {
            Icon(
                Icons.Rounded.Person,
                contentDescription = "Profile",
                tint = Color.White
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DailyStatsRow() {
    val stats = listOf("BMI: 22.5", "BMR: 1800", "Steps: 5,432")
    LazyRow(
        modifier = Modifier.padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(stats) { stat ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + expandVertically()
            ) {
                Card(
                    modifier = Modifier
                        .padding(4.dp)
                        .animateItemPlacement(),
                    backgroundColor = SecondaryColor.copy(alpha = 0.2f),
                    elevation = 4.dp
                ) {
                    Text(
                        text = stat,
                        modifier = Modifier.padding(16.dp),
                        color = PrimaryColor,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun AnimatedCalendarSection() {
    var expanded by remember { mutableStateOf(true) }
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("dd", Locale.getDefault())
    val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
    val monthFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DateRange, "Calendar", tint = PrimaryColor)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Daily Tracker",
                    color = PrimaryColor,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = monthFormat.format(calendar.time),
                color = PrimaryColor,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(7) { offset ->
                    val date = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, offset) }.time
                    val isToday = offset == 0
                    Card(
                        modifier = Modifier
                            .width(80.dp)
                            .clickable { },
                        elevation = 4.dp,
                        backgroundColor = if (isToday) SecondaryColor.copy(0.2f) else Color.White
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(
                                dayFormat.format(date),
                                color = PrimaryColor.copy(0.8f),
                                fontSize = 12.sp
                            )
                            Text(
                                dateFormat.format(date),
                                color = PrimaryColor,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            AnimatedVisibility(visible = expanded) {
                DynamicCalendarSection()
            }
        }
    }
}

@Composable
private fun WaterTrackerSection() {
    var waterCount by remember { mutableStateOf(0) }
    val maxGlasses = 8
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Hydration Tracker", color = PrimaryColor, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = waterCount.toFloat() / maxGlasses,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .clip(RoundedCornerShape(8.dp)),
                color = PrimaryColor,
                backgroundColor = SecondaryColor.copy(alpha = 0.3f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(maxGlasses) { index ->
                    val filled = index < waterCount
                    AnimatedVisibility(
                        visible = true,
                        enter = scaleIn(tween(300, easing = FastOutSlowInEasing))
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(if (filled) PrimaryColor else SecondaryColor.copy(0.3f), CircleShape)
                                .clickable {
                                    waterCount = index + 1
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                if (filled) "${index + 1}" else "",
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
            Text(
                "$waterCount / $maxGlasses glasses",
                color = PrimaryColor,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun SymptomCheckSection() {
    var selectedSymptom by remember { mutableStateOf<String?>(null) }
    val symptoms = listOf(
        "Fatigue" to "Feeling tired",
        "Thirst" to "Excessive thirst",
        "Dizziness" to "Feeling lightheaded"
    )
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text("How are you feeling?", color = PrimaryColor, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(symptoms) { (title, description) ->
                    val isSelected = selectedSymptom == title
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            selectedSymptom = if (isSelected) null else title
                        }
                    ) {
                        AnimatedVisibility(visible = isSelected) {
                            Icon(
                                Icons.Default.CheckCircle,
                                "Selected",
                                tint = Color.Green,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(if (isSelected) PrimaryColor else SecondaryColor.copy(0.3f), CircleShape)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                title.firstOrNull()?.toString() ?: "",
                                color = Color.White,
                                fontSize = 18.sp
                            )
                        }
                        Text(description, color = PrimaryColor, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun DetoxChallengeSection(navController: NavController) {
    var progress by remember { mutableStateOf(0.6f) }
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { navController.navigate("detox") },
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Box {
            // Placeholder for image resource
            Image(
                painter = painterResource(R.drawable.detox_bg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .padding(16.dp)
            ) {
                Text(
                    "7-Day Sugar Detox",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = progress,
                    color = Color.White,
                    backgroundColor = Color.White.copy(alpha = 0.3f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "${(progress * 100).toInt()}% Complete",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavItem("ChatBot", Icons.Rounded.Chat, "ChatBot"),
        NavItem("Scan", Icons.Default.Search, "fruitlist"),
        NavItem("Shop", Icons.Rounded.ShoppingCart, "ecommerce"),
        NavItem("Profile", Icons.Rounded.Person, "profile"),

    )
    BottomNavigation(
        backgroundColor = Color.White,
        elevation = 16.dp,
        modifier = Modifier.shadow(8.dp)
    ) {
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = false,
                onClick = {
                    when (item.route) {
                        "profile" -> func1(navController)
                        else -> navController.navigate(item.route)
                    }
                },
                selectedContentColor = PrimaryColor,
                unselectedContentColor = SecondaryColor
            )
        }
    }
}

private fun func1(navController: NavController) {
    val currentUser = auth.currentUser
    navController.navigate(if (currentUser == null) "auth" else "profile")
}

data class NavItem(val title: String, val icon: ImageVector, val route: String)



@Composable
fun DynamicCalendarSection() {
    val calendar = Calendar.getInstance()
    var selectedDate by remember { mutableStateOf(calendar.time) }
    val dateFormat = remember { SimpleDateFormat("dd", Locale.getDefault()) }
    val dayFormat = remember { SimpleDateFormat("EEE", Locale.getDefault()) }
    val healthData = remember { mutableMapOf<String, String>() }.apply {
        // put(dateFormat.format(calendar.time), "120"// Sample glucose data
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            "Health Calendar",
            color = PrimaryColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            items(7) { offset ->
                val date = calendar.apply { add(Calendar.DAY_OF_YEAR, offset) }.time
                val isToday = offset == 0
                val glucoseLevel = healthData[dateFormat.format(date)] ?: "--"

                AnimatedVisibility(
                    visible = true,
                    enter = slideInHorizontally() + fadeIn()
                ) {
                    Card(
                        modifier = Modifier
                            .width(80.dp)
                            .clickable { selectedDate = date },
                        elevation = 4.dp,
                        backgroundColor = if (isToday) SecondaryColor.copy(0.2f) else Color.White
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(
                                dayFormat.format(date),
                                color = PrimaryColor.copy(0.8f),
                                fontSize = 12.sp
                            )
                            Text(
                                dateFormat.format(date),
                                color = PrimaryColor,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .background(
                                        color = when (glucoseLevel.toIntOrNull()) {
                                            in 0..100 -> Color.Green.copy(0.3f)
                                            in 101..140 -> Color.Yellow.copy(0.3f)
                                            else -> Color.Red.copy(0.3f)
                                        },
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(glucoseLevel, fontSize = 12.sp, color = PrimaryColor)
                            }
                        }
                    }
                }
            }
        }
    }
}

// Middle Box Section with Quick Actions
@Composable
fun MiddleBoxSection(navController: NavController) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(200.dp),
        elevation = 8.dp,
        shape = RoundedCornerShape(16.dp),
        backgroundColor = PrimaryColor.copy(0.1f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            AnimatedVisibility(visible = !expanded) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    QuickActionButton(
                        icon = Icons.Default.MonitorHeart,
                        label = "Glucose",
                        onClick = { navController.navigate("healthMonitor") }
                    )
                    QuickActionButton(
                        icon = Icons.Default.LunchDining,
                        label = "Meals",
                        onClick = { navController.navigate("mealPlanner") }
                    )
                    QuickActionButton(
                        icon = Icons.Default.FitnessCenter,
                        label = "Workout",
                        onClick = { navController.navigate("fitness") }
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn()
            ) {
                Column {
                    HealthProgressBar(
                        title = "Daily Sugar Intake",
                        current = 12.0,
                        max = 25.0,
                        unit = "g",
                        color = PrimaryColor
                    )
                    HealthProgressBar(
                        title = "Water Consumption",
                        current = 5.0,
                        max = 8.0,
                        unit = "glasses",
                        color = Color(0xFF2196F3)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = { expanded = !expanded },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(
                    if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = "Toggle view",
                    tint = PrimaryColor
                )
            }
        }
    }
}

@Composable
fun QuickActionButton(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(SecondaryColor.copy(0.2f), CircleShape)
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = label, tint = PrimaryColor)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, color = PrimaryColor, fontSize = 12.sp)
    }
}

// Health Graph Section with Interactive Trends
@Composable
fun HealthGraphSection() {
    val glucoseData = remember { listOf(120, 135, 128, 142, 118, 130) }
    val maxValue = remember { glucoseData.maxOrNull() ?: 150 }
    val minValue = remember { glucoseData.minOrNull() ?: 80 }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                "Blood Sugar Trends",
                color = PrimaryColor,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .background(SecondaryColor.copy(0.1f))
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val spacePerHour = size.width / (glucoseData.size - 1)
                    val yScale = size.height / (maxValue - minValue).toFloat()

                    // Draw grid lines
                    repeat(5) { i ->
                        val yPos = size.height - (i * size.height / 4)
                        drawLine(
                            color = SecondaryColor.copy(alpha = 0.2f),
                            start = Offset(0f, yPos),
                            end = Offset(size.width, yPos),
                            strokeWidth = 1.dp.toPx()
                        )
                    }

                    // Draw data line
                    val path = Path().apply {
                        glucoseData.forEachIndexed { index, value ->
                            val x = spacePerHour * index
                            val y = size.height - (value - minValue) * yScale
                            if (index == 0) moveTo(x, y) else lineTo(x, y)
                        }
                    }
                    drawPath(
                        path = path,
                        color = PrimaryColor,
                        style = Stroke(width = 2.dp.toPx())
                    )

                    // Draw data points
                    glucoseData.forEachIndexed { index, value ->
                        val x = spacePerHour * index
                        val y = size.height - (value - minValue) * yScale
                        drawCircle(
                            color = PrimaryColor,
                            radius = 4.dp.toPx(),
                            center = Offset(x, y)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("12 AM", color = PrimaryColor, fontSize = 12.sp)
                Text("Current", color = PrimaryColor, fontSize = 12.sp)
            }
        }
    }
}

// Nutrition Tips Section with Expandable Cards
@Composable
fun NutritionTipsSection() {
    val tips = remember {
        listOf(
            NutritionTip(
                title = "Breakfast Ideas",
                icon = R.drawable.ic_breakfast,
                items = listOf("Avocado toast", "Greek yogurt with nuts", "Vegetable omelette")
            ),
            NutritionTip(
                title = "Smart Snacks",
                icon = R.drawable.ic_snacks,
                items = listOf("Mixed nuts", "Celery with peanut butter", "Hard-boiled eggs")
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            "Nutrition Guide",
            color = PrimaryColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(tips) { tip ->
                NutritionTipCard(tip = tip)
            }
        }
    }
}

@Composable
fun NutritionTipCard(tip: NutritionTip) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .width(280.dp)
            .clickable { expanded = !expanded },
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(tip.icon),
                    contentDescription = tip.title,
                    tint = PrimaryColor,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(tip.title, color = PrimaryColor, fontWeight = FontWeight.Bold)
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    tip.items.forEach { item ->
                        Text(
                            "• $item",
                            color = PrimaryColor.copy(0.8f),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }

            Icon(
                if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = "Toggle",
                tint = PrimaryColor,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

// Helper components and data classes
data class NutritionTip(
    val title: String,
    @DrawableRes val icon: Int,
    val items: List<String>
)

@Composable
fun HealthProgressBar(title: String, current: Double, max: Double, unit: String, color: Color) {
    val progress by animateFloatAsState(
        targetValue = (current / max).toFloat(),
        animationSpec = tween(durationMillis = 800)
    )

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, color = PrimaryColor, fontSize = 14.sp)
            Text("${current.toInt()}/$max $unit", color = PrimaryColor, fontSize = 12.sp)
        }
        LinearProgressIndicator(
            progress = progress,
            color = color,
            backgroundColor = color.copy(alpha = 0.1f),
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
        )
    }
}