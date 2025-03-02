package com.example.sugarfree

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun CalculatorScreen(navController: NavController, calculatorType: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5EFF7)) // Light purple background
            .padding(46.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$calculatorType Calculator",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0A0A0A), // Dark purple text Heading
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when (calculatorType) {
            "BMI" -> BMICalculator()
            "Waist to Height ratio" -> WaistToHeightCalculator()
            "Lean Body Mass" -> LeanBodyMassCalculator()
            "Daily Calories" -> DailyCaloriesCalculator()
            "Energy Expenditure" -> EnergyExpenditureCalculator()
            "Weight Loss" -> WeightLossCalculator()
            "Calories Burned" -> CaloriesBurnedCalculator()
            "Body Water" -> BodyWaterCalculator()
            "Blood Volume" -> BloodVolumeCalculator()
            "Blood Pressure" -> BloodPressureCalculator()
            "Blood Alcohol" -> BloodAlcoholCalculator()
            "Smoking Cost" -> SmokingCostCalculator()
            "Water Requirement" -> WaterRequirementCalculator()
            "Body Fat (%)" -> BodyFatCalculator()
        }
    }
}


@Composable
fun BMICalculator() {
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var bmi by remember { mutableStateOf<Double?>(null) }

    Column {
        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Enter Weight (kg)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9A7CC9), // Blue border when focused
                unfocusedBorderColor = Color.Gray, // Gray border when not focused
                backgroundColor = Color(0xFFD2CBDE), // Light Gray background
                textColor = Color.Black // Text color inside the field
            )
        )

        OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Enter Height (m)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9A7CC9), // Blue border when focused
                unfocusedBorderColor = Color.Gray, // Gray border when not focused
                backgroundColor = Color(0xFFD2CBDE), // Light Gray background
                textColor = Color.Black // Text color inside the field
            )
        )

        Button(
            onClick = {
                val w = weight.toDoubleOrNull()
                val h = height.toDoubleOrNull()
                if (w != null && h != null && h > 0) {
                    bmi = w / (h * h)
                }
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0x7C0F0F3B)) // Change this color
        ) {
            Text("Calculate BMI", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        bmi?.let {
            Text(text = "Your BMI: %.2f".format(it), fontSize = 18.sp, modifier = Modifier.padding(top = 16.dp))
        }
    }
}

@Composable
fun WaistToHeightCalculator() {
    var waist by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var ratio by remember { mutableStateOf<Double?>(null) }

    Column {
        OutlinedTextField(
            value = waist,
            onValueChange = { waist = it },
            label = { Text("Enter Waist Circumference (cm)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9A7CC9), // Blue border when focused
                unfocusedBorderColor = Color.Gray, // Gray border when not focused
                backgroundColor = Color(0xFFD2CBDE), // Light Gray background
                textColor = Color.Black // Text color inside the field
            )
        )

        OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Enter Height (cm)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9A7CC9), // Blue border when focused
                unfocusedBorderColor = Color.Gray, // Gray border when not focused
                backgroundColor = Color(0xFFD2CBDE), // Light Gray background
                textColor = Color.Black // Text color inside the field
            )
        )

        Button(
            onClick = {
                val w = waist.toDoubleOrNull()
                val h = height.toDoubleOrNull()
                if (w != null && h != null && h > 0) {
                    ratio = w / h
                }
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0x7C0F0F3B)) // Change this color
        ) {
            Text("Calculate Ratio", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        ratio?.let {
            Text(text = "Waist-to-Height Ratio: %.2f".format(it), fontSize = 18.sp, modifier = Modifier.padding(top = 16.dp))
        }
    }
}

@Composable
fun LeanBodyMassCalculator() {
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var leanBodyMass by remember { mutableStateOf<Double?>(null) }

    Column {
        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Enter Weight (kg)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9A7CC9), // Blue border when focused
                unfocusedBorderColor = Color.Gray, // Gray border when not focused
                backgroundColor = Color(0xFFD2CBDE), // Light Gray background
                textColor = Color.Black // Text color inside the field
            )
        )

        OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Enter Height (cm)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9A7CC9), // Blue border when focused
                unfocusedBorderColor = Color.Gray, // Gray border when not focused
                backgroundColor = Color(0xFFD2CBDE), // Light Gray background
                textColor = Color.Black // Text color inside the field
            )
        )

        Row {
            Text("Gender: ")
            Button(onClick = { gender = "Male" }) { Text("Male") }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { gender = "Female" }) { Text("Female") }
        }

        Button(
            onClick = {
                val w = weight.toDoubleOrNull()
                val h = height.toDoubleOrNull()
                if (w != null && h != null && h > 0) {
                    leanBodyMass = if (gender == "Male") {
                        (0.407 * w) + (0.267 * h) - 19.2
                    } else {
                        (0.252 * w) + (0.473 * h) - 48.3
                    }
                }
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0x7C0F0F3B)) // Change this color
        ) {
            Text("Calculate L B M", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        leanBodyMass?.let {
            Text(text = "Lean Body Mass: %.2f kg".format(it), fontSize = 18.sp, modifier = Modifier.padding(top = 16.dp))
        }
    }
}

@Composable
fun DailyCaloriesCalculator() {
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var calories by remember { mutableStateOf<Double?>(null) }

    Column(modifier = Modifier.fillMaxWidth()) {
       // Text("Daily Calories Calculator", fontSize = 20.sp)

        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Enter Weight (kg)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9A7CC9), // Blue border when focused
                unfocusedBorderColor = Color.Gray, // Gray border when not focused
                backgroundColor = Color(0xFFD2CBDE), // Light Gray background
                textColor = Color.Black // Text color inside the field
            )
        )

        OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Enter Height (cm)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9A7CC9), // Blue border when focused
                unfocusedBorderColor = Color.Gray, // Gray border when not focused
                backgroundColor = Color(0xFFD2CBDE), // Light Gray background
                textColor = Color.Black // Text color inside the field
            )
        )

        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Enter Age") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9A7CC9), // Blue border when focused
                unfocusedBorderColor = Color.Gray, // Gray border when not focused
                backgroundColor = Color(0xFFD2CBDE), // Light Gray background
                textColor = Color.Black // Text color inside the field
            )
        )

        Row {
            Text("Gender: ")
            Button(onClick = { gender = "Male" }) { Text("Male") }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { gender = "Female" }) { Text("Female") }
        }

        Button(
            onClick = {
                val w = weight.toDoubleOrNull()
                val h = height.toDoubleOrNull()
                val a = age.toIntOrNull()
                if (w != null && h != null && a != null) {
                    calories = if (gender == "Male") {
                        88.36 + (13.4 * w) + (4.8 * h) - (5.7 * a)
                    } else {
                        447.6 + (9.2 * w) + (3.1 * h) - (4.3 * a)
                    }
                }
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0x7C0F0F3B)) // Change this color
        ) {
            Text("Calculate Calories", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        calories?.let {
            Text(
                text = "Daily Calories Requirement: %.2f kcal".format(it),
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun EnergyExpenditureCalculator() {
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var activityLevel by remember { mutableStateOf(1.2) } // Default to Sedentary
    var tdee by remember { mutableStateOf<Double?>(null) }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
       // Text("Energy Expenditure Calculator", fontSize = 20.sp)

        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Enter Weight (kg)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9A7CC9), // Blue border when focused
                unfocusedBorderColor = Color.Gray, // Gray border when not focused
                backgroundColor = Color(0xFFD2CBDE), // Light Gray background
                textColor = Color.Black // Text color inside the field
            )
        )

        OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Enter Height (cm)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9A7CC9), // Blue border when focused
                unfocusedBorderColor = Color.Gray, // Gray border when not focused
                backgroundColor = Color(0xFFD2CBDE), // Light Gray background
                textColor = Color.Black // Text color inside the field
            )
        )

        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Enter Age") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9A7CC9), // Blue border when focused
                unfocusedBorderColor = Color.Gray, // Gray border when not focused
                backgroundColor = Color(0xFFD2CBDE), // Light Gray background
                textColor = Color.Black // Text color inside the field
            )
        )

        // Gender selection (Male / Female)
        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            Text("Gender: ")
            Spacer(modifier = Modifier.width(8.dp))
            listOf("Male", "Female").forEach {
                Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                    RadioButton(
                        selected = gender == it,
                        onClick = { gender = it }
                    )
                    Text(it)
                }
            }
        }

        // Activity Level Dropdown
        val activityLevels = mapOf(
            "Sedentary (little to no exercise)" to 1.2,
            "Lightly active (1-3 days/week)" to 1.375,
            "Moderately active (3-5 days/week)" to 1.55,
            "Very active (6-7 days/week)" to 1.725,
            "Super active (athlete)" to 1.9
        )

        var expanded by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = { expanded = true }) {
                Text(activityLevels.keys.firstOrNull { activityLevels[it] == activityLevel } ?: "Select Activity Level")
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                activityLevels.forEach { (label, factor) ->
                    DropdownMenuItem(onClick = {
                        activityLevel = factor
                        expanded = false
                    }) {
                        Text(label)
                    }
                }
            }
        }

        // Calculate TDEE button
        Button(
            onClick = {
                val w = weight.toDoubleOrNull()
                val h = height.toDoubleOrNull()
                val a = age.toIntOrNull()
                if (w != null && h != null && a != null) {
                    val bmr = if (gender == "Male") {
                        10 * w + 6.25 * h - 5 * a + 5
                    } else {
                        10 * w + 6.25 * h - 5 * a - 161
                    }
                    tdee = bmr * activityLevel
                }
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0x7C0F0F3B)) // Change this color
        ) {
            Text("Calculate TDEE", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        // Display TDEE result
        tdee?.let {
            Text(
                text = "Your Estimated TDEE: %.2f kcal/day".format(it),
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun WeightLossCalculator() {
    var currentWeight by remember { mutableStateOf("") }
    var targetWeight by remember { mutableStateOf("") }
    var calorieDeficit by remember { mutableStateOf("") }
    var daysRequired by remember { mutableStateOf<Int?>(null) }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        OutlinedTextField(
            value = currentWeight,
            onValueChange = { currentWeight = it },
            label = { Text("Current Weight (kg)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9A7CC9), // Blue border when focused
                unfocusedBorderColor = Color.Gray, // Gray border when not focused
                backgroundColor = Color(0xFFD2CBDE), // Light Gray background
                textColor = Color.Black // Text color inside the field
            )
        )

        OutlinedTextField(
            value = targetWeight,
            onValueChange = { targetWeight = it },
            label = { Text("Target Weight (kg)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9A7CC9), // Blue border when focused
                unfocusedBorderColor = Color.Gray, // Gray border when not focused
                backgroundColor = Color(0xFFD2CBDE), // Light Gray background
                textColor = Color.Black // Text color inside the field
            )
        )

        OutlinedTextField(
            value = calorieDeficit,
            onValueChange = { calorieDeficit = it },
            label = { Text("Daily Caloric Deficit (kcal)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9A7CC9), // Blue border when focused
                unfocusedBorderColor = Color.Gray, // Gray border when not focused
                backgroundColor = Color(0xFFD2CBDE), // Light Gray background
                textColor = Color.Black // Text color inside the field
            )
        )

        Button(
            onClick = {
                val current = currentWeight.toDoubleOrNull()
                val target = targetWeight.toDoubleOrNull()
                val deficit = calorieDeficit.toDoubleOrNull()

                if (current != null && target != null && deficit != null && deficit > 0) {
                    val weightLossRequired = current - target
                    val days = (weightLossRequired * 7700) / deficit
                    daysRequired = days.toInt()
                }
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0x7C0F0F3B)) // Change this color
        ) {
            Text("Calculate", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        daysRequired?.let {
            Text(
                text = "Estimated Time: $it days",
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun CaloriesBurnedCalculator() {
    var weight by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var metValue by remember { mutableStateOf(8.0) } // Default MET for moderate exercise
    var caloriesBurned by remember { mutableStateOf<Double?>(null) }

    val activityMetValues = mapOf(
        "Walking (3mph)" to 3.5,
        "Running (6mph)" to 10.0,
        "Cycling" to 8.0,
        "Swimming" to 7.0,
        "Jump Rope" to 12.0
    )

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight (kg)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9A7CC9), // Blue border when focused
                unfocusedBorderColor = Color.Gray, // Gray border when not focused
                backgroundColor = Color(0xFFD2CBDE), // Light Gray background
                textColor = Color.Black // Text color inside the field
            )
        )

        OutlinedTextField(
            value = duration,
            onValueChange = { duration = it },
            label = { Text("Duration (minutes)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9A7CC9), // Blue border when focused
                unfocusedBorderColor = Color.Gray, // Gray border when not focused
                backgroundColor = Color(0xFFD2CBDE), // Light Gray background
                textColor = Color.Black // Text color inside the field
            )
        )

        var expanded by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = { expanded = true }) {
                Text(activityMetValues.keys.firstOrNull { activityMetValues[it] == metValue } ?: "Select Activity")
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                activityMetValues.forEach { (activity, met) ->
                    DropdownMenuItem(onClick = {
                        metValue = met
                        expanded = false
                    }) {
                        Text(activity)
                    }
                }
            }
        }

        Button(
            onClick = {
                val w = weight.toDoubleOrNull()
                val t = duration.toDoubleOrNull()

                if (w != null && t != null) {
                    caloriesBurned = (metValue * w * (t / 60))
                }
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0x7C0F0F3B)) // Change this color
        ) {
            Text("Calculate", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        caloriesBurned?.let {
            Text(
                text = "Calories Burned: %.2f kcal".format(it),
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun BodyWaterCalculator() {
    var weight by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var bodyWater by remember { mutableStateOf<Double?>(null) }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight (kg)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9A7CC9), // Blue border when focused
                unfocusedBorderColor = Color.Gray, // Gray border when not focused
                backgroundColor = Color(0xFFD2CBDE), // Light Gray background
                textColor = Color.Black // Text color inside the field
            )
        )

        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Age") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9A7CC9), // Blue border when focused
                unfocusedBorderColor = Color.Gray, // Gray border when not focused
                backgroundColor = Color(0xFFD2CBDE), // Light Gray background
                textColor = Color.Black // Text color inside the field
            )
        )

        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            Text("Gender: ")
            Spacer(modifier = Modifier.width(8.dp))
            listOf("Male", "Female").forEach {
                Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                    RadioButton(
                        selected = gender == it,
                        onClick = { gender = it }
                    )
                    Text(it)
                }
            }
        }

        Button(
            onClick = {
                val w = weight.toDoubleOrNull()
                val a = age.toIntOrNull()

                if (w != null && a != null) {
                    bodyWater = if (gender == "Male") {
                        (2.447 - (0.09156 * a) + (0.1074 * w)) * w
                    } else {
                        (2.097 - (0.1069 * a) + (0.2466 * w)) * w
                    }
                }
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0x7C0F0F3B)) // Change this color
        ) {
            Text("Calculate", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        bodyWater?.let {
            Text(
                text = "Estimated Body Water: %.2f L".format(it),
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}
@Composable
fun BloodVolumeCalculator() {
    var weight by remember { mutableStateOf("") }
    var bloodVolume by remember { mutableStateOf<Double?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {


        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Enter Weight (kg)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9A7CC9), // Blue border when focused
                unfocusedBorderColor = Color.Gray, // Gray border when not focused
                backgroundColor = Color(0xFFD2CBDE), // Light Gray background
                textColor = Color.Black // Text color inside the field
            )
        )

        Button(onClick = {
            val w = weight.toDoubleOrNull()
            if (w != null) {
                bloodVolume = w * 0.07 // Approximate blood volume formula
            }
        },
            modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0x7C0F0F3B)) // Change this color
        ) {
            Text("Blood Volume", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        bloodVolume?.let {
            Text(text = "Your Blood Volume: %.2f liters".format(it), fontSize = 18.sp, modifier = Modifier.padding(top = 16.dp))
        }
    }
}

@Composable
fun BloodPressureCalculator() {
    var systolic by remember { mutableStateOf("") }
    var diastolic by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {


        OutlinedTextField(
            value = systolic,
            onValueChange = { systolic = it },
            label = { Text("Systolic (mmHg)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9A7CC9), // Blue border when focused
                unfocusedBorderColor = Color.Gray, // Gray border when not focused
                backgroundColor = Color(0xFFD2CBDE), // Light Gray background
                textColor = Color.Black // Text color inside the field
            )
        )

        OutlinedTextField(
            value = diastolic,
            onValueChange = { diastolic = it },
            label = { Text("Diastolic (mmHg)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9A7CC9), // Blue border when focused
                unfocusedBorderColor = Color.Gray, // Gray border when not focused
                backgroundColor = Color(0xFFD2CBDE), // Light Gray background
                textColor = Color.Black // Text color inside the field
            )
        )

        Button(onClick = {
            val sys = systolic.toIntOrNull()
            val dia = diastolic.toIntOrNull()

            if (sys != null && dia != null) {
                message = when {
                    sys < 90 || dia < 60 -> "Low Blood Pressure"
                    sys in 90..120 && dia in 60..80 -> "Normal Blood Pressure"
                    sys > 120 || dia > 80 -> "High Blood Pressure"
                    else -> "Invalid Values"
                }
            }
        },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0x7C0F0F3B)) // Change this color
        ) {
            Text("Check BP", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Text(text = message, fontSize = 18.sp, modifier = Modifier.padding(top = 16.dp))
    }
}

@Composable
fun BloodAlcoholCalculator() {
    var weight by remember { mutableStateOf("") }
    var drinks by remember { mutableStateOf("") }
    var bac by remember { mutableStateOf<Double?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {


        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Enter Weight (kg)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9A7CC9), // Blue border when focused
                unfocusedBorderColor = Color.Gray, // Gray border when not focused
                backgroundColor = Color(0xFFD2CBDE), // Light Gray background
                textColor = Color.Black // Text color inside the field
            )
        )

        OutlinedTextField(
            value = drinks,
            onValueChange = { drinks = it },
            label = { Text("Number of Drinks") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9A7CC9), // Blue border when focused
                unfocusedBorderColor = Color.Gray, // Gray border when not focused
                backgroundColor = Color(0xFFD2CBDE), // Light Gray background
                textColor = Color.Black // Text color inside the field
            )
        )

        Button(onClick = {
            val w = weight.toDoubleOrNull()
            val d = drinks.toDoubleOrNull()
            if (w != null && d != null) {
                bac = (d * 10) / (w * 0.68) // Simplified BAC formula
            }
        },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0x7C0F0F3B)) // Change this color
        ) {
            Text("Calculate BAC", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        bac?.let {
            Text(text = "Your BAC: %.3f".format(it), fontSize = 18.sp, modifier = Modifier.padding(top = 16.dp))
        }
    }
}

@Composable
fun SmokingCostCalculator() {
    var packsPerDay by remember { mutableStateOf("") }
    var pricePerPack by remember { mutableStateOf("") }
    var yearlyCost by remember { mutableStateOf<Double?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {

        OutlinedTextField(
            value = packsPerDay,
            onValueChange = { packsPerDay = it },
            label = { Text("Packs Per Day") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9A7CC9), // Blue border when focused
                unfocusedBorderColor = Color.Gray, // Gray border when not focused
                backgroundColor = Color(0xFFD2CBDE), // Light Gray background
                textColor = Color.Black // Text color inside the field
            )
        )

        OutlinedTextField(
            value = pricePerPack,
            onValueChange = { pricePerPack = it },
            label = { Text("Price Per Pack") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9A7CC9), // Blue border when focused
                unfocusedBorderColor = Color.Gray, // Gray border when not focused
                backgroundColor = Color(0xFFD2CBDE), // Light Gray background
                textColor = Color.Black // Text color inside the field
            )
        )

        Button(onClick = {
            val packs = packsPerDay.toDoubleOrNull()
            val price = pricePerPack.toDoubleOrNull()
            if (packs != null && price != null) {
                yearlyCost = packs * price * 365
            }
        },
            modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0x7C0F0F3B)) // Change this color
        ) {
            Text("Yearly Cost", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        yearlyCost?.let {
            Text(text = "Your Yearly Smoking Cost: $%.2f".format(it), fontSize = 18.sp, modifier = Modifier.padding(top = 16.dp))
        }
    }
}

@Composable
fun WaterRequirementCalculator() {
    var weight by remember { mutableStateOf("") }
    var waterIntake by remember { mutableStateOf<Double?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {

        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Enter Weight (kg)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9A7CC9), // Blue border when focused
                unfocusedBorderColor = Color.Gray, // Gray border when not focused
                backgroundColor = Color(0xFFD2CBDE), // Light Gray background
                textColor = Color.Black // Text color inside the field
            )
        )

        Button(onClick = {
            val w = weight.toDoubleOrNull()
            if (w != null) {
                waterIntake = w * 0.033 // General guideline: 33 ml per kg
            }
        },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0x7C0F0F3B)) // Change this color
        ) {
            Text("Calculate", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        waterIntake?.let {
            Text(text = "Your Daily Water Intake: %.2f liters".format(it), fontSize = 18.sp, modifier = Modifier.padding(top = 16.dp))
        }
    }
}

@Composable
fun BodyFatCalculator() {
    var weight by remember { mutableStateOf("") }
    var waist by remember { mutableStateOf("") }
    var bodyFat by remember { mutableStateOf<Double?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {

        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Enter Weight (kg)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9A7CC9), // Blue border when focused
                unfocusedBorderColor = Color.Gray, // Gray border when not focused
                backgroundColor = Color(0xFFD2CBDE), // Light Gray background
                textColor = Color.Black // Text color inside the field
            )
        )

        OutlinedTextField(
            value = waist,
            onValueChange = { waist = it },
            label = { Text("Waist Circumference (cm)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF9A7CC9), // Blue border when focused
                unfocusedBorderColor = Color.Gray, // Gray border when not focused
                backgroundColor = Color(0xFFD2CBDE), // Light Gray background
                textColor = Color.Black // Text color inside the field
            )
        )

        Button(onClick = {
            val w = weight.toDoubleOrNull()
            val wc = waist.toDoubleOrNull()
            if (w != null && wc != null) {
                bodyFat = (wc / w) * 100 // Simplified body fat calculation
            }
        },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0x7C0F0F3B)) // Change this color
        ) {
            Text("Calculate Body Fat %", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        bodyFat?.let {
            Text(text = "Your Body Fat Percentage: %.2f%%".format(it), fontSize = 18.sp, modifier = Modifier.padding(top = 16.dp))
        }
    }
}
