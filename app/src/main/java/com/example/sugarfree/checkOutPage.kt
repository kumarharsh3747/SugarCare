package com.example.sugarfree

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutPage(
    navController: NavController,
    cartViewModel: CartViewModel,
    addressViewModel: AddressViewModel = viewModel(),
    currentUserEmail: String
) {
    val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email ?: return

    val primaryAddress by addressViewModel.primaryAddress.collectAsState()
    val addressList by addressViewModel.addressList.collectAsState()

    // State to hold the selected address
    val selectedAddress = remember { mutableStateOf(primaryAddress) }

    LaunchedEffect(currentUserEmail) {
        addressViewModel.fetchAddresses(currentUserEmail)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Checkout", color = Color.Black) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.Black
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Savings ₹##", color = Color(0xFFFFA726))
                    val totalPrice = cartViewModel.getTotalPrice()
                    Text("You Pay ₹$totalPrice", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Selected Pincode: *****", color = Color.Gray, fontSize = 14.sp)

                Spacer(modifier = Modifier.height(32.dp))

                Text("Delivery Mode", fontWeight = FontWeight.Bold)
                DeliveryModeCard()

                Spacer(modifier = Modifier.height(16.dp))

                // Delivery Address Section
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Delivery Address", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Edit",
                        fontSize = 14.sp,
                        color = Color.Blue,
                        modifier = Modifier.clickable { navController.navigate("addressBook") }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (addressList.isEmpty()) {
                    Text("No saved addresses", modifier = Modifier.padding(16.dp))
                } else {
                    // Scrollable list of addresses
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f) // Make the LazyColumn take up available space
                    ) {
                        items(addressList) { address ->
                            AddressCard1(
                                name = address.name,
                                address = address.address,
                                phoneNumber = address.phoneNumber,
                                isPrimary = address.isPrimary,
                                isSelected = selectedAddress.value == address, // Highlight selected address
                                onSelect = { selectedAddress.value = address } // Handle selection
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                selectedAddress.value?.let {
                    Text(
                        "Selected Address: ${it.name}, ${it.address}, Contact: ${it.phoneNumber}",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Confirm Address Button at the bottom
                Button(
                    onClick = {
                        navController.navigate("selectPaymentPage") // Proceed with the selected address
                    },
                    enabled = selectedAddress.value != null,
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Confirm Address")
                }
            }
        }
    }
}

// Update AddressCard1 to handle selection
@Composable
fun AddressCard1(
    name: String,
    address: String,
    phoneNumber: String,
    isPrimary: Boolean,
    isSelected: Boolean, // Add parameter to indicate selection
    onSelect: () -> Unit // Callback when an address is selected
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onSelect() }, // Call onSelect on click
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFD0E8FF) else Color(0xFFF7F7F7) // Highlight selected address
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                // Optional Primary Label
                if (isPrimary) {
                    Text(
                        text = "(Primary)",
                        fontSize = 14.sp,
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = address,
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Contact: $phoneNumber",
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
fun DeliveryModeCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Home Delivery", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.weight(1f))
            Text("Free", color = Color.Gray, fontSize = 14.sp)
        }
    }
}
