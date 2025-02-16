package com.example.sugarfree


data class Product1(
    val product: ProductDetails?
)

data class ProductDetails(
    val product_name: String?,
    val brand: String?,
    val code: String?,
    val image_url: String?,
    val ingredients_text: String?,
    val nutriments: Nutriments?
)

data class Nutriments(
    val energy_kcal: Double?,
    val fat: Double?,
    val carbohydrates: Double?,
    val sugars: Double?,
    val proteins: Double?
)
