package com.example.sugarfree

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class ReminderBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title")
        val message = intent.getStringExtra("message")
        val notificationId = intent.getIntExtra("id", 0) // Retrieve the unique ID

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            val notification = NotificationCompat.Builder(context, "reminder_channel")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(notificationId, notification) // Use the unique ID
        } else {
            println("POST_NOTIFICATIONS permission is not granted.")
        }
    }
}
