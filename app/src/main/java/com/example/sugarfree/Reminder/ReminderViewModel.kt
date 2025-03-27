package com.example.sugarfree.Reminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReminderViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()

    // StateFlow for reminders
    private val _reminders = MutableStateFlow<List<ReminderPlan>>(emptyList())
    val reminders: StateFlow<List<ReminderPlan>> get() = _reminders

    // StateFlow for errors
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    init {
        // Fetch reminders from Firestore
        fetchReminders()
    }

    private fun fetchReminders() {
        firestore.collection("reminders")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    // Handle Firestore errors
                    _error.value = "Failed to load reminders: ${exception.message}"
                    return@addSnapshotListener
                }
                val reminders = snapshot?.documents?.mapNotNull { document ->
                    document.toObject(ReminderPlan::class.java)
                } ?: emptyList()
                _reminders.value = reminders
            }
    }

    fun addReminder(reminder: ReminderPlan) {
        firestore.collection("reminders").document(reminder.id).set(reminder)
            .addOnSuccessListener {
                // Success: No action needed
            }
            .addOnFailureListener { exception ->
                // Handle failure
                _error.value = "Failed to add reminder: ${exception.message}"
            }
    }

    fun deleteReminder(id: String) {
        firestore.collection("reminders").document(id).delete()
            .addOnSuccessListener {
                // Success: No action needed
            }
            .addOnFailureListener { exception ->
                // Handle failure
                _error.value = "Failed to delete reminder: ${exception.message}"
            }
    }

    fun editReminder(updatedReminder: ReminderPlan) {
        firestore.collection("reminders").document(updatedReminder.id).set(updatedReminder)
            .addOnSuccessListener {
                // Success: No action needed
            }
            .addOnFailureListener { exception ->
                // Handle failure
                _error.value = "Failed to update reminder: ${exception.message}"
            }
    }
}