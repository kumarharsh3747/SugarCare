package com.example.sugarfree

//import androidx.compose.foundation.ExperimentalFoundationApi
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.combinedClickable
//import androidx.compose.foundation.horizontalScroll
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowBack
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import com.google.firebase.Firebase
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.auth
//
//private lateinit var auth: FirebaseAuth
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ProfileScreen(navController: NavController) {
//    auth=Firebase.auth
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text(text = "Profile") },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(imageVector = Icons. AutoMirrored. Filled. ArrowBack, contentDescription = "Back")
//                    }
//                }
//            )
//
//        },
//        content = { paddingValues ->
//            Box(modifier = Modifier.padding(paddingValues)) {
//                Funout(navController = navController)
//            }
//        }
//
//    )
//
//}
//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun Funout(navController: NavController)
//{
//    var currentUser=auth.currentUser
//    Row(
//        modifier = Modifier
////                .fillMaxWidth()
//            .horizontalScroll(rememberScrollState())
//            .padding(vertical = 8.dp),
//        horizontalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        Box(
//            modifier = Modifier
//                .size(width = 200.dp, height = 200.dp)
//                .clip(RoundedCornerShape(8.dp))
//                .combinedClickable(
//                    onClick = { auth.signOut() },
//                    onDoubleClick = {navController.navigate(route = "home")},
//                )
//                .padding(8.dp)
//            ,
//            contentAlignment = Alignment.Center
//        ) {
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.logout),
//                    contentDescription = "Box Image",
//                    modifier = Modifier
//                        .size(150.dp, 150.dp)
//
//                )
//                Spacer(modifier = Modifier.height(2.dp))
//                Text(
//                    text = "Logout",
//                    color = Color(103, 80, 164),
//                    textAlign = TextAlign.Center
//                )
//            }
//
//
//        }
//    }
//}
//


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

private lateinit var auth: FirebaseAuth


@Composable
fun ProfilePage(navController: NavController) {
    auth = Firebase.auth
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Profile picture
        Image(
            painter = painterResource(id = R.drawable.user),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(16.dp))
        val currentUser = (auth.currentUser)?.email

        if (currentUser != null) {
            Text(
                text = currentUser,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // User bio (You can fetch user bio from Firebase as well)
        Text(
            text = "Health Enthusiast",
            fontSize = 18.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Divider
        Divider(color = Color.Black, thickness = 1.dp)

        Spacer(modifier = Modifier.height(16.dp))

        // Button to edit profile
        Button(onClick = { /* Handle edit profile click */ }) {
            Text(text = "Edit Profile")

        }
        Button(onClick = { func(navController=navController) }){

            Text(text = "Logout")}
    }
}


fun func(navController:NavController): () -> Unit {
    navController.navigate(route = "home")
    auth.signOut()
    return { }
}


//@Composable
//fun ProfilePicture(image: ImageBitmap) {
//    Image(
//        painter = BitmapPainter(image),
//        contentDescription = "Profile Picture",
//        modifier = Modifier
//            .size(120.dp)
//            .clip(CircleShape)
//    )
//}