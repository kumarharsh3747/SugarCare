package com.example.sugarfree

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DietPlans() {
    // Days of the week
    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    // Diet chart data for each day
    val mondayDietChart = listOf(
        "Breakfast (8:00-8:30AM): 2 Slice brown bread + 1 slice low fat cheese + 1 Boiled egg + 1/2 cup low fat milk.",
        "Mid-Meal (11:00-11:30AM): 1 Portion fruit (Avoid high energy fruits. Eg: Banana, Mango, Chikku.)",
        "Lunch (2:00-2:30PM): Veg pulav rice 1 cup + 1/2 cup Soya Chunk curry + 1/2 cup Low fat curd.",
        "Evening (4:00-4:30PM): 1 cup light tea + 2 wheat rusk.",
        "Dinner (8:00-8:30PM): 2 roti/ Chapathi + Ladies finger subji 1/2 cup."
    )

    val tuesdayDietChart = listOf(
        "Breakfast (8:00-8:30AM): Chappati 3 + 1/2 cup Potato green peas curry.",
        "Mid-Meal (11:00-11:30AM): 1/2 cup boiled black channa.",
        "Lunch (2:00-2:30PM): 1 cup rice + 1/2 cup Dhal + Palak subji 1/2 cup + 1/2 cup low fat curd.",
        "Evening (4:00-4:30PM): 1 Portion fruit (Avoid high energy fruits. Eg: Banana, Mango, Chikku.)",
        "Dinner (8:00-8:30PM): Broken wheat upma 1 cup + 1/2 cup green beans subji."
    )

    val wednesdayDietChart = listOf(
        "Breakfast (8:00-8:30AM): Methi Parata 2 + 1 tbs green chutney.",
        "Mid-Meal (11:00-11:30AM): 1 Portion fruit (Avoid high energy fruits. Eg: Banana, Mango, Chikku.)",
        "Lunch (2:00-2:30PM): 1 cup rice + chicken curry (150 gm chicken + 1 cup cucumber salad).",
        "Evening (4:00-4:30PM): 1 Cup light tea + Brown rice flakes poha 1 cup.",
        "Dinner (8:00-8:30PM): Wheat dosa 3 + 1/2 cup Bitter guard subji."
    )

    val thursdayDietChart = listOf(
        "Breakfast (8:00-8:30AM): Vegetable Oats Upma 1 cup + 1/2 cup low fat milk.",
        "Mid-Meal (11:00-11:30AM): Plain Yoghurt with raw vegetables / grilled vegetables - 1 cup.",
        "Lunch (2:00-2:30PM): 1/2 cup rice + 2 medium chappati + 1/2 cup Kidney beans curry + Snake guard subji 1/2 cup.",
        "Evening (4:00-4:30PM): 1 cup boiled channa + light tea 1 cup.",
        "Dinner (8:00-8:30PM): 2 Roti/ chapati + 1/2 cup mix veg curry."
    )

    val fridayDietChart = listOf(
        "Breakfast (8:00-8:30AM): Mix veg Poha 1 cup + 1/2 cup low fat milk.",
        "Mid-Meal (11:00-11:30AM): 1 Portion fruit (Avoid high energy fruits. Eg: Banana, Mango, Chikku.)",
        "Lunch (2:00-2:30PM): 3 Chappati + 1/2 cup cluster beans subji + Fish curry (100g fish) 1/2 cup.",
        "Evening (4:00-4:30PM): 1 cup tea + 2 biscuits (Nutrichoice or Digestiva or Oatmeal.)",
        "Dinner (8:00-8:30PM): 2 Roti / chappathi + Ridge guard subji 1/2 cup."
    )

    val saturdayDietChart = listOf(
        "Breakfast (8:00-8:30AM): Utappam 2 + 1 tbs green chutney.",
        "Mid-Meal (11:00-11:30AM): 1 cup boiled channa.",
        "Lunch (2:00-2:30PM): 1 cup rice + Soya chunk curry 1/2 cup + Ladies finger subji 1/2 cup + small cup low fat curd.",
        "Evening (4:00-4:30PM): 1 Portion fruit (Avoid high energy fruits. Eg: Banana, Mango, Chikku.)",
        "Dinner (8:00-8:30PM): Broken wheat upma 1 cup + 1/2 cup green beans subji."
    )

    val sundayDietChart = listOf(
        "Breakfast (8:00-8:30AM): 4 Idli + Sambar 1/2 cup / 1 table spoon Green chutney / Tomato Chutney.",
        "Mid-Meal (11:00-11:30AM): Green gram sprouts 1 cup.",
        "Lunch (2:00-2:30PM): 3 Roti + 1/2 cup salad + Fish curry (100 gm fish) + 1/2 cup cabbage subji.",
        "Evening (4:00-4:30PM): 1 Portion fruit (Avoid high energy fruits. Eg: Banana, Mango, Chikku.)",
        "Dinner (8:00-8:30PM): 2 Roti / chappati + Tomato subji 1/2 cup."
    )

    // Track the checked state for each meal
    var checkedStates by remember { mutableStateOf(MutableList(7 * 5) { false }) } // 7 days * 5 meals

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Diet Plans",fontSize = 40.sp, fontWeight = FontWeight.Bold,color = MaterialTheme.colorScheme.onSecondary) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0x7C0F0F3B)
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE3D4EA))
                .padding(paddingValues) // Padding to accommodate the TopAppBar
                .padding(20.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            item {
               /* Text(text = "Diet Plans for Diabetic Patients", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))*/

                // Row for circular buttons representing days of the week
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(daysOfWeek) { day ->
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape)
                                .background(Color(0x7C0F0F3B)) // Set background color here of buttons
                                .clickable { /* Handle day click */ }
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = day, fontSize = 18.sp, fontWeight = FontWeight.Bold,color = MaterialTheme.colorScheme.onSecondary)
                        }
                    }
                }
            }

            // Function to display diet chart for a specific day
            fun displayDietChart(title: String, dietChart: List<String>, offset: Int) {
                // Subheading
                item {
                    Text(
                        text = title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp) // Space below the heading
                    )

                    // Box for diet chart with lavender background
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .border(BorderStroke(1.dp, Color.Gray)) // Border around the box
                            .background(Color(0xFFE6E6FA)) // Set background color here (lavender)
                            .padding(16.dp) // Padding inside the box
                    ) {
                        Column {
                            dietChart.forEachIndexed { index, meal ->
                                Row(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = checkedStates[offset + index],
                                        onCheckedChange = { isChecked ->
                                            checkedStates[offset + index] = isChecked
                                        }
                                    )
                                    Text(text = meal, modifier = Modifier.padding(start = 8.dp))
                                }
                            }
                        }
                    }
                }
            }

            // Display each day's diet chart with offsets
            displayDietChart("Monday Diet Chart", mondayDietChart, 0)
            displayDietChart("Tuesday Diet Chart", tuesdayDietChart, 5)
            displayDietChart("Wednesday Diet Chart", wednesdayDietChart, 10)
            displayDietChart("Thursday Diet Chart", thursdayDietChart, 15)
            displayDietChart("Friday Diet Chart", fridayDietChart, 20)
            displayDietChart("Saturday Diet Chart", saturdayDietChart, 25)
            displayDietChart("Sunday Diet Chart", sundayDietChart, 30)
        }
    }
}

