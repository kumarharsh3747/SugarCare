package com.example.sugarfree

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberDismissState
import androidx.compose.material.DismissDirection
import androidx.compose.material.SwipeToDismiss
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReminderScreen(navController: NavHostController) {
    val context = LocalContext.current
    var plans by remember { mutableStateOf(loadReminders(context)) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedPlanIndex by remember { mutableStateOf(-1) }
    var tempTitle by rememberSaveable { mutableStateOf("") }
    var tempDetails by rememberSaveable { mutableStateOf("") }
    var tempNotificationMessage by rememberSaveable { mutableStateOf("") }
    var tempTime by remember { mutableStateOf(Calendar.getInstance()) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<ReminderPlan?>(null) }

    LaunchedEffect(Unit) {
        plans.forEach { scheduleExactAlarm(context, it) }
    }

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
                    onClick = {
                        setSmartReminder(
                            context,
                            "💧 Time to Hydrate!",
                            "Drink 1 glass of water",
                            "Stay hydrated! Your body needs water!",
                            30
                        )
                    }
                )
                QuickReminderButton(
                    icon = Icons.Default.Restaurant,
                    label = "Meal",
                    color = Color(0xFF4CAF50),
                    onClick = {
                        setSmartReminder(
                            context,
                            "🍎 Meal Time!",
                            "Eat a balanced meal",
                            "Time to nourish your body!",
                            180
                        )
                    }
                )
                QuickReminderButton(
                    icon = Icons.Default.MedicalServices,
                    label = "Medicine",
                    color = Color(0xFFE91E63),
                    onClick = {
                        setSmartReminder(
                            context,
                            "💊 Medicine Reminder!",
                            "Take 2 doses after meal",
                            "Don't forget your medication!",
                            120
                        )
                    }
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
                itemsIndexed(plans) { index, plan ->
                    SwipeToDismissContainer(
                        item = plan,
                        onDismiss = {
                            itemToDelete = plan
                            showDeleteConfirm = true
                        }
                    ) {
                        ReminderPlanItem(
                            plan = plan,
                            onEdit = {
                                selectedPlanIndex = index
                                tempTitle = plan.title
                                tempDetails = plan.details
                                tempNotificationMessage = plan.notificationMessage
                                tempTime.timeInMillis = plan.time
                                showDialog = true
                            },
                            onDelete = {
                                itemToDelete = plan
                                showDeleteConfirm = true
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            FloatingActionButton(
                onClick = {
                    selectedPlanIndex = -1
                    tempTitle = ""
                    tempDetails = ""
                    tempNotificationMessage = ""
                    tempTime = Calendar.getInstance()
                    showDialog = true
                },
                backgroundColor = MaterialTheme.colors.primary,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Plan", tint = Color.White)
            }
        }

        if (showDialog) {
            ReminderDialog(
                title = if (selectedPlanIndex == -1) "New Reminder" else "Edit Reminder",
                initialTitle = tempTitle,
                initialDetails = tempDetails,
                initialNotificationMessage = tempNotificationMessage,
                initialTime = tempTime,
                onDismiss = { showDialog = false },
                onConfirm = { title, details, message, time ->
                    val newPlan = ReminderPlan(
                        id = UUID.randomUUID().toString(),
                        title = title,
                        details = details,
                        notificationMessage = message,
                        time = time.timeInMillis
                    )
                    plans = if (selectedPlanIndex == -1) {
                        plans + newPlan
                    } else {
                        plans.toMutableList().apply { set(selectedPlanIndex, newPlan) }
                    }
                    saveReminders(context, plans)
                    scheduleExactAlarm(context, newPlan)
                    showDialog = false
                }
            )
        }

        if (showDeleteConfirm) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirm = false },
                title = { Text("Delete Reminder") },
                text = { Text("Are you sure you want to delete this reminder?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            plans = plans.filter { it.id != itemToDelete?.id }
                            saveReminders(context, plans)
                            itemToDelete?.let { cancelAlarm(context, it) }
                            showDeleteConfirm = false
                        }
                    ) { Text("Delete") }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDeleteConfirm = false }
                    ) { Text("Cancel") }
                }
            )
        }
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
        elevation = 0.dp // Remove shadow
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
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = 0.dp // Remove shadow
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
                text = formatTime(plan.time),
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
    var dialogTitle by remember { mutableStateOf(initialTitle) }
    var details by remember { mutableStateOf(initialDetails) }
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
                    value = dialogTitle,
                    onValueChange = { dialogTitle = it },
                    label = { Text("Reminder Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = details,
                    onValueChange = { details = it },
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
                        onClick = { onConfirm(dialogTitle, details, notificationMessage, selectedTime) },
                        enabled = dialogTitle.isNotBlank()
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
    val timePickerDialog = TimePickerDialog(
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToDismissContainer(
    item: ReminderPlan,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    val dismissState = rememberDismissState()

    if (dismissState.isDismissed(DismissDirection.EndToStart)) {
        onDismiss()
    }

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.EndToStart),
        background = {
            val color = when (dismissState.dismissDirection) {
                DismissDirection.EndToStart -> Color.Red.copy(alpha = 0.8f)
                else -> Color.Transparent
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        dismissContent = { content() }
    )
}

// Data persistence functions
private fun saveReminders(context: Context, reminders: List<ReminderPlan>) {
    val sharedPref = context.getSharedPreferences("reminders", Context.MODE_PRIVATE)
    val json = Gson().toJson(reminders)
    sharedPref.edit().putString("reminders_list", json).apply()
}

private fun loadReminders(context: Context): List<ReminderPlan> {
    val sharedPref = context.getSharedPreferences("reminders", Context.MODE_PRIVATE)
    val json = sharedPref.getString("reminders_list", null)
    return if (json != null) {
        val type = object : TypeToken<List<ReminderPlan>>() {}.type
        Gson().fromJson(json, type) ?: emptyList()
    } else {
        emptyList()
    }
}

// Alarm management functions
fun cancelAlarm(context: Context, plan: ReminderPlan) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, ReminderReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        plan.id.hashCode(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    alarmManager.cancel(pendingIntent)
}

@RequiresApi(Build.VERSION_CODES.S)
fun checkExactAlarmPermission(context: Context): Boolean {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    return alarmManager.canScheduleExactAlarms()
}

fun scheduleExactAlarm(context: Context, plan: ReminderPlan) {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !checkExactAlarmPermission(context)) {
            Toast.makeText(context, "Exact alarm permission required", Toast.LENGTH_LONG).show()
            context.startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = Uri.parse("package:${context.packageName}")
            })
            return
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("title", plan.title)
            putExtra("message", plan.notificationMessage)
            putExtra("details", plan.details)
            putExtra("id", plan.id)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            plan.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply { timeInMillis = plan.time }
        val triggerAtMillis = calendar.timeInMillis

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
        }
    } catch (e: SecurityException) {
        Toast.makeText(context, "Alarm permission denied", Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Error scheduling alarm: ${e.message}", Toast.LENGTH_LONG).show()
    }
}

// Notification implementation
class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Reminder"
        val message = intent.getStringExtra("message") ?: "Time for your reminder!"
        val details = intent.getStringExtra("details") ?: ""
        showNotification(context, title, message, details)
    }

    private fun showNotification(context: Context, title: String, message: String, details: String) {
        val notificationManager = ContextCompat.getSystemService(context, NotificationManager::class.java)
            ?: throw IllegalStateException("NotificationManager is not available")

        createNotificationChannel(context)

        val soundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val vibrationPattern = longArrayOf(0, 500, 200, 500)

        val notification = NotificationCompat.Builder(context, "reminder_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText("$message\n\n$details"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(soundUri)
            .setVibrate(vibrationPattern)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(Random().nextInt(), notification)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "reminder_channel",
                "Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Health reminder notifications"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
                setSound(
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                    android.media.AudioAttributes.Builder()
                        .setUsage(android.media.AudioAttributes.USAGE_NOTIFICATION)
                        .build()
                )
            }
            val notificationManager = ContextCompat.getSystemService(context, NotificationManager::class.java)
                ?: throw IllegalStateException("NotificationManager is not available")
            notificationManager.createNotificationChannel(channel)
        }
    }
}

private fun setSmartReminder(
    context: Context,
    title: String,
    details: String,
    notificationMessage: String,
    minutes: Int
) {
    val plan = ReminderPlan(
        id = UUID.randomUUID().toString(),
        title = title,
        details = details,
        notificationMessage = notificationMessage,
        time = System.currentTimeMillis() + minutes * 60_000L
    )
    scheduleExactAlarm(context, plan)
}

private fun formatTime(timestamp: Long): String {
    val calendar = Calendar.getInstance().apply { timeInMillis = timestamp }
    return String.format(
        "%02d:%02d %s",
        calendar.get(Calendar.HOUR) + if (calendar.get(Calendar.AM_PM) == Calendar.AM) 0 else 12,
        calendar.get(Calendar.MINUTE),
        if (calendar.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"
    )
}

data class ReminderPlan(
    val id: String,
    val title: String,
    val details: String = "",
    val notificationMessage: String = "",
    val time: Long
)