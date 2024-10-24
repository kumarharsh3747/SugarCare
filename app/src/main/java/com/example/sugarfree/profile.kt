package com.example.sugarfree




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