package com.example.sugarfree

import android.util.Log
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

data class Address(
    val addressId: String = "",
    val address: String = "",
    val isPrimary: Boolean = false,
    val name: String = "",
    val phoneNumber: String = ""
)


class AddressViewModel : ViewModel() {
    var addresses: List<Address> = emptyList() // List to hold addresses
    private val _addressList = MutableStateFlow<List<Address>>(emptyList())
    val addressList: StateFlow<List<Address>> = _addressList
    private val firestore: FirebaseFirestore = Firebase.firestore
    private val _primaryAddress = MutableStateFlow<Address?>(null)
    val primaryAddress: StateFlow<Address?> = _primaryAddress

    fun fetchAddresses(currentUserEmail: String) {
        val sanitizedEmail = currentUserEmail.replace(".", ",")
        viewModelScope.launch {
            try {
                val result = firestore.collection("users")
                    .document(sanitizedEmail)
                    .collection("addresses")
                    .get()
                    .await()

                val fetchedAddresses = result.map { document ->
                    document.toObject(Address::class.java).copy(
                        addressId = document.id // Save the document ID
                    )
                }

                _addressList.value = fetchedAddresses
                _primaryAddress.value = fetchedAddresses.find { it.isPrimary }

                // Log fetched addresses for debugging
                Log.d("AddressViewModel", "Fetched addresses: $fetchedAddresses")
            } catch (e: Exception) {
                Log.e("AddressViewModel", "Error fetching addresses: ${e.message}")
            }
        }
    }
    fun setAsPrimaryAddress(currentUserEmail: String, addressId: String) {
        val sanitizedEmail = currentUserEmail.replace(".", ",")
        viewModelScope.launch {
            try {
                // Reset all addresses to not primary
                val allAddresses = firestore.collection("users")
                    .document(sanitizedEmail)
                    .collection("addresses")
                    .get()
                    .await()

                allAddresses.forEach { document ->
                    firestore.collection("users")
                        .document(sanitizedEmail)
                        .collection("addresses")
                        .document(document.id)
                        .update("isPrimary", false)
                        .await()
                }

                // Now set the selected address to primary
                firestore.collection("users")
                    .document(sanitizedEmail)
                    .collection("addresses")
                    .document(addressId)
                    .update("isPrimary", true)
                    .await()

                fetchAddresses(currentUserEmail) // Refresh the address list
            } catch (e: Exception) {
                Log.e("AddressViewModel", "Error setting primary address: ${e.message}")
            }
        }
    }



    // Delete an address from Firestore
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

                Log.d("AddressViewModel", "Address deleted: $addressId")
                fetchAddresses(currentUserEmail) // Refresh address list after deletion
            } catch (e: Exception) {
                Log.e("AddressViewModel", "Error deleting address: ${e.message}")
            }
        }
    }

    // Add a new address to Firestore
    fun addAddress(currentUserEmail: String, address: Address) {
        val sanitizedEmail = currentUserEmail.replace(".", ",")
        viewModelScope.launch {
            try {
                val addressData = mapOf(
                    "name" to address.name,
                    "address" to address.address,
                    "phoneNumber" to address.phoneNumber,  // Include phone number
                    "isPrimary" to address.isPrimary
                )
                firestore.collection("users")
                    .document(sanitizedEmail)
                    .collection("addresses")
                    .add(addressData)  // Save the new address
                Log.d("AddressViewModel", "Address added: ${address.name}")
            } catch (e: Exception) {
                Log.e("AddressViewModel", "Error adding address: ${e.message}")
            }
        }
    }



}