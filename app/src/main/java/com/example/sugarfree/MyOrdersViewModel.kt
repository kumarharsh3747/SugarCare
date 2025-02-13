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

class OrderViewModel : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()
    private val firestore: FirebaseFirestore = Firebase.firestore

    fun fetchOrders(currentUserEmail: String) {
        val sanitizedEmail = currentUserEmail.replace(".", ",")
        viewModelScope.launch {
            try {
                val result = firestore.collection("users")
                    .document(sanitizedEmail)
                    .collection("orders")
                    .get()
                    .await()

                val fetchedOrders = result.map { document ->
                    val order = document.toObject(Order::class.java).copy(
                        orderId = document.id // Save the document ID
                    )
                    order
                }

                _orders.value = fetchedOrders

                // Log fetched orders for debugging
                Log.d("OrderViewModel", "Fetched orders: $fetchedOrders")
            } catch (e: Exception) {
                Log.e("OrderViewModel", "Error fetching orders: ${e.message}", e)
            }
        }
    }

}

data class Order(
    val orderId: String = "",
    val items: List<OrderItem> = emptyList(),
    val timestamp: Long = 0,
    val totalPrice: Double = 0.0,
    val userEmail: String = ""
)

data class OrderItem(
    val name: String = "",
    val price: Double = 0.0
)
