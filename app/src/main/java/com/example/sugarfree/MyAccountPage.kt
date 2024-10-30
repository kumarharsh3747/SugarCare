package com.example.sugarfree

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAccountPage(navController: NavController, currentUserEmail: String) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Account", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)  // Apply padding from Scaffold
                .padding(16.dp)
        ) {

            // List of Account Options
            AccountOptionRow(
                icon = Icons.Default.Person,
                label = "My Profile",
                onClick = { navController.navigate("MyProfilePage") }
            )

            AccountOptionRow(
                icon = Icons.Default.ShoppingBag,
                label = "My Orders",
                onClick = { /* Navigate to Orders */ }
            )

            AccountOptionRow(
                icon = Icons.Default.LocationOn,
                label = "Address Book",
                onClick = { navController.navigate("addressBook") }
            )
        }
    }
}

@Composable
fun AccountOptionRow(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    textColor: Color = Color.Black
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            color = textColor
        )
    }
}
