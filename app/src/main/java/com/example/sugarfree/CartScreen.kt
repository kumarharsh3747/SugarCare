package com.example.sugarfree

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.material.icons.filled.ShoppingCart


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController, cartViewModel: CartViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Shopping Cart") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
        )

        // Check if cart is empty
        if (cartViewModel.cartItems.isEmpty()) {
            // Show empty cart message
            EmptyCartView()
        } else {
            // Display cart items
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cartViewModel.cartItems) { product ->
                    CartItem(product = product, cartViewModel = cartViewModel)
                }
            }

            // Total price and place order section
            TotalPriceSection(cartViewModel = cartViewModel, navController = navController, isCartEmpty = cartViewModel.cartItems.isEmpty())
        }
    }
}

@Composable
fun EmptyCartView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingCart, // Use any icon you prefer
            contentDescription = "Empty Cart",
            modifier = Modifier.size(64.dp),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Your cart is empty",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
    }
}

@Composable
fun TotalPriceSection(cartViewModel: CartViewModel, navController: NavController, isCartEmpty: Boolean) {
    Column(modifier = Modifier.padding(16.dp)) {
        HorizontalDivider()

        // Total Price Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Display the total price
            Text(
                text = "Total: ₹${cartViewModel.getTotalPrice()}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Button(
                onClick = {
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    Log.d("CartScreen", "Current user: $currentUser")

                    if (currentUser != null) {
                        // User is logged in, navigate to CheckoutPage
                        navController.navigate("checkout") // Update to include passing the ViewModel if needed
                    } else {
                        // User is not logged in, navigate to the login screen
                        navController.navigate("auth")
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                enabled = !isCartEmpty // Disable button if the cart is empty
            ) {
                Text("Place Order")
            }
        }
    }
}
@Composable
fun CartItem(product: Product, cartViewModel: CartViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Product Image
                Image(
                    painter = rememberAsyncImagePainter(product.imageUrl),
                    contentDescription = product.name,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Product Details
                Column(modifier = Modifier.weight(1f)) {
                    Text(product.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("₹${product.price}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF43A047))
                    Text(text = "5% off ₹${product.price + 50}", fontSize = 14.sp, color = Color.Gray)  // Example discount

                    Spacer(modifier = Modifier.height(8.dp))

                    // Quantity Selector (placeholder, can be replaced with a dropdown)
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Qty: 1", fontSize = 14.sp) // Placeholder for quantity selection
                        Row {
                            TextButton(onClick = { /* Save for Later Logic */ }) {
                                Text("Save for later")
                            }
                            TextButton(onClick = { cartViewModel.removeFromCart(product) }) {
                                Text("Remove")
                            }
                        }
                    }
                }
            }
        }
    }
}
