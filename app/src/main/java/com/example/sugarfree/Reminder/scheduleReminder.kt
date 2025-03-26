package com.example.sugarfree.Reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import java.util.*

fun scheduleReminder(context: Context, reminder: ReminderPlan) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
        putExtra("notificationId", reminder.id.hashCode())
        putExtra("title", reminder.title)
        putExtra("message", reminder.notificationMessage)
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        reminder.id.hashCode(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Ensure the reminder time is in the future
    val currentTime = System.currentTimeMillis()
    var scheduledTime = reminder.time

    if (scheduledTime < currentTime) {
        Log.w("AlarmScheduler", "Reminder time is in the past (${Date(scheduledTime)}). Adjusting to next day.")
        val calendar = Calendar.getInstance().apply {
            timeInMillis = scheduledTime
            add(Calendar.DAY_OF_YEAR, 1) // Move to the next day
        }
        scheduledTime = calendar.timeInMillis
    }

    // Check for exact alarm permission (Android 12+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
        val settingsIntent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
        startActivity(context, settingsIntent, null)
        Log.e("AlarmScheduler", "Exact alarm permission not granted. Requesting permission.")
        return
    }

    // Schedule the alarm correctly based on Android version
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, scheduledTime, pendingIntent)
    } else {
        alarmManager.set(AlarmManager.RTC_WAKEUP, scheduledTime, pendingIntent)
    }

    Log.d("AlarmScheduler", "Alarm scheduled for: ${Date(scheduledTime)}")
}
