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
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.EmergencyRequestEntity
import com.example.ui.components.EmergencyNoticeBanner
import com.example.ui.theme.BloodRed
import com.example.ui.theme.BorderLight
import com.example.ui.theme.MedicalGreen
import com.example.ui.theme.MedicalNavy
import com.example.ui.theme.MedicalOrange
import com.example.ui.theme.MedicalRed
import com.example.ui.theme.MedicalRedContainer
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.BloodBridgeViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EmergencyRequestScreen(
    viewModel: BloodBridgeViewModel,
    onSubmitSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val authState by viewModel.authState.collectAsState()
    val bloodBanks by viewModel.allBloodBanks.collectAsState()
    val user = authState.currentUser

    var patientName by remember { mutableStateOf("") }
    var selectedBloodGroup by remember { mutableStateOf("A+") }
    var selectedComponent by remember { mutableStateOf("WHOLE_BLOOD") }
    var unitsNeededStr by remember { mutableStateOf("2") }
    var selectedUrgency by remember { mutableStateOf("CRITICAL") }
    var hospitalName by remember { mutableStateOf("") }
    var selectedBloodBankId by remember { mutableStateOf<Long?>(null) }
    var contactPhone by remember { mutableStateOf(user?.phoneNumber ?: "") }
    var additionalNotes by remember { mutableStateOf("") }

    var errorMsg by remember { mutableStateOf<String?>(null) }

    val bloodGroups = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
    val components = listOf("WHOLE_BLOOD", "PLASMA", "PLATELETS", "RBC")
    val urgencies = listOf("CRITICAL", "URGENT", "NORMAL")

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("emergency_request_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Permanent Warning Banner
        EmergencyNoticeBanner()

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderLight))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AddAlert, contentDescription = null, tint = BloodRed, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Submit Emergency Blood Request",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Request will be submitted with status PENDING for immediate admin review and broadcast to matching donors.",
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                if (errorMsg != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MedicalRedContainer)
                            .padding(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Error, contentDescription = null, tint = MedicalRed, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(errorMsg ?: "", color = MedicalRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Patient Name
                OutlinedTextField(
                    value = patientName,
                    onValueChange = {
                        patientName = it
                        errorMsg = null
                    },
                    modifier = Modifier.fillMaxWidth().testTag("request_patient_name_input"),
                    label = { Text("Patient Full Name *") },
                    placeholder = { Text("e.g. Meenakshi Sundaram") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = BloodRed) },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Required Blood Group
                Text("Required Blood Group *", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary)
                Spacer(modifier = Modifier.height(4.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    for (bg in bloodGroups) {
                        val isSelected = selectedBloodGroup == bg
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedBloodGroup = bg },
                            label = { Text(bg, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BloodRed,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("request_bg_chip_$bg")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Required Component
                Text("Required Component *", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary)
                Spacer(modifier = Modifier.height(4.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    for (comp in components) {
                        val isSelected = selectedComponent == comp
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedComponent = comp },
                            label = { Text(comp.replace("_", " "), fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MedicalNavy,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Units Needed & Urgency Level
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = unitsNeededStr,
                        onValueChange = { unitsNeededStr = it },
                        modifier = Modifier.weight(1f).testTag("request_units_input"),
                        label = { Text("Units Needed *") },
                        placeholder = { Text("e.g. 2") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Column(modifier = Modifier.weight(1.5f)) {
                        Text("Urgency Level *", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            for (urg in urgencies) {
                                val isSelected = selectedUrgency == urg
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedUrgency = urg },
                                    label = { Text(urg, fontSize = 10.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = if (urg == "CRITICAL") MedicalRed else if (urg == "URGENT") MedicalOrange else MedicalGreen,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Hospital Name
                OutlinedTextField(
                    value = hospitalName,
                    onValueChange = {
                        hospitalName = it
                        errorMsg = null
                    },
                    modifier = Modifier.fillMaxWidth().testTag("request_hospital_input"),
                    label = { Text("Hospital Name & Department *") },
                    placeholder = { Text("e.g. GKNM Hospital, ICU Ward 3, Coimbatore") },
                    leadingIcon = { Icon(Icons.Default.LocalHospital, contentDescription = null, tint = BloodRed) },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Contact Phone
                OutlinedTextField(
                    value = contactPhone,
                    onValueChange = {
                        contactPhone = it
                        errorMsg = null
                    },
                    modifier = Modifier.fillMaxWidth().testTag("request_contact_phone_input"),
                    label = { Text("Emergency Contact Phone *") },
                    placeholder = { Text("+91 9876543210") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = BloodRed) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Additional Notes
                OutlinedTextField(
                    value = additionalNotes,
                    onValueChange = { additionalNotes = it },
                    modifier = Modifier.fillMaxWidth().testTag("request_notes_input"),
                    label = { Text("Additional Clinical Notes (Optional)") },
                    placeholder = { Text("e.g. Cardiac surgery patient, required before 4:00 PM") },
                    maxLines = 3,
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (patientName.isBlank()) {
                            errorMsg = "Patient name is required."
                            return@Button
                        }
                        val units = unitsNeededStr.toIntOrNull()
                        if (units == null || units <= 0) {
                            errorMsg = "Please enter a valid number of blood units (minimum 1)."
                            return@Button
                        }
                        if (hospitalName.isBlank()) {
                            errorMsg = "Hospital name and location are required."
                            return@Button
                        }
                        if (contactPhone.isBlank()) {
                            errorMsg = "Contact phone number is required."
                            return@Button
                        }

                        val userObj = user ?: return@Button

                        val request = EmergencyRequestEntity(
                            requestedByUserId = userObj.id,
                            patientName = patientName.trim(),
                            bloodGroup = selectedBloodGroup,
                            component = selectedComponent,
                            unitsNeeded = units,
                            bloodBankId = selectedBloodBankId,
                            hospitalName = hospitalName.trim(),
                            urgencyLevel = selectedUrgency,
                            contactPhone = contactPhone.trim(),
                            additionalNotes = additionalNotes.trim()
                        )

                        viewModel.createEmergencyRequest(request) { success, err ->
                            if (success) {
                                onSubmitSuccess()
                            } else {
                                errorMsg = err ?: "Failed to submit request."
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BloodRed),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_emergency_request_button")
                ) {
                    Text("Submit Emergency Blood Request", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}
