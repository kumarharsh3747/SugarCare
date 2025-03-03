package com.example.sugarfree

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

@SuppressLint("ScheduleExactAlarm")
fun scheduleReminder(context: Context, reminder: ReminderPlan) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
        putExtra("title", reminder.title)
        putExtra("message", reminder.notificationMessage)
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        reminder.id.hashCode(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    alarmManager.setExact(
        AlarmManager.RTC_WAKEUP,
        reminder.time,
        pendingIntent
    )
}
