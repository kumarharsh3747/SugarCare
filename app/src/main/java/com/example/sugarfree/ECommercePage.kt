package com.example.sugarfree

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter

// Data class for product
data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val imageUrl: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ECommercePage(navController: NavController, cartViewModel: CartViewModel) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    val products = listOf(
        Product(1, "Sugar-Free Gold+", 288.0, "https://firebasestorage.googleapis.com/v0/b/sugarfree-df710.appspot.com/o/Sugar-Free%20Gold%2B.webp?alt=media&token=7ba63b2e-5703-4863-98af-75805053e0b6"),

        Product(2, "Sugar-Free Green Stevia", 221.0, "https://example.com/green.jpg"),
        Product(3, "Sugar-Free Natura", 149.0, "https://example.com/natura.jpg"),
        Product(4, "Sugar-Free Cake", 15.0, "https://example.com/cake.jpg"),
        Product(5, "Sugar-Free Chyawanprash", 225.0, "https://example.com/chyawanprash.jpg"),
        Product(6, "Sugar-Free Rusk", 30.0, "https://example.com/rusk.jpg"),
        Product(7, "Sugar-Free Ketchup", 120.0, "https://example.com/ketchup.jpg"),
        Product(8, "Sugar-Free Fruit Jam", 150.0, "https://example.com/jam.jpg"),
        Product(9, "Sugar-Free Rasgulla", 360.0, "https://example.com/rasgulla.jpg"),
        Product(10, "Sugar-Free Chocolate", 80.0, "https://example.com/chocolate.jpg"),
        Product(11, "Sugar-Free Gummies", 10.0, "https://example.com/gummies.jpg"),
        Product(12, "Sugar-Free Cookies", 40.0, "https://example.com/cookies.jpg"),
    )

    val filteredProducts = products.filter {
        it.name.replace(" ", "", ignoreCase = true)
            .contains(searchQuery.text.replace(" ", "", ignoreCase = true), ignoreCase = true)
    }


    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(
                    text = "Store",
                    fontSize = 20.sp,
                    color = Color.White
                )
            },
            actions = {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search...") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
                    modifier = Modifier
                        .width(200.dp)
                        .padding(8.dp)
                )
                IconButton(onClick = {
                    // Navigate to Cart when clicked
                    navController.navigate("cart")
                }) {
                    BadgedBox(badge = { Badge { Text(cartViewModel.cartItems.size.toString()) } }) {
                        Icon(Icons.Filled.ShoppingCart, contentDescription = "Cart")
                    }
                }
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF734F96))
        )

        LazyColumn {
            items(filteredProducts.chunked(2)) { rowProducts ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    rowProducts.forEach { product ->
                        ProductCard(product, cartViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, cartViewModel: CartViewModel) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .padding(8.dp)
            .clickable { /* Handle product click */ },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberImagePainter(product.imageUrl),
                contentDescription = product.name,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(product.name, fontSize = 16.sp)
            Text("₹${product.price}", fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Delivery by 23rd Oct",
                fontSize = 12.sp,
                color = Color.Green,
                textAlign = TextAlign.Start
            )
            Button(
                onClick = { cartViewModel.addToCart(product) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add to Cart")
            }
        }
    }
}
