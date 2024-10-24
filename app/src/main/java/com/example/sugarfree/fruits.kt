package com.example.sugarfree

import android.content.Context
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.opencsv.CSVReader
import java.io.InputStreamReader

// Fruit data class to hold name, nutrients, and image filename
//data class FruitNutritionalInfo(val name: String, val nutrients: Map<String, String>, val imageFilename: String)

// Load fruits from CSV
fun loadFruits(context: Context): List<FruitNutritionalInfo> {
    val fruits = mutableListOf<FruitNutritionalInfo>()
    try {
        val inputStream = context.assets.open("fruits.csv")
        val csvReader = CSVReader(InputStreamReader(inputStream))
        val data = csvReader.readAll()

        // Assuming first row contains headers and last column is image filename
        val headers = data[0].toList().drop(1)
        for (i in 1 until data.size) {
            val row = data[i]
            val name = row[0]
            val imageFilename = row.last().trim()  // Last column for image filename
            val nutrients = headers.zip(row.drop(1).dropLast(1)).toMap().mapValues { it.value.trim() }
            fruits.add(FruitNutritionalInfo(name, nutrients, imageFilename))
        }
        csvReader.close()
    } catch (e: Exception) {
        Log.e("CSV Error", "Error reading CSV file: ${e.message}", e)
    }
    return fruits
}

@Composable
fun FruitAppScreen(navController: NavController) {
    val context = LocalContext.current
    val fruitList = remember { loadFruits(context) }
    var searchQuery by remember { mutableStateOf("") }

    // Filter fruits based on search query
    val filteredFruits = fruitList.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    // Wrap with ScrollableColumn to allow scrolling
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search Fruits") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Use LazyColumn for scrollable list of fruits
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(filteredFruits) { fruit ->
                FruitRow(fruit = fruit, navController = navController)
            }
        }
    }
}

@Composable
fun FruitRow(fruit: FruitNutritionalInfo, navController: NavController) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .clickable { navController.navigate("fruitDetails/${fruit.name}") }
            .padding(8.dp)
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(16.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
    ) {
        // Dynamically load image resource by filename (without extension)
        val imageResId = context.resources.getIdentifier(fruit.imageFilename, "drawable", context.packageName)

        if (imageResId != 0) {
            // Display the fruit image
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = fruit.name,
                modifier = Modifier
                    .height(60.dp)
                    .width(60.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Crop
            )
        } else {
            // Fallback to placeholder image if the resource is not found
            Image(
                painter = painterResource(id = R.drawable.placeholder),
                contentDescription = "Placeholder",
                modifier = Modifier
                    .height(60.dp)
                    .width(60.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Crop
            )
        }

        // Display the fruit name
        Text(
            text = fruit.name,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}
@Composable
fun NutritionalInfoDisplay(fruit: FruitNutritionalInfo) {
    val context = LocalContext.current
    val imageResId = context.resources.getIdentifier(fruit.imageFilename, "drawable", context.packageName)

    // Create a scroll state for vertical scrolling
    val scrollState = rememberScrollState()

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(scrollState), // Enable scrolling
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display fruit image
            if (imageResId != 0) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = fruit.name,
                    modifier = Modifier
                        .height(500.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Fallback to placeholder
                Image(
                    painter = painterResource(id = R.drawable.placeholder),
                    contentDescription = "Placeholder",
                    modifier = Modifier
                        .height(500.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display the fruit name
            Text(
                text = fruit.name,
                fontSize = 24.sp,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Display nutrients
            Text(
                text = "Nutritional Information:",
                fontSize = 20.sp,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Nutrients in a list format
            Column {
                fruit.nutrients.filter { it.value.isNotEmpty() && it.value != "0" && it.value != "0.0" }
                    .forEach { (nutrient, value) ->
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text(
                                text = "$nutrient:",
                                modifier = Modifier.weight(1f),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = value,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
            }
        }
    }
}
