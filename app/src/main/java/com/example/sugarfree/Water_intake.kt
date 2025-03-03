import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.apache.commons.lang3.ObjectUtils.Null
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

data class WaterIntakeData(
    val date: LocalDate,
    val amountInLiters: Double,
    val goalInLiters: Double = 3.1, // Default goal
    val containers: List<WaterContainer> = emptyList(),
    val intakeLog: List<WaterIntakeEntry> = emptyList()
)

data class WaterContainer(
    val name: String,
    val volumeInMl: Int,
    val volumeInOz: Double,
    val icon: String,
    val count: Int = 0
)

data class WaterIntakeEntry(
    val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val amountInMl: Int,
    val containerName: String? = null,
    val containerIcon: String? = null
)


@Composable
fun WaterIntakeApp(navController: NavController) {
    // State for the entire app
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showCalendar by remember { mutableStateOf(false) }
    var showAddCustomDialog by remember { mutableStateOf(false) }
    var waterIntakeData by remember { mutableStateOf(generateSampleData(selectedDate)) }
    var showSuccessMessage by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()


    // Function to add water intake
    fun addWaterIntake(amountInMl: Int, containerName: String? = null, containerIcon: String? = null) {
        val entry = WaterIntakeEntry(
            amountInMl = amountInMl,
            containerName = containerName,
            containerIcon = containerIcon
        )

        // Update the data
        val updatedIntakeLog = waterIntakeData.intakeLog + entry
        val totalAmountInLiters = updatedIntakeLog.sumOf { it.amountInMl } / 1000.0

        waterIntakeData = waterIntakeData.copy(
            amountInLiters = totalAmountInLiters,
            intakeLog = updatedIntakeLog
        )

        // Show success message briefly
        showSuccessMessage = true
        scope.launch {
            delay(2000)
            showSuccessMessage = false
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                TopAppBar(
                    onCalendarClick = { showCalendar = true },
                    onClick = { navController.navigate("Home") }
                )
                DateSelector(
                    selectedDate = selectedDate,
                    onDateSelected = {
                        selectedDate = it
                        // Load data for the selected date
                        waterIntakeData = generateSampleData(selectedDate)
                    }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    WaterLevelIndicator(
                        percentage = calculatePercentage(
                            waterIntakeData.amountInLiters,
                            waterIntakeData.goalInLiters
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(400.dp)
                    )

                    HydrationInfo(
                        waterIntakeData = waterIntakeData,
                        onAddCustomClick = { showAddCustomDialog = true },
                        onContainerClick = { container ->
                            addWaterIntake(
                                amountInMl = container.volumeInMl,
                                containerName = container.name,
                                containerIcon = container.icon
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(400.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "BENEFITS",
                        color = Color(0xFF3F51B5),
                        fontWeight = FontWeight.Bold
                    )

                    EditButton()

                    Text(
                        text = "GOAL",
                        color = Color(0xFF3F51B5),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Success message
            AnimatedVisibility(
                visible = showSuccessMessage,
                enter = fadeIn() + slideInVertically { it },
                exit = fadeOut() + slideOutVertically { it },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF4CAF50)
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Water intake added successfully!",
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Calendar dialog
            if (showCalendar) {
                CalendarDialog(
                    initialDate = selectedDate,
                    onDateSelected = { date ->
                        selectedDate = date
                        // Load data for the selected date
                        waterIntakeData = generateSampleData(selectedDate)
                        showCalendar = false
                    },
                    onDismiss = { showCalendar = false }
                )
            }

            // Add custom intake dialog
            if (showAddCustomDialog) {
                AddCustomIntakeDialog(
                    onConfirm = { amountInMl ->
                        addWaterIntake(amountInMl, "Custom")
                        showAddCustomDialog = false
                    },
                    onDismiss = { showAddCustomDialog = false }
                )
            }
        }
    }
}

@Composable
fun TopAppBar(onCalendarClick: () -> Unit, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint = Color.Gray,
            modifier = Modifier.clickable { onClick() }
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Water Intake List",
            fontSize = 20.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = "Calendar",
            tint = Color.Gray,
            modifier = Modifier.clickable { onCalendarClick() }
        )
    }
}

@Composable
fun DateSelector(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    // Generate 5 dates: 2 before selected date, selected date, 2 after selected date
    val dates = (-2..2).map { selectedDate.plusDays(it.toLong()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        dates.forEach { date ->
            val isSelected = date == selectedDate
            val month = date.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            val day = date.dayOfMonth.toString()
            val weekday = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())

            DateItem(
                month = month,
                day = day,
                weekday = weekday,
                selected = isSelected,
                onClick = { onDateSelected(date) }
            )
        }
    }
}

@Composable
fun DateItem(
    month: String,
    day: String,
    weekday: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) Color(0xFF6200EE) else Color.Transparent
    val textColor = if (selected) Color.White else Color.Gray

    Column(
        modifier = Modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .background(backgroundColor)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = month,
            fontSize = 16.sp,
            color = textColor
        )
        Text(
            text = day,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
        Text(
            text = weekday,
            fontSize = 16.sp,
            color = textColor
        )
    }
}

@Composable
fun WaterLevelIndicator(
    percentage: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray.copy(alpha = 0.3f))
    ) {
        // Water level
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(percentage)
                .align(Alignment.BottomCenter)
                .background(Color(0xFF2196F3))
        )

        // Lines indicating water measurement
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            repeat(30) {
                Spacer(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxWidth(0.8f)
                        .height(1.dp)
                        .background(Color.Gray.copy(alpha = 0.5f))
                )
            }
        }

        // Bubbles (decorative)
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(Color(0xFF1A78C2), shape = RoundedCornerShape(4.dp))
                .align(Alignment.BottomStart)
                .offset(x = 20.dp, y = (-100).dp)
        )

        Box(
            modifier = Modifier
                .size(6.dp)
                .background(Color(0xFF1A78C2), shape = RoundedCornerShape(3.dp))
                .align(Alignment.BottomEnd)
                .offset(x = (-15).dp, y = (-180).dp)
        )

        Box(
            modifier = Modifier
                .size(10.dp)
                .background(Color(0xFF1A78C2), shape = RoundedCornerShape(5.dp))
                .align(Alignment.BottomStart)
                .offset(x = 30.dp, y = (-250).dp)
        )
    }
}

@Composable
fun HydrationInfo(
    waterIntakeData: WaterIntakeData,
    onAddCustomClick: () -> Unit,
    onContainerClick: (WaterContainer) -> Unit,
    modifier: Modifier = Modifier
) {
    val percentageValue = (calculatePercentage(
        waterIntakeData.amountInLiters,
        waterIntakeData.goalInLiters
    ) * 100).toInt()

    val remainingLiters = (waterIntakeData.goalInLiters - waterIntakeData.amountInLiters)
        .coerceAtLeast(0.0)

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Hydration Level",
            fontSize = 18.sp,
            color = Color.Black
        )

        Text(
            text = "$percentageValue %",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Text(
            text = "You Have Logged ${String.format("%.2f", waterIntakeData.amountInLiters)} Litres",
            fontSize = 16.sp,
            color = Color.Black
        )

        Text(
            text = "${String.format("%.2f", remainingLiters)} Liters to go",
            fontSize = 16.sp,
            color = Color.Black
        )

        Button(
            onClick = onAddCustomClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Add Custom Intake",
                color = Color.White
            )
        }

        Text(
            text = "Or Add",
            modifier = Modifier.padding(vertical = 8.dp),
            fontSize = 16.sp,
            color = Color.Black
        )

        // Standard container options
        val containerOptions = listOf(
            WaterContainer("Shot Glass", 75, 2.5, "🥃"),
            WaterContainer("Goblet", 125, 4.2, "🍷"),
            WaterContainer("Small Mug", 250, 8.4, "☕"),
            WaterContainer("Tumbler", 330, 11.1, "🥤"),
            WaterContainer("Pint", 500, 16.9, "🍺"),
            WaterContainer("Large Mug", 750, 25.3, "🍵"),
            WaterContainer("1L Bottle", 1000, 33.8, "🍶"),
            WaterContainer("Pitcher", 1500, 50.7, "🏺")
        )

        // Container options grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.height(200.dp)
        ) {
            items(containerOptions) { container ->
                ContainerOption(
                    name = container.name,
                    volume = "${container.volumeInMl} ml / ${container.volumeInOz} fl. oz",
                    icon = container.icon,
                    onClick = { onContainerClick(container) }
                )
            }
        }
    }
}

@Composable
fun ContainerOption(
    name: String,
    volume: String,
    icon: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray.copy(alpha = 0.2f))
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = icon,
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = name,
            fontSize = 14.sp,
            color = Color(0xFF6200EE),
            textAlign = TextAlign.Center
        )
        Text(
            text = volume,
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AddCustomIntakeDialog(
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var amountText by remember { mutableStateOf("") }
    var selectedUnit by remember { mutableStateOf("ml") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Add Custom Water Intake",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = amountText,
                        onValueChange = { amountText = it.filter { char -> char.isDigit() } },
                        modifier = Modifier.weight(2f),
                        label = { Text("Amount") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Unit selector
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = Color.Gray,
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(8.dp)
                        ) {
                            var expanded by remember { mutableStateOf(false) }

                            Text(
                                text = selectedUnit,
                                modifier = Modifier.weight(1f)
                            )

                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Select Unit",
                                modifier = Modifier.clickable { expanded = true }
                            )

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("ml") },
                                    onClick = {
                                        selectedUnit = "ml"
                                        expanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("oz") },
                                    onClick = {
                                        selectedUnit = "oz"
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            val amount = amountText.toIntOrNull() ?: 0
                            val amountInMl = if (selectedUnit == "oz") {
                                (amount * 29.574).toInt() // Convert oz to ml
                            } else {
                                amount
                            }
                            onConfirm(amountInMl)
                        },
                        enabled = amountText.isNotEmpty() && amountText.toIntOrNull() ?: 0 > 0,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                    ) {
                        Text("Add")
                    }
                }
            }
        }
    }
}

@Composable
fun EditButton() {
    Button(
        onClick = { /* Edit Intake */ },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Edit",
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Edit Intake",
            color = Color.White
        )
    }
}

@Composable
fun CalendarDialog(
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    var currentMonth by remember { mutableStateOf(YearMonth.from(initialDate)) }
    var selectedDate by remember { mutableStateOf(initialDate) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Calendar header with month and year
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                        Text(text = "<", fontSize = 20.sp)
                    }

                    Text(
                        text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                        Text(text = ">", fontSize = 20.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Days of week header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    for (day in DayOfWeek.values()) {
                        Text(
                            text = day.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Calendar grid
                val firstDayOfMonth = currentMonth.atDay(1)
                val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
                val lastDay = currentMonth.atEndOfMonth().dayOfMonth

                val daysInGrid = mutableListOf<LocalDate?>()

                // Add empty spaces for days before the first day of month
                repeat(firstDayOfWeek) {
                    daysInGrid.add(null)
                }

                // Add actual days of the month
                for (day in 1..lastDay) {
                    daysInGrid.add(currentMonth.atDay(day))
                }

                // Display calendar grid (7 columns for each day of week)
                val rows = (daysInGrid.size + 6) / 7

                for (row in 0 until rows) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        for (col in 0 until 7) {
                            val index = row * 7 + col
                            if (index < daysInGrid.size) {
                                val date = daysInGrid[index]
                                CalendarDay(
                                    date = date,
                                    isSelected = date == selectedDate,
                                    isToday = date == LocalDate.now(),
                                    onDateClick = {
                                        if (date != null) {
                                            selectedDate = date
                                        }
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            } else {
                                // Empty space for padding
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { onDateSelected(selectedDate) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                    ) {
                        Text("Select")
                    }
                }
            }
        }
    }
}

@Composable
fun CalendarDay(
    date: LocalDate?,
    isSelected: Boolean,
    isToday: Boolean,
    onDateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(4.dp)
            .size(36.dp)
            .clip(CircleShape)
            .background(
                when {
                    isSelected -> Color(0xFF6200EE)
                    isToday -> Color(0xFFE6E0EC)
                    else -> Color.Transparent
                }
            )
            .clickable(enabled = date != null, onClick = onDateClick),
        contentAlignment = Alignment.Center
    ) {
        if (date != null) {
            Text(
                text = date.dayOfMonth.toString(),
                color = when {
                    isSelected -> Color.White
                    else -> Color.Black
                }
            )
        }
    }
}

// Helper functions
private fun calculatePercentage(amount: Double, goal: Double): Float {
    return (amount / goal).toFloat().coerceIn(0f, 1f)
}

private fun generateSampleData(date: LocalDate): WaterIntakeData {
    // Generate slightly different data for different dates
    val seed = date.dayOfMonth.toDouble() / 31.0
    val amount = 2.0 + seed
    return WaterIntakeData(
        date = date,
        amountInLiters = amount,
        goalInLiters = 3.1
    )
}

