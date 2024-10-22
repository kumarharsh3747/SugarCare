package com.example.sugarfree

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.platform.LocalContext

@Composable
fun AuthPage(navController: NavController) {
    var isLoginMode by remember { mutableStateOf(true) }

    if (isLoginMode) {
        LoginPage(navController) { isLoginMode = false } // Correctly passing the switch function
    } else {
        SignupPage(navController) { isLoginMode = true }
    }
}

@Composable
fun LoginPage(navController: NavController, onSwitchToSignup: () -> Unit) {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color.LightGray, Color.White))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White)
                .padding(32.dp)
        ) {
            Text(
                text = "Login",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Email Input
            BasicTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box(
                        Modifier
                            .background(Color.LightGray, shape = MaterialTheme.shapes.small)
                            .padding(16.dp)
                    ) {
                        if (email.text.isEmpty()) {
                            Text(text = "Email")
                        }
                        innerTextField()
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Input
            BasicTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box(
                        Modifier
                            .background(Color.LightGray, shape = MaterialTheme.shapes.small)
                            .padding(16.dp)
                    ) {
                        if (password.text.isEmpty()) {
                            Text(text = "Password")
                        }
                        innerTextField()
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Login Button
            Button(
                onClick = {
                    val emailInput = email.text
                    val passwordInput = password.text

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(emailInput, passwordInput)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                navController.navigate("home") // Navigate to Home Page on success
                            } else {
                                Toast.makeText(context, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(text = "Login", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sign Up Text
            Text(
                text = "Don’t have an account? Sign Up",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable { onSwitchToSignup() }, // Correctly switching to Signup
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun SignupPage(navController: NavController, onSwitchToLogin: () -> Unit) {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var confirmPassword by remember { mutableStateOf(TextFieldValue("")) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color.LightGray, Color.White))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White)
                .padding(32.dp)
        ) {
            Text(
                text = "Sign Up",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Email Input
            BasicTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box(
                        Modifier
                            .background(Color.LightGray, shape = MaterialTheme.shapes.small)
                            .padding(16.dp)
                    ) {
                        if (email.text.isEmpty()) {
                            Text(text = "Email")
                        }
                        innerTextField()
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Input
            BasicTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box(
                        Modifier
                            .background(Color.LightGray, shape = MaterialTheme.shapes.small)
                            .padding(16.dp)
                    ) {
                        if (password.text.isEmpty()) {
                            Text(text = "Password")
                        }
                        innerTextField()
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password Input
            BasicTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box(
                        Modifier
                            .background(Color.LightGray, shape = MaterialTheme.shapes.small)
                            .padding(16.dp)
                    ) {
                        if (confirmPassword.text.isEmpty()) {
                            Text(text = "Confirm Password")
                        }
                        innerTextField()
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Sign Up Button
            Button(
                onClick = {
                    val emailInput = email.text
                    val passwordInput = password.text
                    val confirmPasswordInput = confirmPassword.text

                    // Check if passwords match
                    if (passwordInput != confirmPasswordInput) {
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_LONG).show()
                        return@Button
                    }

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailInput, passwordInput)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
//                                navController.navigate("home") // Navigate to Home Page on success
                                Toast.makeText(context, "Signup Successful", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(context, "Signup failed ", Toast.LENGTH_LONG).show()
                            }
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(text = "Sign Up", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Already have an account Text
            Text(
                text = "Already have an account? Login",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable { onSwitchToLogin() },
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }
    }
}
