package com.example.sugarfree

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf

class UserViewModel : ViewModel() {
    // Mutable state for tracking whether the user is logged in
    var isLoggedIn = mutableStateOf(false)

    // Function to update login status (you can use Firebase/Auth APIs)
    fun login() {
        isLoggedIn.value = true
    }

    fun logout() {
        isLoggedIn.value = false
    }

    // Check if user is logged in
    fun checkLoginStatus(): Boolean {
        return isLoggedIn.value
    }
}
