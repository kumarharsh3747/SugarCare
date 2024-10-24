package com.example.sugarfree

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun FruitDetailsScreen(fruitName: String) {
    val context = LocalContext.current
    val fruitList = remember { loadFruits(context) }
    val selectedFruit = fruitList.find { it.name == fruitName }

    selectedFruit?.let {
        NutritionalInfoDisplay(it)
    }
}
