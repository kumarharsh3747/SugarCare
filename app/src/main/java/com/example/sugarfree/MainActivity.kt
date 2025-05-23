package com.example.sugarfree

import WaterIntakeApp
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sugarfree.Reminder.HydrateReminderScreen
import com.example.sugarfree.Reminder.MealReminderScreen
import com.example.sugarfree.Reminder.MedicineReminderScreen
import com.example.sugarfree.Reminder.ReminderScreen
import com.example.sugarfree.ui.theme.SugarFreeTheme
//import com.example.sugarfreeupdated.HealthChallengeScreen
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check and request POST_NOTIFICATIONS permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val REQUEST_CODE_POST_NOTIFICATIONS = 0
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_CODE_POST_NOTIFICATIONS
                )
            }
        }
        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Create Notification Channel
        createNotificationChannel()

        enableEdgeToEdge()  // Custom function for edge-to-edge display

        setContent {
            val navController = rememberNavController()
            val cartViewModel: CartViewModel = viewModel()
            val addressViewModel: AddressViewModel by viewModels()
            val user = FirebaseAuth.getInstance().currentUser
            val loggedInUserEmail = user?.email ?: ""
            val ordersViewModel: OrderViewModel = viewModel()

            // Setup Navigation
            NavHost(navController = navController, startDestination = "home") {
                composable("home") { MainScreen(navController) }
                composable("auth") { AuthPage(navController) }
                composable("calculator/{calculatorType}", arguments = listOf(
                    navArgument("calculatorType") { type = NavType.StringType }
                )) { backStackEntry ->
                    val calculatorType = backStackEntry.arguments?.getString("calculatorType") ?: ""
                    CalculatorScreen(navController, calculatorType)
                }
                composable("healthCare") { HealthCareHome(navController) }
                composable("foodScanner") { FoodScannerUI(navController) }
                composable("foodscanner") {
                    BarcodeScannerScreen { scannedBarcode ->
                        navController.navigate("foodDetails/$scannedBarcode")
                    }
                }
                composable("foodDetails/{barcode}") { backStackEntry ->
                    val barcode = backStackEntry.arguments?.getString("barcode") ?: ""
                    FetchNutritionInfoScreen(barcode)
                }
                composable("detox") { DetoxTimerPage(navController) }
                composable("fruitlist") { FruitAppScreen(navController) }
                composable("healthMonitor") { HealthMonitor(navController) }
                composable("symptoms") { Symptoms() }
                composable("diet") { DietPlans() }
                composable("blood_sugar_checker") { BloodSugarChecker(navController) }
                composable("cart") { CartScreen(navController, cartViewModel) }
                composable("checkout") {
                    CheckoutPage(
                        navController = navController,
                        addressViewModel = addressViewModel,
                        currentUserEmail = loggedInUserEmail,
                        cartViewModel = cartViewModel
                    )
                }
                composable("selectPaymentPage") {
                    SelectPaymentPage(
                        navController = navController,
                        totalAmount = cartViewModel.getTotalPrice()
                    )
                }
                composable("orderConfirmation") {
                    OrderConfirmationPage(navController, cartViewModel)
                }
                composable("myOrders") {
                    MyOrdersPage(navController, ordersViewModel, loggedInUserEmail)
                }
                composable("ecommerce") { ECommercePage(navController, cartViewModel) }
                composable("account") { MyAccountPage(navController, loggedInUserEmail) }
                composable("MyProfilePage") { MyProfilePage(navController) }
                composable("addressBook") {
                    AddressBookPage(navController, addressViewModel, loggedInUserEmail)
                }
                composable("addNewAddress") {
                    AddNewAddressPage(navController, addressViewModel, loggedInUserEmail)
                }
                composable("profile") { ProfilePage(navController, addressViewModel, ordersViewModel) }
                composable("challanges") { challanges(navController) }
                composable("details1") { HealthTipsScreen(navController) }
                composable("details2") {
                    val gameState = remember { GameState() }
                    HealthTipsScreen2(navController, gameState)
                }
                composable("details3") {
                    val gameState = remember { GameState() }
                    HealthTipsScreen3(navController, gameState)
                }
                composable("fruitDetails/{fruitName}") { backStackEntry ->
                    val fruitName = backStackEntry.arguments?.getString("fruitName") ?: ""
                    FruitDetailsScreen(fruitName)
                }
                composable("productDetail/{productId}") { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId")?.toInt() ?: 0
                    ProductDetailPage(productId, navController, cartViewModel)
                }
                composable("ChatBot") { ChatScreen(navController, PaddingValues()) }
                composable("Reminders") { ReminderScreen(navController) }
                composable("Water_intake") { WaterIntakeApp(navController) }
                composable("MedicineReminder") {
                    MedicineReminderScreen(navController)
                }
                composable("MealReminder") {
                    MealReminderScreen(navController)
                }
                composable("HydrateReminder") {
                    HydrateReminderScreen(navController)
                }
              //  composable("game"){   HealthChallengeScreen(navController = navController)}

            }
        }
    }

    /**
     * Function to create a notification channel (Required for Android 8.0 and above)
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "sugarfree_notifications", // Unique channel ID
                "Sugar Free Alerts",  // User-visible name
                NotificationManager.IMPORTANCE_HIGH  // Importance level
            ).apply {
                description = "Notifications for sugar intake alerts and reminders"
            }

            // Register the channel with the system
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
