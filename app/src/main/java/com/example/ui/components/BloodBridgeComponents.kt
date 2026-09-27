package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.BloodBankEntity
import com.example.data.entity.BloodStockEntity
import com.example.data.entity.EmergencyRequestEntity
import com.example.data.repository.DonorDisplayItem
import com.example.ui.theme.BloodRed
import com.example.ui.theme.BloodRedContainer
import com.example.ui.theme.BorderLight
import com.example.ui.theme.MedicalBlue
import com.example.ui.theme.MedicalGreen
import com.example.ui.theme.MedicalGreenContainer
import com.example.ui.theme.MedicalNavy
import com.example.ui.theme.MedicalOrange
import com.example.ui.theme.MedicalOrangeContainer
import com.example.ui.theme.MedicalRed
import com.example.ui.theme.MedicalRedContainer
import com.example.ui.theme.OnBloodRedContainer
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.BloodCompatibility
import com.example.util.SecurityUtil
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BloodGroupBadge(
    bloodGroup: String,
    modifier: Modifier = Modifier,
    large: Boolean = false
) {
    val sizeDp = if (large) 52.dp else 38.dp
    val fontSize = if (large) 18.sp else 13.sp

    Box(
        modifier = modifier
            .size(sizeDp)
            .clip(CircleShape)
            .background(BloodRedContainer)
            .border(1.5.dp, BloodRed, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = bloodGroup,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize,
            color = BloodRed,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val badgeInfo: Triple<Color, Color, String> = when (status.uppercase()) {
        "AVAILABLE", "APPROVED", "ACTIVE", "VERIFIED", "ACCEPTED" ->
            Triple(MedicalGreenContainer, MedicalGreen, status)
        "NOT AVAILABLE", "PENDING", "UNVERIFIED" ->
            Triple(MedicalOrangeContainer, MedicalOrange, status)
        "CRITICAL", "BLOCKED", "REJECTED", "CANCELLED", "DECLINED" ->
            Triple(MedicalRedContainer, MedicalRed, status)
        "FULFILLED" ->
            Triple(Color(0xFFE0F2FE), MedicalBlue, "FULFILLED")
        else -> Triple(Color(0xFFF1F5F9), TextSecondary, status)
    }
    val bgColor = badgeInfo.first
    val textColor = badgeInfo.second
    val label = badgeInfo.third

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label.replace("_", " "),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}

@Composable
fun EmergencyNoticeBanner(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("emergency_banner"),
        colors = CardDefaults.cardColors(containerColor = BloodRedContainer),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BloodRed.copy(alpha = 0.4f)))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Emergency Alert",
                tint = BloodRed,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "Emergency Life-Threatening Notice",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = OnBloodRedContainer
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "For life-threatening emergencies, contact local emergency services first. BloodBridge helps coordinate blood availability and donor connections.",
                    fontSize = 12.sp,
                    color = OnBloodRedContainer.copy(alpha = 0.9f),
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
fun StatCard(
    count: String,
    label: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.testTag("stat_card_$label"),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderLight))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = label, tint = accentColor, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = count,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
            )
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = TextSecondary
            )
        }
    }
}

@Composable
fun DonorCard(
    donor: DonorDisplayItem,
    isLoggedIn: Boolean,
    onCallClick: (String) -> Unit,
    onDirectionsClick: (Double, Double, String) -> Unit,
    onViewClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cooldownActive = BloodCompatibility.isCooldownActive(donor.profile.lastDonationDate)
    val remainingDays = BloodCompatibility.getDonorCooldownRemainingDays(donor.profile.lastDonationDate)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("donor_card_${donor.profile.id}"),
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
                BloodGroupBadge(bloodGroup = donor.profile.bloodGroup)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = donor.firstName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextPrimary
                        )
                        if (donor.profile.verifiedByAdmin) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Verified Donor",
                                tint = MedicalGreen,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${donor.profile.city}, ${donor.profile.state}",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    if (cooldownActive) {
                        StatusBadge(status = "COOLDOWN")
                    } else if (donor.profile.isAvailable) {
                        StatusBadge(status = "AVAILABLE")
                    } else {
                        StatusBadge(status = "NOT AVAILABLE")
                    }
                    if (donor.profile.verifiedByAdmin) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Verified Donor",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = MedicalGreen
                        )
                    }
                }
            }

            if (cooldownActive) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MedicalOrangeContainer.copy(alpha = 0.5f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Donation cooldown active ($remainingDays days remaining)",
                        fontSize = 11.sp,
                        color = MedicalOrange,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onViewClick,
                    modifier = Modifier.weight(1f).testTag("view_donor_button"),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("View Donor", fontSize = 12.sp)
                }

                if (isLoggedIn && donor.profile.isAvailable && !cooldownActive) {
                    Button(
                        onClick = { onCallClick(donor.fullPhone) },
                        modifier = Modifier.weight(1f).testTag("call_donor_button"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BloodRed)
                    ) {
                        Icon(Icons.Default.Call, contentDescription = "Call", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Call", fontSize = 12.sp)
                    }
                    OutlinedButton(
                        onClick = {
                            onDirectionsClick(
                                donor.profile.latitude,
                                donor.profile.longitude,
                                "${donor.firstName}'s Area, ${donor.profile.city}"
                            )
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("directions_button")
                    ) {
                        Icon(Icons.Default.Directions, contentDescription = "Directions", modifier = Modifier.size(16.dp))
                    }
                } else if (!isLoggedIn) {
                    // Protected info banner
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF1F5F9))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lock, contentDescription = "Protected", modifier = Modifier.size(12.dp), tint = TextMuted)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Login to Contact", fontSize = 11.sp, color = TextSecondary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BloodBankCard(
    bank: BloodBankEntity,
    stockList: List<BloodStockEntity>,
    onGetDirections: (Double, Double, String) -> Unit,
    onCallBank: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("blood_bank_card_${bank.id}"),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderLight))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(BloodRedContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Bloodtype,
                        contentDescription = "Blood Bank",
                        tint = BloodRed,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = bank.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = "${bank.address}, ${bank.city}, ${bank.state} - ${bank.pincode}",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Lic: ${bank.licenseNumber}",
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Stock grid preview
            Text(
                text = "Available Blood Stock Units:",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val stockMap = stockList.groupBy { it.bloodGroup }
                val groupsToShow = listOf("A+", "B+", "O+", "AB+", "O-")
                for (bg in groupsToShow) {
                    val totalUnits = stockMap[bg]?.sumOf { it.unitsAvailable } ?: 0
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (totalUnits > 0) Color(0xFFF0FDF4) else Color(0xFFFEF2F2))
                            .border(1.dp, if (totalUnits > 0) Color(0xFFBBF7D0) else Color(0xFFFECACA), RoundedCornerShape(8.dp))
                            .padding(vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = bg, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (totalUnits > 0) MedicalGreen else MedicalRed)
                            Text(text = "$totalUnits u", fontSize = 10.sp, color = TextSecondary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onCallBank(bank.contactPhone) },
                    modifier = Modifier.weight(1f).testTag("call_blood_bank_button"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MedicalNavy)
                ) {
                    Icon(Icons.Default.Call, contentDescription = "Call", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Call Bank", fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = { onGetDirections(bank.latitude, bank.longitude, bank.name) },
                    modifier = Modifier.weight(1f).testTag("directions_blood_bank_button"),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Directions, contentDescription = "Directions", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Directions", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun RequestCard(
    request: EmergencyRequestEntity,
    isAdmin: Boolean = false,
    onApprove: (() -> Unit)? = null,
    onReject: (() -> Unit)? = null,
    onFulfill: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(request.createdAt))

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("request_card_${request.id}"),
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
                BloodGroupBadge(bloodGroup = request.bloodGroup)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Patient: ${request.patientName}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = "Req #REQ-${request.id} • $dateStr",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    StatusBadge(status = request.status)
                    Spacer(modifier = Modifier.height(4.dp))
                    StatusBadge(status = request.urgencyLevel)
                }
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
                    Text("Hospital:", fontSize = 12.sp, color = TextSecondary)
                    Text(request.hospitalName, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Component / Units:", fontSize = 12.sp, color = TextSecondary)
                    Text("${request.component} • ${request.unitsNeeded} Units", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = BloodRed)
                }
                if (request.additionalNotes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Note: ${request.additionalNotes}",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        lineHeight = 14.sp
                    )
                }
            }

            // Admin buttons or user cancel button
            if (isAdmin && request.status == "PENDING") {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onApprove?.invoke() },
                        colors = ButtonDefaults.buttonColors(containerColor = MedicalGreen),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).testTag("admin_approve_button")
                    ) {
                        Text("Approve & Alert", fontSize = 12.sp)
                    }
                    Button(
                        onClick = { onReject?.invoke() },
                        colors = ButtonDefaults.buttonColors(containerColor = MedicalRed),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).testTag("admin_reject_button")
                    ) {
                        Text("Reject", fontSize = 12.sp)
                    }
                }
            } else if (isAdmin && request.status == "APPROVED") {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { onFulfill?.invoke() },
                    colors = ButtonDefaults.buttonColors(containerColor = MedicalBlue),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().testTag("admin_fulfill_button")
                ) {
                    Text("Mark as Fulfilled", fontSize = 12.sp)
                }
            } else if (!isAdmin && request.status == "PENDING" && onCancel != null) {
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedButton(
                    onClick = onCancel,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().testTag("cancel_request_button")
                ) {
                    Text("Cancel Request", fontSize = 12.sp, color = MedicalRed)
                }
            }
        }
    }
}

@Composable
fun EmptyState(
    icon: ImageVector = Icons.Default.ErrorOutline,
    title: String,
    message: String,
    actionButtonText: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp)
            .testTag("empty_state"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color(0xFFF1F5F9)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = title, tint = TextMuted, modifier = Modifier.size(32.dp))
        }
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = title,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = message,
            fontSize = 13.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )
        if (actionButtonText != null && onActionClick != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onActionClick,
                colors = ButtonDefaults.buttonColors(containerColor = BloodRed),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("empty_state_action_button")
            ) {
                Text(actionButtonText, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun LoadingSpinner(modifier: Modifier = Modifier, text: String = "Loading...") {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp)
            .testTag("loading_spinner"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = BloodRed, strokeWidth = 3.dp, modifier = Modifier.size(36.dp))
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = text, fontSize = 13.sp, color = TextSecondary)
    }
}
