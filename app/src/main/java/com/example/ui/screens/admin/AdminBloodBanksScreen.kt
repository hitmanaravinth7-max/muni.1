package com.example.ui.screens.admin

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.BloodBankEntity
import com.example.data.entity.BloodStockEntity
import com.example.ui.components.BloodGroupBadge
import com.example.ui.components.EmptyState
import com.example.ui.theme.BloodRed
import com.example.ui.theme.BloodRedContainer
import com.example.ui.theme.BorderLight
import com.example.ui.theme.MedicalGreen
import com.example.ui.theme.MedicalNavy
import com.example.ui.theme.MedicalRed
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.BloodBridgeViewModel

@Composable
fun AdminBloodBanksScreen(
    viewModel: BloodBridgeViewModel,
    modifier: Modifier = Modifier
) {
    val bloodBanks by viewModel.allBloodBanks.collectAsState()
    val allStock by viewModel.allBloodStock.collectAsState()

    var selectedBankId by remember { mutableStateOf<Long?>(null) }
    var showAddBankDialog by remember { mutableStateOf(false) }

    val activeBank = bloodBanks.firstOrNull { it.id == selectedBankId } ?: bloodBanks.firstOrNull()
    val bankStocks = allStock.filter { it.bloodBankId == (activeBank?.id ?: 0L) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("admin_blood_banks_screen")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Blood Centers & Stock",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Manage multi-component blood inventory",
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }

            Button(
                onClick = { showAddBankDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = BloodRed),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("admin_add_bank_button")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Center", fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Center selection tabs
        if (bloodBanks.isNotEmpty()) {
            val selectedIndex = bloodBanks.indexOfFirst { it.id == activeBank?.id }.coerceAtLeast(0)
            ScrollableTabRow(
                selectedTabIndex = selectedIndex,
                edgePadding = 0.dp,
                containerColor = Color.Transparent,
                divider = {}
            ) {
                bloodBanks.forEachIndexed { index, bank ->
                    Tab(
                        selected = activeBank?.id == bank.id,
                        onClick = { selectedBankId = bank.id },
                        text = {
                            Text(
                                text = bank.city,
                                fontWeight = if (activeBank?.id == bank.id) FontWeight.Bold else FontWeight.Normal,
                                color = if (activeBank?.id == bank.id) BloodRed else TextSecondary
                            )
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (activeBank == null) {
            EmptyState(
                icon = Icons.Default.LocalHospital,
                title = "No Blood Centers",
                message = "Add your first blood bank center to begin stock management."
            )
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(12.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderLight))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = activeBank.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                    Text(text = "${activeBank.address}, ${activeBank.city} • Ph: ${activeBank.contactPhone}", fontSize = 12.sp, color = TextSecondary)
                    Text(text = "Licence: ${activeBank.licenseNumber}", fontSize = 11.sp, color = TextMuted)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("Stock Inventory by Group & Component (Never Negative):", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(6.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(bankStocks, key = { it.id }) { stockItem ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("stock_item_${stockItem.id}"),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        shape = RoundedCornerShape(10.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BorderLight))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                BloodGroupBadge(bloodGroup = stockItem.bloodGroup)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = stockItem.component.replace("_", " "),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "Updated: ${stockItem.lastUpdatedBy}",
                                        fontSize = 10.sp,
                                        color = TextMuted
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = {
                                        if (stockItem.unitsAvailable > 0) {
                                            viewModel.adminUpdateStock(stockItem.id, stockItem.unitsAvailable - 1)
                                        }
                                    },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFF1F5F9))
                                        .testTag("decrement_stock_${stockItem.id}")
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = "Decrement", modifier = Modifier.size(16.dp))
                                }

                                Text(
                                    text = "${stockItem.unitsAvailable} Units",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (stockItem.unitsAvailable <= 3) MedicalRed else MedicalGreen,
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                )

                                IconButton(
                                    onClick = {
                                        viewModel.adminUpdateStock(stockItem.id, stockItem.unitsAvailable + 1)
                                    },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(BloodRedContainer)
                                        .testTag("increment_stock_${stockItem.id}")
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Increment", tint = BloodRed, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddBankDialog) {
        var bankName by remember { mutableStateOf("") }
        var bankAddress by remember { mutableStateOf("") }
        var bankCity by remember { mutableStateOf("") }
        var bankState by remember { mutableStateOf("") }
        var bankPincode by remember { mutableStateOf("") }
        var bankPhone by remember { mutableStateOf("") }
        var bankLic by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddBankDialog = false },
            title = { Text("Add Blood Bank Center") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = bankName, onValueChange = { bankName = it }, label = { Text("Center Name") }, singleLine = true)
                    OutlinedTextField(value = bankAddress, onValueChange = { bankAddress = it }, label = { Text("Address") }, singleLine = true)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(value = bankCity, onValueChange = { bankCity = it }, label = { Text("City") }, modifier = Modifier.weight(1f), singleLine = true)
                        OutlinedTextField(value = bankState, onValueChange = { bankState = it }, label = { Text("State") }, modifier = Modifier.weight(1f), singleLine = true)
                    }
                    OutlinedTextField(value = bankPincode, onValueChange = { bankPincode = it }, label = { Text("PIN Code") }, singleLine = true)
                    OutlinedTextField(value = bankPhone, onValueChange = { bankPhone = it }, label = { Text("Contact Phone") }, singleLine = true)
                    OutlinedTextField(value = bankLic, onValueChange = { bankLic = it }, label = { Text("License Number") }, singleLine = true)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (bankName.isNotBlank() && bankCity.isNotBlank()) {
                            val newBank = BloodBankEntity(
                                name = bankName.trim(),
                                address = bankAddress.trim(),
                                city = bankCity.trim(),
                                state = if (bankState.isBlank()) "Tamil Nadu" else bankState.trim(),
                                pincode = bankPincode.trim(),
                                latitude = 11.0168,
                                longitude = 76.9558,
                                contactPhone = bankPhone.trim(),
                                licenseNumber = bankLic.trim()
                            )
                            viewModel.adminAddBloodBank(newBank) {
                                showAddBankDialog = false
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BloodRed)
                ) {
                    Text("Add & Initialize Stock")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddBankDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
