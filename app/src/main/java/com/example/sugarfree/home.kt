package com.example.sugarfree



import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info

import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.*
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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay


private lateinit var auth: FirebaseAuth
// ...
// Initialize Firebase Auth

@Composable
fun Home(navController: NavController) {
    val image1 = painterResource(id = R.drawable.image5)
    val image2 = painterResource(id = R.drawable.image6)
    val image3 = painterResource(id = R.drawable.image7)


    auth = Firebase.auth


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
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = { navController.navigate("healthMonitor") },
//                modifier = Modifier.padding(16.dp)
//            ) {
//                Icon(
//                    imageVector = Icons.Filled.Chat,
//                    contentDescription = "Health Monitor",
//                    tint = MaterialTheme.colorScheme.onSecondary
//                )
//            }
//        }
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
//                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(width = 200.dp, height = 200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { navController.navigate(route = "detox") }
                    .padding(8.dp)
                    ,
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.timer),
                        contentDescription = "Box Image",
                        modifier = Modifier
                            .size(150.dp, 150.dp)

                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "DetoxTimer",
                        color = Color(103, 80, 164),
                        textAlign = TextAlign.Center
                    )
                }


            }
        }



        Row(
            modifier = Modifier
//                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(width = 200.dp, height = 200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { navController.navigate(route = "healthMonitor") }
                    .padding(8.dp)
                ,
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.health),
                        contentDescription = "Box Image",
                        modifier = Modifier
                            .size(150.dp, 150.dp)

                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Health Monitor",
                        color = Color(103, 80, 164),
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
    val currentUser = auth.currentUser

     if(currentUser==null)
     {
         TopAppBar(
             title = { Text(text = "Login") },
             navigationIcon = {
                 Box(modifier = Modifier.padding(start = 8.dp)) {
                     Image(
                         painter = painterResource(id = R.drawable.user),
                         contentDescription = "Profile Picture",
                         modifier = Modifier
                             .size(40.dp)
                             .clip(CircleShape)
                             .clickable { navController.navigate("auth") } // Navigate to AuthPage for login/signup
                     )
                 }
             }
         )
     }
    else
    {
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
                            .clickable { navController.navigate("profile") } // Navigate to AuthPage for login/signup
                    )
                }
            }
        )

    }
}
