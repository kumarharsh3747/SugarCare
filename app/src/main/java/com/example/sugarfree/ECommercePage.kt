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
        // Products from 1-50
        Product(1, "Sugar-Free Gold+", 288.0, "https://firebasestorage.googleapis.com/v0/b/sugarfree-df710.appspot.com/o/Sugar-Free%20Gold%2B.webp?alt=media&token=7ba63b2e-5703-4863-98af-75805053e0b6"),
        Product(2, "Sugar-Free Green Stevia", 221.0, "https://firebasestorage.googleapis.com/v0/b/sugarfree-df710.appspot.com/o/Sugar-Free%20Green%20Stevia.png?alt=media&token=8c3bac4b-9056-4c84-a603-b5e1810e9741"),
        Product(3, "Sugar-Free Natural", 149.0, "https://firebasestorage.googleapis.com/v0/b/sugarfree-df710.appspot.com/o/Sugar-Free%20Natural.webp?alt=media&token=daac5a0e-bd89-4a56-b369-75c87bc3d9b2"),
        Product(4, "Sugar-Free Cake", 15.0, "https://firebasestorage.googleapis.com/v0/b/sugarfree-df710.appspot.com/o/Sugar-Free%20Cake.webp?alt=media&token=8461aac3-86b2-4923-b519-e050f7617c42"),
        Product(5, "Sugar-Free Chyawanprash", 225.0, "https://firebasestorage.googleapis.com/v0/b/sugarfree-df710.appspot.com/o/Sugar-Free%20Chyawanprash.webp?alt=media&token=db0efe0e-6481-42ed-a89d-23a90123056f"),
        Product(6, "Sugar-Free Rusk", 30.0, "https://firebasestorage.googleapis.com/v0/b/sugarfree-df710.appspot.com/o/Sugar-Free%20Rusk.jpg?alt=media&token=330ae00a-b146-405e-a6cc-fcfc2118ebb2"),
        Product(7, "Sugar-Free Ketchup", 120.0, "https://firebasestorage.googleapis.com/v0/b/sugarfree-df710.appspot.com/o/Sugar-Free%20Ketchup.jpg?alt=media&token=ddadaca0-9b15-4295-8e0b-398cda2bb8f2"),
        Product(8, "Sugar-Free Fruit Jam", 150.0, "https://firebasestorage.googleapis.com/v0/b/sugarfree-df710.appspot.com/o/Sugar-Free%20Fruit%20Jam.jpg?alt=media&token=c11b3f25-dfac-4d7b-b8b4-fbf89c85e2d7"),
        Product(9, "Sugar-Free Rasgulla", 360.0, "https://firebasestorage.googleapis.com/v0/b/sugarfree-df710.appspot.com/o/Sugar-Free%20Rasgulla.jpg?alt=media&token=c7de64d9-6b68-47c1-98e8-aadfd5991442"),
        Product(10, "Sugar-Free Chocolate", 80.0, "https://firebasestorage.googleapis.com/v0/b/sugarfree-df710.appspot.com/o/Sugar-Free%20Chocolate.jpg?alt=media&token=6c2cb569-f593-4af0-9ddb-b910ca489091"),
        Product(11, "Sugar-Free Gummies", 10.0, "https://firebasestorage.googleapis.com/v0/b/sugarfree-df710.appspot.com/o/Sugar-Free%20Gummies.jpg?alt=media&token=e8d88006-5635-48a5-a4ba-e2e1b9e4a2a1"),
        Product(12, "Sugar-Free Cookies", 40.0, "https://firebasestorage.googleapis.com/v0/b/sugarfree-df710.appspot.com/o/Sugar-Free%20Cake.webp?alt=media&token=8461aac3-86b2-4923-b519-e050f7617c42"),
        Product(13, "Sugar-Free Honey", 200.0, ""),
        Product(14, "Sugar-Free Peanut Butter", 180.0, ""),
        Product(15, "Sugar-Free Ice Cream", 100.0, ""),
        Product(16, "Sugar-Free Granola", 320.0, ""),
        Product(17, "Sugar-Free Syrup", 140.0, ""),
        Product(18, "Sugar-Free Lollipops", 50.0, ""),
        Product(19, "Sugar-Free Candy", 60.0, ""),
        Product(20, "Sugar-Free Muffin", 25.0, ""),
        Product(21, "Sugar-Free Protein Bars", 220.0, ""),
        Product(22, "Sugar-Free Yogurt", 80.0, ""),
        Product(23, "Sugar-Free Energy Drink", 110.0, ""),
        Product(24, "Sugar-Free Cola", 50.0, ""),
        Product(25, "Sugar-Free Cereal", 210.0, ""),
        Product(26, "Sugar-Free Marshmallows", 90.0, ""),
        Product(27, "Sugar-Free Brownie", 35.0, ""),
        Product(28, "Sugar-Free Donut", 40.0, ""),
        Product(29, "Sugar-Free Trail Mix", 300.0, ""),
        Product(30, "Sugar-Free Almond Butter", 250.0, ""),
        Product(31, "Sugar-Free Popsicles", 45.0, ""),
        Product(32, "Sugar-Free Chewing Gum", 20.0, ""),
        Product(33, "Sugar-Free Lemonade", 70.0, ""),
        Product(34, "Sugar-Free Jellies", 15.0, ""),
        Product(35, "Sugar-Free Choco Spread", 260.0, ""),
        Product(36, "Sugar-Free Ice Cream Sandwich", 85.0, ""),
        Product(37, "Sugar-Free Mango Sorbet", 120.0, ""),
        Product(38, "Sugar-Free Protein Shake", 150.0, ""),
        Product(39, "Sugar-Free Crackers", 100.0, ""),
        Product(40, "Sugar-Free Fig Bars", 90.0, ""),
        Product(41, "Sugar-Free Oatmeal Cookies", 110.0, ""),
        Product(42, "Sugar-Free Wafers", 60.0, ""),
        Product(43, "Sugar-Free Mints", 30.0, ""),
        Product(44, "Sugar-Free Shortbread", 85.0, ""),
        Product(45, "Sugar-Free Pudding", 70.0, ""),
        Product(46, "Sugar-Free Cappuccino Mix", 160.0, ""),
        Product(47, "Sugar-Free Herbal Tea", 80.0, ""),
        Product(48, "Sugar-Free Chocolate Milk", 90.0, ""),
        Product(49, "Sugar-Free Biscotti", 70.0, ""),
        Product(50, "Sugar-Free Apple Pie", 300.0, ""),

        // Additional Products from 51-100
        Product(51, "Sugar-Free Pancake Mix", 130.0, ""),
        Product(52, "Sugar-Free Coconut Water", 90.0, ""),
        Product(53, "Sugar-Free Frosting", 75.0, ""),
        Product(54, "Sugar-Free Croissant", 85.0, ""),
        Product(55, "Sugar-Free Gelato", 100.0, ""),
        Product(56, "Sugar-Free Almond Milk", 60.0, ""),
        Product(57, "Sugar-Free Churros", 110.0, ""),
        Product(58, "Sugar-Free Hot Chocolate", 120.0, ""),
        Product(59, "Sugar-Free Energy Gel", 200.0, ""),
        Product(60, "Sugar-Free Smoothie Mix", 150.0, ""),
        Product(61, "Sugar-Free Macarons", 90.0, ""),
        Product(62, "Sugar-Free Pizza Base", 70.0, ""),
        Product(63, "Sugar-Free Jelly Beans", 50.0, ""),
        Product(64, "Sugar-Free Matcha", 170.0, ""),
        Product(65, "Sugar-Free Soda", 40.0, ""),
        Product(66, "Sugar-Free Trail Bars", 180.0, ""),
        Product(67, "Sugar-Free Instant Coffee", 120.0, ""),
        Product(68, "Sugar-Free Coconut Cookies", 95.0, ""),
        Product(69, "Sugar-Free Spreads", 220.0, ""),
        Product(70, "Sugar-Free Hazelnut Butter", 250.0, ""),
        Product(71, "Sugar-Free Apple Chips", 80.0, ""),
        Product(72, "Sugar-Free Pumpkin Seeds", 90.0, ""),
        Product(73, "Sugar-Free Pretzels", 65.0, ""),
        Product(74, "Sugar-Free Marshmallow Creme", 130.0, ""),
        Product(75, "Sugar-Free Protein Muffins", 140.0, ""),
        Product(76, "Sugar-Free Chocolate Spread", 200.0, ""),
        Product(77, "Sugar-Free Peanut Brittle", 75.0, ""),
        Product(78, "Sugar-Free Frozen Yogurt", 110.0, ""),
        Product(79, "Sugar-Free Cashew Butter", 240.0, ""),
        Product(80, "Sugar-Free Zucchini Bread", 95.0, ""),
        Product(81, "Sugar-Free Milkshake", 85.0, ""),
        Product(82, "Sugar-Free Snack Bars", 70.0, ""),
        Product(83, "Sugar-Free Banana Chips", 65.0, ""),
        Product(84, "Sugar-Free Almond Brittle", 180.0, ""),
        Product(85, "Sugar-Free Energy Bites", 130.0, ""),
        Product(86, "Sugar-Free Carrot Cake", 160.0, ""),
        Product(87, "Sugar-Free Ice Lollies", 55.0, ""),
        Product(88, "Sugar-Free Coffee Syrup", 90.0, ""),
        Product(89, "Sugar-Free Pops", 50.0, ""),
        Product(90, "Sugar-Free Toffee", 60.0, ""),
        Product(91, "Sugar-Free Tiramisu", 250.0, ""),
        Product(92, "Sugar-Free Pecan Pie", 290.0, ""),
        Product(93, "Sugar-Free Protein Pancakes", 210.0, ""),
        Product(94, "Sugar-Free Baked Chips", 100.0, ""),
        Product(95, "Sugar-Free Dried Berries", 110.0, ""),
        Product(96, "Sugar-Free Fruit Bars", 90.0, ""),
        Product(97, "Sugar-Free Ice Cream Bars", 85.0, ""),
        Product(98, "Sugar-Free Cinnamon Rolls", 120.0, ""),
        Product(99, "Sugar-Free Latte Mix", 160.0, ""),
        Product(100, "Sugar-Free Fruit Yogurt", 80.0, "")
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
                text = "Delivery by 26th Oct",
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
