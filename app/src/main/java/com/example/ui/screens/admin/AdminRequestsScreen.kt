package com.example.ui.screens.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.EmptyState
import com.example.ui.components.RequestCard
import com.example.ui.theme.BloodRed
import com.example.ui.theme.MedicalGreen
import com.example.ui.theme.MedicalNavy
import com.example.ui.theme.MedicalOrange
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.viewmodel.BloodBridgeViewModel

@Composable
fun AdminRequestsScreen(
    viewModel: BloodBridgeViewModel,
    modifier: Modifier = Modifier
) {
    val allRequests by viewModel.allEmergencyRequests.collectAsState()

    var statusFilter by remember { mutableStateOf("ALL") }

    val statuses = listOf("ALL", "PENDING", "APPROVED", "FULFILLED", "REJECTED")

    val filteredRequests = allRequests.filter { req ->
        if (statusFilter == "ALL") true else req.status == statusFilter
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("admin_requests_screen")
    ) {
        Text(
            text = "Emergency Requests Review",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            text = "Approving a request broadcasts automated alerts to compatible, available donors",
            fontSize = 12.sp,
            color = TextMuted
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            for (s in statuses) {
                val isSelected = statusFilter == s
                FilterChip(
                    selected = isSelected,
                    onClick = { statusFilter = s },
                    label = { Text(s, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = if (s == "PENDING") BloodRed else if (s == "APPROVED") MedicalGreen else MedicalNavy,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (filteredRequests.isEmpty()) {
            EmptyState(
                icon = Icons.Default.Bloodtype,
                title = "No Requests Found",
                message = "There are no emergency requests under status '$statusFilter'."
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredRequests, key = { it.id }) { req ->
                    RequestCard(
                        request = req,
                        isAdmin = true,
                        onApprove = { viewModel.adminApproveRequest(req.id) },
                        onReject = { viewModel.adminRejectRequest(req.id) },
                        onFulfill = { viewModel.adminFulfillRequest(req.id) }
                    )
                }
            }
        }
    }
}
