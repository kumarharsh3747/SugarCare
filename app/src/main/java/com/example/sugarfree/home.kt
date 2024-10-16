package com.example.sugarfree
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material.icons.filled.Chat
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun Home(navController: NavController) {
    val image1 = painterResource(id = R.drawable.image1)
    val image2 = painterResource(id = R.drawable.image2)
    val image3 = painterResource(id = R.drawable.image3)


    Scaffold(
        topBar = { AppBar3(navController) },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                InfiniteLoopedImageBox(
                    image1 = image1,
                    image2 = image2,
                    image3 = image3,
                    stopTime = 3000,
                    navController = navController
                )
            }
        },
        bottomBar = { BottomNavigationBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("ChatBot") },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Chat,
                    contentDescription = "Chatbot",
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    )
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primary,
        content = {
            Spacer(modifier = Modifier.weight(1f, true))

            // Food Scanner Icon Button
            IconButton(onClick = { navController.navigate("foodScanner") }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "foodScanner")
            }

            Spacer(modifier = Modifier.weight(1f, true))

            // Detox Icon Button (First)
            IconButton(onClick = { navController.navigate("detox") }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "detox")
            }

           Spacer(modifier = Modifier.weight(1f, true))

            // Detox Icon Button (Second)
            IconButton(onClick = { navController.navigate("fruitlist") }) {
                Icon(imageVector = Icons.Default.Info, contentDescription = "fruitlist")
            }

            Spacer(modifier = Modifier.weight(1f, true))
            IconButton(onClick = { navController.navigate("fruitlist") }) {
                Icon(imageVector = Icons.Default.Home, contentDescription = "fruitlist")
            }

            Spacer(modifier = Modifier.weight(1f, true))
        }
    )

}

@Composable
fun InfiniteLoopedImageBox(
    image1: Painter,
    image2: Painter,
    image3: Painter,
    stopTime: Long = 2000,
    navController: NavController
) {
    val images = listOf(image1, image2, image3)
    var currentIndex by remember { mutableStateOf(0) }

    LaunchedEffect(true) {
        while (true) {
            delay(stopTime)
            currentIndex = (currentIndex + 1) % images.size
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(width = 300.dp, height = 200.dp)
                .background(Color.White)
        ) {
            Image(
                painter = images[currentIndex],
                contentDescription = "Image ${currentIndex + 1}",
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Recommended for you", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(width = 150.dp, height = 150.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .clickable { navController.navigate("c") }
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.sc),
                        contentDescription = "Box Image",
                        modifier = Modifier.size(100.dp, 100.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Sugar Free\nonly at ₹99",
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar3(navController: NavController) {
    TopAppBar(
        title = { Text(text = "Welcome") },
        navigationIcon = {
            Box(modifier = Modifier.padding(start = 8.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable { navController.navigate("profile") }
                )
            }
        }
    )
}
