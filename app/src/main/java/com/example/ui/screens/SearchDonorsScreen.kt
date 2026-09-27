package com.example.ui.screens

import android.content.Context
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.DonorDisplayItem
import com.example.ui.components.BloodGroupBadge
import com.example.ui.components.DonorCard
import com.example.ui.components.EmptyState
import com.example.ui.components.StatusBadge
import com.example.ui.theme.BloodRed
import com.example.ui.theme.BloodRedContainer
import com.example.ui.theme.BorderLight
import com.example.ui.theme.MedicalGreen
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.BloodBridgeViewModel
import com.example.util.AppIntents
import com.example.util.BloodCompatibility

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchDonorsScreen(
    viewModel: BloodBridgeViewModel,
    initialBloodGroupFilter: String? = null,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val allDonors by viewModel.allDonors.collectAsState()
    val authState by viewModel.authState.collectAsState()

    var selectedBloodGroup by remember { mutableStateOf(initialBloodGroupFilter ?: "ALL") }
    var cityFilter by remember { mutableStateOf("") }
    var availableOnly by remember { mutableStateOf(false) }
    var showFilters by remember { mutableStateOf(false) }

    var selectedDonorForDetails by remember { mutableStateOf<DonorDisplayItem?>(null) }

    val bloodGroupsList = listOf("ALL", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")

    // Filter logic
    val filteredDonors = allDonors.filter { donor ->
        val groupMatch = if (selectedBloodGroup == "ALL") true else donor.profile.bloodGroup == selectedBloodGroup
        val cityMatch = if (cityFilter.isBlank()) true else {
            donor.profile.city.contains(cityFilter.trim(), ignoreCase = true) ||
            donor.profile.state.contains(cityFilter.trim(), ignoreCase = true) ||
            donor.profile.addressLine.contains(cityFilter.trim(), ignoreCase = true)
        }
        val availabilityMatch = if (availableOnly) {
            donor.profile.isAvailable && !BloodCompatibility.isCooldownActive(donor.profile.lastDonationDate)
        } else true

        groupMatch && cityMatch && availabilityMatch
    }.take(30)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("search_donors_screen")
    ) {
        // Privacy Notice Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Security, contentDescription = "Privacy Shield", tint = BloodRed, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Donor Privacy Protection: Contact info is protected. Only registered, authorized users can initiate direct contact with verified available donors.",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search inputs
        OutlinedTextField(
            value = cityFilter,
            onValueChange = { cityFilter = it },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("search_city_input"),
            placeholder = { Text("Filter by City or District (e.g. Coimbatore, Chennai)") },
            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = BloodRed) },
            trailingIcon = {
                if (cityFilter.isNotEmpty()) {
                    IconButton(onClick = { cityFilter = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear")
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Blood Group Chips Filter
        Text("Blood Group:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary)
        Spacer(modifier = Modifier.height(4.dp))

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
                    label = { Text(bg, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BloodRed,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("filter_chip_$bg")
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(
                selected = availableOnly,
                onClick = { availableOnly = !availableOnly },
                label = { Text("Available Only (No Cooldown)", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MedicalGreen,
                    selectedLabelColor = Color.White
                ),
                modifier = Modifier.testTag("filter_available_only")
            )

            Text(
                text = "${filteredDonors.size} results",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextMuted
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Donors List
        if (filteredDonors.isEmpty()) {
            EmptyState(
                icon = Icons.Default.Search,
                title = "No Matching Donors Found",
                message = "Try clearing your filters or changing your blood group or location search.",
                actionButtonText = "Reset Filters",
                onActionClick = {
                    selectedBloodGroup = "ALL"
                    cityFilter = ""
                    availableOnly = false
                }
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredDonors, key = { it.profile.id }) { donor ->
                    DonorCard(
                        donor = donor,
                        isLoggedIn = authState.isLoggedIn,
                        onCallClick = { phone ->
                            AppIntents.dialPhoneNumber(context, phone)
                        },
                        onDirectionsClick = { lat, lng, label ->
                            AppIntents.openGoogleMaps(context, lat, lng, label, donor.profile.city)
                        },
                        onViewClick = {
                            selectedDonorForDetails = donor
                        }
                    )
                }
            }
        }
    }

    // View Donor Details Dialog (Strict Privacy Preserved)
    selectedDonorForDetails?.let { donor ->
        AlertDialog(
            onDismissRequest = { selectedDonorForDetails = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BloodGroupBadge(bloodGroup = donor.profile.bloodGroup)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = "${donor.firstName} (Donor #${donor.profile.id})", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = "${donor.profile.city}, ${donor.profile.state}", fontSize = 12.sp, color = TextSecondary)
                    }
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Verification Status:", fontSize = 12.sp, color = TextSecondary)
                        StatusBadge(status = if (donor.profile.verifiedByAdmin) "VERIFIED" else "UNVERIFIED")
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Availability:", fontSize = 12.sp, color = TextSecondary)
                        StatusBadge(status = if (donor.profile.isAvailable) "AVAILABLE" else "NOT AVAILABLE")
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total Blood Donations:", fontSize = 12.sp, color = TextSecondary)
                        Text("${donor.profile.totalDonations} donations", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Compatible with Recipients:", fontSize = 12.sp, color = TextSecondary)
                        Text(donor.profile.bloodGroup, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BloodRed)
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF8FAFC))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "Privacy Note: Under BloodBridge data protection rules, private residential address lines and raw contact numbers are restricted from public disclosure.",
                            fontSize = 11.sp,
                            color = TextMuted,
                            lineHeight = 15.sp
                        )
                    }
                }
            },
            confirmButton = {
                if (authState.isLoggedIn && donor.profile.isAvailable) {
                    Button(
                        onClick = {
                            AppIntents.dialPhoneNumber(context, donor.fullPhone)
                            selectedDonorForDetails = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BloodRed)
                    ) {
                        Text("Call Donor")
                    }
                } else if (!authState.isLoggedIn) {
                    Button(
                        onClick = {
                            selectedDonorForDetails = null
                            onNavigateToLogin()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BloodRed)
                    ) {
                        Text("Login to Contact")
                    }
                } else {
                    TextButton(onClick = { selectedDonorForDetails = null }) {
                        Text("Close")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedDonorForDetails = null }) {
                    Text("Close")
                }
            }
        )
    }
}
