package com.example.sugarfree

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf


class CartViewModel : ViewModel() {
    // List to hold cart items
    var cartItems = mutableStateListOf<Product>()
    var addresses: List<Address> = emptyList()
    // Function to add products to the cart
    fun addToCart(product: Product) {
        cartItems.add(product)
    }

    // Function to remove products from the cart
    fun removeFromCart(product: Product) {
        cartItems.remove(product)
    }

    // Function to calculate the total price of items in the cart
    fun getTotalPrice(): Double {
        return cartItems.sumOf { it.price }
    }

    // Optionally, you can clear the cart
    fun clearCart() {
        cartItems.clear()
    }



}
