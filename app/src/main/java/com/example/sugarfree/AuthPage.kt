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

// Color palette matching the screenshot
object SugarFreeColors {
    val Background = Color(0xFFF5F5F5)
    val Primary = Color(0xFF5E7C8B)
    val Secondary = Color(0xFFB0BEC5)
    val TextPrimary = Color(0xFF37474F)
    val TextSecondary = Color(0xFF607D8B)
}

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
            .background(SugarFreeColors.Background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(24.dp)
                .background(Color.White, shape = MaterialTheme.shapes.large)
                .padding(32.dp)
//                .shadow(4.dp, shape = MaterialTheme.shapes.large)
        ) {
            Text(
                text = "Welcome Back",
                fontSize = 28.sp,
                fontWeight = FontWeight.Medium,
                color = SugarFreeColors.TextPrimary,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            CustomTextField(
                value = email,
                placeholder = "Email",
                placeholderColor = SugarFreeColors.TextSecondary
            ) { email = it }

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                value = password,
                placeholder = "Password",
                isPassword = true,
                placeholderColor = SugarFreeColors.TextSecondary
            ) { password = it }

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
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SugarFreeColors.Primary
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Login",
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Don't have an account? Sign Up",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable { onSwitchToSignup() },
                textAlign = TextAlign.Center,
                color = SugarFreeColors.TextSecondary
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
            .background(SugarFreeColors.Background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(24.dp)
                .background(Color.White, shape = MaterialTheme.shapes.large)
                .padding(32.dp)
//                .shadow(4.dp, shape = MaterialTheme.shapes.large)
        ) {
            Text(
                text = "Create Account",
                fontSize = 28.sp,
                fontWeight = FontWeight.Medium,
                color = SugarFreeColors.TextPrimary,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            CustomTextField(
                value = email,
                placeholder = "Email",
                placeholderColor = SugarFreeColors.TextSecondary
            ) { email = it }

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                value = password,
                placeholder = "Password",
                isPassword = true,
                placeholderColor = SugarFreeColors.TextSecondary
            ) { password = it }

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                value = confirmPassword,
                placeholder = "Confirm Password",
                isPassword = true,
                placeholderColor = SugarFreeColors.TextSecondary
            ) { confirmPassword = it }

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
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SugarFreeColors.Primary
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Sign Up",
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Already have an account? Login",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable { onSwitchToLogin() },
                textAlign = TextAlign.Center,
                color = SugarFreeColors.TextSecondary
            )
        }
    }
}

@Composable
fun CustomTextField(
    value: TextFieldValue,
    placeholder: String,
    isPassword: Boolean = false,
    placeholderColor: Color = Color.Gray,
    onValueChange: (TextFieldValue) -> Unit
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, SugarFreeColors.Secondary, shape = MaterialTheme.shapes.small)
            .padding(16.dp),
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        decorationBox = { innerTextField ->
            Box {
                if (value.text.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = placeholderColor
                    )
                }
                innerTextField()
            }
        }
    )
}