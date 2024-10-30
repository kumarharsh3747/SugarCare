package com.example.sugarfree

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class Address(
    val addressId: String = "",  // Add an ID for address identification
    val name: String,
    val address: String,
    val isPrimary: Boolean = false
)

class AddressViewModel : ViewModel() {
    // MutableStateList to hold the addresses as Address objects
    val addressList = mutableStateListOf<Address>()

    // Firebase Firestore instance
    private val firestore: FirebaseFirestore = Firebase.firestore

    // Function to add a new address for the current user
    fun addAddress(currentUserEmail: String, newAddress: Address) {
        firestore.collection("users")
            .document(currentUserEmail) // Use the user's email as the document ID
            .collection("addresses") // Sub-collection for addresses
            .add(newAddress) // Add the new address
            .addOnSuccessListener {
                addressList.add(newAddress) // Add to local list
            }
            .addOnFailureListener { exception ->
                println("Error adding address: ${exception.message}")
            }
    }

    // Function to fetch addresses for the current user
    fun fetchAddresses(currentUserEmail: String) {
        firestore.collection("users")
            .document(currentUserEmail)
            .collection("addresses")
            .get()
            .addOnSuccessListener { querySnapshot ->
                addressList.clear() // Clear existing addresses
                for (document in querySnapshot.documents) {
                    val address = document.toObject(Address::class.java)
                    if (address != null) {
                        addressList.add(address) // Add fetched address to the list
                    }
                }
            }
            .addOnFailureListener { exception ->
                println("Error fetching addresses: ${exception.message}")
            }
    }

    // Function to delete an address for the current user
    fun deleteAddress(currentUserEmail: String, addressId: String) {
        firestore.collection("users")
            .document(currentUserEmail)
            .collection("addresses")
            .document(addressId)
            .delete()
            .addOnSuccessListener {
                // Remove the address from the local list
                addressList.removeIf { it.addressId == addressId } // Ensure Address has an addressId property
            }
            .addOnFailureListener { exception ->
                println("Error deleting address: ${exception.message}")
            }
    }

}
