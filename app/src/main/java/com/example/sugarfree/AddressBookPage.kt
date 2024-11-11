package com.example.sugarfree

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import kotlinx.coroutines.launch
import androidx.compose.material3.MaterialTheme // Make sure this import is included

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressBookPage(navController: NavController, addressViewModel: AddressViewModel = viewModel(), currentUserEmail: String) {
    val addressList by addressViewModel.addressList.collectAsState()

    LaunchedEffect(currentUserEmail) {
        addressViewModel.fetchAddresses(currentUserEmail)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Address Book", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (addressList.isEmpty()) {
                Text("No saved addresses", modifier = Modifier.padding(16.dp))
            } else {
                LazyColumn {
                    items(addressList) { address ->
                        AddressCard(
                            name = address.name,
                            address = address.address,
                            phoneNumber = address.phoneNumber,
                            isPrimary = address.isPrimary,
                            onEditClick = { /* Handle edit */ },
                            onDeleteClick = {
                                addressViewModel.deleteAddress(currentUserEmail, address.addressId)
                            },
                            onSetPrimaryClick = {
                                addressViewModel.setAsPrimaryAddress(currentUserEmail, address.addressId)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(
                onClick = {
                    navController.navigate("addNewAddress") // Navigate to Add Address Page
                },
                border = BorderStroke(1.dp, Color.Gray),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp)
            ) {
                Text(
                    text = "+ ADD NEW ADDRESS",
                    fontSize = 16.sp,
                    color = Color.Green,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun AddressCard(
    name: String,
    address: String,
    phoneNumber: String,
    isPrimary: Boolean = false,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onSetPrimaryClick: () -> Unit // Add onSetPrimaryClick as a parameter
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { /* Handle select action if needed */ },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7))
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

//                if (isPrimary) {
//                    Text(
//                        text = "Primary",
//                        fontSize = 14.sp,
//                        color = Color.Green,
//                        modifier = Modifier.padding(start = 8.dp)
//                    )
//                } else {
//                    Text(
//                        text = "Set as Primary",
//                        fontSize = 14.sp,
//                        color = Color.White,
//                        modifier = Modifier
//                            .padding(start = 8.dp)
//                            .background(color = Color.Green, shape = RoundedCornerShape(4.dp))
//                            .padding(horizontal = 8.dp, vertical = 4.dp)
//                            .clickable { onSetPrimaryClick() } // Call the function to set this address as primary
//                    )
//                }
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

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "EDIT",
                    fontSize = 16.sp,
                    color = Color.Green,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onEditClick() }
                )

                Text(
                    text = "DELETE",
                    fontSize = 16.sp,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onDeleteClick() }
                )
            }
        }
    }
}
