package com.example.sugarfree


import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

val Purple2 = Color(0xFF9370DB)

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Home2(navController: NavController) {
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFEFEFEF) // Light gray background
    ) {
        Scaffold(
            //
            bottomBar = {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primary,
                    content = {
                        Spacer(modifier = Modifier.weight(1f, true))

                        // Food Scanner Icon Button
                        IconButton(onClick = { navController.navigate("foodScanner") }) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "foodScanner")
                        }

                        Spacer(modifier = Modifier.weight(1f, true))

                        // Detox Icon Button (First)
                        IconButton(onClick = { navController.navigate("detox") }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "detox")
                        }

                        Spacer(modifier = Modifier.weight(1f, true))

                        // Detox Icon Button (Second)
                        IconButton(onClick = { navController.navigate("fruitlist") }) {
                            Icon(imageVector = Icons.Default.Info, contentDescription = "fruitlist")
                        }

                        Spacer(modifier = Modifier.weight(1f, true))
                        IconButton(onClick = { navController.navigate("fruitlist") }) {
                            Icon(imageVector = Icons.Default.Home, contentDescription = "fruitlist")
                        }

                        Spacer(modifier = Modifier.weight(1f, true))
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { /* Handle FAB click */ }) {
                    Icon(painterResource(id = R.drawable.dragonfruit), contentDescription = "Add")
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            //isFloatingActionButtonDocked = true,
            content = { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(Color.LightGray)
                ) {
                    DynamicCategoryGrid(navController)
                }
            }
        )
    }
}

// Dynamic grid of categories
@Composable
fun DynamicCategoryGrid(navController: NavController) {
    val categories = listOf(
        Category("Mango", R.drawable.mango, "home2"),
        Category("Cherry", R.drawable.cherry, "Javaa"),
        Category("Dragonfruit", R.drawable.dragonfruit, "os"),
        Category("Pineapple", R.drawable.pinapple, "web"),
        Category("Banana", R.drawable.banana, "android"),
        Category("Orange", R.drawable.orange, "dbms"),
        Category("Pomygranate", R.drawable.pomygranate, "cpp"),

        )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        categories.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                rowItems.forEach { category ->
                    CategoryBox(
                        navController = navController,
                        imageId = category.imageRes,
                        title = category.name,
                        route = category.route
                    )
                }
            }
        }
    }
}

// Category Box Composable
@Composable
fun CategoryBox(navController: NavController, imageId: Int, title: String, route: String) {
    Box(
        modifier = Modifier
            .size(160.dp, 120.dp)
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .clickable { navController.navigate(route) }
            .padding(20.dp)
            .shadow(15.dp, RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = imageId),
                contentDescription = "$title Icon",
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

// Data class to hold category information
data class Category(val name: String, val imageRes: Int, val route: String)
