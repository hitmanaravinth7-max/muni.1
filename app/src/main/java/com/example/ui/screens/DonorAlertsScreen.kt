package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BloodGroupBadge
import com.example.ui.components.EmptyState
import com.example.ui.components.StatusBadge
import com.example.ui.theme.BloodRed
import com.example.ui.theme.BloodRedContainer
import com.example.ui.theme.BorderLight
import com.example.ui.theme.MedicalGreen
import com.example.ui.theme.MedicalGreenContainer
import com.example.ui.theme.MedicalNavy
import com.example.ui.theme.MedicalRed
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.BloodBridgeViewModel
import com.example.util.AppIntents
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DonorAlertsScreen(
    viewModel: BloodBridgeViewModel,
    onNavigateToBecomeDonor: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val authState by viewModel.authState.collectAsState()
    val donorAlerts by viewModel.donorAlerts.collectAsState()

    val donorProfile = authState.donorProfile

    if (donorProfile == null) {
        EmptyState(
            icon = Icons.Default.Notifications,
            title = "Donor Profile Required",
            message = "Emergency donor alerts are only available to registered blood donors. Complete your donor registration to receive emergency match notifications.",
            actionButtonText = "Become a Donor",
            onActionClick = onNavigateToBecomeDonor,
            modifier = modifier
        )
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("donor_alerts_screen")
    ) {
        // Privacy Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = BloodRedContainer),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Security, contentDescription = null, tint = BloodRed, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Emergency Privacy Rule: Requester direct contact phone number is strictly locked until you accept the alert to confirm your willingness to donate.",
                    fontSize = 11.sp,
                    color = TextPrimary,
                    lineHeight = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Emergency Matching Alerts (${donorAlerts.size})",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            StatusBadge(status = "Group ${donorProfile.bloodGroup}")
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (donorAlerts.isEmpty()) {
            EmptyState(
                icon = Icons.Default.Notifications,
                title = "No Active Emergency Alerts",
                message = "There are no pending blood alerts matching your blood group right now. You will be notified when an urgent request is approved in your area."
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(donorAlerts, key = { it.alert.id }) { item ->
                    val alert = item.alert
                    val req = item.request
                    val isAccepted = alert.status == "ACCEPTED"
                    val isDeclined = alert.status == "DECLINED"
                    val isPending = alert.status == "PENDING"
                    val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(alert.createdAt))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("alert_card_${alert.id}"),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderLight))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                BloodGroupBadge(bloodGroup = req.bloodGroup)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = req.hospitalName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "Alert #ALT-${alert.id} • $dateStr",
                                        fontSize = 11.sp,
                                        color = TextMuted
                                    )
                                }
                                StatusBadge(status = alert.status)
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFF8FAFC))
                                    .padding(10.dp)
                            ) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Component / Units Needed:", fontSize = 12.sp, color = TextSecondary)
                                    Text("${req.component} • ${req.unitsNeeded} Units", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BloodRed)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Urgency Level:", fontSize = 12.sp, color = TextSecondary)
                                    StatusBadge(status = req.urgencyLevel)
                                }
                                if (req.additionalNotes.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Notes: ${req.additionalNotes}", fontSize = 11.sp, color = TextSecondary)
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Contact info condition
                            if (isAccepted) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(MedicalGreenContainer)
                                        .padding(12.dp)
                                ) {
                                    Column {
                                        Text("Requester Contact (Unlocked):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MedicalGreen)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column {
                                                Text(item.requesterName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                                                Text(item.requesterPhone, fontSize = 13.sp, color = TextSecondary)
                                            }
                                            Button(
                                                onClick = { AppIntents.dialPhoneNumber(context, item.requesterPhone) },
                                                colors = ButtonDefaults.buttonColors(containerColor = MedicalGreen),
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier.testTag("call_requester_button")
                                            ) {
                                                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Call Requester")
                                            }
                                        }
                                    }
                                }
                            } else if (isPending) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFF1F5F9))
                                        .padding(8.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Lock, contentDescription = null, tint = TextMuted, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Requester phone is hidden. Accept the alert to unlock contact details.", fontSize = 11.sp, color = TextMuted)
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = { viewModel.respondToAlert(alert.id, true) },
                                        colors = ButtonDefaults.buttonColors(containerColor = MedicalGreen),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f).testTag("accept_alert_button")
                                    ) {
                                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Accept")
                                    }

                                    OutlinedButton(
                                        onClick = { viewModel.respondToAlert(alert.id, false) },
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f).testTag("decline_alert_button")
                                    ) {
                                        Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Decline", color = MedicalRed)
                                    }
                                }
                            } else {
                                Text(
                                    text = "You declined this alert.",
                                    fontSize = 12.sp,
                                    color = TextMuted
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
