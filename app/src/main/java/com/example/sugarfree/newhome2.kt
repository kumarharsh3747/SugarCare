package com.example.sugarfree

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

private lateinit var auth: FirebaseAuth

private val PrimaryColor = Color(0xFF2A4D69)    // Deep navy blue
private val SecondaryColor = Color(0xFFFF7F50)  // Coral accent
private val BackgroundGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFFF8F9FA), Color(0xFFE9ECEF)) // Light gray gradient
)

@Composable
fun MainScreen(navController: NavController) {
    auth = Firebase.auth
    Scaffold(
        //topBar = { AppToolbar() },
        bottomBar = { BottomNavigationBar(navController) },
        containerColor = Color.Transparent
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
        ) {
            GreetingSection()
            QuickActionsRow(navController)
            WeeklyOverview()
            HealthTipsSection()
           // ChallengesScreen(navController)
            Spacer(modifier = Modifier.height(80.dp))
            val scrollState = rememberScrollState()

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .horizontalScroll(scrollState)
                    .padding(16.dp)
            ) {
               // SectionHeader("Challenges")

                CourseBox(
                    imageId = R.drawable.image5,
                    contentDescription = "Course 1",
                    text = "",
                    onClick = {
                        navController.navigate("details1")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                CourseBox(
                    imageId = R.drawable.image6,
                    contentDescription = "Course 2",
                    text = "",
                    onClick = {
                        navController.navigate("details2")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                CourseBox(
                    imageId = R.drawable.image7,
                    contentDescription = "Course 3",
                    text = "",
                    onClick = {
                        navController.navigate("details3")
                    }
                )
            }


        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppToolbar() {
    CenterAlignedTopAppBar(
        title = {
            Text("SugarFree",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor
                ))
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GreetingSection() {
    CenterAlignedTopAppBar(
        title = {
            Text("SugarFree",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor
                ))
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        )
    )
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Good Morning!",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = Color.DarkGray
            ))
        Text("Track your daily nutrition",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.Gray
            ))
    }
}

@Composable
private fun QuickActionsRow(navController: NavController) {
    val actions = listOf(
        QuickAction("Health", Icons.Default.MonitorHeart) { navController.navigate("healthMonitor") },
        QuickAction("Track Meal", Icons.Default.WaterDrop) { navController.navigate("Water_intake") },
        QuickAction("Reminder", Icons.Default.AddAlarm) { navController.navigate("Reminders") },
        QuickAction("challanges", Icons.Default.MenuBook) { navController.navigate("challanges")

        }

    )

    LazyRow(
        modifier = Modifier.padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(actions) { action ->
            ElevatedCard(
                modifier = Modifier
                    .width(120.dp)
                    .height(140.dp)
                    .clickable { action.onClick() }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = action.icon,
                        contentDescription = action.label,
                        tint = PrimaryColor,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(action.label,
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color.DarkGray
                        ))
                }
            }
        }
    }
}
@Composable
private fun WeeklyOverview() {
    val weekData = listOf(
        "Mon" to "Exercise",
        "Tue" to "Hydration",
        "Wed" to "Yoga",
        "Thu" to "Meditation",
        "Fri" to "Healthy Diet",
        "Sat" to "Mental Wellness",
        "Sun" to "Rest & Recovery"
    )

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Weekly Health Plan",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(weekData) { (day, activity) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            day,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.Gray
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(
                                    color = SecondaryColor.copy(alpha = 0.2f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite, // Change to relevant icons
                                contentDescription = activity,
                                tint = PrimaryColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            activity,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = PrimaryColor,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HealthTipsSection() {
    val tips = listOf(
        "Choose whole fruits over juices",
        "Stay hydrated throughout the day",
        "Read nutrition labels carefully",
        "Plan meals in advance"
    )

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Daily Tips",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor
                ))

            Spacer(modifier = Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                tips.forEach { tip ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = SecondaryColor
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(tip,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.DarkGray
                            ))
                    }
                }
            }
        }
    }
}

data class QuickAction(val label: String, val icon: ImageVector, val onClick: () -> Unit)



@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavItem("ChatBot", Icons.AutoMirrored.Rounded.Chat, "ChatBot"),
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
//
//@Composable
//fun ChallengesScreen(navController: NavController) {
//    val scrollState = rememberScrollState()
//
//    Row(
//        modifier = Modifier
//            .fillMaxSize()
//            .horizontalScroll(scrollState)
//            .padding(16.dp)
//    ) {
//        SectionHeader("Challenges")
//
//        CourseBox(
//            imageId = R.drawable.image5,
//            contentDescription = "Course 1",
//            text = "",
//            onClick = {
//                navController.navigate("details1")
//            }
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//
//        CourseBox(
//            imageId = R.drawable.image6,
//            contentDescription = "Course 2",
//            text = "",
//            onClick = {
//                navController.navigate("details2")
//            }
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//
//        CourseBox(
//            imageId = R.drawable.image7,
//            contentDescription = "Course 3",
//            text = "",
//            onClick = {
//                navController.navigate("details3")
//            }
//        )
//    }
//}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        color = Color.Black,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )
}

@Composable
private fun CourseBox(imageId: Int, contentDescription: String, text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray)
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageId),
                contentDescription = contentDescription,
                modifier = Modifier
                    .size(300.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = text,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}
