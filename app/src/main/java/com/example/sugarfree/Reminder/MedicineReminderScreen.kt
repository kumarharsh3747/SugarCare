package com.example.sugarfree.Reminder

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import java.util.*

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MedicineReminderScreen(navController: NavHostController) {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val viewModel: ReminderViewModel = viewModel()
    val reminders by viewModel.reminders.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Medicine Reminders",
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(reminders.filter { it.title.contains("Medicine", ignoreCase = true) }) { plan ->
                    ReminderPlanItem(
                        plan = plan,
                        onEdit = {
                            // Navigate to edit dialog or pass data to ReminderDialog
                            showDialog = true
                        },
                        onDelete = {
                            viewModel.deleteReminder(plan.id)
                        }
                    )
                }
            }
            FloatingActionButton(
                onClick = { showDialog = true },
                backgroundColor = MaterialTheme.colors.primary,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Medicine Reminder", tint = Color.White)
            }
        }
    }

    if (showDialog) {
        val initialTime = Calendar.getInstance()
        ReminderDialog(
            title = "Add Medicine Reminder",
            initialTitle = "Medicine",
            initialDetails = "Have a Medicine",
            initialNotificationMessage = "Time for a Medicine!",
            initialTime = initialTime.apply {
                timeInMillis = System.currentTimeMillis() + 30 * 60 * 1000 // Default 30 minutes from now
            },
            onDismiss = { showDialog = false },
            onConfirm = { title, details, message, time ->
                if (title.isEmpty()) {
                    Toast.makeText(context, "Reminder title cannot be empty", Toast.LENGTH_SHORT).show()
                    return@ReminderDialog
                }
                val newReminder = ReminderPlan(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    details = details,
                    notificationMessage = message,
                    time = time.timeInMillis
                )
                viewModel.addReminder(newReminder)
                scheduleReminder(context, newReminder)
                showDialog = false
            }
        )
    }
}