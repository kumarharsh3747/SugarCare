package com.example.sugarfree


import WaterIntakeApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.PaddingValues
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
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        enableEdgeToEdge()  // Custom function for edge-to-edge display
        setContent {
            val navController = rememberNavController()
            val cartViewModel: CartViewModel = viewModel() // Create an instance of CartViewModel
            val addressViewModel: AddressViewModel by viewModels() // Create an instance of AddressViewModel
            val user = FirebaseAuth.getInstance().currentUser
            val loggedInUserEmail = user?.email ?: "" // Fetch current user's email
            val ordersViewModel: OrderViewModel = viewModel() // Get ViewModel instance
            // Setup Navigation
            NavHost(navController = navController, startDestination = "home") { // Start with home
                composable("home") {
                    MainScreen(navController) // Home composable with profile icon
                }
                composable("auth") {
                    AuthPage(navController) // This handles both login and signup
                }
                composable("foodScanner") {
                    FoodScannerUI(navController)
                }
                composable("foodscanner") {
                    BarcodeScannerScreen { scannedBarcode ->
                        navController.navigate("foodDetails/$scannedBarcode")
                    }
                }
                composable("foodDetails/{barcode}") { backStackEntry ->
                    val barcode = backStackEntry.arguments?.getString("barcode") ?: ""
                    FetchNutritionInfoScreen(barcode)
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
                composable("checkout") {
                    CheckoutPage(
                        navController = navController,
                        addressViewModel = addressViewModel,
                        currentUserEmail = "user@example.com", // Pass the current user's email here
                        cartViewModel = cartViewModel // Pass the cartViewModel if needed
                    )
                }
                composable("selectPaymentPage") {
                    SelectPaymentPage(
                        navController = navController,
                        totalAmount = cartViewModel.getTotalPrice()

                    )
                }
                composable("orderConfirmation") {
                    OrderConfirmationPage(
                        navController = navController,
                        cartViewModel = cartViewModel
                    )
                }


                composable("myOrders") {

                    MyOrdersPage(navController = navController, orderViewModel = ordersViewModel, loggedInUserEmail) // Pass ViewModel to MyOrdersPage
                }



                composable("ecommerce") {
                    ECommercePage(navController, cartViewModel) // Pass the navController here
                }
                composable("account") {
                    MyAccountPage(navController, loggedInUserEmail) // Pass the loggedInUserEmail here
                }
                composable("MyProfilePage") {
                    MyProfilePage(navController) // Pass the navController here
                }
                composable("addressBook") {
                    AddressBookPage(navController, addressViewModel, loggedInUserEmail) // Pass loggedInUserEmail
                }
                composable("addNewAddress") {
                    AddNewAddressPage(navController, addressViewModel, loggedInUserEmail) // Pass loggedInUserEmail
                }
                composable("profile") {
                    ProfilePage(navController,addressViewModel,ordersViewModel) // Pass the navController here
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

                composable("productDetail/{productId}") { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId")?.toInt() ?: 0
                    ProductDetailPage(productId = productId, navController = navController, cartViewModel = cartViewModel)
                }

                composable("ChatBot"){
                    ChatScreen(navController, PaddingValues())
                }

                composable("Water_intake") {
                    WaterIntakeApp(navController)  // Pass the navController here
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
