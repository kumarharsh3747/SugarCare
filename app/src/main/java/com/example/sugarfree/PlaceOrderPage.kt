package com.example.sugarfree

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@Composable
fun OrderConfirmationPage(
    navController: NavHostController,
    cartViewModel: CartViewModel
) {
    val scope = rememberCoroutineScope()
    val firestore = FirebaseFirestore.getInstance()
    val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email

    // State variables for loading and error handling
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Clear the cart and save order to Firebase when the page is first loaded
    LaunchedEffect(Unit) {
        if (currentUserEmail != null) {
            saveOrderToFirebase(cartViewModel, firestore, currentUserEmail) { success, error ->
                isLoading = false // Stop loading after the save attempt
                if (!success) {
                    errorMessage = error // Set error message if saving fails
                }
            }
        } else {
            isLoading = false // Stop loading if no user is logged in
            errorMessage = "User not logged in." // Handle the case where user email is null
        }
        cartViewModel.clearCart() // Clear cart after attempting to save order
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator() // Show loading indicator
        } else {
            errorMessage?.let {
                Text(
                    text = "Error: $it",
                    color = Color.Red,
                    fontSize = 16.sp
                )
            } ?: run {
                Text("Order Confirmed!", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Thank you for your purchase.", fontSize = 16.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { navController.popBackStack("ecommerce", inclusive = true) }) {
                Text("Back to Home")
            }
        }
    }
}

fun saveOrderToFirebase(
    cartViewModel: CartViewModel,
    firestore: FirebaseFirestore,
    userEmail: String,
    onComplete: (Boolean, String?) -> Unit // Callback to handle success or failure
) {
    val orderItems = cartViewModel.cartItems.map { product ->
        mapOf(
            "name" to product.name,
            "price" to product.price,
            // "quantity" to product.quantity // assuming `quantity` is part of `Product`
        )
    }

    val orderData = mapOf(
        "userEmail" to userEmail,
        "items" to orderItems,
        "totalPrice" to cartViewModel.getTotalPrice(),
        "timestamp" to System.currentTimeMillis() // for ordering by date
    )

    // Save the order in a "orders" collection under the user's document
    firestore.collection("users")
        .document(userEmail.replace(".", ",")) // Ensure email is sanitized
        .collection("orders")
        .add(orderData)
        .addOnSuccessListener {
            onComplete(true, null) // Indicate success
            println("Order saved successfully")
        }
        .addOnFailureListener { e ->
            onComplete(false, e.message) // Indicate failure with error message
            println("Error saving order: ${e.message}")
        }
}
