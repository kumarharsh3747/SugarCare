package com.example.sugarfree

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sugarfree.ui.theme.SugarFreeTheme
import com.google.firebase.FirebaseApp
import android.app.Application



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this) // Initialize Firebase
        enableEdgeToEdge() // Assuming this is a custom function you implemented

        setContent {
            val navController = rememberNavController()
            val cartViewModel: CartViewModel = viewModel() // Create an instance of CartViewModel

            NavHost(navController = navController, startDestination = "home") { // Start with home
                composable("home") {
                    Home(navController) // Home composable with profile icon
                }
                composable("auth") {
                    AuthPage(navController) // This handles both login and signup
                }
                composable("foodScanner") {
                    FoodScannerSimpleUI(navController)
                }
                composable("detox") {
                    DetoxTimerPage(navController) // Pass the cartViewModel here if needed
                }
                composable("fruitlist") {
                    Home2(navController)
                }
                composable("healthMonitor") {
                    HealthMonitor(navController)
                }
                composable("symptoms") {
                    Symptoms()
                }
                composable("diet") {
                    DietPlans()
                }
                composable("blood_sugar_checker") {
                    BloodSugarChecker(navController)
                }
                composable("cart") {
                    CartScreen(navController, cartViewModel) // Pass the navController here
                }
                composable("ecommerce") {
                    ECommercePage(navController, cartViewModel) // Pass the navController and CartViewModel here
                }
                composable("profile") {
                    ProfileScreen(navController) // Pass the navController here
                }
                composable("placeOrder") {
                    PlaceOrderPage(cartViewModel.cartItems, onBack = { navController.popBackStack() }) {
                        // Handle the order placement logic here
                        cartViewModel.cartItems.clear() // Clear cart after placing the order
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SugarFreeTheme {
        Greeting("Android")
    }
}



class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
