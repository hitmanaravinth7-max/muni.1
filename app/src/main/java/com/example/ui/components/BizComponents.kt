package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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

@Composable
fun MetricKpiCard(
    title: String,
    value: String,
    subtext: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.testTag("kpi_card_$title"),
        colors = CardDefaults.cardColors(containerColor = BizCardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(BizBorder))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = BizTextSecondary)
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = BizTextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtext,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = if (subtext.contains("+") || subtext.contains("Healthy") || subtext.contains("Optimal")) BizEmerald else BizTextMuted
            )
        }
    }
}

@Composable
fun RiskBadge(riskLevel: String, modifier: Modifier = Modifier) {
    val (bgColor, textColor) = when (riskLevel.uppercase()) {
        "LOW" -> Pair(BizEmeraldContainer, BizEmerald)
        "MODERATE" -> Pair(BizAmberContainer, BizAmber)
        "ELEVATED", "CRITICAL" -> Pair(BizRoseContainer, BizRose)
        else -> Pair(BizBlueContainer, BizBluePrimary)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = "$riskLevel RISK",
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold,
            color = textColor
        )
    }
}

@Composable
fun HealthScorePill(score: Int, rating: String, modifier: Modifier = Modifier) {
    val (bgColor, textColor) = when {
        score >= 80 -> Pair(BizEmeraldContainer, BizEmerald)
        score >= 60 -> Pair(BizBlueContainer, BizBluePrimary)
        score >= 40 -> Pair(BizAmberContainer, BizAmber)
        else -> Pair(BizRoseContainer, BizRose)
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(textColor)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "$score/100 • $rating",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
fun AiConsultingBanner(
    title: String = "AI Executive Advisory Active",
    subtitle: String = "Powered by Gemini Generative AI and predictive machine learning diagnostics",
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
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
                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                Text(subtitle, fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
            }
        }
    }
}
