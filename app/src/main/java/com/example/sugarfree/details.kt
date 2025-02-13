package com.example.sugarfree
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.draw.clip
@Composable
fun HealthTipsScreen(navController: NavController) {
    // List of categories and their corresponding emojis
    val categoriesWithEmojis = listOf(
        "🥒 Vegetables",  // Cucumber emoji for vegetables
        "🍎 Fruits",      // Apple emoji for fruits
        "🥜 Nuts",        // Peanut emoji for nuts
        "🥗 Salads"       // Salad emoji for salads
    )

    // Tips list from the image
    val tipsList = listOf(
        "You should also have a few, 2-3 default meal options that are easy and fast to make, as well as healthy. That way you can always get something quick to eat out of the fridge and doesn't take effort.",
        "If you need to eat out, choose dishes based around a lean protein source and lots of veggies or salad. Avoid those with sauces and that have been fried.",
        "Replace sodas and processed fruit drinks with sparkling water flavored with a squeeze of fresh fruit juice. Nice ones to try are lemon, lime, tangerine, pomegranate, and grapefruit. Adding in fresh mint and cucumber slices makes a refreshing change.",
        "Replace sugar and dairy laden hot drinks with herbal teas, green tea, black coffee, and golden milk. Golden milk is a fragrant chai-style drink made from coconut milk, turmeric, ginger, and other spices. Sweeten hot drinks with a dash of raw honey or maple syrup. Avoid sweetened lattes or sugary coffee shop drinks."
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Add title "What to eat??" on top
        Text(
            text = "What to eat??",
            fontSize = 28.sp,
            color = Color(0xFF6200EA), // Purple color
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Add the image inside a Box with padding on all sides
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.image5), // Replace with actual image resource
                contentDescription = "Top Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)) // Rounded corners for the image
                    .height(200.dp) // Adjust height as needed
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Displaying the category emojis with labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            categoriesWithEmojis.forEach { category ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val parts = category.split(" ") // Split emoji and category name

                    // Display the emoji on one line
                    Text(
                        text = parts[0], // Emoji part
                        fontSize = 40.sp, // Large size for emoji
                        modifier = Modifier.padding(bottom = 4.dp) // Add padding below emoji
                    )
                    // Display the category name on the next line
                    Text(
                        text = parts[1], // Category name part
                        fontSize = 16.sp, // Normal text size for category
                        color = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Title for Tips
        Text(
            text = "4 Tips to Succeed",
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Displaying tips from the image dynamically
        tipsList.forEachIndexed { index, tip ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = "${index + 1}️⃣", // Display the number with emoji
                    fontSize = 24.sp,
                    color = Color(0xFF6200EA), // Purple color for the numbers
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = tip,
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Start button
//        Button(
//            onClick = {
//                // Handle button click logic here
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 8.dp),
//            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6200EA))
//        ) {
//            Text(text = "🚀 START NOW", color = Color.White)
//        }
    }
}

@Composable
fun HealthTipsScreen2(navController: NavController) {
    // Food items with emojis
    val foodItemsWithEmojis = listOf(
        "🥩 Grass-fed Beef",
        "🐟 Wild Salmon",
        "🥓 Bacon",
        "🥚 Eggs",
        "🥜 Raw Nuts",
        "🫒 Olive Oil",
        "🥥 Coconut Oil",
        "🥑 Avocados",
        "🥦 Low-Carb Veggies"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()) // Make the page scrollable
    ) {
        // Add the image inside a Box with padding on all sides
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.image6), // Replace with actual image resource
                contentDescription = "Top Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)) // Rounded corners for the image
                    .height(200.dp) // Adjust height as needed
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Benefits Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Benefits",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF240046) // Dark color for title
            )

        }

        Spacer(modifier = Modifier.height(16.dp))

        // List of benefits with emojis
        Column {
            Text(text = "🐆 Fat loss", fontSize = 16.sp, color = Color.Gray)
            Text(text = "🧠 Mental focus and better mood, thanks to decreased brain inflammation.", fontSize = 16.sp, color = Color.Gray)
            Text(text = "🍉 Decreased hunger and food cravings.", fontSize = 16.sp, color = Color.Gray)
            Text(text = "🚴‍♂️ All-day energy with no crash, thanks to stable blood sugar.", fontSize = 16.sp, color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // What to do section
        Text(
            text = "What to do",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF240046)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Avoid all refined sugars including white sugar, corn syrup and brown sugar. This also includes all added sweeteners such as sugar alcohols, molasses, stevia, coconut palm sugar, xylitol, agave, honey, maple syrup, and all artificial sweeteners such as truvia, splenda, nutrasweet, etc. This is important to start enjoying the real taste of food.",
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // What Can I Eat section
        Text(
            text = "What Can I Eat",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF240046)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Displaying food items with emojis in a 3-column grid-like view
        Column {
            // Using Row to organize the food items with emojis
            for (i in foodItemsWithEmojis.indices step 3) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    foodItemsWithEmojis.getOrNull(i)?.let { item ->
                        Text(text = item, fontSize = 18.sp, color = Color(0xFF240046))
                    }
                    foodItemsWithEmojis.getOrNull(i + 1)?.let { item ->
                        Text(text = item, fontSize = 18.sp, color = Color(0xFF240046))
                    }
                    foodItemsWithEmojis.getOrNull(i + 2)?.let { item ->
                        Text(text = item, fontSize = 18.sp, color = Color(0xFF240046))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Start Now button
//        Button(
//            onClick = {
//                // Handle start now click
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 8.dp),
//            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6200EA))
//        ) {
//            Text(text = "🚀 START NOW", color = Color.White)
//        }
    }
}

@Composable
fun HealthTipsScreen3(navController: NavController) {
    // List of categories and their corresponding emojis
    val categoriesWithEmojis = listOf(
        "🥒 Vegetables",  // Cucumber emoji for vegetables
        "🍎 Fruits",      // Apple emoji for fruits
        "🥜 Nuts",        // Peanut emoji for nuts
        "🥗 Salads"       // Salad emoji for salads
    )

    // Tips list from the image
    val tipsList = listOf(
        "You should also have a few, 2-3 default meal options that are easy and fast to make, as well as healthy. That way you can always get something quick to eat out of the fridge and doesn't take effort.",
        "If you need to eat out, choose dishes based around a lean protein source and lots of veggies or salad. Avoid those with sauces and that have been fried.",
        "Replace sodas and processed fruit drinks with sparkling water flavored with a squeeze of fresh fruit juice. Nice ones to try are lemon, lime, tangerine, pomegranate, and grapefruit. Adding in fresh mint and cucumber slices makes a refreshing change.",
        "Replace sugar and dairy laden hot drinks with herbal teas, green tea, black coffee, and golden milk. Golden milk is a fragrant chai-style drink made from coconut milk, turmeric, ginger, and other spices. Sweeten hot drinks with a dash of raw honey or maple syrup. Avoid sweetened lattes or sugary coffee shop drinks."
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Add title "What to eat??" on top
        Text(
            text = "What to eat??",
            fontSize = 28.sp,
            color = Color(0xFF6200EA), // Purple color
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Add the image inside a Box with padding on all sides
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.image5), // Replace with actual image resource
                contentDescription = "Top Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)) // Rounded corners for the image
                    .height(200.dp) // Adjust height as needed
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Displaying the category emojis with labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            categoriesWithEmojis.forEach { category ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val parts = category.split(" ") // Split emoji and category name

                    // Display the emoji on one line
                    Text(
                        text = parts[0], // Emoji part
                        fontSize = 40.sp, // Large size for emoji
                        modifier = Modifier.padding(bottom = 4.dp) // Add padding below emoji
                    )
                    // Display the category name on the next line
                    Text(
                        text = parts[1], // Category name part
                        fontSize = 16.sp, // Normal text size for category
                        color = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Title for Tips
        Text(
            text = "4 Tips to Succeed",
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Displaying tips from the image dynamically
        tipsList.forEachIndexed { index, tip ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = "${index + 1}️⃣", // Display the number with emoji
                    fontSize = 24.sp,
                    color = Color(0xFF6200EA), // Purple color for the numbers
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = tip,
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

//        // Start button
//        Button(
//            onClick = {
//                // Handle button click logic here
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 8.dp),
//            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6200EA))
//        ) {
//            Text(text = "🚀 START NOW", color = Color.White)
//        }
    }
}