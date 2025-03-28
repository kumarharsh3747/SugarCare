package com.example.sugarfree

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

// Game state class to track user progress
 class GameState {
    var points by mutableStateOf(0)
    var completedChallenges by mutableStateOf(emptySet<String>())
    var unlockedAchievements by mutableStateOf(emptySet<String>())
}

@Composable
fun HealthTipsScreen(navController: NavController, gameState: GameState = remember { GameState() }) {
    val scrollState = rememberScrollState()
    Spacer(modifier = Modifier.height(16.dp))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        // Gamification Header
        GameHeader(gameState)

        Spacer(modifier = Modifier.height(16.dp))

        // New interactive quiz game
        SugarQuizGame(gameState)

        Spacer(modifier = Modifier.height(16.dp))

        // Daily challenge section
        DailyChallengeSection(gameState)
    }
}

@Composable
private fun GameHeader(gameState: GameState) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFF6200EA), Color(0xFF03DAC5))
                    )
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Points display
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🏆 Points", color = Color.White)
                Text(gameState.points.toString(), color = Color.White, fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Progress meter
            val progress by animateFloatAsState(
                targetValue = (gameState.points / 100f).coerceIn(0f, 1f),
                label = "progress"
            )
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.size(48.dp),
                color = Color.White,
                strokeWidth = 4.dp
            )
        }
    }
}

@Composable
private fun SugarQuizGame(gameState: GameState) {
    Spacer(modifier = Modifier.height(16.dp))
    val questions = remember {
        listOf(
            QuizQuestion(
                "Which has more sugar?",
                "Apple 🍎" to "Soda Can 🥤",
                correctAnswer = 2
            ),
            QuizQuestion(
                "Which is healthier?",
                "Salad 🥗" to "Donut 🍩",
                correctAnswer = 1
            )
        )
    }

    var currentQuestion by remember { mutableStateOf(0) }
    var showResult by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("🎮 Sugar Quiz", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))

            if (currentQuestion < questions.size) {
                val question = questions[currentQuestion]
                Text(question.text, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    QuizButton(option = question.options.first) {
                        isCorrect = question.correctAnswer == 1
                        if (isCorrect) gameState.points += 20
                        showResult = true
                    }
                    QuizButton(option = question.options.second) {
                        isCorrect = question.correctAnswer == 2
                        if (isCorrect) gameState.points += 20
                        showResult = true
                    }
                }

                if (showResult) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isCorrect) "✅ Correct! +20 points" else "❌ Try again!",
                        color = if (isCorrect) Color.Green else Color.Red
                    )

                    LaunchedEffect(showResult) {
                        delay(2000)
                        showResult = false
                        currentQuestion++
                    }
                }
            } else {
                Text("Quiz completed! 🎉", color = Color.Green)
            }
        }
    }
}

@Composable
private fun QuizButton(option: String, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(option, fontSize = 16.sp) // Display text with emoji
    }
}

@Composable
private fun DailyChallengeSection(gameState: GameState) {
    val challenges = remember {
        listOf(
            "Read 3 health tips" to 30,
            "Complete quiz" to 50,
            "Share with friend" to 40
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("🔥 Daily Challenges", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))

            challenges.forEach { (challenge, points) ->
                ChallengeItem(
                    challenge = challenge,
                    points = points,
                    completed = gameState.completedChallenges.contains(challenge),
                    onComplete = {
                        gameState.completedChallenges = gameState.completedChallenges + challenge
                        gameState.points += points
                    }
                )
            }
        }
    }
}

@Composable
private fun ChallengeItem(challenge: String, points: Int, completed: Boolean, onComplete: () -> Unit) {
    Spacer(modifier = Modifier.height(16.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon for challenge status (Completed or Not Completed)
        Icon(
            imageVector = if (completed) Icons.Default.Check else Icons.Default.Close,
            contentDescription = "Challenge status",
            tint = if (completed) Color.Green else Color.Red
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Challenge text
        Text(challenge, modifier = Modifier.weight(1f))

        // Points display
        Text("+$points", color = Color(0xFF6200EA))

        // Claim button (only if challenge is not completed)
        if (!completed) {
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onComplete, modifier = Modifier.height(32.dp)) {
                Text("Claim")
            }
        }
    }
}

data class QuizQuestion(
    val text: String,
    val options: Pair<String, String>,
    val correctAnswer: Int
)


@Composable
fun HealthTipsScreen2(navController: NavController, gameState: GameState) {
    Spacer(modifier = Modifier.height(16.dp))
    val foodItems = remember {
        listOf(
            "🥩 Grass-fed Beef" to false,
            "🐟 Wild Salmon" to false,
            "🥓 Bacon" to false,
            "🥚 Eggs" to false,
            "🥜 Raw Nuts" to false,
            "🫒 Olive Oil" to false,
            "🥥 Coconut Oil" to false,
            "🥑 Avocados" to false,
            "🥦 Low-Carb Veggies" to false
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        GameHeader(gameState)

        // Existing content
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.image6),
                contentDescription = "Top Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .height(200.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Interactive Food Collector Game
        Text(
            text = "🍎 Food Collector",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF240046)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(foodItems.size) { index ->
                FoodCollectorItem(
                    item = foodItems[index].first,
                    collected = foodItems[index].second,
                    onClick = {
                        gameState.points += 15
                        // Update collection status
                    }
                )
            }
        }

        // Rest of existing content remains the same...
        // [Include all your original HealthTipsScreen2 content here]
    }
}

@Composable
fun HealthTipsScreen3(navController: NavController, gameState: GameState) {
    Spacer(modifier = Modifier.height(16.dp))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        GameHeader(gameState)

        // Progress Challenge
        DailyProgressChallenge(gameState)

        // Existing content
        Text(
            text = "What to eat??",
            fontSize = 28.sp,
            color = Color(0xFF6200EA),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Interactive Meal Planner
        MealPlannerGame(gameState)

        // Rest of existing content remains the same...
        // [Include all your original HealthTipsScreen3 content here]
    }
}

// New Game Components
@Composable
private fun FoodCollectorItem(item: String, collected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .clickable(enabled = !collected) { onClick() },
        elevation = CardDefaults.cardElevation(if (collected) 0.dp else 4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(if (collected) Color.LightGray else Color.White)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = item.substringBefore(" "),
                fontSize = 24.sp
            )
            Text(
                text = item.substringAfter(" "),
                fontSize = 12.sp,
                color = if (collected) Color.Gray else Color.DarkGray
            )
            if (!collected) {
                Text("+15", color = Color.Green, fontSize = 10.sp)
            }
        }
    }
}

@Composable
private fun MealPlannerGame(gameState: GameState) {
    var selectedItems by remember { mutableStateOf(emptySet<String>()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("🍽️ Daily Meal Planner", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))

            val mealOptions = listOf("🥗 Salad", "🍗 Grilled Chicken", "🥦 Steamed Veggies", "🍳 Omelette")

            mealOptions.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedItems = if (selectedItems.contains(option)) {
                                selectedItems - option
                            } else {
                                if (selectedItems.size < 3) selectedItems + option
                                else selectedItems
                            }
                        }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = selectedItems.contains(option),
                        onCheckedChange = null
                    )
                    Text(option, modifier = Modifier.weight(1f))
                }
            }

            Button(
                onClick = {
                    if (selectedItems.size == 3) {
                        gameState.points += 50
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Complete Plan +50")
            }
        }
    }
}
@Composable
private fun DailyProgressChallenge(gameState: GameState) {
    var waterCount by remember { mutableStateOf(0) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("💧 Water Tracking", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) { // <- Fixed misplaced parenthesis
                Button(
                    onClick = {
                        if (waterCount < 8) {
                            waterCount++
                            gameState.points += 10
                        }
                    },
                    enabled = waterCount < 8
                ) {
                    Text("Add Glass +10")
                }

                Text("$waterCount/8", fontSize = 24.sp)
            }
        }
    }
}
