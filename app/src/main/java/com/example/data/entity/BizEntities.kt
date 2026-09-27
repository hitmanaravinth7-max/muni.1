package com.example.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "business_profiles")
data class BusinessProfileEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "industry") val industry: String, // "B2B SaaS", "E-Commerce", "HealthTech", "Fintech", "Professional Services", "D2C"
    @ColumnInfo(name = "stage") val stage: String, // "IDEA", "SEED", "GROWTH", "SCALE_UP"
    @ColumnInfo(name = "monthly_revenue") val monthlyRevenue: Double,
    @ColumnInfo(name = "monthly_expenses") val monthlyExpenses: Double,
    @ColumnInfo(name = "cash_balance") val cashBalance: Double,
    @ColumnInfo(name = "customer_count") val customerCount: Int,
    @ColumnInfo(name = "nps_score") val npsScore: Int = 45,
    @ColumnInfo(name = "cac") val cac: Double = 850.0,
    @ColumnInfo(name = "arpu") val arpu: Double = 220.0,
    @ColumnInfo(name = "target_market") val targetMarket: String = "",
    @ColumnInfo(name = "value_proposition") val valueProposition: String = "",
    @ColumnInfo(name = "primary_goal") val primaryGoal: String = "Accelerate ARR growth while achieving profitability",
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "financial_metrics")
data class FinancialMetricEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "business_id") val businessId: Long,
    @ColumnInfo(name = "month_index") val monthIndex: Int,
    @ColumnInfo(name = "month_label") val monthLabel: String,
    @ColumnInfo(name = "revenue") val revenue: Double,
    @ColumnInfo(name = "expenses") val expenses: Double,
    @ColumnInfo(name = "new_customers") val newCustomers: Int,
    @ColumnInfo(name = "churned_customers") val churnedCustomers: Int
)

@Entity(tableName = "consulting_messages")
data class ConsultingMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "business_id") val businessId: Long,
    @ColumnInfo(name = "sender_role") val senderRole: String, // "USER" or "AI_CONSULTANT"
    @ColumnInfo(name = "message_text") val messageText: String,
    @ColumnInfo(name = "practice_domain") val practiceDomain: String = "STRATEGY",
    @ColumnInfo(name = "source_badge") val sourceBadge: String = "GEMINI_AI",
    @ColumnInfo(name = "timestamp") val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "strategic_plans")
data class StrategicPlanEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "business_id") val businessId: Long,
    @ColumnInfo(name = "plan_type") val planType: String, // "SWOT", "BUSINESS_CANVAS", "30_60_90_ROADMAP"
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "content_json") val contentJson: String,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
)
