package com.example.sugarfree

import android.graphics.Bitmap

data class Chat(
    val prompt: String,
    val bitmap: Bitmap? = null,
    val isFromUser: Boolean
)