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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.ml.BusinessMlEngine
import com.example.ml.ChurnPredictionInput
import com.example.ui.components.RiskBadge
import com.example.ui.theme.BizAmber
import com.example.ui.theme.BizBlue
import com.example.ui.theme.BizBlueContainer
import com.example.ui.theme.BizBluePrimary
import com.example.ui.theme.BizBorder
import com.example.ui.theme.BizCardWhite
import com.example.ui.theme.BizEmerald
import com.example.ui.theme.BizEmeraldContainer
import com.example.ui.theme.BizNavy
import com.example.ui.theme.BizRose
import com.example.ui.theme.BizRoseContainer
import com.example.ui.theme.BizTextMuted
import com.example.ui.theme.BizTextPrimary
import com.example.ui.theme.BizTextSecondary
import com.example.ui.viewmodel.BizAdvisorViewModel

@Composable
fun MlDiagnosticsScreen(
    viewModel: BizAdvisorViewModel,
    modifier: Modifier = Modifier
) {
    val currentProfile by viewModel.selectedProfile.collectAsState()
    val forecast by viewModel.revenueForecast.collectAsState()
    val diagnostics by viewModel.financialDiagnostics.collectAsState()

    val profile = currentProfile ?: return

    // Interactive Churn Simulator States
    var simNps by remember { mutableIntStateOf(profile.npsScore) }
    var simUsageDrop by remember { mutableDoubleStateOf(14.0) }
    var simSupportTickets by remember { mutableDoubleStateOf(0.8) }

    val liveChurnResult = remember(simNps, simUsageDrop, simSupportTickets, profile.monthlyRevenue, profile.customerCount) {
        BusinessMlEngine.predictChurnRisk(
            ChurnPredictionInput(
                customerCount = profile.customerCount,
                monthlyRecurringRevenue = profile.monthlyRevenue,
                npsScore = simNps,
                usageDropPercent = simUsageDrop,
                supportTicketsPerCustomer = simSupportTickets,
                avgContractLengthMonths = if (profile.industry.contains("SaaS")) 12 else 1
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("ml_diagnostics_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = BizNavy),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(BizBluePrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.QueryStats, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Machine Learning Diagnostics Engine", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                        Text("${profile.name} • Predictive Algorithmic Modeling", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                    }
                }
            }
        }

        // Section 1: Time-Series Revenue Regression Modeling
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = BizCardWhite),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BizBorder))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("1. Time-Series Revenue Regression Model", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = BizTextPrimary)
                Text("Ordinary Least Squares (OLS) trend projection with 90% confidence bands", fontSize = 11.sp, color = BizTextSecondary)

                Spacer(modifier = Modifier.height(14.dp))

                forecast?.let { fc ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Next Quarter Run Rate", fontSize = 11.sp, color = BizTextMuted)
                            Text("$${String.format("%,.0f", fc.nextQuarterRevenue)}", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = BizTextPrimary)
                        }
                        Column {
                            Text("MoM Growth Momentum", fontSize = 11.sp, color = BizTextMuted)
                            Text("+${String.format("%.1f", fc.averageMonthlyGrowthRate)}%", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = BizEmerald)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Model Fit (R²)", fontSize = 11.sp, color = BizTextMuted)
                            Text("${String.format("%.2f", fc.rSquared)}", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = BizBluePrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Projected Months Forecast (Mean, Lower & Upper Bands):", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = BizTextPrimary)
                    Spacer(modifier = Modifier.height(8.dp))

                    fc.forecastPoints.forEach { pt ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFF8FAFC))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(pt.monthLabel, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = BizTextPrimary)
                            Text("Predicted: $${String.format("%,.0f", pt.predictedRevenue)}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = BizBluePrimary)
                            Text("Range: $${String.format("%.0fk", pt.lowerBound / 1000)} - $${String.format("%.0fk", pt.upperBound / 1000)}", fontSize = 11.sp, color = BizTextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text("ML Synthesis: ${fc.trendSummary}", fontSize = 11.sp, color = BizTextSecondary, lineHeight = 15.sp)
                }
            }
        }

        // Section 2: Interactive Churn & Retention Machine Learning Simulator
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = BizCardWhite),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BizBorder))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("2. Churn Risk Machine Learning Simulator", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = BizTextPrimary)
                        Text("Adjust customer telemetry to simulate probability shifts", fontSize = 11.sp, color = BizTextSecondary)
                    }
                    RiskBadge(riskLevel = liveChurnResult.riskLevel)
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Simulated Output Card
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (liveChurnResult.churnProbabilityPercent > 40) BizRoseContainer else BizEmeraldContainer)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Predicted Monthly Churn Probability", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = BizTextPrimary)
                        Text("${String.format("%.1f", liveChurnResult.churnProbabilityPercent)}%", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = if (liveChurnResult.churnProbabilityPercent > 40) BizRose else BizEmerald)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Simulated MRR At Risk", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = BizTextPrimary)
                        Text("$${String.format("%,.0f", liveChurnResult.estimatedMmrAtRisk)}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BizTextPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Sliders
                Text("Net Promoter Score (NPS): $simNps", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = BizTextPrimary)
                Slider(
                    value = simNps.toFloat(),
                    onValueChange = { simNps = it.toInt() },
                    valueRange = -50f..100f,
                    colors = SliderDefaults.colors(thumbColor = BizBluePrimary, activeTrackColor = BizBluePrimary)
                )

                Text("30-Day Usage Inactivity Drop-off: ${simUsageDrop.toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = BizTextPrimary)
                Slider(
                    value = simUsageDrop.toFloat(),
                    onValueChange = { simUsageDrop = it.toDouble() },
                    valueRange = 0f..80f,
                    colors = SliderDefaults.colors(thumbColor = BizAmber, activeTrackColor = BizAmber)
                )

                Text("Support Tickets per Account: ${String.format("%.1f", simSupportTickets)}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = BizTextPrimary)
                Slider(
                    value = simSupportTickets.toFloat(),
                    onValueChange = { simSupportTickets = it.toDouble() },
                    valueRange = 0f..4f,
                    colors = SliderDefaults.colors(thumbColor = BizRose, activeTrackColor = BizRose)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Prescribed Machine Learning Interventions:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BizTextPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                liveChurnResult.recommendedInterventions.forEach { action ->
                    Row(modifier = Modifier.padding(vertical = 3.dp), verticalAlignment = Alignment.Top) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BizEmerald, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(action, fontSize = 11.sp, color = BizTextSecondary, lineHeight = 15.sp)
                    }
                }
            }
        }

        // Section 3: Unit Economics Diagnostics
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = BizCardWhite),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BizBorder))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("3. Unit Economics & Break-Even Analysis", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = BizTextPrimary)
                Text("LTV/CAC and capital efficiency benchmarks", fontSize = 11.sp, color = BizTextSecondary)

                Spacer(modifier = Modifier.height(14.dp))

                diagnostics?.let { diag ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Customer Lifetime Value (CLV)", fontSize = 11.sp, color = BizTextMuted)
                            Text("$${String.format("%,.0f", diag.ltv)}", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = BizTextPrimary)
                        }
                        Column {
                            Text("CAC Payback Period", fontSize = 11.sp, color = BizTextMuted)
                            Text("${String.format("%.1f", diag.cacPaybackMonths)} months", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = BizBluePrimary)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Runway Buffer", fontSize = 11.sp, color = BizTextMuted)
                            Text("${String.format("%.1f", diag.runwayMonths)} mos", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = BizEmerald)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    diag.diagnosticInsights.forEach { ins ->
                        Row(modifier = Modifier.padding(vertical = 3.dp), verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.Lightbulb, contentDescription = null, tint = BizAmber, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(ins, fontSize = 11.sp, color = BizTextSecondary)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}
