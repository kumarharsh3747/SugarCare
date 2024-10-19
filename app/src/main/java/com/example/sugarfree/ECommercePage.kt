package com.example.sugarfree

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import androidx.compose.foundation.background


// Data class for product
data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val imageUrl: String
)

@OptIn(ExperimentalMaterial3Api::class) // Suppress experimental API warning
@Composable
fun ECommercePage(navController: NavController) {
    // Cart state: List of added products
    val cartItems = remember { mutableStateListOf<Product>() }

    val products = remember {
        listOf(
            Product(1, "Sugar-Free Chocolate", 80.0, "https://example.com/chocolate.jpg"),
            Product(2, "Sugar-Free Gummies", 10.0, "https://example.com/gummies.jpg"),
            Product(3, "Sugar-Free Cookies", 40.0, "https://example.com/cookies.jpg"),
            Product(4, "Sugar-Free Cake", 15.0, "https://example.com/cake.jpg"),
            Product(5, "Sugar-Free Chyawanprash", 225.0, "https://example.com/cake.jpg"),
            Product(6, "Sugar-Free Rusk", 30.0, "https://example.com/cake.jpg"),
            Product(7, "Sugar-Free Ketchup", 120.0, "https://example.com/cake.jpg"),
            Product(8, "Sugar-Free Fruit Jam", 150.0, "https://example.com/cake.jpg"),
            Product(9, "Sugar-Free Rasgulla", 360.0, "https://example.com/cake.jpg")
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(1.dp)) {
        // Top App Bar with cart icon
        TopAppBar(
            title = {
                Text(
                    "E-Commerce Store",
                    color = Color(0xFFE6E6FA)
                )
            },
            actions = {
                IconButton(onClick = { /* Navigate to cart screen */ }) {
                    BadgedBox(badge = { Badge { Text(cartItems.size.toString()) } }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                    }
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF734F96))
        )

        Row(modifier = Modifier.fillMaxSize()) {
            CategorySidebar(modifier = Modifier.weight(1.3f))
            ProductGrid(products, cartItems, modifier = Modifier.weight(3f))
        }
    }
}

@Composable
fun CategorySidebar(modifier: Modifier = Modifier) {
    val categories = listOf("Grocery", "Dairy & Beverages", "Packaged Food", "Fruits & Vegetables", "Home & Kitchen")

    LazyColumn(
        modifier
            .fillMaxHeight()
            .padding(8.dp)
            .background(Color.LightGray)
    ) {
        items(categories) { category ->
            Text(
                text = category,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable { /* Handle category click */ },
                fontSize = 16.sp,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
fun ProductGrid(products: List<Product>, cartItems: MutableList<Product>, modifier: Modifier = Modifier) {
    LazyColumn(modifier) {
        items(products.chunked(2)) { rowProducts ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                rowProducts.forEach { product ->
                    ProductCard(product, cartItems)
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, cartItems: MutableList<Product>) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(0.4f) // Adjust width for grid
            .clickable { /* Handle product click */ },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Product Image
            Image(
                painter = rememberImagePainter(product.imageUrl),
                contentDescription = product.name,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = product.name, fontSize = 18.sp)
            Text(
                text = "₹${product.price}",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Start
            )

            // Add to Cart Button
            Button(onClick = { cartItems.add(product) }) {
                Text("Add to Cart")
            }
        }
    }
}
