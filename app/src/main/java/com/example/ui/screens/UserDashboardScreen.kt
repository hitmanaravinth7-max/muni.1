package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BloodGroupBadge
import com.example.ui.components.EmergencyNoticeBanner
import com.example.ui.components.StatusBadge
import com.example.ui.theme.BloodRed
import com.example.ui.theme.BloodRedContainer
import com.example.ui.theme.BorderLight
import com.example.ui.theme.MedicalGreen
import com.example.ui.theme.MedicalNavy
import com.example.ui.theme.MedicalOrange
import com.example.ui.theme.MedicalOrangeContainer
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.BloodBridgeViewModel
import com.example.util.BloodCompatibility
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun UserDashboardScreen(
    viewModel: BloodBridgeViewModel,
    onNavigateToBecomeDonor: () -> Unit,
    onNavigateToEmergencyRequest: () -> Unit,
    onNavigateToMyRequests: () -> Unit,
    onNavigateToAlerts: () -> Unit,
    onNavigateToSearchDonors: () -> Unit,
    modifier: Modifier = Modifier
) {
    val authState by viewModel.authState.collectAsState()
    val myRequests by viewModel.myRequests.collectAsState()
    val donorAlerts by viewModel.donorAlerts.collectAsState()
    val userMessage by viewModel.userMessage.collectAsState()

    val user = authState.currentUser
    val donorProfile = authState.donorProfile

    val cooldownActive = donorProfile?.let { BloodCompatibility.isCooldownActive(it.lastDonationDate) } ?: false
    val remainingDays = donorProfile?.let { BloodCompatibility.getDonorCooldownRemainingDays(it.lastDonationDate) } ?: 0L

    val pendingAlertsCount = donorAlerts.count { it.alert.status == "PENDING" }
    val activeRequestsCount = myRequests.count { it.status == "PENDING" || it.status == "APPROVED" }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("user_dashboard_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = BloodRedContainer),
            shape = RoundedCornerShape(16.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BloodRed.copy(alpha = 0.2f)))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Welcome, ${user?.fullName ?: "User"}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )
                Text(
                    text = "${user?.email} • ${user?.phoneNumber}",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }

        // Emergency Notice
        EmergencyNoticeBanner()

        // Quick Actions Row
        Column {
            Text(
                text = "Quick Actions",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onNavigateToEmergencyRequest,
                    colors = ButtonDefaults.buttonColors(containerColor = BloodRed),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f).testTag("action_new_request_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Emergency Request", fontSize = 11.sp, maxLines = 1)
                }

                OutlinedButton(
                    onClick = onNavigateToSearchDonors,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f).testTag("action_search_donors_button")
                ) {
                    Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Search Donors", fontSize = 11.sp, maxLines = 1)
                }
            }
        }

        // Donor Status Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("donor_status_card"),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(16.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderLight))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Favorite, contentDescription = null, tint = BloodRed, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Donor Status",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    if (donorProfile != null) {
                        StatusBadge(status = if (donorProfile.verifiedByAdmin) "VERIFIED" else "UNVERIFIED")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (donorProfile == null) {
                    // User is not a donor yet
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF8FAFC))
                            .padding(14.dp)
                    ) {
                        Column {
                            Text(
                                text = "You are not registered as a blood donor yet.",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Register your blood group to receive emergency donor alerts in your area.",
                                fontSize = 12.sp,
                                color = TextSecondary,
                                lineHeight = 16.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = onNavigateToBecomeDonor,
                        colors = ButtonDefaults.buttonColors(containerColor = BloodRed),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("become_blood_donor_button")
                    ) {
                        Icon(Icons.Default.Favorite, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Become a Blood Donor", fontWeight = FontWeight.Bold)
                    }
                } else {
                    // User is already a donor
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BloodGroupBadge(bloodGroup = donorProfile.bloodGroup, large = true)
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Group ${donorProfile.bloodGroup} Donor",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = TextPrimary
                            )
                            Text(
                                text = "${donorProfile.city}, ${donorProfile.state} • ${donorProfile.totalDonations} donations",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Cooldown notice
                    if (cooldownActive) {
                        val lastDateStr = donorProfile.lastDonationDate?.let {
                            SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(it))
                        } ?: "N/A"
                        val eligibleDateStr = donorProfile.lastDonationDate?.let {
                            SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(it + 90L * 86400000L))
                        } ?: "N/A"

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(MedicalOrangeContainer)
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Warning, contentDescription = null, tint = MedicalOrange, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Donation cooldown active", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MedicalOrange)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Last Donation: $lastDateStr", fontSize = 12.sp, color = TextPrimary)
                                Text("Eligible From: $eligibleDateStr ($remainingDays days remaining)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Availability Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Current Availability:", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                            Text(
                                text = if (cooldownActive) "Locked in cooldown" else if (donorProfile.isAvailable) "Available to donate" else "Not available",
                                fontSize = 12.sp,
                                color = if (donorProfile.isAvailable && !cooldownActive) MedicalGreen else TextMuted
                            )
                        }

                        Switch(
                            checked = donorProfile.isAvailable && !cooldownActive,
                            enabled = !cooldownActive,
                            onCheckedChange = { checked ->
                                viewModel.toggleAvailability(checked)
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = MedicalGreen, checkedTrackColor = MedicalGreen.copy(alpha = 0.5f)),
                            modifier = Modifier.testTag("donor_availability_switch")
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = onNavigateToBecomeDonor,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("edit_donor_profile_button")
                    ) {
                        Text("Update Donor Profile & Health Details", fontSize = 12.sp)
                    }
                }
            }
        }

        // Active Blood Requests Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(16.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderLight))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Bloodtype, contentDescription = null, tint = BloodRed, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "My Blood Requests",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                    Text(
                        text = "View All (${myRequests.size})",
                        color = BloodRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clickable { onNavigateToMyRequests() }
                            .padding(4.dp)
                            .testTag("dashboard_view_all_requests")
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (myRequests.isEmpty()) {
                    Text(
                        text = "You have not submitted any emergency requests.",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                } else {
                    val latestReq = myRequests.first()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF8FAFC))
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BloodGroupBadge(bloodGroup = latestReq.bloodGroup)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Patient: ${latestReq.patientName}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("${latestReq.hospitalName} • ${latestReq.unitsNeeded} units", fontSize = 11.sp, color = TextSecondary)
                        }
                        StatusBadge(status = latestReq.status)
                    }
                }
            }
        }

        // Recent Alerts Card (if donor)
        if (donorProfile != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderLight))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Notifications, contentDescription = null, tint = MedicalNavy, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Emergency Donor Alerts",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                        Text(
                            text = "View Alerts ($pendingAlertsCount Pending)",
                            color = BloodRed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .clickable { onNavigateToAlerts() }
                                .padding(4.dp)
                                .testTag("dashboard_view_all_alerts")
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (donorAlerts.isEmpty()) {
                        Text(
                            text = "No emergency alerts for your blood group at this time.",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    } else {
                        val firstAlert = donorAlerts.first()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFF8FAFC))
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BloodGroupBadge(bloodGroup = firstAlert.request.bloodGroup)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Emergency: ${firstAlert.request.hospitalName}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("${firstAlert.request.unitsNeeded} units • ${firstAlert.request.urgencyLevel}", fontSize = 11.sp, color = TextSecondary)
                            }
                            StatusBadge(status = firstAlert.alert.status)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}
