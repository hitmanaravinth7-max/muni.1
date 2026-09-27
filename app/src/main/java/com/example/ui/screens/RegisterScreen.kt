package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BloodRed
import com.example.ui.theme.BloodRedContainer
import com.example.ui.theme.BorderLight
import com.example.ui.theme.MedicalGreen
import com.example.ui.theme.MedicalGreenContainer
import com.example.ui.theme.MedicalRed
import com.example.ui.theme.MedicalRedContainer
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.BloodBridgeViewModel

@Composable
fun RegisterScreen(
    viewModel: BloodBridgeViewModel,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val authState by viewModel.authState.collectAsState()

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var agreeTerms by remember { mutableStateOf(false) }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Validation errors
    var fullNameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var termsError by remember { mutableStateOf<String?>(null) }

    fun validate(): Boolean {
        var valid = true

        if (fullName.isBlank()) {
            fullNameError = "Full Name is required."
            valid = false
        } else {
            fullNameError = null
        }

        if (email.isBlank()) {
            emailError = "Email is required."
            valid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            emailError = "Please enter a valid email address."
            valid = false
        } else {
            emailError = null
        }

        val cleanPhone = phone.trim().replace(Regex("[^0-9+]"), "")
        if (cleanPhone.length < 10) {
            phoneError = "Valid phone number is required (10+ digits)."
            valid = false
        } else {
            phoneError = null
        }

        if (password.length < 8) {
            passwordError = "Password must be at least 8 characters."
            valid = false
        } else {
            passwordError = null
        }

        if (confirmPassword != password) {
            confirmPasswordError = "Passwords do not match."
            valid = false
        } else {
            confirmPasswordError = null
        }

        if (!agreeTerms) {
            termsError = "You must agree to the Terms of Service & Privacy Policy."
            valid = false
        } else {
            termsError = null
        }

        return valid
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
            .testTag("register_screen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(BloodRedContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Favorite, contentDescription = null, tint = BloodRed, modifier = Modifier.size(30.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Create BloodBridge Account",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextPrimary
        )

        Text(
            text = "Join our life-saving community as a user or donor",
            fontSize = 12.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("register_card"),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(16.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderLight))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Success banner
                if (authState.registerSuccess) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MedicalGreenContainer)
                            .padding(12.dp)
                            .testTag("register_success_banner")
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MedicalGreen, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Registration Successful!", fontWeight = FontWeight.Bold, color = MedicalGreen, fontSize = 13.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Your account has been created. Please sign in to continue.", fontSize = 12.sp, color = MedicalGreen)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    viewModel.resetRegisterSuccess()
                                    onNavigateToLogin()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MedicalGreen),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Proceed to Sign In")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Error banner
                if (authState.registerError != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MedicalRedContainer)
                            .padding(10.dp)
                            .testTag("register_error_banner")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Error, contentDescription = null, tint = MedicalRed, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(authState.registerError ?: "", fontSize = 12.sp, color = MedicalRed, fontWeight = FontWeight.SemiBold)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Full Name
                OutlinedTextField(
                    value = fullName,
                    onValueChange = {
                        fullName = it
                        fullNameError = null
                    },
                    modifier = Modifier.fillMaxWidth().testTag("register_name_input"),
                    label = { Text("Full Name *") },
                    placeholder = { Text("e.g. Arun Kumar") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = BloodRed) },
                    isError = fullNameError != null,
                    supportingText = fullNameError?.let { { Text(it, color = MedicalRed, fontSize = 11.sp) } },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Email
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = null
                    },
                    modifier = Modifier.fillMaxWidth().testTag("register_email_input"),
                    label = { Text("Email Address *") },
                    placeholder = { Text("e.g. arun@example.com") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = BloodRed) },
                    isError = emailError != null,
                    supportingText = emailError?.let { { Text(it, color = MedicalRed, fontSize = 11.sp) } },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Phone Number
                OutlinedTextField(
                    value = phone,
                    onValueChange = {
                        phone = it
                        phoneError = null
                    },
                    modifier = Modifier.fillMaxWidth().testTag("register_phone_input"),
                    label = { Text("Phone Number *") },
                    placeholder = { Text("+91 9876543210") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = BloodRed) },
                    isError = phoneError != null,
                    supportingText = phoneError?.let { { Text(it, color = MedicalRed, fontSize = 11.sp) } },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Password
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = null
                    },
                    modifier = Modifier.fillMaxWidth().testTag("register_password_input"),
                    label = { Text("Password (min 8 chars) *") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = BloodRed) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, contentDescription = null)
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    isError = passwordError != null,
                    supportingText = passwordError?.let { { Text(it, color = MedicalRed, fontSize = 11.sp) } },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Confirm Password
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        confirmPasswordError = null
                    },
                    modifier = Modifier.fillMaxWidth().testTag("register_confirm_password_input"),
                    label = { Text("Confirm Password *") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = BloodRed) },
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, contentDescription = null)
                        }
                    },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    isError = confirmPasswordError != null,
                    supportingText = confirmPasswordError?.let { { Text(it, color = MedicalRed, fontSize = 11.sp) } },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Agree to terms
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.testTag("register_terms_row")
                ) {
                    Checkbox(
                        checked = agreeTerms,
                        onCheckedChange = {
                            agreeTerms = it
                            termsError = null
                        },
                        colors = CheckboxDefaults.colors(checkedColor = BloodRed)
                    )
                    Text(
                        text = "I agree to BloodBridge Terms of Service & Privacy Policy *",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        lineHeight = 15.sp
                    )
                }
                if (termsError != null) {
                    Text(termsError ?: "", color = MedicalRed, fontSize = 11.sp, modifier = Modifier.padding(start = 12.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Submit Button
                Button(
                    onClick = {
                        if (validate()) {
                            viewModel.register(fullName, email, phone, password, confirmPassword, agreeTerms) { success, _ ->
                                // Register success handles banner
                            }
                        }
                    },
                    enabled = !authState.isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = BloodRed),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("register_submit_button")
                ) {
                    if (authState.isLoading) {
                        CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Creating Account...", fontSize = 14.sp)
                    } else {
                        Text("Create Account", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Already have an account? ", fontSize = 13.sp, color = TextSecondary)
                    Text(
                        text = "Sign In",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = BloodRed,
                        modifier = Modifier
                            .testTag("register_to_login_link")
                            .clickable { onNavigateToLogin() }
                    )
                }
            }
        }
    }
}
