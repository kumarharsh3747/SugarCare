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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.opencsv.CSVReader
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader

// Data class to hold fruit information
//data class FruitNutritionalInfo(val name: String, val nutrients: Map<String, String>, val imageUrl: String)

// Load fruits with caching and timestamp check
fun loadFruitsWithCacheAndTimestampCheck(context: Context, onFruitsLoaded: (List<FruitNutritionalInfo>) -> Unit) {
    val cacheFile = File(context.cacheDir, "fruits.csv")
    val prefs = context.getSharedPreferences("FruitAppPrefs", Context.MODE_PRIVATE)
    val lastDownloadedTimestamp = prefs.getLong("csv_last_modified", 0L)

    val storage = Firebase.storage
    val storageRef = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/sugarfree-df710.appspot.com/o/fruits.csv?alt=media&token=9b533676-f84a-436f-a81f-b942e5c5e3e0") // Firebase CSV URL

    // Check metadata to see if the file is updated
    storageRef.metadata.addOnSuccessListener { metadata ->
        val firebaseTimestamp = metadata.updatedTimeMillis

        // Check if the local file is outdated
        if (cacheFile.exists() && firebaseTimestamp == lastDownloadedTimestamp) {
            // Load from cache if it exists and matches the timestamp
            loadFruitsFromLocalFile(cacheFile, onFruitsLoaded)
        } else {
            // Download new file and update the timestamp in SharedPreferences
            storageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener { bytes ->
                // Save to cache, overwriting if exists
                FileOutputStream(cacheFile).use { it.write(bytes) }

                // Update the last modified timestamp
                prefs.edit().putLong("csv_last_modified", firebaseTimestamp).apply()

                // Load the new data from the cached file
                loadFruitsFromBytes(bytes, onFruitsLoaded)
            }.addOnFailureListener { e ->
                Log.e("Firebase Error", "Error downloading CSV file: ${e.message}", e)
            }
        }
    }.addOnFailureListener { e ->
        Log.e("Metadata Error", "Failed to get metadata: ${e.message}", e)
    }
}

// Load fruits from a local CSV file
fun loadFruitsFromLocalFile(file: File, onFruitsLoaded: (List<FruitNutritionalInfo>) -> Unit) {
    val fruits = mutableListOf<FruitNutritionalInfo>()
    try {
        val csvReader = CSVReader(InputStreamReader(file.inputStream()))
        val data = csvReader.readAll()

        val headers = data[0].toList().drop(1)
        for (i in 1 until data.size) {
            val row = data[i]
            val name = row[0]
            val imageUrl = row.last().trim()
            val nutrients = headers.zip(row.drop(1).dropLast(1)).toMap().mapValues { it.value.trim() }
            fruits.add(FruitNutritionalInfo(name, nutrients, imageUrl))
        }
        csvReader.close()
    } catch (e: Exception) {
        Log.e("CSV Error", "Error reading CSV file: ${e.message}", e)
    }
    onFruitsLoaded(fruits)
}

// Load fruits from downloaded byte data
fun loadFruitsFromBytes(bytes: ByteArray, onFruitsLoaded: (List<FruitNutritionalInfo>) -> Unit) {
    val fruits = mutableListOf<FruitNutritionalInfo>()
    try {
        val csvReader = CSVReader(InputStreamReader(ByteArrayInputStream(bytes)))
        val data = csvReader.readAll()

        val headers = data[0].toList().drop(1)
        for (i in 1 until data.size) {
            val row = data[i]
            val name = row[0]
            val imageUrl = row.last().trim()
            val nutrients = headers.zip(row.drop(1).dropLast(1)).toMap().mapValues { it.value.trim() }
            fruits.add(FruitNutritionalInfo(name, nutrients, imageUrl))
        }
        csvReader.close()
    } catch (e: Exception) {
        Log.e("CSV Error", "Error reading CSV file from bytes: ${e.message}", e)
    }
    onFruitsLoaded(fruits)
}

@Composable
fun FruitAppScreen(navController: NavController) {
    val context = LocalContext.current
    var fruitList by remember { mutableStateOf(listOf<FruitNutritionalInfo>()) }
    var searchQuery by remember { mutableStateOf("") }

    // Load fruits from Firebase or cache
    LaunchedEffect(Unit) {
        loadFruitsWithCacheAndTimestampCheck(context) { loadedFruits ->
            fruitList = loadedFruits
        }
    }

    // Filter fruits based on search query
    val filteredFruits = fruitList.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

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

        // Scrollable list of fruits
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(filteredFruits) { fruit ->
                FruitRow(fruit = fruit, navController = navController)
            }
        }
    }
}

@Composable
fun FruitRow(fruit: FruitNutritionalInfo, navController: NavController) {
    Row(
        modifier = Modifier
            .clickable { navController.navigate("fruitDetails/${fruit.name}") }
            .padding(8.dp)
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(16.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
    ) {
        AsyncImage(
            model = fruit.imageUrl,
            contentDescription = fruit.name,
            modifier = Modifier
                .height(60.dp)
                .width(60.dp)
                .padding(end = 16.dp),
            contentScale = ContentScale.Crop
        )

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
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Image in a square box with padding on all sides
            Box(
                modifier = Modifier
                    .size(500.dp) // Set a square size for the box
                    .padding(16.dp) // Padding around the image box
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
            ) {
                AsyncImage(
                    model = fruit.imageUrl,
                    contentDescription = fruit.name,
                    modifier = Modifier
                        .fillMaxSize(), // Fill the box size
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = fruit.name,
                fontSize = 24.sp,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Nutritional Information:",
                fontSize = 20.sp,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

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
