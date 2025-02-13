package com.example.sugarfree

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack


private lateinit var auth: FirebaseAuth

data class UserProfile(
    val firstName: String = "",
    val lastName: String = "",
    val mobileNumber: String = "",
    val email: String = ""
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfilePage(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val currentUserEmail = auth.currentUser?.email ?: ""
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var saveStatus by remember { mutableStateOf("") } // For showing save status message

    // Fetch user profile data on first composition
    LaunchedEffect(Unit) {
        fetchUserProfile(currentUserEmail, onProfileFetched = { profile ->
            firstName = profile.firstName
            lastName = profile.lastName
            mobileNumber = profile.mobileNumber
        })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "My Profile", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { // Back button
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF4CAF50)) // Custom color for the top bar
            )
        }
    ) { paddingValues ->
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Ensures content is below the TopAppBar
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileTextField(
                label = "First Name *",
                value = firstName,
                onValueChange = { firstName = it }
            )
            ProfileTextField(
                label = "Last Name *",
                value = lastName,
                onValueChange = { lastName = it }
            )
            ProfileTextField(
                label = "Your Mobile Number *",
                value = mobileNumber,
                onValueChange = { mobileNumber = it },
                keyboardType = KeyboardType.Phone
            )

            // Display the email as a non-editable Text
            Text(
                text = "Your Email Id: $currentUserEmail",
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    saveProfileToFirebase(
                        UserProfile(firstName, lastName, mobileNumber, currentUserEmail),
                        onComplete = { success, message ->
                            saveStatus = if (success) "Profile saved successfully!" else "Error: $message"
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                )
            ) {
                Text(text = "SAVE CHANGES", color = Color.White)
            }

            // Show status message
            if (saveStatus.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = saveStatus, color = if (saveStatus.contains("Error")) Color.Red else Color.Green)
            }
        }
    }
}


// Function to fetch user profile data from Firebase
fun fetchUserProfile(email: String, onProfileFetched: (UserProfile) -> Unit) {
    val database = FirebaseDatabase.getInstance().reference
    val sanitizedEmail = email.replace(".", ",") // Sanitize email

    database.child("users").child(sanitizedEmail).get()
        .addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                val profile = dataSnapshot.getValue(UserProfile::class.java) ?: UserProfile()
                onProfileFetched(profile)
            } else {
                onProfileFetched(UserProfile()) // Return empty profile
            }
        }
        .addOnFailureListener { error ->
            println("Failed to fetch profile: ${error.message}")
            onProfileFetched(UserProfile()) // Return empty profile on failure
        }
}

@Composable
fun ProfileTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE0E0E0))
                .padding(12.dp)
        )
    }
}

// Function to Save Profile Data to Firebase
fun saveProfileToFirebase(profile: UserProfile, onComplete: (Boolean, String?) -> Unit) {
    val database = FirebaseDatabase.getInstance().reference
    val sanitizedEmail = profile.email.replace(".", ",") // Sanitize email

    database.child("users").child(sanitizedEmail)
        .setValue(profile)
        .addOnSuccessListener {
            onComplete(true, null) // Success
        }
        .addOnFailureListener { error ->
            onComplete(false, error.message) // Failure with error message
        }
}
