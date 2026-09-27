package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.DonorProfileEntity
import com.example.ui.components.BloodGroupBadge
import com.example.ui.theme.BloodRed
import com.example.ui.theme.BloodRedContainer
import com.example.ui.theme.BorderLight
import com.example.ui.theme.MedicalGreen
import com.example.ui.theme.MedicalNavy
import com.example.ui.theme.MedicalOrange
import com.example.ui.theme.MedicalOrangeContainer
import com.example.ui.theme.MedicalRed
import com.example.ui.theme.MedicalRedContainer
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.BloodBridgeViewModel
import com.example.util.BloodCompatibility
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DonorProfileScreen(
    viewModel: BloodBridgeViewModel,
    onSaveSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val authState by viewModel.authState.collectAsState()
    val user = authState.currentUser
    val existingProfile = authState.donorProfile

    var bloodGroup by remember { mutableStateOf(existingProfile?.bloodGroup ?: "O+") }
    var dateOfBirth by remember { mutableStateOf(existingProfile?.dateOfBirth ?: "1998-05-15") }
    var gender by remember { mutableStateOf(existingProfile?.gender ?: "MALE") }
    var weightKgStr by remember { mutableStateOf(existingProfile?.weightKg?.toString() ?: "65.0") }
    var addressLine by remember { mutableStateOf(existingProfile?.addressLine ?: "") }
    var city by remember { mutableStateOf(existingProfile?.city ?: "Coimbatore") }
    var state by remember { mutableStateOf(existingProfile?.state ?: "Tamil Nadu") }
    var pincode by remember { mutableStateOf(existingProfile?.pincode ?: "641001") }

    var lastDonationDaysAgoStr by remember {
        val days = existingProfile?.lastDonationDate?.let {
            ((System.currentTimeMillis() - it) / (86400000L)).toString()
        } ?: ""
        mutableStateOf(days)
    }

    var isAvailable by remember { mutableStateOf(existingProfile?.isAvailable ?: true) }
    var hasChronicIllness by remember { mutableStateOf(existingProfile?.hasChronicIllness ?: false) }
    var onMedication by remember { mutableStateOf(existingProfile?.onMedication ?: false) }
    var recentSurgery by remember { mutableStateOf(existingProfile?.recentSurgery ?: false) }
    var consentGiven by remember { mutableStateOf(existingProfile?.consentGiven ?: true) }

    // Validation errors
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val bloodGroupsList = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
    val gendersList = listOf("MALE", "FEMALE", "OTHER")

    // Cooldown calculation
    val parsedDaysAgo = lastDonationDaysAgoStr.toLongOrNull()
    val isUnderCooldown = parsedDaysAgo != null && parsedDaysAgo < 90L
    val cooldownRemaining = if (isUnderCooldown && parsedDaysAgo != null) 90L - parsedDaysAgo else 0L

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("donor_profile_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = BloodRedContainer),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Favorite, contentDescription = null, tint = BloodRed, modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (existingProfile == null) "Become a Verified Blood Donor" else "Update Donor Profile",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Your donor information helps save lives during critical emergencies.",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        if (errorMsg != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MedicalRedContainer)
                    .padding(12.dp)
            ) {
                Text(errorMsg ?: "", color = MedicalRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Section 1: Blood Group & Basic Info
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(16.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderLight))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("1. Blood Group *", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    for (bg in bloodGroupsList) {
                        val isSelected = bloodGroup == bg
                        FilterChip(
                            selected = isSelected,
                            onClick = { bloodGroup = bg },
                            label = { Text(bg, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BloodRed,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("donor_bg_chip_$bg")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text("Gender *", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary)
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (g in gendersList) {
                        FilterChip(
                            selected = gender == g,
                            onClick = { gender = g },
                            label = { Text(g, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MedicalNavy,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Date of Birth & Weight
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = dateOfBirth,
                        onValueChange = { dateOfBirth = it },
                        modifier = Modifier.weight(1f).testTag("donor_dob_input"),
                        label = { Text("Date of Birth *") },
                        placeholder = { Text("YYYY-MM-DD") },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = weightKgStr,
                        onValueChange = { weightKgStr = it },
                        modifier = Modifier.weight(1f).testTag("donor_weight_input"),
                        label = { Text("Weight (kg) *") },
                        placeholder = { Text("50+") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }
        }

        // Section 2: Residential Address
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(16.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderLight))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("2. Residential Location *", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text("Used exclusively for emergency radius matching. Never shown publicly.", fontSize = 11.sp, color = TextSecondary)
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = addressLine,
                    onValueChange = { addressLine = it },
                    modifier = Modifier.fillMaxWidth().testTag("donor_address_input"),
                    label = { Text("Residential Address Line *") },
                    placeholder = { Text("Street, Area, Landmark") },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        modifier = Modifier.weight(1f).testTag("donor_city_input"),
                        label = { Text("City *") },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = state,
                        onValueChange = { state = it },
                        modifier = Modifier.weight(1f).testTag("donor_state_input"),
                        label = { Text("State *") },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = pincode,
                    onValueChange = { pincode = it },
                    modifier = Modifier.fillMaxWidth().testTag("donor_pincode_input"),
                    label = { Text("PIN Code *") },
                    placeholder = { Text("e.g. 641001") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }

        // Section 3: Donation History & Cooldown
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(16.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderLight))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("3. Donation History & Cooldown", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = lastDonationDaysAgoStr,
                    onValueChange = { lastDonationDaysAgoStr = it },
                    modifier = Modifier.fillMaxWidth().testTag("donor_last_donation_input"),
                    label = { Text("Days Since Last Donation (leave blank if first time)") },
                    placeholder = { Text("e.g. 120 (must be 90+ days to donate)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                if (isUnderCooldown) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MedicalOrangeContainer)
                            .padding(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = MedicalOrange, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Donation cooldown active ($cooldownRemaining days remaining). Your availability will be set to Not Available until eligible.",
                                fontSize = 12.sp,
                                color = MedicalOrange,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Availability toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Active Availability", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        Text(
                            text = if (isUnderCooldown) "Disabled due to 90-day cooldown" else "Available for emergency donor calls",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                    Switch(
                        checked = isAvailable && !isUnderCooldown,
                        enabled = !isUnderCooldown,
                        onCheckedChange = { isAvailable = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = MedicalGreen, checkedTrackColor = MedicalGreen.copy(alpha = 0.5f))
                    )
                }
            }
        }

        // Section 4: Health Criteria & Consent
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(16.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderLight))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("4. Health & Medical Clearance", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = hasChronicIllness, onCheckedChange = { hasChronicIllness = it })
                    Text("I have a chronic illness (diabetes, cardiac, hypertension)", fontSize = 12.sp, color = TextSecondary)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = onMedication, onCheckedChange = { onMedication = it })
                    Text("Currently taking prescription medications (antibiotics/steroids)", fontSize = 12.sp, color = TextSecondary)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = recentSurgery, onCheckedChange = { recentSurgery = it })
                    Text("Recent major surgery, tattoo, or piercing within last 6 months", fontSize = 12.sp, color = TextSecondary)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = consentGiven,
                        onCheckedChange = { consentGiven = it },
                        colors = CheckboxDefaults.colors(checkedColor = BloodRed)
                    )
                    Text(
                        text = "I consent to voluntary blood donation and certify that the above health info is true and accurate. *",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                }
            }
        }

        // Save Button
        Button(
            onClick = {
                val weight = weightKgStr.toDoubleOrNull()
                if (weight == null || weight < 50.0) {
                    errorMsg = "Donor must weigh at least 50 kg for blood donation."
                    return@Button
                }

                if (addressLine.isBlank() || city.isBlank() || state.isBlank() || pincode.isBlank()) {
                    errorMsg = "Please complete all residential address fields."
                    return@Button
                }

                if (!consentGiven) {
                    errorMsg = "Voluntary donation consent is required."
                    return@Button
                }

                errorMsg = null
                val userObj = user ?: return@Button

                val lastDonationEpoch = parsedDaysAgo?.let {
                    System.currentTimeMillis() - (it * 86400000L)
                }

                val effectiveAvailability = if (isUnderCooldown) false else isAvailable

                val profileToSave = DonorProfileEntity(
                    id = existingProfile?.id ?: 0,
                    userId = userObj.id,
                    bloodGroup = bloodGroup,
                    dateOfBirth = dateOfBirth,
                    gender = gender,
                    weightKg = weight,
                    addressLine = addressLine.trim(),
                    city = city.trim(),
                    state = state.trim(),
                    pincode = pincode.trim(),
                    latitude = existingProfile?.latitude ?: 11.0168,
                    longitude = existingProfile?.longitude ?: 76.9558,
                    lastDonationDate = lastDonationEpoch,
                    isAvailable = effectiveAvailability,
                    hasChronicIllness = hasChronicIllness,
                    onMedication = onMedication,
                    recentSurgery = recentSurgery,
                    totalDonations = existingProfile?.totalDonations ?: 0,
                    consentGiven = consentGiven,
                    verifiedByAdmin = existingProfile?.verifiedByAdmin ?: false
                )

                viewModel.saveDonorProfile(profileToSave) { success, err ->
                    if (success) {
                        onSaveSuccess()
                    } else {
                        errorMsg = err ?: "Failed to save donor profile."
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = BloodRed),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("save_donor_profile_button")
        ) {
            Text("Save & Register Donor Profile", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}
