package com.example.sugarfree

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceOrderPage(cartItems: List<Product>, onBack: () -> Unit, onPlaceOrder: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TopAppBar(
            title = { Text("Place Order") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )

        if (cartItems.isEmpty()) {
            Text("Your cart is empty.", modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center)
        } else {
            LazyColumn {
                items(cartItems) { product ->
                    CartItemCardPlaceOrder(product) // Use renamed function
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onPlaceOrder,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Confirm Order")
            }
        }
    }
}

@Composable
fun CartItemCardPlaceOrder(product: Product) { // Renamed function
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(product.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text("₹${product.price}", fontSize = 14.sp)
        }
    }
}
