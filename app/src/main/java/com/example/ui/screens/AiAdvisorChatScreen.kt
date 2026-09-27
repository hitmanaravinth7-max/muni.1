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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.ui.theme.BizBlue
import com.example.ui.theme.BizBlueContainer
import com.example.ui.theme.BizBluePrimary
import com.example.ui.theme.BizBorder
import com.example.ui.theme.BizCardWhite
import com.example.ui.theme.BizEmerald
import com.example.ui.theme.BizEmeraldContainer
import com.example.ui.theme.BizNavy
import com.example.ui.theme.BizTextMuted
import com.example.ui.theme.BizTextPrimary
import com.example.ui.theme.BizTextSecondary
import com.example.ui.viewmodel.BizAdvisorViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AiAdvisorChatScreen(
    viewModel: BizAdvisorViewModel,
    initialDomain: String = "STRATEGY",
    modifier: Modifier = Modifier
) {
    val currentProfile by viewModel.selectedProfile.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val isGenerating by viewModel.isGeneratingAi.collectAsState()

    var inputQuery by remember { mutableStateOf("") }
    var selectedDomain by remember { mutableStateOf(initialDomain) }

    val listState = rememberLazyListState()

    val domains = listOf(
        "STRATEGY" to "Strategy & Moats",
        "GTM" to "Go-To-Market & Pricing",
        "FINANCE" to "Unit Economics & Runway",
        "PRODUCT" to "Product-Market Fit",
        "OPERATIONS" to "Scaling & Org"
    )

    val promptSuggestions = listOf(
        "How do we raise our LTV/CAC ratio above 3.5x?",
        "Formulate an enterprise outbound sales strategy",
        "What are our critical churn risks and countermeasures?",
        "How should we prepare our investor data room for Series A?",
        "Design an annual pre-paid contract migration offer"
    )

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val profile = currentProfile ?: return

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("ai_advisor_chat_screen")
    ) {
        // Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = BizNavy),
            shape = RoundedCornerShape(14.dp)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(BizBluePrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.SmartToy, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("BizAdvisor AI Consultant", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
                    Text("Consulting for ${profile.name} • ${profile.industry}", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Practice Domain Selector
        Text("Consulting Practice Domain:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = BizTextSecondary)
        Spacer(modifier = Modifier.height(4.dp))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for ((key, label) in domains) {
                val isSelected = selectedDomain == key
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedDomain = key },
                    label = { Text(label, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BizBluePrimary,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Messages Stream
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                val isUser = msg.senderRole == "USER"

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                ) {
                    if (!isUser) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(BizBluePrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.SmartToy, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(0.85f),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isUser) BizBluePrimary else BizCardWhite
                        ),
                        shape = RoundedCornerShape(14.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        border = if (isUser) null else CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BizBorder))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            if (!isUser) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Executive Consultant",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = BizBluePrimary
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(BizEmeraldContainer)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(msg.sourceBadge, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = BizEmerald)
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                            }

                            Text(
                                text = msg.messageText,
                                fontSize = 13.sp,
                                color = if (isUser) Color.White else BizTextPrimary,
                                lineHeight = 18.sp
                            )
                        }
                    }

                    if (isUser) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(BizNavy),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            if (isGenerating) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), color = BizBluePrimary, strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Synthesizing strategic recommendations...", fontSize = 12.sp, color = BizTextSecondary)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Quick Suggestion Pills
        if (messages.size <= 2) {
            Text("Suggested Inquiries:", fontSize = 10.sp, color = BizTextMuted, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                promptSuggestions.take(3).forEach { suggestion ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(BizBlueContainer)
                            .clickable {
                                viewModel.sendConsultantQuery(suggestion, selectedDomain)
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(suggestion, fontSize = 11.sp, color = BizBluePrimary)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Input Field & Send Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputQuery,
                onValueChange = { inputQuery = it },
                modifier = Modifier
                    .weight(1f)
                    .testTag("consultant_chat_input"),
                placeholder = { Text("Ask strategy, GTM, pricing, or burn...", fontSize = 12.sp) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (inputQuery.isNotBlank() && !isGenerating) {
                        val text = inputQuery
                        inputQuery = ""
                        viewModel.sendConsultantQuery(text, selectedDomain)
                    }
                },
                enabled = inputQuery.isNotBlank() && !isGenerating,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (inputQuery.isNotBlank() && !isGenerating) BizBluePrimary else Color(0xFFCBD5E1))
                    .testTag("consultant_chat_send_btn")
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(18.dp))
            }
        }
    }
}
