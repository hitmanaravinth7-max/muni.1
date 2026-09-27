package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BizBluePrimary
import com.example.ui.theme.BizNavy

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NewBusinessProfileDialog(
    onDismiss: () -> Unit,
    onSubmit: (
        name: String,
        industry: String,
        stage: String,
        revenue: Double,
        expenses: Double,
        cash: Double,
        customers: Int,
        targetMarket: String,
        valueProp: String,
        goal: String
    ) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedIndustry by remember { mutableStateOf("B2B SaaS") }
    var selectedStage by remember { mutableStateOf("GROWTH") }
    var revenueStr by remember { mutableStateOf("25000") }
    var expensesStr by remember { mutableStateOf("22000") }
    var cashStr by remember { mutableStateOf("95000") }
    var customersStr by remember { mutableStateOf("120") }
    var targetMarket by remember { mutableStateOf("Mid-market businesses") }
    var valueProp by remember { mutableStateOf("Faster workflow automation with AI") }
    var primaryGoal by remember { mutableStateOf("Double MRR in next 6 months") }

    val industries = listOf("B2B SaaS", "E-Commerce", "HealthTech", "FinTech", "Services", "Mobile App")
    val stages = listOf("IDEA", "SEED", "GROWTH", "SCALE_UP")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Business Profile", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Company / Startup Name *") },
                    placeholder = { Text("e.g. Lumina Tech") },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                Text("Industry Sector:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    industries.forEach { ind ->
                        FilterChip(
                            selected = selectedIndustry == ind,
                            onClick = { selectedIndustry = ind },
                            label = { Text(ind, fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BizBluePrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Text("Company Stage:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    stages.forEach { st ->
                        FilterChip(
                            selected = selectedStage == st,
                            onClick = { selectedStage = st },
                            label = { Text(st, fontSize = 9.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BizNavy,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedTextField(
                        value = revenueStr,
                        onValueChange = { revenueStr = it },
                        label = { Text("Monthly Rev ($)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = expensesStr,
                        onValueChange = { expensesStr = it },
                        label = { Text("Monthly Burn ($)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedTextField(
                        value = cashStr,
                        onValueChange = { cashStr = it },
                        label = { Text("Cash Balance ($)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = customersStr,
                        onValueChange = { customersStr = it },
                        label = { Text("Customers") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                OutlinedTextField(
                    value = valueProp,
                    onValueChange = { valueProp = it },
                    label = { Text("Core Value Proposition") },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = primaryGoal,
                    onValueChange = { primaryGoal = it },
                    label = { Text("Primary Strategic Objective") },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val rev = revenueStr.toDoubleOrNull() ?: 10000.0
                        val exp = expensesStr.toDoubleOrNull() ?: 8000.0
                        val cash = cashStr.toDoubleOrNull() ?: 50000.0
                        val cust = customersStr.toIntOrNull() ?: 50
                        onSubmit(name, selectedIndustry, selectedStage, rev, exp, cash, cust, targetMarket, valueProp, primaryGoal)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BizBluePrimary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Create & Diagnose")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
