package com.example.sugarfree

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
@Composable
fun FetchNutritionInfoScreen(barcode: String) {
    val scope = rememberCoroutineScope()
    var foodData by remember { mutableStateOf<ProductDetails?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(barcode) {
        scope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.searchFoodByBarcode(barcode)
                val foodDetails = response.product
                if (foodDetails != null) {
                    foodData = foodDetails
                } else {
                    error = "No food found"
                }
            } catch (e: Exception) {
                error = "Error fetching data: ${e.message}"
            }
            isLoading = false
        }
    }


    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        when {
            isLoading -> CircularProgressIndicator()
            error != null -> Text(error!!, color = MaterialTheme.colorScheme.error)
            foodData != null -> FoodNutritionDisplay(foodData!!)
        }
    }
}

@Composable
fun FoodNutritionDisplay(food: ProductDetails) {
    Column(Modifier.padding(16.dp)) {
        Text(food.product_name ?: "Unknown Product", style = MaterialTheme.typography.headlineSmall)
        food.nutriments?.let {
            Text("Energy: ${it.energy_kcal} kcal")
            Text("Fat: ${it.fat} g")
            Text("Carbs: ${it.carbohydrates} g")
            Text("Sugars: ${it.sugars} g")
            Text("Proteins: ${it.proteins} g")
        }
        Text("Ingredients: ${food.ingredients_text ?: "Not available"}")
    }
}
