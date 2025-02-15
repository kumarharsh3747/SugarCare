package com.example.sugarfree

import androidx.compose.foundation.*
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
import androidx.compose.ui.draw.shadow
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun AuthPage(navController: NavController, returnTo: String? = null) {
    var isLoginMode by remember { mutableStateOf(true) }

    if (isLoginMode) {
        LoginPage(navController, returnTo = returnTo) { isLoginMode = false }
    } else {
        SignupPage(navController, returnTo = returnTo) { isLoginMode = true }
    }
}

@Composable
fun LoginPage(navController: NavController, returnTo: String? = null, onSwitchToSignup: () -> Unit) {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFB2DFDB), Color.White))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White, shape = MaterialTheme.shapes.medium)
                .padding(32.dp)
                .border(1.dp, Color.LightGray, shape = MaterialTheme.shapes.medium)
                .shadow(4.dp, shape = MaterialTheme.shapes.medium)
        ) {
            Text(
                text = "Login",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            CustomTextField(value = email, placeholder = "Email") { email = it }
            Spacer(modifier = Modifier.height(16.dp))
            CustomTextField(value = password, placeholder = "Password", isPassword = true) { password = it }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val emailInput = email.text
                    val passwordInput = password.text

                    if (emailInput.isNotEmpty() && passwordInput.isNotEmpty()) {
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailInput, passwordInput)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    if (returnTo != null) {
                                        navController.navigate(returnTo)
                                    } else {
                                        navController.navigate("home")
                                    }
                                } else {
                                    Toast.makeText(context, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                    } else {
                        Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B))
            ) {
                Text(text = "Login", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Don’t have an account? Sign Up",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable { onSwitchToSignup() },
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun SignupPage(navController: NavController, returnTo: String? = null, onSwitchToLogin: () -> Unit) {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var confirmPassword by remember { mutableStateOf(TextFieldValue("")) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFECEFF1), Color.White))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White, shape = MaterialTheme.shapes.medium)
                .padding(32.dp)
                .border(1.dp, Color.LightGray, shape = MaterialTheme.shapes.medium)
                .shadow(4.dp, shape = MaterialTheme.shapes.medium)
        ) {
            Text(
                text = "Sign Up",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            CustomTextField(value = email, placeholder = "Email") { email = it }
            Spacer(modifier = Modifier.height(16.dp))
            CustomTextField(value = password, placeholder = "Password", isPassword = true) { password = it }
            Spacer(modifier = Modifier.height(16.dp))
            CustomTextField(value = confirmPassword, placeholder = "Confirm Password", isPassword = true) { confirmPassword = it }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val emailInput = email.text
                    val passwordInput = password.text
                    val confirmPasswordInput = confirmPassword.text

                    if (passwordInput != confirmPasswordInput) {
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_LONG).show()
                        return@Button
                    }

                    if (emailInput.isNotEmpty() && passwordInput.isNotEmpty()) {
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailInput, passwordInput)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(context, "Signup Successful", Toast.LENGTH_LONG).show()
                                    if (returnTo != null) {
                                        navController.navigate(returnTo)
                                    } else {
                                        navController.navigate("home")
                                    }
                                } else {
                                    Toast.makeText(context, "Signup failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                    } else {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B))
            ) {
                Text(text = "Sign Up", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

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

@Composable
fun CustomTextField(value: TextFieldValue, placeholder: String, isPassword: Boolean = false, onValueChange: (TextFieldValue) -> Unit) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        decorationBox = { innerTextField ->
            Box(
                Modifier
                    .background(Color.LightGray, shape = MaterialTheme.shapes.small)
                    .padding(16.dp)
            ) {
                if (value.text.isEmpty()) {
                    Text(text = placeholder, color = Color.Gray)
                }
                innerTextField()
            }
        }
    )
}
