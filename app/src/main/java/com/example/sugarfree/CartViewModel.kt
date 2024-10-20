package com.example.sugarfree

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf

class CartViewModel : ViewModel() {
    // List to hold cart items
    var cartItems = mutableStateListOf<Product>()

    // Function to add products to the cart
    fun addToCart(product: Product) {
        cartItems.add(product)
    }

    // Optionally, you can add methods to remove items or clear the cart
}
