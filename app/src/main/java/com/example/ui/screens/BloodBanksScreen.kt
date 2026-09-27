package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BloodBankCard
import com.example.ui.components.EmptyState
import com.example.ui.theme.BloodRed
import com.example.ui.theme.BorderLight
import com.example.ui.theme.MedicalGreen
import com.example.ui.theme.MedicalNavy
import com.example.ui.theme.MedicalRed
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.BloodBridgeViewModel
import com.example.util.AppIntents

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BloodBanksScreen(
    viewModel: BloodBridgeViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val bloodBanks by viewModel.allBloodBanks.collectAsState()
    val allStock by viewModel.allBloodStock.collectAsState()

    var cityFilter by remember { mutableStateOf("") }
    var selectedBloodGroup by remember { mutableStateOf("ALL") }
    var selectedComponent by remember { mutableStateOf("ALL") }

    val bloodGroupsList = listOf("ALL", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
    val componentsList = listOf("ALL", "WHOLE_BLOOD", "PLASMA", "PLATELETS", "RBC")

    val stockByBank = allStock.groupBy { it.bloodBankId }

    val filteredBanks = bloodBanks.filter { bank ->
        val cityMatch = if (cityFilter.isBlank()) true else {
            bank.city.contains(cityFilter.trim(), ignoreCase = true) ||
            bank.state.contains(cityFilter.trim(), ignoreCase = true) ||
            bank.name.contains(cityFilter.trim(), ignoreCase = true)
        }

        val bankStocks = stockByBank[bank.id] ?: emptyList()
        val groupMatch = if (selectedBloodGroup == "ALL") true else {
            bankStocks.any { it.bloodGroup == selectedBloodGroup && it.unitsAvailable > 0 }
        }

        val componentMatch = if (selectedComponent == "ALL") true else {
            bankStocks.any { it.component == selectedComponent && it.unitsAvailable > 0 }
        }

        cityMatch && groupMatch && componentMatch
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("blood_banks_screen")
    ) {
        // Search Input
        OutlinedTextField(
            value = cityFilter,
            onValueChange = { cityFilter = it },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("search_bank_city_input"),
            placeholder = { Text("Search by City, State, or Bank Name") },
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

        // Component filter chips
        Text("Component:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary)
        Spacer(modifier = Modifier.height(4.dp))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for (comp in componentsList) {
                val isSelected = selectedComponent == comp
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedComponent = comp },
                    label = { Text(comp.replace("_", " "), fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MedicalNavy,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("filter_comp_$comp")
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Blood group chips
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
                    label = { Text(bg, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BloodRed,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("filter_bank_bg_$bg")
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "${filteredBanks.size} Registered Blood Centers Found",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextMuted
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (filteredBanks.isEmpty()) {
            EmptyState(
                icon = Icons.Default.LocalHospital,
                title = "No Blood Banks Found",
                message = "No blood centers match your selected filters. Try changing your filters.",
                actionButtonText = "Reset Filters",
                onActionClick = {
                    cityFilter = ""
                    selectedBloodGroup = "ALL"
                    selectedComponent = "ALL"
                }
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredBanks, key = { it.id }) { bank ->
                    val stock = stockByBank[bank.id] ?: emptyList()
                    BloodBankCard(
                        bank = bank,
                        stockList = stock,
                        onGetDirections = { lat, lng, name ->
                            AppIntents.openGoogleMaps(context, lat, lng, name, bank.city)
                        },
                        onCallBank = { phone ->
                            AppIntents.dialPhoneNumber(context, phone)
                        }
                    )
                }
            }
        }
    }
}
