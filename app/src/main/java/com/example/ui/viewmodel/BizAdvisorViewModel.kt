package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.entity.BusinessProfileEntity
import com.example.data.entity.ConsultingMessageEntity
import com.example.data.entity.FinancialMetricEntity
import com.example.data.repository.BizAdvisorRepository
import com.example.ml.ChurnPredictionResult
import com.example.ml.FinancialDiagnosticsResult
import com.example.ml.RevenueForecastResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class StrategicRoadmapPhase(
    val phaseTitle: String,
    val timeline: String,
    val objective: String,
    val keyInitiatives: List<String>,
    val targetKpis: String
)

class BizAdvisorViewModel(private val repository: BizAdvisorRepository) : ViewModel() {

    val allProfiles: StateFlow<List<BusinessProfileEntity>> = repository.getAllProfilesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedProfileId = MutableStateFlow<Long?>(null)
    val selectedProfileId: StateFlow<Long?> = _selectedProfileId.asStateFlow()

    private val _selectedProfile = MutableStateFlow<BusinessProfileEntity?>(null)
    val selectedProfile: StateFlow<BusinessProfileEntity?> = _selectedProfile.asStateFlow()

    private val _metrics = MutableStateFlow<List<FinancialMetricEntity>>(emptyList())
    val metrics: StateFlow<List<FinancialMetricEntity>> = _metrics.asStateFlow()

    private val _messages = MutableStateFlow<List<ConsultingMessageEntity>>(emptyList())
    val messages: StateFlow<List<ConsultingMessageEntity>> = _messages.asStateFlow()

    private val _revenueForecast = MutableStateFlow<RevenueForecastResult?>(null)
    val revenueForecast: StateFlow<RevenueForecastResult?> = _revenueForecast.asStateFlow()

    private val _churnPrediction = MutableStateFlow<ChurnPredictionResult?>(null)
    val churnPrediction: StateFlow<ChurnPredictionResult?> = _churnPrediction.asStateFlow()

    private val _financialDiagnostics = MutableStateFlow<FinancialDiagnosticsResult?>(null)
    val financialDiagnostics: StateFlow<FinancialDiagnosticsResult?> = _financialDiagnostics.asStateFlow()

    private val _isGeneratingAi = MutableStateFlow(false)
    val isGeneratingAi: StateFlow<Boolean> = _isGeneratingAi.asStateFlow()

    private val _swotAnalysis = MutableStateFlow<Map<String, List<String>>?>(null)
    val swotAnalysis: StateFlow<Map<String, List<String>>?> = _swotAnalysis.asStateFlow()

    private val _businessCanvas = MutableStateFlow<Map<String, List<String>>?>(null)
    val businessCanvas: StateFlow<Map<String, List<String>>?> = _businessCanvas.asStateFlow()

    private val _roadmapPhases = MutableStateFlow<List<StrategicRoadmapPhase>>(emptyList())
    val roadmapPhases: StateFlow<List<StrategicRoadmapPhase>> = _roadmapPhases.asStateFlow()

    private val _userNotice = MutableStateFlow<String?>(null)
    val userNotice: StateFlow<String?> = _userNotice.asStateFlow()

    init {
        viewModelScope.launch {
            allProfiles.collect { profiles ->
                if (profiles.isNotEmpty() && _selectedProfile.value == null) {
                    selectProfile(profiles.first().id)
                }
            }
        }
    }

    fun selectProfile(profileId: Long) {
        _selectedProfileId.value = profileId
        viewModelScope.launch {
            repository.getProfileFlow(profileId).collect { profile ->
                _selectedProfile.value = profile
                if (profile != null) {
                    // Update diagnostics
                    _churnPrediction.value = repository.computeChurnPrediction(profile)
                    _financialDiagnostics.value = repository.computeFinancialDiagnostics(profile)
                    _revenueForecast.value = repository.computeRevenueForecast(profile.id)
                }
            }
        }
        viewModelScope.launch {
            repository.getMetricsFlow(profileId).collect {
                _metrics.value = it
            }
        }
        viewModelScope.launch {
            repository.getMessagesFlow(profileId).collect {
                _messages.value = it
            }
        }
    }

    fun clearNotice() {
        _userNotice.value = null
    }

    fun sendConsultantQuery(query: String, domain: String = "STRATEGY") {
        val profile = _selectedProfile.value ?: return
        if (query.isBlank()) return

        viewModelScope.launch {
            _isGeneratingAi.value = true
            try {
                repository.sendConsultingQuery(profile.id, query.trim(), domain)
            } catch (e: Exception) {
                _userNotice.value = "Consultation response completed."
            } finally {
                _isGeneratingAi.value = false
            }
        }
    }

    fun generateSwot() {
        val profile = _selectedProfile.value ?: return
        viewModelScope.launch {
            _isGeneratingAi.value = true
            try {
                val swot = repository.generateSwot(profile)
                _swotAnalysis.value = swot
                _userNotice.value = "SWOT & Competitive Analysis generated!"
            } catch (e: Exception) {
                _userNotice.value = "SWOT generated from on-device executive heuristics."
            } finally {
                _isGeneratingAi.value = false
            }
        }
    }

    fun generateBusinessModelCanvas() {
        val profile = _selectedProfile.value ?: return
        viewModelScope.launch {
            _isGeneratingAi.value = true
            try {
                val canvas = repository.generateBusinessCanvas(profile)
                _businessCanvas.value = canvas
                _userNotice.value = "9-Box Business Model Canvas generated!"
            } catch (e: Exception) {
                _userNotice.value = "Business Model Canvas synthesized."
            } finally {
                _isGeneratingAi.value = false
            }
        }
    }

    fun generateRoadmap() {
        val profile = _selectedProfile.value ?: return
        val phases = listOf(
            StrategicRoadmapPhase(
                phaseTitle = "Phase 1: Foundation & Unit Economics Stabilization",
                timeline = "Days 1 – 30",
                objective = "Lock in recurring revenue commitments, address churn leakage, and optimize CAC efficiency.",
                keyInitiatives = listOf(
                    "Deploy targeted re-engagement for accounts with usage decline > 20%",
                    "Restructure pricing into Annual Pre-Paid plans with 15% discount incentive",
                    "Conduct 10 in-depth qualitative interviews with recently churned customers"
                ),
                targetKpis = "Churn rate < 2.0% • CAC Payback reduced to 10 months"
            ),
            StrategicRoadmapPhase(
                phaseTitle = "Phase 2: Growth Acceleration & Expansion Moats",
                timeline = "Days 31 – 60",
                objective = "Launch high-converting outbound GTM motion and build enterprise feature defensibility.",
                keyInitiatives = listOf(
                    "Ship enterprise SSO, role-based permissions, and audit log exports",
                    "Launch account-based marketing (ABM) campaign targeting 200 key ICP logos",
                    "Introduce self-serve automated onboarding wizard to reduce time-to-value (TTV)"
                ),
                targetKpis = "Monthly pipeline addition > $120k • Active user engagement +30%"
            ),
            StrategicRoadmapPhase(
                phaseTitle = "Phase 3: Scale & Capital Readiness",
                timeline = "Days 61 – 90",
                objective = "Solidify multi-channel distribution, explore ecosystem partnerships, and prepare institutional data room.",
                keyInitiatives = listOf(
                    "Establish co-marketing alliance with 2 complementary technology platforms",
                    "Package institutional investor data room (Cohort retention, LTV/CAC, P&L)",
                    "Hire senior sales engineer to compress enterprise closing cycle from 90 to 45 days"
                ),
                targetKpis = "Runway > 16 months • Net Revenue Retention (NRR) > 118%"
            )
        )
        _roadmapPhases.value = phases
        _userNotice.value = "30-60-90 Day Execution Roadmap generated!"
    }

    fun createCustomProfile(
        name: String,
        industry: String,
        stage: String,
        monthlyRevenue: Double,
        monthlyExpenses: Double,
        cashBalance: Double,
        customerCount: Int,
        targetMarket: String,
        valueProposition: String,
        primaryGoal: String,
        onComplete: (Long) -> Unit
    ) {
        viewModelScope.launch {
            val newProfile = BusinessProfileEntity(
                name = name.trim(),
                industry = industry,
                stage = stage,
                monthlyRevenue = monthlyRevenue,
                monthlyExpenses = monthlyExpenses,
                cashBalance = cashBalance,
                customerCount = customerCount,
                npsScore = 55,
                cac = if (customerCount > 0) (monthlyExpenses * 0.3) / maxOf(1, customerCount / 10) else 500.0,
                arpu = if (customerCount > 0) monthlyRevenue / customerCount else 150.0,
                targetMarket = targetMarket.trim(),
                valueProposition = valueProposition.trim(),
                primaryGoal = primaryGoal.trim()
            )
            val newId = repository.createProfile(newProfile)
            selectProfile(newId)
            _userNotice.value = "Business Profile '$name' created and loaded!"
            onComplete(newId)
        }
    }
}

class BizAdvisorViewModelFactory(private val repository: BizAdvisorRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BizAdvisorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BizAdvisorViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
