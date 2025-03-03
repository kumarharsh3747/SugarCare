package com.example.sugarfree

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import java.util.*

@Composable
fun ReminderScreen(navController: NavHostController) {
    val viewModel: ReminderViewModel = viewModel()
    val reminders by viewModel.reminders.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var editReminder by remember { mutableStateOf<ReminderPlan?>(null) }

    val context = LocalContext.current

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
                text = "Health Reminders",
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                QuickReminderButton(
                    icon = Icons.Default.LocalDrink,
                    label = "Hydrate",
                    color = Color(0xFF2196F3),
                    onClick = {}
                )
                QuickReminderButton(
                    icon = Icons.Default.Restaurant,
                    label = "Meal",
                    color = Color(0xFF4CAF50),
                    onClick = {}
                )
                QuickReminderButton(
                    icon = Icons.Default.MedicalServices,
                    label = "Medicine",
                    color = Color(0xFFE91E63),
                    onClick = {}
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Scheduled Plans",
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.secondary,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(reminders.size) { index ->
                    ReminderPlanItem(
                        plan = reminders[index],
                        onEdit = {
                            editReminder = reminders[index]
                            showDialog = true
                        },
                        onDelete = {
                            viewModel.deleteReminder(reminders[index].id)
                        }
                    )
                }
            }

            FloatingActionButton(
                onClick = {
                    editReminder = null
                    showDialog = true
                },
                backgroundColor = MaterialTheme.colors.primary,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Plan", tint = Color.White)
            }
        }
    }

    if (showDialog) {
        val initialTime = Calendar.getInstance()
        ReminderDialog(
            title = if (editReminder == null) "Add Reminder" else "Edit Reminder",
            initialTitle = editReminder?.title ?: "",
            initialDetails = editReminder?.details ?: "",
            initialNotificationMessage = editReminder?.notificationMessage ?: "",
            initialTime = initialTime.apply {
                timeInMillis = editReminder?.time ?: System.currentTimeMillis()
            },
            onDismiss = { showDialog = false },
            onConfirm = { title, details, message, time ->
                // Validation: Ensure the title is not empty
                if (title.isEmpty()) {
                    Toast.makeText(context, "Reminder title cannot be empty", Toast.LENGTH_SHORT).show()
                    return@ReminderDialog
                }

                // Create a new ReminderPlan object
                val newReminder = ReminderPlan(
                    id = editReminder?.id ?: UUID.randomUUID().toString(),
                    title = title,
                    details = details,
                    notificationMessage = message,
                    time = time.timeInMillis
                )

                // Add or edit the reminder in the ViewModel
                if (editReminder == null) {
                    viewModel.addReminder(newReminder)
                } else {
                    viewModel.editReminder(newReminder)
                }

                // Schedule the reminder
                scheduleReminder(context, newReminder)

                // Close the dialog
                showDialog = false
            }
        )
    }
}

@Composable
fun QuickReminderButton(icon: ImageVector, label: String, color: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(100.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = color.copy(alpha = 0.2f),
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.body2,
                color = color
            )
        }
    }
}

@Composable
fun ReminderPlanItem(plan: ReminderPlan, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = plan.title,
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = plan.details,
                style = MaterialTheme.typography.caption,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = android.text.format.DateFormat.getTimeFormat(LocalContext.current).format(Date(plan.time)),
                style = MaterialTheme.typography.caption,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onEdit) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colors.primary
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colors.error
                    )
                }
            }
        }
    }
}

@Composable
fun ReminderDialog(
    title: String,
    initialTitle: String,
    initialDetails: String,
    initialNotificationMessage: String,
    initialTime: Calendar,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, Calendar) -> Unit
) {
    var reminderTitle by remember { mutableStateOf(initialTitle) }
    var reminderDetails by remember { mutableStateOf(initialDetails) }
    var notificationMessage by remember { mutableStateOf(initialNotificationMessage) }
    var selectedTime by remember { mutableStateOf(initialTime) }
    val context = LocalContext.current


    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .clip(RoundedCornerShape(16.dp)),
            color = MaterialTheme.colors.background
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = reminderTitle,
                    onValueChange = { reminderTitle = it },
                    label = { Text("Reminder Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = reminderDetails,
                    onValueChange = { reminderDetails = it },
                    label = { Text("Details (e.g., 2 doses of medicine)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = notificationMessage,
                    onValueChange = { notificationMessage = it },
                    label = { Text("Notification Message") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Time Picker Placeholder
                TimePickerDialog(
                    context = context,
                    initialHour = selectedTime.get(Calendar.HOUR_OF_DAY),
                    initialMinute = selectedTime.get(Calendar.MINUTE),
                    onTimeSet = { hour, minute ->
                        selectedTime = Calendar.getInstance().apply {
                            set(Calendar.HOUR_OF_DAY, hour)
                            set(Calendar.MINUTE, minute)
                        }
                    }
                )


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onConfirm(reminderTitle, reminderDetails, notificationMessage, selectedTime)
                        },
                        enabled = reminderTitle.isNotEmpty()
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}
@Composable
private fun TimePickerDialog(
    context: Context,
    initialHour: Int,
    initialMinute: Int,
    onTimeSet: (Int, Int) -> Unit
) {
    val timePickerDialog = android.app.TimePickerDialog(
        context,
        { _, hour, minute -> onTimeSet(hour, minute) },
        initialHour,
        initialMinute,
        true
    )

    DisposableEffect(Unit) {
        timePickerDialog.show()
        onDispose { timePickerDialog.dismiss() }
    }
}

data class ReminderPlan(
    val id: String,
    val title: String,
    val details: String = "",
    val notificationMessage: String = "",
    val time: Long
)