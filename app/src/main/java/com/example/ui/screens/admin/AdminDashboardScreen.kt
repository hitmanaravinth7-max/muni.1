package com.example.ui.screens.admin

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.StatCard
import com.example.ui.theme.BloodRed
import com.example.ui.theme.BloodRedContainer
import com.example.ui.theme.BorderLight
import com.example.ui.theme.MedicalBlue
import com.example.ui.theme.MedicalGreen
import com.example.ui.theme.MedicalNavy
import com.example.ui.theme.MedicalOrange
import com.example.ui.theme.MedicalRed
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.BloodBridgeViewModel

@Composable
fun AdminDashboardScreen(
    viewModel: BloodBridgeViewModel,
    onNavigateToUsers: () -> Unit,
    onNavigateToDonors: () -> Unit,
    onNavigateToBloodBanks: () -> Unit,
    onNavigateToRequests: () -> Unit,
    onNavigateToActivityLog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val users by viewModel.allUsers.collectAsState()
    val donors by viewModel.allDonors.collectAsState()
    val requests by viewModel.allEmergencyRequests.collectAsState()
    val bloodBanks by viewModel.allBloodBanks.collectAsState()
    val allStock by viewModel.allBloodStock.collectAsState()

    val totalUsers = users.size
    val activeDonors = donors.count { it.profile.isAvailable && it.isUserActive }
    val pendingRequests = requests.count { it.status == "PENDING" }
    val fulfilledRequests = requests.count { it.status == "FULFILLED" }
    val totalBanks = bloodBanks.size
    val lowStockCount = allStock.count { it.unitsAvailable <= 3 }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("admin_dashboard_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Admin Badge Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MedicalNavy),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(BloodRed),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Shield, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "System Administration Portal",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Real-time emergency monitoring, verification & inventory controls",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Live Statistics (Row 1)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                count = totalUsers.toString(),
                label = "Total Users",
                icon = Icons.Default.People,
                accentColor = MedicalBlue,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                count = activeDonors.toString(),
                label = "Active Donors",
                icon = Icons.Default.VerifiedUser,
                accentColor = MedicalGreen,
                modifier = Modifier.weight(1f)
            )
        }

        // Live Statistics (Row 2)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                count = pendingRequests.toString(),
                label = "Pending Requests",
                icon = Icons.Default.NotificationsActive,
                accentColor = if (pendingRequests > 0) MedicalRed else MedicalOrange,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                count = totalBanks.toString(),
                label = "Blood Centers",
                icon = Icons.Default.LocalHospital,
                accentColor = MedicalNavy,
                modifier = Modifier.weight(1f)
            )
        }

        // Live Statistics (Row 3)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                count = lowStockCount.toString(),
                label = "Low Stock Alerts",
                icon = Icons.Default.Warning,
                accentColor = MedicalOrange,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                count = fulfilledRequests.toString(),
                label = "Fulfilled Requests",
                icon = Icons.Default.CheckCircle,
                accentColor = BloodRed,
                modifier = Modifier.weight(1f)
            )
        }

        // Management Modules Menu
        Text(
            text = "Administration Management Modules",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        AdminModuleCard(
            title = "Emergency Requests Management",
            subtitle = "$pendingRequests pending approvals • Broadcasts to eligible donors",
            icon = Icons.Default.Bloodtype,
            accentColor = BloodRed,
            badge = if (pendingRequests > 0) "$pendingRequests Pending" else null,
            onClick = onNavigateToRequests,
            testTag = "admin_nav_requests"
        )

        AdminModuleCard(
            title = "Donor Verification & Management",
            subtitle = "${donors.size} registered donors • Review health criteria & verification",
            icon = Icons.Default.VerifiedUser,
            accentColor = MedicalGreen,
            onClick = onNavigateToDonors,
            testTag = "admin_nav_donors"
        )

        AdminModuleCard(
            title = "Blood Banks & Stock Inventory",
            subtitle = "$totalBanks blood banks • Multi-component inventory (RBC, Platelets, Plasma)",
            icon = Icons.Default.LocalHospital,
            accentColor = MedicalBlue,
            onClick = onNavigateToBloodBanks,
            testTag = "admin_nav_banks"
        )

        AdminModuleCard(
            title = "User Account Management",
            subtitle = "$totalUsers registered user accounts • Role & security access control",
            icon = Icons.Default.People,
            accentColor = MedicalNavy,
            onClick = onNavigateToUsers,
            testTag = "admin_nav_users"
        )

        AdminModuleCard(
            title = "System Activity & Audit Log",
            subtitle = "Immutable read-only log of all system actions, approvals & logins",
            icon = Icons.Default.History,
            accentColor = Color(0xFF64748B),
            onClick = onNavigateToActivityLog,
            testTag = "admin_nav_activity_log"
        )

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun AdminModuleCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    badge: String? = null,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag(testTag),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(14.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderLight))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                    if (badge != null) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(BloodRed)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(badge, color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = subtitle, fontSize = 11.sp, color = TextSecondary)
            }
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
        }
    }
}
