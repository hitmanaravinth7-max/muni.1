package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
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
import com.example.ui.components.AiConsultingBanner
import com.example.ui.components.HealthScorePill
import com.example.ui.components.MetricKpiCard
import com.example.ui.components.RiskBadge
import com.example.ui.theme.BizAmber
import com.example.ui.theme.BizBlue
import com.example.ui.theme.BizBlueContainer
import com.example.ui.theme.BizBluePrimary
import com.example.ui.theme.BizBorder
import com.example.ui.theme.BizCardWhite
import com.example.ui.theme.BizEmerald
import com.example.ui.theme.BizNavy
import com.example.ui.theme.BizRose
import com.example.ui.theme.BizTextMuted
import com.example.ui.theme.BizTextPrimary
import com.example.ui.theme.BizTextSecondary
import com.example.ui.theme.BizViolet
import com.example.ui.viewmodel.BizAdvisorViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExecutiveDashboardScreen(
    viewModel: BizAdvisorViewModel,
    onNavigateToChat: (domain: String) -> Unit,
    onNavigateToMlDiagnostics: () -> Unit,
    onNavigateToStrategicTools: (toolIndex: Int) -> Unit,
    onShowNewProfileDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val allProfiles by viewModel.allProfiles.collectAsState()
    val currentProfile by viewModel.selectedProfile.collectAsState()
    val forecast by viewModel.revenueForecast.collectAsState()
    val churn by viewModel.churnPrediction.collectAsState()
    val diagnostics by viewModel.financialDiagnostics.collectAsState()

    val profile = currentProfile ?: return

    val netProfit = profile.monthlyRevenue - profile.monthlyExpenses
    val isProfitable = netProfit >= 0

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("executive_dashboard_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Business Profile Switcher Bar
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = BizCardWhite),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BizBorder))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Active Business Profile", fontSize = 11.sp, color = BizTextMuted, fontWeight = FontWeight.SemiBold)
                        Text(profile.name, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = BizTextPrimary)
                    }

                    OutlinedButton(
                        onClick = onShowNewProfileDialog,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("add_business_profile_btn")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("New Profile", fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Profile Selector Pills
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (p in allProfiles) {
                        val isSelected = p.id == profile.id
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.selectProfile(p.id) },
                            label = { Text(p.name, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BizBluePrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Sub-tags
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("${profile.industry} • ${profile.stage.replace("_", " ")}", fontSize = 12.sp, color = BizTextSecondary)
                    diagnostics?.let {
                        HealthScorePill(score = it.healthScore, rating = it.healthRating)
                    }
                }
            }
        }

        // AI Advisory Active Banner
        AiConsultingBanner()

        // Core Financial & Unit Economics KPI Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricKpiCard(
                title = "Monthly Revenue",
                value = "$${String.format("%,.0f", profile.monthlyRevenue)}",
                subtext = "+14.2% MoM Growth",
                icon = Icons.Default.AttachMoney,
                accentColor = BizEmerald,
                modifier = Modifier.weight(1f)
            )

            MetricKpiCard(
                title = if (isProfitable) "Monthly Net Profit" else "Net Monthly Burn",
                value = "$${String.format("%,.0f", kotlin.math.abs(netProfit))}",
                subtext = if (isProfitable) "Self-Sustaining" else "${String.format("%.1f", diagnostics?.runwayMonths ?: 12.0)} mos runway",
                icon = if (isProfitable) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                accentColor = if (isProfitable) BizEmerald else BizRose,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricKpiCard(
                title = "LTV / CAC Ratio",
                value = "${String.format("%.1f", diagnostics?.ltvCacRatio ?: 3.5)}x",
                subtext = "Benchmark > 3.0x (Optimal)",
                icon = Icons.Default.Speed,
                accentColor = BizBluePrimary,
                modifier = Modifier.weight(1f)
            )

            MetricKpiCard(
                title = "CAC Payback Period",
                value = "${String.format("%.1f", diagnostics?.cacPaybackMonths ?: 11.0)} mos",
                subtext = "Fast capital recycling",
                icon = Icons.Default.DateRange,
                accentColor = BizViolet,
                modifier = Modifier.weight(1f)
            )
        }

        // Machine Learning Predictive Revenue Forecast Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToMlDiagnostics() }
                .testTag("dashboard_ml_forecast_card"),
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(BizBlueContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.QueryStats, contentDescription = null, tint = BizBluePrimary, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Machine Learning Revenue Forecast", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = BizTextPrimary)
                            Text("Time-series regression & trend estimation", fontSize = 11.sp, color = BizTextMuted)
                        }
                    }

                    Text("View Full ML >", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BizBluePrimary)
                }

                Spacer(modifier = Modifier.height(14.dp))

                forecast?.let { fc ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Next Quarter Run Rate", fontSize = 11.sp, color = BizTextSecondary)
                            Text("$${String.format("%,.0f", fc.nextQuarterRevenue)}", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = BizTextPrimary)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Model Confidence (R²)", fontSize = 11.sp, color = BizTextSecondary)
                            Text("${String.format("%.2f", fc.rSquared * 100)}%", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = BizEmerald)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Predictive Month Bars Visualization
                    Text("Predicted Next 4 Months Trajectory:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = BizTextSecondary)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val maxForecast = fc.forecastPoints.maxOfOrNull { it.predictedRevenue } ?: 100000.0
                        fc.forecastPoints.take(4).forEach { pt ->
                            val heightFraction = (pt.predictedRevenue / maxForecast).coerceIn(0.2, 1.0).toFloat()
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(BizBlueContainer)
                                    .padding(vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("$${String.format("%.0fk", pt.predictedRevenue / 1000.0)}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BizBluePrimary)
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .width(18.dp)
                                        .height((36 * heightFraction).dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(BizBluePrimary)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(pt.monthLabel, fontSize = 9.sp, color = BizTextSecondary)
                            }
                        }
                    }
                }
            }
        }

        // Machine Learning Churn Risk Predictor Preview
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToMlDiagnostics() }
                .testTag("dashboard_churn_card"),
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFEF2F2)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = BizRose, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("ML Churn Probability Diagnostic", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = BizTextPrimary)
                            Text("NPS & behavioral drop-off classifier", fontSize = 11.sp, color = BizTextMuted)
                        }
                    }

                    churn?.let {
                        RiskBadge(riskLevel = it.riskLevel)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                churn?.let { c ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Churn Probability Score", fontSize = 11.sp, color = BizTextSecondary)
                            Text("${String.format("%.1f", c.churnProbabilityPercent)}%", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = BizRose)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Monthly MRR At Risk", fontSize = 11.sp, color = BizTextSecondary)
                            Text("$${String.format("%,.0f", c.estimatedMmrAtRisk)}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BizTextPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Top Prescribed ML Action: ${c.recommendedInterventions.firstOrNull() ?: "Maintain customer check-in cadence."}", fontSize = 11.sp, color = BizTextSecondary)
                }
            }
        }

        // Strategic Generative AI Consultation Hub Actions
        Text("AI Executive Consultant Tools", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = BizTextPrimary)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ConsultantActionCard(
                title = "Ask AI Consultant",
                subtitle = "Multi-turn executive advisory chat",
                icon = Icons.Default.Chat,
                accentColor = BizBluePrimary,
                onClick = { onNavigateToChat("STRATEGY") },
                modifier = Modifier.weight(1f),
                testTag = "action_ai_chat"
            )

            ConsultantActionCard(
                title = "Business Model Canvas",
                subtitle = "Generate 9-box Osterwalder model",
                icon = Icons.Default.Business,
                accentColor = BizViolet,
                onClick = { onNavigateToStrategicTools(0) },
                modifier = Modifier.weight(1f),
                testTag = "action_canvas"
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ConsultantActionCard(
                title = "SWOT & Moats Analysis",
                subtitle = "Defensibility & strategic threats",
                icon = Icons.Default.Security,
                accentColor = BizEmerald,
                onClick = { onNavigateToStrategicTools(1) },
                modifier = Modifier.weight(1f),
                testTag = "action_swot"
            )

            ConsultantActionCard(
                title = "30-60-90 Day Roadmap",
                subtitle = "Actionable execution milestones",
                icon = Icons.Default.Timeline,
                accentColor = BizAmber,
                onClick = { onNavigateToStrategicTools(2) },
                modifier = Modifier.weight(1f),
                testTag = "action_roadmap"
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun ConsultantActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String
) {
    Card(
        modifier = modifier
            .clickable { onClick() }
            .testTag(testTag),
        colors = CardDefaults.cardColors(containerColor = BizCardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(14.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BizBorder))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = BizTextPrimary)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, fontSize = 11.sp, color = BizTextSecondary, lineHeight = 14.sp)
        }
    }
}
