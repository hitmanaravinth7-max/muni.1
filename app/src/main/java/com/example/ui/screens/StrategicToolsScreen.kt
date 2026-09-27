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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.ui.theme.BizAmber
import com.example.ui.theme.BizAmberContainer
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
import com.example.ui.theme.BizViolet
import com.example.ui.theme.BizVioletContainer
import com.example.ui.viewmodel.BizAdvisorViewModel

@Composable
fun StrategicToolsScreen(
    viewModel: BizAdvisorViewModel,
    initialTab: Int = 0,
    modifier: Modifier = Modifier
) {
    val currentProfile by viewModel.selectedProfile.collectAsState()
    val swot by viewModel.swotAnalysis.collectAsState()
    val canvas by viewModel.businessCanvas.collectAsState()
    val roadmap by viewModel.roadmapPhases.collectAsState()
    val isGenerating by viewModel.isGeneratingAi.collectAsState()

    var selectedTab by remember { mutableIntStateOf(initialTab) }

    val tabs = listOf("Model Canvas", "SWOT Analysis", "90-Day Roadmap")
    val profile = currentProfile ?: return

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("strategic_tools_screen")
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent,
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == index) BizBluePrimary else BizTextSecondary
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> CanvasTabContent(
                    profileName = profile.name,
                    canvas = canvas,
                    isGenerating = isGenerating,
                    onGenerate = { viewModel.generateBusinessModelCanvas() }
                )
                1 -> SwotTabContent(
                    profileName = profile.name,
                    swot = swot,
                    isGenerating = isGenerating,
                    onGenerate = { viewModel.generateSwot() }
                )
                2 -> RoadmapTabContent(
                    profileName = profile.name,
                    roadmap = roadmap,
                    isGenerating = isGenerating,
                    onGenerate = { viewModel.generateRoadmap() }
                )
            }
        }
    }
}

@Composable
private fun CanvasTabContent(
    profileName: String,
    canvas: Map<String, List<String>>?,
    isGenerating: Boolean,
    onGenerate: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("9-Box Business Model Canvas", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = BizTextPrimary)
                Text("Osterwalder framework tailored for $profileName", fontSize = 11.sp, color = BizTextMuted)
            }

            Button(
                onClick = onGenerate,
                enabled = !isGenerating,
                colors = ButtonDefaults.buttonColors(containerColor = BizBluePrimary),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("generate_canvas_btn")
            ) {
                if (isGenerating) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(14.dp))
                } else {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (canvas == null) "Generate Canvas" else "Refresh AI", fontSize = 11.sp)
                }
            }
        }

        if (canvas == null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Business, contentDescription = null, tint = BizTextMuted, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("No Canvas Generated Yet", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = BizTextPrimary)
                    Text("Tap 'Generate Canvas' to synthesize all 9 building blocks with AI.", fontSize = 12.sp, color = BizTextSecondary)
                }
            }
        } else {
            canvas.forEach { (blockName, itemsList) ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = BizCardWhite),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BizBorder))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(BizBluePrimary)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = blockName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = BizTextPrimary)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        itemsList.forEach { item ->
                            Row(modifier = Modifier.padding(vertical = 2.dp), verticalAlignment = Alignment.Top) {
                                Text("• ", color = BizBluePrimary, fontWeight = FontWeight.Bold)
                                Text(item, fontSize = 12.sp, color = BizTextSecondary, lineHeight = 16.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SwotTabContent(
    profileName: String,
    swot: Map<String, List<String>>?,
    isGenerating: Boolean,
    onGenerate: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("SWOT & Competitive Moats Analysis", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = BizTextPrimary)
                Text("Defensibility & threat assessment for $profileName", fontSize = 11.sp, color = BizTextMuted)
            }

            Button(
                onClick = onGenerate,
                enabled = !isGenerating,
                colors = ButtonDefaults.buttonColors(containerColor = BizEmerald),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("generate_swot_btn")
            ) {
                if (isGenerating) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(14.dp))
                } else {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (swot == null) "Run SWOT AI" else "Refresh SWOT", fontSize = 11.sp)
                }
            }
        }

        if (swot == null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = BizTextMuted, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("No SWOT Analysis Generated Yet", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = BizTextPrimary)
                    Text("Tap 'Run SWOT AI' to evaluate internal strengths, weaknesses & external market moats.", fontSize = 12.sp, color = BizTextSecondary)
                }
            }
        } else {
            swot.forEach { (category, bulletPoints) ->
                val (headerColor, bgColor) = when (category.lowercase()) {
                    "strengths" -> Pair(BizEmerald, BizEmeraldContainer)
                    "weaknesses" -> Pair(BizAmber, BizAmberContainer)
                    "opportunities" -> Pair(BizBluePrimary, BizBlueContainer)
                    else -> Pair(BizRose, BizRoseContainer)
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = BizCardWhite),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BizBorder))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(bgColor)
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(category.uppercase(), fontWeight = FontWeight.Bold, fontSize = 11.sp, color = headerColor)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        bulletPoints.forEach { pt ->
                            Row(modifier = Modifier.padding(vertical = 2.dp), verticalAlignment = Alignment.Top) {
                                Text("• ", color = headerColor, fontWeight = FontWeight.Bold)
                                Text(pt, fontSize = 12.sp, color = BizTextSecondary, lineHeight = 16.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RoadmapTabContent(
    profileName: String,
    roadmap: List<com.example.ui.viewmodel.StrategicRoadmapPhase>,
    isGenerating: Boolean,
    onGenerate: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("30-60-90 Day Execution Roadmap", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = BizTextPrimary)
                Text("Phased operational milestones for $profileName", fontSize = 11.sp, color = BizTextMuted)
            }

            Button(
                onClick = onGenerate,
                enabled = !isGenerating,
                colors = ButtonDefaults.buttonColors(containerColor = BizAmber),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("generate_roadmap_btn")
            ) {
                if (isGenerating) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(14.dp))
                } else {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (roadmap.isEmpty()) "Generate Roadmap" else "Refresh Roadmap", fontSize = 11.sp)
                }
            }
        }

        if (roadmap.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Timeline, contentDescription = null, tint = BizTextMuted, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("No Execution Roadmap Generated", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = BizTextPrimary)
                    Text("Tap 'Generate Roadmap' to create sequenced 30, 60, and 90-day action plans.", fontSize = 12.sp, color = BizTextSecondary)
                }
            }
        } else {
            roadmap.forEach { phase ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = BizCardWhite),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BizBorder))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(phase.phaseTitle, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = BizTextPrimary)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(BizBlueContainer)
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(phase.timeline, fontWeight = FontWeight.Bold, fontSize = 10.sp, color = BizBluePrimary)
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(phase.objective, fontSize = 11.sp, color = BizTextSecondary, lineHeight = 15.sp)

                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Key Tactical Initiatives:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = BizTextPrimary)
                        phase.keyInitiatives.forEach { init ->
                            Row(modifier = Modifier.padding(vertical = 2.dp), verticalAlignment = Alignment.Top) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BizEmerald, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(init, fontSize = 11.sp, color = BizTextSecondary, lineHeight = 15.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFF1F5F9))
                                .padding(8.dp)
                        ) {
                            Text("Target KPIs: ${phase.targetKpis}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BizNavy)
                        }
                    }
                }
            }
        }
    }
}
