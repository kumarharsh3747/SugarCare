package com.example.sugarfree

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

data class Address(
    val name: String = "",
    val address: String = "",
    val isPrimary: Boolean = false,
    val addressId: String = "" // Assuming you have this for deletion
)

class AddressViewModel : ViewModel() {
    private val _addressList = MutableStateFlow<List<Address>>(emptyList())
    val addressList: StateFlow<List<Address>> = _addressList.asStateFlow()
    val firestore: FirebaseFirestore = Firebase.firestore

    fun fetchAddresses(currentUserEmail: String) {
        val sanitizedEmail = currentUserEmail.replace(".", ",")
        viewModelScope.launch {
            try {
                val result = firestore.collection("users")
                    .document(sanitizedEmail)
                    .collection("addresses")
                    .get()
                    .await() // Await Firestore operation

                val fetchedAddresses = result.map { document ->
                    document.toObject(Address::class.java) // Ensure this is correct
                }

                _addressList.value = fetchedAddresses
            } catch (e: Exception) {
                Log.e("AddressViewModel", "Error fetching addresses: ${e.message}")
            }
        }
    }

    // Make sure this function exists
    fun deleteAddress(currentUserEmail: String, addressId: String) {
        val sanitizedEmail = currentUserEmail.replace(".", ",")
        viewModelScope.launch {
            try {
                firestore.collection("users")
                    .document(sanitizedEmail)
                    .collection("addresses")
                    .document(addressId)
                    .delete()
                    .await()

                // Optionally, fetch addresses again after deletion
                fetchAddresses(currentUserEmail)
            } catch (e: Exception) {
                Log.e("AddressViewModel", "Error deleting address: ${e.message}")
            }
        }
    }
    // Function to add address
    fun addAddress(currentUserEmail: String, address: Address) {
        val sanitizedEmail = currentUserEmail.replace(".", ",")
        viewModelScope.launch {
            try {
                firestore.collection("users")
                    .document(sanitizedEmail)
                    .collection("addresses")
                    .add(address) // Add address to Firestore
            } catch (e: Exception) {
                Log.e("AddressViewModel", "Error adding address: ${e.message}")
            }
        }
    }
}
