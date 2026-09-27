package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import com.example.ui.components.NewBusinessProfileDialog
import com.example.ui.screens.AiAdvisorChatScreen
import com.example.ui.screens.ExecutiveDashboardScreen
import com.example.ui.screens.MlDiagnosticsScreen
import com.example.ui.screens.StrategicToolsScreen
import com.example.ui.theme.BizAdvisorTheme
import com.example.ui.theme.BizBlue
import com.example.ui.theme.BizBlueContainer
import com.example.ui.theme.BizBluePrimary
import com.example.ui.theme.BizNavy
import com.example.ui.theme.BizTextMuted
import com.example.ui.theme.BizTextPrimary
import com.example.ui.theme.BizTextSecondary
import com.example.ui.viewmodel.BizAdvisorViewModel
import com.example.ui.viewmodel.BizAdvisorViewModelFactory

enum class BizScreen(val title: String) {
    DASHBOARD("Executive Dashboard"),
    ML_DIAGNOSTICS("ML Analytics & Forecasting"),
    AI_ADVISOR_CHAT("AI Strategic Consultant"),
    STRATEGIC_TOOLS("Strategic Models & Roadmaps")
}

class MainActivity : ComponentActivity() {

    private val viewModel: BizAdvisorViewModel by viewModels {
        val app = application as BloodBridgeApp
        BizAdvisorViewModelFactory(app.bizAdvisorRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BizAdvisorTheme {
                BizAdvisorAppContent(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BizAdvisorAppContent(viewModel: BizAdvisorViewModel) {
    val currentProfile by viewModel.selectedProfile.collectAsState()
    val userNotice by viewModel.userNotice.collectAsState()

    val screenStack = remember { mutableStateListOf(BizScreen.DASHBOARD) }
    val currentScreen = screenStack.lastOrNull() ?: BizScreen.DASHBOARD

    var chatInitialDomain by remember { mutableStateOf("STRATEGY") }
    var strategicToolInitialTab by remember { mutableIntStateOf(0) }

    var showNewProfileDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    fun navigateTo(screen: BizScreen) {
        if (screen == BizScreen.DASHBOARD) {
            screenStack.clear()
            screenStack.add(BizScreen.DASHBOARD)
        } else {
            if (screenStack.lastOrNull() != screen) {
                screenStack.add(screen)
            }
        }
    }

    fun navigateBack() {
        if (screenStack.size > 1) {
            screenStack.removeAt(screenStack.size - 1)
        }
    }

    BackHandler(enabled = screenStack.size > 1) {
        navigateBack()
    }

    LaunchedEffect(userNotice) {
        userNotice?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearNotice()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { navigateTo(BizScreen.DASHBOARD) }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(BizNavy),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "BizAdvisor AI",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 17.sp,
                                color = BizNavy
                            )
                            currentProfile?.let {
                                Text(
                                    text = "${it.name} • ${it.industry}",
                                    fontSize = 10.sp,
                                    color = BizTextMuted
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    if (screenStack.size > 1) {
                        IconButton(onClick = { navigateBack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { showAboutDialog = true }) {
                        Icon(Icons.Default.Info, contentDescription = "About", tint = BizNavy)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
                containerColor = Color.White
            ) {
                NavigationBarItem(
                    selected = currentScreen == BizScreen.DASHBOARD,
                    onClick = { navigateTo(BizScreen.DASHBOARD) },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                    label = { Text("Overview", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BizBluePrimary,
                        selectedTextColor = BizBluePrimary,
                        indicatorColor = BizBlueContainer
                    )
                )

                NavigationBarItem(
                    selected = currentScreen == BizScreen.ML_DIAGNOSTICS,
                    onClick = { navigateTo(BizScreen.ML_DIAGNOSTICS) },
                    icon = { Icon(Icons.Default.QueryStats, contentDescription = "ML Engine") },
                    label = { Text("ML Engine", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BizBluePrimary,
                        selectedTextColor = BizBluePrimary,
                        indicatorColor = BizBlueContainer
                    )
                )

                NavigationBarItem(
                    selected = currentScreen == BizScreen.AI_ADVISOR_CHAT,
                    onClick = {
                        chatInitialDomain = "STRATEGY"
                        navigateTo(BizScreen.AI_ADVISOR_CHAT)
                    },
                    icon = { Icon(Icons.Default.SmartToy, contentDescription = "AI Advisor") },
                    label = { Text("AI Advisor", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BizBluePrimary,
                        selectedTextColor = BizBluePrimary,
                        indicatorColor = BizBlueContainer
                    )
                )

                NavigationBarItem(
                    selected = currentScreen == BizScreen.STRATEGIC_TOOLS,
                    onClick = {
                        strategicToolInitialTab = 0
                        navigateTo(BizScreen.STRATEGIC_TOOLS)
                    },
                    icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "Strategy") },
                    label = { Text("Strategy", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BizBluePrimary,
                        selectedTextColor = BizBluePrimary,
                        indicatorColor = BizBlueContainer
                    )
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                BizScreen.DASHBOARD -> ExecutiveDashboardScreen(
                    viewModel = viewModel,
                    onNavigateToChat = { domain ->
                        chatInitialDomain = domain
                        navigateTo(BizScreen.AI_ADVISOR_CHAT)
                    },
                    onNavigateToMlDiagnostics = { navigateTo(BizScreen.ML_DIAGNOSTICS) },
                    onNavigateToStrategicTools = { toolTab ->
                        strategicToolInitialTab = toolTab
                        navigateTo(BizScreen.STRATEGIC_TOOLS)
                    },
                    onShowNewProfileDialog = { showNewProfileDialog = true }
                )

                BizScreen.ML_DIAGNOSTICS -> MlDiagnosticsScreen(
                    viewModel = viewModel
                )

                BizScreen.AI_ADVISOR_CHAT -> AiAdvisorChatScreen(
                    viewModel = viewModel,
                    initialDomain = chatInitialDomain
                )

                BizScreen.STRATEGIC_TOOLS -> StrategicToolsScreen(
                    viewModel = viewModel,
                    initialTab = strategicToolInitialTab
                )
            }
        }
    }

    if (showNewProfileDialog) {
        NewBusinessProfileDialog(
            onDismiss = { showNewProfileDialog = false },
            onSubmit = { name, industry, stage, rev, exp, cash, cust, targetMarket, valueProp, goal ->
                viewModel.createCustomProfile(
                    name = name,
                    industry = industry,
                    stage = stage,
                    monthlyRevenue = rev,
                    monthlyExpenses = exp,
                    cashBalance = cash,
                    customerCount = cust,
                    targetMarket = targetMarket,
                    valueProposition = valueProp,
                    primaryGoal = goal
                ) {
                    showNewProfileDialog = false
                    navigateTo(BizScreen.DASHBOARD)
                }
            }
        )
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text("About BizAdvisor AI", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        "BizAdvisor AI is an executive management consultant and strategic intelligence platform fusing Predictive Machine Learning and Gemini Generative AI.",
                        fontSize = 13.sp,
                        color = BizTextPrimary,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "• Machine Learning Engine: Ordinary Least Squares (OLS) time-series revenue regression, logistic churn risk classifier, and unit economics diagnostic.\n• Generative AI: Strategic moats, 9-box Business Model Canvas, automated SWOT, and 30-60-90 day execution roadmaps.",
                        fontSize = 11.sp,
                        color = BizTextSecondary,
                        lineHeight = 16.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}
