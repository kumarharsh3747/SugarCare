package com.example.sugarfree

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

@Composable
fun FruitDetailsScreen(fruitName: String) {
    val context = LocalContext.current
    var fruitList by remember { mutableStateOf<List<FruitNutritionalInfo>>(emptyList()) }
    var selectedFruit by remember { mutableStateOf<FruitNutritionalInfo?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Launch a coroutine to download and load the fruits data from Firebase Storage
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            loadFruitsWithCacheAndTimestampCheck(context) { loadedFruits ->
                fruitList = loadedFruits
                selectedFruit = fruitList.find { it.name == fruitName }
            }
        }
    }

    // Display nutritional information if the selected fruit is available
    selectedFruit?.let {
        NutritionalInfoDisplay(it)
    }
}