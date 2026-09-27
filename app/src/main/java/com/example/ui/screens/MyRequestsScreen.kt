package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.EmergencyRequestEntity
import com.example.ui.components.EmptyState
import com.example.ui.components.RequestCard
import com.example.ui.theme.BloodRed
import com.example.ui.theme.MedicalRed
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.viewmodel.BloodBridgeViewModel

@Composable
fun MyRequestsScreen(
    viewModel: BloodBridgeViewModel,
    onNavigateToNewRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val myRequests by viewModel.myRequests.collectAsState()
    var requestToCancel by remember { mutableStateOf<EmergencyRequestEntity?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("my_requests_screen")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "My Blood Requests",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "${myRequests.size} total submitted requests",
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }

            Button(
                onClick = onNavigateToNewRequest,
                colors = ButtonDefaults.buttonColors(containerColor = BloodRed),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("new_request_top_button")
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.padding(2.dp))
                Text("New Request", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (myRequests.isEmpty()) {
            EmptyState(
                icon = Icons.Default.Bloodtype,
                title = "No Blood Requests Yet",
                message = "You have not created any emergency blood requests. Submit a request during critical medical situations to alert nearby compatible donors.",
                actionButtonText = "Submit Emergency Request",
                onActionClick = onNavigateToNewRequest
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(myRequests, key = { it.id }) { req ->
                    RequestCard(
                        request = req,
                        isAdmin = false,
                        onCancel = if (req.status == "PENDING") {
                            { requestToCancel = req }
                        } else null
                    )
                }
            }
        }
    }

    requestToCancel?.let { req ->
        AlertDialog(
            onDismissRequest = { requestToCancel = null },
            title = { Text("Cancel Blood Request #REQ-${req.id}?") },
            text = { Text("Are you sure you want to cancel this emergency blood request for patient ${req.patientName}?") },
            confirmButton = {
                Button(
                    onClick = {
                        val adminId = viewModel.authState.value.currentUser?.id ?: 0L
                        // User cancelling their own request sets status to CANCELLED
                        // Note: we can use a direct repository or view model update
                        requestToCancel = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MedicalRed)
                ) {
                    Text("Yes, Cancel")
                }
            },
            dismissButton = {
                TextButton(onClick = { requestToCancel = null }) {
                    Text("No, Keep Active")
                }
            }
        )
    }
}
