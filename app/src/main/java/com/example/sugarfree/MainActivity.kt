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
import com.google.firebase.Firebase
import com.google.firebase.initialize

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Firebase.initialize(this)
        enableEdgeToEdge()  // Assuming this is a custom function you implemented
        setContent {
            val navController = rememberNavController()
            val cartViewModel: CartViewModel = viewModel() // Create an instance of CartViewModel


            NavHost(navController = navController, startDestination = "home") { // Start with home
                composable("home") {
                    MainScreen(navController) // Home composable with profile icon
                }
                composable("auth") {
                    AuthPage(navController) // This handles both login and signup
                }
                composable("foodScanner") {
                    FoodScannerSimpleUI(navController)
                }
                composable("detox") {
                    DetoxTimerPage(navController) // Pass the cartViewModel here
                }
                composable("fruitlist") {
                    FruitAppScreen(navController)
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
                  ECommercePage(navController,cartViewModel) // Pass the navController here
            }


                composable("profile") {
                    ProfilePage(navController) // Pass the navController here
                }
                composable("challanges") {
                    challanges(navController) // Pass the navController here
                }
                composable("details1") {
                    HealthTipsScreen(navController) // Pass the navController here
                }
                composable("details2") {
                    HealthTipsScreen2(navController) // Pass the navController here
                }
                composable("details3") {
                    HealthTipsScreen3(navController) // Pass the navController here
                }

                composable("fruitDetails/{fruitName}") { backStackEntry ->
                    val fruitName = backStackEntry.arguments?.getString("fruitName") ?: ""
                    FruitDetailsScreen(fruitName)
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
