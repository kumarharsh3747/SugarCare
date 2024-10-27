package com.example.sugarfree

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


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
    var products by remember { mutableStateOf(listOf<Product>()) }

    val db = Firebase.firestore

    // Fetch products from Firestore in real time
    LaunchedEffect(Unit) {
        db.collection("products")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    // Handle any errors here
                    return@addSnapshotListener
                }

                val productList = mutableListOf<Product>()
                for (doc in value!!) {
                    val product = Product(
                        id = doc.getLong("id")?.toInt() ?: 0,
                        name = doc.getString("name") ?: "",
                        price = doc.getDouble("price") ?: 0.0,
                        imageUrl = doc.getString("imageUrl") ?: ""
                    )
                    productList.add(product)
                }
                products = productList
            }
    }

    val filteredProducts = products.filter {
        it.name.replace(" ", "", ignoreCase = true)
            .contains(searchQuery.text.replace(" ", "", ignoreCase = true), ignoreCase = true)
    }

    // UI remains unchanged
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
                text = "Delivery by.......",
                fontSize = 12.sp,
                color = Color.Black,
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
