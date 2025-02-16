package com.example.sugarfree

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Define the API interface for Open Food Facts
interface FoodApi {
    @GET("api/v0/product/{barcode}.json")
    suspend fun searchFoodByBarcode(
        @Path("barcode") barcode: String
    ): Product1
}


// Retrofit instance
object RetrofitInstance {
    val api: FoodApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://world.openfoodfacts.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FoodApi::class.java)
    }
}
