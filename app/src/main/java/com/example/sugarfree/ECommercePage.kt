package com.example.sugarfree

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


// Data class for product
data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val quantity: Int = 1,
    val imageUrl: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ECommercePage(navController: NavController, cartViewModel: CartViewModel) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var products by remember { mutableStateOf(listOf<Product>()) }

    val db = Firebase.firestore

    // Fetch products from Firestore in real-time
    LaunchedEffect(Unit) {
        db.collection("products")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.e("Firestore", "Error fetching products", e)
                    return@addSnapshotListener
                }
                val productList = value?.map { doc ->
                    Product(
                        id = doc.getLong("id")?.toInt() ?: 0,
                        name = doc.getString("name") ?: "",
                        price = doc.getDouble("price") ?: 0.0,
                        imageUrl = doc.getString("imageUrl") ?: ""
                    )
                } ?: emptyList()
                products = productList
            }
    }

    val filteredProducts = products.filter {
        it.name.replace(" ", "", ignoreCase = true)
            .contains(searchQuery.text.replace(" ", "", ignoreCase = true), ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("", fontSize = 30.sp, color = Color.White) },
                actions = {
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("SugarCare",fontSize = 20.sp) },
                        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
                        modifier = Modifier
                            .width(250.dp)
                            .padding(4.dp)
                    )
                    IconButton(onClick = { navController.navigate("cart") }) {
                        BadgedBox(badge = { Badge { Text(cartViewModel.cartItems.size.toString()) } }) {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = "Cart",tint=Color.White)
                        }
                    }
                    IconButton(onClick = {
                        // Check login status using FirebaseAuth
                        val currentUser = FirebaseAuth.getInstance().currentUser
                        Log.d("Nav", "Current user: $currentUser") // Log current user

                        if (currentUser != null) {
                            // User is logged in, navigate to the account screen
                            navController.navigate("account")
                            Log.d("Nav", "User is logged in, navigating to account.")
                        } else {
                            // User is not logged in, navigate to the login screen
                            navController.navigate("auth")
                            Log.d("Nav", "User is not logged in, navigating to authPage.")
                        }
                    }) {
                        Icon(Icons.Filled.AccountCircle, contentDescription = "", tint = Color.White)
                    }

                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back",tint=Color.White)
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF734F96))
            )
        },
         // Add bottom nav here
    ) { paddingValues ->
        LazyColumn(contentPadding = paddingValues) {
            items(filteredProducts.chunked(2)) { rowProducts ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    rowProducts.forEach { product ->
                        ProductCard(product, cartViewModel, navController) // Pass navController here
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, cartViewModel: CartViewModel, navController: NavController) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .padding(8.dp)
            .clickable {
                navController.navigate("productDetail/${product.id}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Log the image URL
            Log.d("ProductCard", "Image URL: ${product.imageUrl}")

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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailPage(productId: Int, navController: NavController, cartViewModel: CartViewModel) {
    // Fetch the selected product from Firestore by its ID
    var product by remember { mutableStateOf<Product?>(null) }
    val db = Firebase.firestore

    LaunchedEffect(productId) {
        db.collection("products")
            .whereEqualTo("id", productId)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val doc = result.documents[0]
                    product = Product(
                        id = doc.getLong("id")?.toInt() ?: 0,
                        name = doc.getString("name") ?: "",
                        price = doc.getDouble("price") ?: 0.0,
                        imageUrl = doc.getString("imageUrl") ?: ""
                    )
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error fetching product details", e)
            }
    }

    product?.let {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(it.name, fontSize = 24.sp, color = Color.White) },
                    actions = {
                        IconButton(onClick = { navController.navigate("cart") }) {
                            BadgedBox(badge = { Badge { Text(cartViewModel.cartItems.size.toString()) } }) {
                                Icon(Icons.Filled.ShoppingCart, contentDescription = "Cart",tint=Color.White)
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF734F96)),


                )
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = Color.White,
                    contentPadding = PaddingValues(8.dp)
                ) {
                    Button(
                        onClick = { cartViewModel.addToCart(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text("Add to Cart", color = Color.White)
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .padding(bottom = 64.dp) // To prevent overlap with the bottom button
                ) {
                    Image(
                        painter = rememberImagePainter(it.imageUrl),
                        contentDescription = it.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = it.name, fontSize = 28.sp, color = Color.Black)
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "₹${it.price}", fontSize = 22.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "This is a detailed description of the product. It includes features, specifications, and other important information about the product.",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }
        }
    } ?: run {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    }
}
