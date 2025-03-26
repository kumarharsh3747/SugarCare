package com.example.sugarfree.Reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager

class ReminderBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Reminder"
        val message = intent.getStringExtra("message") ?: "Time for your reminder!"

        // Wake up the device if it's sleeping
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "sugarfree:AlarmWakeLock"
        )
        wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/)

        // Start AlarmActivity
        val alarmIntent = Intent(context, AlarmActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Important for launching activity from a BroadcastReceiver
            putExtra("title", title)
            putExtra("message", message)
        }
        context.startActivity(alarmIntent)
    }
}
