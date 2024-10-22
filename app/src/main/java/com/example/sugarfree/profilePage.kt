package com.example.sugarfree

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

private lateinit var auth: FirebaseAuth
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    auth=Firebase.auth
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons. AutoMirrored. Filled. ArrowBack, contentDescription = "Back")
                    }
                }
            )

        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                Funout(navController = navController)
            }
        }

    )

}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Funout(navController: NavController)
{
    var currentUser=auth.currentUser
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
                .combinedClickable(
                    onClick = { auth.signOut() },
                    onDoubleClick = {navController.navigate(route = "home")},
                )
                .padding(8.dp)
            ,
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logout),
                    contentDescription = "Box Image",
                    modifier = Modifier
                        .size(150.dp, 150.dp)

                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Logout",
                    color = Color(103, 80, 164),
                    textAlign = TextAlign.Center
                )
            }


        }
    }
}

