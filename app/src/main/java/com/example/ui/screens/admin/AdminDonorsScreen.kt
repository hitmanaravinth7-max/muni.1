package com.example.ui.screens.admin

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BloodGroupBadge
import com.example.ui.components.EmptyState
import com.example.ui.components.StatusBadge
import com.example.ui.theme.BloodRed
import com.example.ui.theme.BorderLight
import com.example.ui.theme.MedicalGreen
import com.example.ui.theme.MedicalNavy
import com.example.ui.theme.MedicalOrange
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.BloodBridgeViewModel
import com.example.util.BloodCompatibility

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AdminDonorsScreen(
    viewModel: BloodBridgeViewModel,
    modifier: Modifier = Modifier
) {
    val allDonors by viewModel.allDonors.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedBloodGroup by remember { mutableStateOf("ALL") }
    var verificationFilter by remember { mutableStateOf("ALL") }

    val bloodGroupsList = listOf("ALL", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")

    val filteredDonors = allDonors.filter { donor ->
        val queryMatch = if (searchQuery.isBlank()) true else {
            donor.firstName.contains(searchQuery.trim(), ignoreCase = true) ||
            donor.profile.city.contains(searchQuery.trim(), ignoreCase = true) ||
            donor.profile.state.contains(searchQuery.trim(), ignoreCase = true)
        }
        val groupMatch = if (selectedBloodGroup == "ALL") true else donor.profile.bloodGroup == selectedBloodGroup
        val verifyMatch = when (verificationFilter) {
            "VERIFIED" -> donor.profile.verifiedByAdmin
            "UNVERIFIED" -> !donor.profile.verifiedByAdmin
            else -> true
        }

        queryMatch && groupMatch && verifyMatch
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("admin_donors_screen")
    ) {
        Text(
            text = "Donor Verification & Management",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            text = "Verify donor identity, view health disclosures & cooldown status",
            fontSize = 12.sp,
            color = TextMuted
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth().testTag("admin_donor_search_input"),
            placeholder = { Text("Search by Donor Name or City") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = BloodRed) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear")
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for (bg in bloodGroupsList) {
                val isSelected = selectedBloodGroup == bg
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedBloodGroup = bg },
                    label = { Text(bg, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BloodRed,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            for (v in listOf("ALL", "VERIFIED", "UNVERIFIED")) {
                val isSelected = verificationFilter == v
                FilterChip(
                    selected = isSelected,
                    onClick = { verificationFilter = v },
                    label = { Text(v, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = if (v == "VERIFIED") MedicalGreen else MedicalNavy,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (filteredDonors.isEmpty()) {
            EmptyState(
                icon = Icons.Default.Favorite,
                title = "No Donors Found",
                message = "No donors match your search and filter criteria."
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredDonors, key = { it.profile.id }) { donor ->
                    val isVerified = donor.profile.verifiedByAdmin
                    val cooldownActive = BloodCompatibility.isCooldownActive(donor.profile.lastDonationDate)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_donor_card_${donor.profile.id}"),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderLight))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                BloodGroupBadge(bloodGroup = donor.profile.bloodGroup)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${donor.firstName} (ID #${donor.profile.id})",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "${donor.profile.city}, ${donor.profile.state} • ${donor.profile.gender}, ${donor.profile.weightKg}kg",
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                    Text(
                                        text = "Address: ${donor.profile.addressLine} • PIN: ${donor.profile.pincode}",
                                        fontSize = 11.sp,
                                        color = TextMuted
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    StatusBadge(status = if (isVerified) "VERIFIED" else "UNVERIFIED")
                                    Spacer(modifier = Modifier.height(4.dp))
                                    StatusBadge(status = if (cooldownActive) "COOLDOWN" else if (donor.profile.isAvailable) "AVAILABLE" else "NOT AVAILABLE")
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Health disclosure summary
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFF8FAFC))
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Chronic: ${if (donor.profile.hasChronicIllness) "Yes" else "No"} • Surgery: ${if (donor.profile.recentSurgery) "Yes" else "No"} • Meds: ${if (donor.profile.onMedication) "Yes" else "No"}",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                                Text(
                                    text = "${donor.profile.totalDonations} donations",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = BloodRed
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = { viewModel.adminVerifyDonor(donor.profile.id, !isVerified) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isVerified) MedicalOrange else MedicalGreen
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.testTag("verify_donor_btn_${donor.profile.id}")
                                ) {
                                    Icon(
                                        imageVector = if (isVerified) Icons.Default.Close else Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isVerified) "Unverify Donor" else "Verify Donor",
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
