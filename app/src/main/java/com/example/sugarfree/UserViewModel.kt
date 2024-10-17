package com.example.sugarfree

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    var isLoggedIn = mutableStateOf(false)
    var userId = mutableStateOf("")

    fun login(userId: String) {
        this.userId.value = userId
        isLoggedIn.value = true
    }

    fun logout() {
        userId.value = ""
        isLoggedIn.value = false
    }
}
