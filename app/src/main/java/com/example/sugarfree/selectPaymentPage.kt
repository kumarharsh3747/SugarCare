package com.example.sugarfree

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectPaymentPage(
    navController: NavController,
    totalAmount: Double


) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Payment Mode", color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Payment Summary Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("₹$totalAmount", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text("Total Amount to Pay", fontSize = 16.sp, color = Color.Gray)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("MRP: ₹$totalAmount", color = Color.Gray)
                    Text("Delivery charge: ₹49", color = Color.Gray)
                    Text("Discount: ₹49", color = Color.Gray)
                    Text("Your Savings: ₹49", color = Color(0xFFFFA726), fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Tax has been included in the total amount", color = Color(0xFF90CAF9), fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Payment Options
            Text("Pay On Delivery", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            PaymentOptionCard(
                option = "Cash On Delivery",
                description = "Pay by Cash / Card / UPI on Delivery",
                onClick = {
                    // Navigate to confirmation or order completion page
                    navController.navigate("orderConfirmation")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Pay Online", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            PaymentOptionCard(
                option = "UPI",
                description = "Google Pay / PhonePe / PayTM & more",
                onClick = { /* Handle UPI payment */ }
            )
            PaymentOptionCard(
                option = "Debit Card",
                description = "Visa / Master Card / Rupay",
                onClick = { /* Handle Debit Card payment */ }
            )
            PaymentOptionCard(
                option = "Credit Card",
                description = "Visa / Master Card / Rupay",
                onClick = { /* Handle Credit Card payment */ }
            )
            PaymentOptionCard(
                option = "Net Banking",
                description = "Select from a List of Banks",
                onClick = { /* Handle Net Banking payment */ }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Warning Text
            Text(
                text = "Please complete your payment in the next 10 minutes to avoid cancellation.",
                color = Color(0xFF3B3704),
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun PaymentOptionCard(option: String, description: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF4CAF50))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(option, fontWeight = FontWeight.Bold)
                Text(description, fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}

