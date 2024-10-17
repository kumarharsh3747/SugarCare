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
import com.example.sugarfree.ui.theme.SugarFreeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  // Assuming this is a custom function you implemented
        setContent {
            val navController = rememberNavController()
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
                    ECommercePage(navController)
                }
                composable("fruitlist") {
                    Home2(navController)
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