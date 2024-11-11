package com.example.sugarfree

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailsPage(order: Order, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(text = "Order Number: ${order.orderId}", fontWeight = FontWeight.Bold)
            //Text(text = "Ordered Date: ${order.orderDate}")
            Text(text = "Total Amount: ₹${order.totalPrice}", fontWeight = FontWeight.Bold)
            //Text(text = "Payment Mode: ${order.paymentMode}")

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Delivery Address:", fontWeight = FontWeight.Bold)
            //Text(text = order.deliveryAddress)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = { /* Handle Download Invoice */ }) {
                    Text(text = "DOWNLOAD INVOICE")
                }
                TextButton(onClick = { /* Handle Reorder */ }) {
                    Text(text = "REORDER")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Additional Details Sections
            Text(text = "Payment Details", fontWeight = FontWeight.Bold)
            // Expandable view implementation for Payment Details

            Spacer(modifier = Modifier.height(8.dp))

            //Text(text = "Shipment 1 (${order.itemCount} Items)", fontWeight = FontWeight.Bold)
            // Expandable view implementation for Shipment Details
        }
    }
}
