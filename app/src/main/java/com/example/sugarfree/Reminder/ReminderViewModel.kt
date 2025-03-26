package com.example.sugarfree.Reminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReminderViewModel : ViewModel() {
    private val _reminders = MutableStateFlow<List<ReminderPlan>>(emptyList())
    val reminders: StateFlow<List<ReminderPlan>> get() = _reminders

    fun addReminder(reminder: ReminderPlan) {
        viewModelScope.launch {
            _reminders.value = _reminders.value + reminder
        }
    }

    fun deleteReminder(id: String) {
        viewModelScope.launch {
            _reminders.value = _reminders.value.filter { it.id != id }
        }
    }

    fun editReminder(updatedReminder: ReminderPlan) {
        viewModelScope.launch {
            _reminders.value = _reminders.value.map {
                if (it.id == updatedReminder.id) updatedReminder else it
            }
        }
    }
}