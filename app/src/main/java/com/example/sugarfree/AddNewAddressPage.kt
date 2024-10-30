package com.example.sugarfree

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewAddressPage(navController: NavController, addressViewModel: AddressViewModel, currentUserEmail: String) {
    // State for form inputs
    var fullName by remember { mutableStateOf("") }
    var pincode by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("") }
    var street by remember { mutableStateOf("") }
    var houseNo by remember { mutableStateOf("") }
    var landmark by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Address", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp)  // Space between elements
        ) {
            // Form fields for address details
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Contact Number for Order Delivery") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = pincode,
                onValueChange = { pincode = it },
                label = { Text("Pincode") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = area,
                onValueChange = { area = it },
                label = { Text("Area") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = street,
                onValueChange = { street = it },
                label = { Text("Locality/Street Name/Apartment") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = houseNo,
                onValueChange = { houseNo = it },
                label = { Text("Wing/Floor/Flat/House No.") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = landmark,
                onValueChange = { landmark = it },
                label = { Text("Landmark (optional)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("City") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state,
                onValueChange = { state = it },
                label = { Text("State") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // Validate inputs
                    if (fullName.isNotBlank() && pincode.isNotBlank() && phoneNumber.isNotBlank()) {
                        // Create a new address object
                        val newAddress = Address(
                            name = fullName,
                            address = """
                                $houseNo, $street, $area
                                $city, $state - $pincode
                                Landmark: $landmark
                            """.trimIndent(),
                            phoneNumber = phoneNumber,  // Include phone number
                            isPrimary = false
                        )

                        // Call ViewModel to add address
                        addressViewModel.addAddress(currentUserEmail, newAddress)
                        navController.popBackStack() // Navigate back after saving
                    } else {
                        // Handle empty fields (optional: show a snackbar or toast)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("SAVE TO ADDRESS BOOK")
            }
        }
    }
}
