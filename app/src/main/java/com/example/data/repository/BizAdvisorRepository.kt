package com.example.data.repository

import android.content.Context
import com.example.ai.GeminiConsultantService
import com.example.data.dao.BizAdvisorDao
import com.example.data.database.AppDatabase
import com.example.data.entity.BusinessProfileEntity
import com.example.data.entity.ConsultingMessageEntity
import com.example.data.entity.FinancialMetricEntity
import com.example.data.entity.StrategicPlanEntity
import com.example.ml.BusinessMlEngine
import com.example.ml.ChurnPredictionInput
import com.example.ml.ChurnPredictionResult
import com.example.ml.FinancialDiagnosticsResult
import com.example.ml.RevenueForecastResult
import com.example.ml.UnitEconomicsMetrics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BizAdvisorRepository(private val dao: BizAdvisorDao) {

    companion object {
        @Volatile
        private var INSTANCE: BizAdvisorRepository? = null

        fun getInstance(context: Context): BizAdvisorRepository {
            return INSTANCE ?: synchronized(this) {
                val db = AppDatabase.getDatabase(context)
                val repo = BizAdvisorRepository(db.bizAdvisorDao())
                INSTANCE = repo
                CoroutineScope(Dispatchers.IO).launch {
                    repo.seedDemoProfilesIfEmpty()
                }
                repo
            }
        }
    }

    suspend fun seedDemoProfilesIfEmpty() = withContext(Dispatchers.IO) {
        val existing = dao.getAllProfilesFlow().firstOrNull()
        if (!existing.isNullOrEmpty()) return@withContext

        // 1. Apex AI SaaS
        val apexId = dao.insertProfile(
            BusinessProfileEntity(
                name = "Apex AI Systems",
                industry = "B2B SaaS / Enterprise AI",
                stage = "SCALE_UP",
                monthlyRevenue = 85000.0,
                monthlyExpenses = 62000.0,
                cashBalance = 480000.0,
                customerCount = 380,
                npsScore = 68,
                cac = 1200.0,
                arpu = 224.0,
                targetMarket = "Mid-to-large enterprise ops directors",
                valueProposition = "Autonomous workflow optimization cutting operations overhead by 40%",
                primaryGoal = "Accelerate ARR from $1M to $3M while maintaining net retention > 115%"
            )
        )

        // Seed 9 months of revenue data for Apex AI
        val apexRevenues = listOf(42000.0, 46500.0, 51000.0, 56800.0, 62500.0, 68000.0, 73500.0, 79200.0, 85000.0)
        val apexExpenses = listOf(48000.0, 49000.0, 52000.0, 54000.0, 56000.0, 58000.0, 59500.0, 61000.0, 62000.0)
        val apexMetrics = apexRevenues.indices.map { i ->
            FinancialMetricEntity(
                businessId = apexId,
                monthIndex = i + 1,
                monthLabel = "Month ${i + 1}",
                revenue = apexRevenues[i],
                expenses = apexExpenses[i],
                newCustomers = 28 + (i * 3),
                churnedCustomers = 3 + (i % 2)
            )
        }
        dao.insertMetrics(apexMetrics)

        // Initial welcome consulting message
        dao.insertMessage(
            ConsultingMessageEntity(
                businessId = apexId,
                senderRole = "AI_CONSULTANT",
                messageText = "Welcome to BizAdvisor AI. I have reviewed Apex AI Systems' financial trajectories and telemetry. Your LTV/CAC stands at an elite 4.2x with strong linear momentum. What strategic priority shall we tackle today: Enterprise GTM pricing, churn mitigation, or Series A fundraising readiness?",
                practiceDomain = "STRATEGY"
            )
        )

        // 2. GreenRoots D2C
        val grId = dao.insertProfile(
            BusinessProfileEntity(
                name = "GreenRoots Organics",
                industry = "E-Commerce / D2C",
                stage = "GROWTH",
                monthlyRevenue = 42000.0,
                monthlyExpenses = 39000.0,
                cashBalance = 115000.0,
                customerCount = 1250,
                npsScore = 52,
                cac = 42.0,
                arpu = 34.0,
                targetMarket = "Eco-conscious urban millennials and young families",
                valueProposition = "Zero-plastic organic personal care refills delivered on flexible subscription",
                primaryGoal = "Improve repeat purchase rate and reduce paid ad dependency"
            )
        )

        val grRevenues = listOf(28000.0, 31000.0, 33500.0, 35000.0, 38200.0, 42000.0)
        val grExpenses = listOf(26000.0, 29000.0, 32000.0, 33500.0, 36000.0, 39000.0)
        val grMetrics = grRevenues.indices.map { i ->
            FinancialMetricEntity(
                businessId = grId,
                monthIndex = i + 1,
                monthLabel = "Month ${i + 1}",
                revenue = grRevenues[i],
                expenses = grExpenses[i],
                newCustomers = 180 + (i * 15),
                churnedCustomers = 45 + (i * 4)
            )
        }
        dao.insertMetrics(grMetrics)

        // 3. HealthPulse Care
        val hpId = dao.insertProfile(
            BusinessProfileEntity(
                name = "HealthPulse Network",
                industry = "Healthcare / Services",
                stage = "SCALE_UP",
                monthlyRevenue = 115000.0,
                monthlyExpenses = 96000.0,
                cashBalance = 240000.0,
                customerCount = 620,
                npsScore = 74,
                cac = 380.0,
                arpu = 185.0,
                targetMarket = "Preventative chronic wellness patients and corporate health programs",
                valueProposition = "Integrated concierge primary health and continuous diagnostic telemetry",
                primaryGoal = "Fund and execute 4th regional clinic expansion"
            )
        )

        val hpRevenues = listOf(82000.0, 88000.0, 94000.0, 101000.0, 108000.0, 115000.0)
        val hpExpenses = listOf(78000.0, 82000.0, 85000.0, 89000.0, 92000.0, 96000.0)
        val hpMetrics = hpRevenues.indices.map { i ->
            FinancialMetricEntity(
                businessId = hpId,
                monthIndex = i + 1,
                monthLabel = "Month ${i + 1}",
                revenue = hpRevenues[i],
                expenses = hpExpenses[i],
                newCustomers = 35 + (i * 2),
                churnedCustomers = 6
            )
        }
        dao.insertMetrics(hpMetrics)
    }

    // Profiles
    fun getAllProfilesFlow(): Flow<List<BusinessProfileEntity>> = dao.getAllProfilesFlow()
    fun getProfileFlow(id: Long): Flow<BusinessProfileEntity?> = dao.getProfileByIdFlow(id)

    suspend fun createProfile(profile: BusinessProfileEntity): Long = withContext(Dispatchers.IO) {
        val id = dao.insertProfile(profile)
        // Insert baseline 3 months
        val rev = profile.monthlyRevenue
        val exp = profile.monthlyExpenses
        val metrics = listOf(
            FinancialMetricEntity(businessId = id, monthIndex = 1, monthLabel = "M-2", revenue = rev * 0.85, expenses = exp * 0.9, newCustomers = 20, churnedCustomers = 2),
            FinancialMetricEntity(businessId = id, monthIndex = 2, monthLabel = "M-1", revenue = rev * 0.92, expenses = exp * 0.95, newCustomers = 25, churnedCustomers = 3),
            FinancialMetricEntity(businessId = id, monthIndex = 3, monthLabel = "Current", revenue = rev, expenses = exp, newCustomers = 30, churnedCustomers = 3)
        )
        dao.insertMetrics(metrics)
        id
    }

    suspend fun updateProfile(profile: BusinessProfileEntity) = withContext(Dispatchers.IO) {
        dao.updateProfile(profile)
    }

    // Metrics & ML Computations
    fun getMetricsFlow(businessId: Long): Flow<List<FinancialMetricEntity>> = dao.getMetricsForBusinessFlow(businessId)

    suspend fun computeRevenueForecast(businessId: Long): RevenueForecastResult = withContext(Dispatchers.IO) {
        val metrics = dao.getMetricsForBusinessList(businessId)
        val revenues = metrics.map { it.revenue }
        BusinessMlEngine.forecastRevenue(revenues)
    }

    fun computeChurnPrediction(profile: BusinessProfileEntity): ChurnPredictionResult {
        return BusinessMlEngine.predictChurnRisk(
            ChurnPredictionInput(
                customerCount = profile.customerCount,
                monthlyRecurringRevenue = profile.monthlyRevenue,
                npsScore = profile.npsScore,
                usageDropPercent = 14.5,
                supportTicketsPerCustomer = 0.8,
                avgContractLengthMonths = if (profile.industry.contains("SaaS")) 12 else 1
            )
        )
    }

    fun computeFinancialDiagnostics(profile: BusinessProfileEntity): FinancialDiagnosticsResult {
        val burnRate = maxOf(0.0, profile.monthlyExpenses - profile.monthlyRevenue)
        return BusinessMlEngine.evaluateFinancialDiagnostics(
            UnitEconomicsMetrics(
                cac = profile.cac,
                arpu = profile.arpu,
                grossMarginPercent = 75.0,
                monthlyChurnRatePercent = 2.2,
                cashBalance = profile.cashBalance,
                monthlyBurnRate = burnRate,
                fixedCostsMonthly = profile.monthlyExpenses * 0.65,
                pricePerUnit = profile.arpu,
                variableCostPerUnit = profile.arpu * 0.25
            )
        )
    }

    // Consulting Chat & Generative AI
    fun getMessagesFlow(businessId: Long): Flow<List<ConsultingMessageEntity>> = dao.getMessagesForBusinessFlow(businessId)

    suspend fun sendConsultingQuery(
        businessId: Long,
        userQuery: String,
        domain: String = "STRATEGY"
    ): ConsultingMessageEntity = withContext(Dispatchers.IO) {
        val profile = dao.getProfileById(businessId)
        val context = if (profile != null) {
            "Company: ${profile.name}, Industry: ${profile.industry}, Stage: ${profile.stage}, MRR: $${profile.monthlyRevenue}, Burn: $${profile.monthlyExpenses}, Target: ${profile.targetMarket}, Goal: ${profile.primaryGoal}"
        } else "General Startup Consultation"

        // Insert User message
        dao.insertMessage(
            ConsultingMessageEntity(
                businessId = businessId,
                senderRole = "USER",
                messageText = userQuery,
                practiceDomain = domain
            )
        )

        // Call Gemini
        val reply = GeminiConsultantService.generateConsultingAdvice(context, userQuery, domain)

        // Insert Consultant message
        val consultantMsg = ConsultingMessageEntity(
            businessId = businessId,
            senderRole = "AI_CONSULTANT",
            messageText = reply.replyText,
            practiceDomain = domain,
            sourceBadge = reply.source
        )
        val id = dao.insertMessage(consultantMsg)
        consultantMsg.copy(id = id)
    }

    suspend fun clearChat(businessId: Long) = withContext(Dispatchers.IO) {
        dao.clearMessagesForBusiness(businessId)
    }

    // Strategic Plans
    fun getPlansFlow(businessId: Long): Flow<List<StrategicPlanEntity>> = dao.getPlansForBusinessFlow(businessId)

    suspend fun generateBusinessCanvas(profile: BusinessProfileEntity): Map<String, List<String>> = withContext(Dispatchers.IO) {
        GeminiConsultantService.generateBusinessModelCanvas(
            businessName = profile.name,
            industry = profile.industry,
            valueProposition = profile.valueProposition
        )
    }

    suspend fun generateSwot(profile: BusinessProfileEntity): Map<String, List<String>> = withContext(Dispatchers.IO) {
        GeminiConsultantService.generateSwotAnalysis(
            companyName = profile.name,
            industry = profile.industry
        )
    }
}
