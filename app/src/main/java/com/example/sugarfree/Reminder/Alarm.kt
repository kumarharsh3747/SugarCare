package com.example.sugarfree.Reminder

import android.app.Activity
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class AlarmActivity : ComponentActivity() {
    private var ringtone: Ringtone? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Show over lock screen
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )

        val title = intent.getStringExtra("title") ?: "Reminder"
        val message = intent.getStringExtra("message") ?: "Time for your reminder!"

        // Use the system alarm sound
        val alarmUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(applicationContext, alarmUri)
        ringtone?.play()

        setContent {
            AlarmScreen(
                title = title,
                message = message,
                onDismiss = {
                    ringtone?.stop()
                    finish()
                }
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ringtone?.stop()
    }
}

@Composable
fun AlarmScreen(title: String, message: String, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = title, fontSize = 30.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = message, fontSize = 18.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = onDismiss) {
                Text(text = "Dismiss", fontSize = 20.sp)
            }
        }
    }
}
