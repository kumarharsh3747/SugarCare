package com.example.sugarfree

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressBookPage(navController: NavController, addressViewModel: AddressViewModel, currentUserEmail: String){
    // Observe the addresses from the ViewModel
    val addressList by remember { mutableStateOf(addressViewModel.addressList) }

    // Fetch addresses for the current user
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
                addressList.forEach { address ->
                    AddressCard(
                        name = address.name,
                        address = address.address,
                        isPrimary = address.isPrimary,
                        onEditClick = { /* Handle edit */ },
                        onDeleteClick = {
                            addressViewModel.deleteAddress(currentUserEmail, address.addressId) // Pass addressId for deletion
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(
                onClick = {
                    navController.navigate("addNewAddress")  // Navigate to Add Address Page
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
    isPrimary: Boolean = false,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
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

                if (isPrimary) {
                    Text(
                        text = "Primary",
                        fontSize = 14.sp,
                        color = Color.Green,
                        modifier = Modifier.padding(start = 8.dp)
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