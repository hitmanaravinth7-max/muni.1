package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.EmptyState
import com.example.ui.screens.BloodBanksScreen
import com.example.ui.screens.DonorAlertsScreen
import com.example.ui.screens.DonorProfileScreen
import com.example.ui.screens.EmergencyRequestScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.HowItWorksScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MyRequestsScreen
import com.example.ui.screens.RegisterScreen
import com.example.ui.screens.SearchDonorsScreen
import com.example.ui.screens.UserDashboardScreen
import com.example.ui.screens.admin.AdminActivityLogScreen
import com.example.ui.screens.admin.AdminBloodBanksScreen
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.screens.admin.AdminDonorsScreen
import com.example.ui.screens.admin.AdminRequestsScreen
import com.example.ui.screens.admin.AdminUsersScreen
import com.example.ui.theme.BloodBridgeTheme
import com.example.ui.theme.BloodRed
import com.example.ui.theme.BloodRedContainer
import com.example.ui.theme.MedicalGreen
import com.example.ui.theme.MedicalNavy
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.BloodBridgeViewModel
import com.example.ui.viewmodel.BloodBridgeViewModelFactory
import kotlinx.coroutines.launch

enum class AppScreen(val route: String, val title: String) {
    HOME("/", "BloodBridge"),
    SEARCH_DONORS("/search-donors", "Find Donors"),
    BLOOD_BANKS("/blood-banks", "Blood Centers"),
    HOW_IT_WORKS("/how-it-works", "How It Works"),
    LOGIN("/login", "Sign In"),
    REGISTER("/register", "Register"),
    USER_DASHBOARD("/dashboard", "Dashboard"),
    DONOR_PROFILE("/donor-profile", "Donor Profile"),
    NEW_REQUEST("/requests/new", "Emergency Request"),
    MY_REQUESTS("/requests", "My Requests"),
    DONOR_ALERTS("/alerts", "Donor Alerts"),
    ADMIN_DASHBOARD("/admin/dashboard", "Admin Dashboard"),
    ADMIN_USERS("/admin/users", "Manage Users"),
    ADMIN_DONORS("/admin/donors", "Manage Donors"),
    ADMIN_BLOOD_BANKS("/admin/blood-banks", "Manage Blood Banks"),
    ADMIN_REQUESTS("/admin/requests", "Manage Requests"),
    ADMIN_ACTIVITY_LOG("/admin/activity-log", "Activity Log"),
    NOT_FOUND("/404", "Page Not Found")
}

class MainActivity : ComponentActivity() {

    private val viewModel: BloodBridgeViewModel by viewModels {
        val app = application as BloodBridgeApp
        BloodBridgeViewModelFactory(app.repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BloodBridgeTheme {
                BloodBridgeAppContent(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BloodBridgeAppContent(viewModel: BloodBridgeViewModel) {
    val authState by viewModel.authState.collectAsState()
    val userMessage by viewModel.userMessage.collectAsState()
    val donorAlerts by viewModel.donorAlerts.collectAsState()
    val pendingAlertsCount = donorAlerts.count { it.alert.status == "PENDING" }

    val screenStack = remember { mutableStateListOf(AppScreen.HOME) }
    val currentScreen = screenStack.lastOrNull() ?: AppScreen.HOME

    var initialDonorSearchGroup by remember { mutableStateOf<String?>(null) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Helper navigation functions
    fun navigateTo(screen: AppScreen) {
        // Route protection
        if (!authState.isLoggedIn) {
            val protectedScreens = setOf(
                AppScreen.USER_DASHBOARD,
                AppScreen.DONOR_PROFILE,
                AppScreen.NEW_REQUEST,
                AppScreen.MY_REQUESTS,
                AppScreen.DONOR_ALERTS,
                AppScreen.ADMIN_DASHBOARD,
                AppScreen.ADMIN_USERS,
                AppScreen.ADMIN_DONORS,
                AppScreen.ADMIN_BLOOD_BANKS,
                AppScreen.ADMIN_REQUESTS,
                AppScreen.ADMIN_ACTIVITY_LOG
            )
            if (screen in protectedScreens) {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Please sign in to access this feature.")
                }
                screenStack.add(AppScreen.LOGIN)
                return
            }
        } else if (!authState.isAdmin) {
            val adminScreens = setOf(
                AppScreen.ADMIN_DASHBOARD,
                AppScreen.ADMIN_USERS,
                AppScreen.ADMIN_DONORS,
                AppScreen.ADMIN_BLOOD_BANKS,
                AppScreen.ADMIN_REQUESTS,
                AppScreen.ADMIN_ACTIVITY_LOG
            )
            if (screen in adminScreens) {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Access Denied: Administrator role required.")
                }
                screenStack.add(AppScreen.USER_DASHBOARD)
                return
            }
        }

        if (screen == AppScreen.HOME) {
            screenStack.clear()
            screenStack.add(AppScreen.HOME)
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

    // Back handler for Android system back button
    BackHandler(enabled = screenStack.size > 1) {
        navigateBack()
    }

    // Display user messages via snackbar
    LaunchedEffect(userMessage) {
        userMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearUserMessage()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .width(300.dp)
                    .windowInsetsPadding(WindowInsets.safeDrawing)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(BloodRedContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Favorite, contentDescription = null, tint = BloodRed, modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("BloodBridge", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = BloodRed)
                            Text("Emergency Network", fontSize = 11.sp, color = TextMuted)
                        }
                    }

                    if (authState.isLoggedIn) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Signed in as: ${authState.currentUser?.fullName ?: ""}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Role: ${authState.currentUser?.role}",
                            fontSize = 11.sp,
                            color = if (authState.isAdmin) BloodRed else MedicalGreen
                        )
                    }
                }

                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))

                // Public items
                NavigationDrawerItem(
                    label = { Text("Home") },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    selected = currentScreen == AppScreen.HOME,
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        navigateTo(AppScreen.HOME)
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text("Search Donors") },
                    icon = { Icon(Icons.Default.Search, contentDescription = null) },
                    selected = currentScreen == AppScreen.SEARCH_DONORS,
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        initialDonorSearchGroup = null
                        navigateTo(AppScreen.SEARCH_DONORS)
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text("Blood Centers Directory") },
                    icon = { Icon(Icons.Default.LocalHospital, contentDescription = null) },
                    selected = currentScreen == AppScreen.BLOOD_BANKS,
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        navigateTo(AppScreen.BLOOD_BANKS)
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text("How It Works & Criteria") },
                    icon = { Icon(Icons.Default.Info, contentDescription = null) },
                    selected = currentScreen == AppScreen.HOW_IT_WORKS,
                    onClick = {
                        coroutineScope.launch { drawerState.close() }
                        navigateTo(AppScreen.HOW_IT_WORKS)
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                if (authState.isLoggedIn && !authState.isAdmin) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Text("Donor & Requests", fontSize = 11.sp, color = TextMuted, modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))

                    NavigationDrawerItem(
                        label = { Text("Dashboard") },
                        icon = { Icon(Icons.Default.Dashboard, contentDescription = null) },
                        selected = currentScreen == AppScreen.USER_DASHBOARD,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navigateTo(AppScreen.USER_DASHBOARD)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Become / Edit Donor Profile") },
                        icon = { Icon(Icons.Default.Person, contentDescription = null) },
                        selected = currentScreen == AppScreen.DONOR_PROFILE,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navigateTo(AppScreen.DONOR_PROFILE)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Submit Emergency Request") },
                        icon = { Icon(Icons.Default.AddAlert, contentDescription = null) },
                        selected = currentScreen == AppScreen.NEW_REQUEST,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navigateTo(AppScreen.NEW_REQUEST)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("My Requests") },
                        icon = { Icon(Icons.Default.Bloodtype, contentDescription = null) },
                        selected = currentScreen == AppScreen.MY_REQUESTS,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navigateTo(AppScreen.MY_REQUESTS)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Donor Alerts") },
                        icon = {
                            BadgedBox(badge = {
                                if (pendingAlertsCount > 0) {
                                    Badge { Text(pendingAlertsCount.toString()) }
                                }
                            }) {
                                Icon(Icons.Default.Notifications, contentDescription = null)
                            }
                        },
                        selected = currentScreen == AppScreen.DONOR_ALERTS,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navigateTo(AppScreen.DONOR_ALERTS)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }

                if (authState.isAdmin) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Text("Administration", fontSize = 11.sp, color = BloodRed, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))

                    NavigationDrawerItem(
                        label = { Text("Admin Dashboard") },
                        icon = { Icon(Icons.Default.Shield, contentDescription = null) },
                        selected = currentScreen == AppScreen.ADMIN_DASHBOARD,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navigateTo(AppScreen.ADMIN_DASHBOARD)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Manage Requests") },
                        icon = { Icon(Icons.Default.Bloodtype, contentDescription = null) },
                        selected = currentScreen == AppScreen.ADMIN_REQUESTS,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navigateTo(AppScreen.ADMIN_REQUESTS)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Manage Donors") },
                        icon = { Icon(Icons.Default.VerifiedUser, contentDescription = null) },
                        selected = currentScreen == AppScreen.ADMIN_DONORS,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navigateTo(AppScreen.ADMIN_DONORS)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Blood Banks & Stock") },
                        icon = { Icon(Icons.Default.LocalHospital, contentDescription = null) },
                        selected = currentScreen == AppScreen.ADMIN_BLOOD_BANKS,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navigateTo(AppScreen.ADMIN_BLOOD_BANKS)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("User Management") },
                        icon = { Icon(Icons.Default.People, contentDescription = null) },
                        selected = currentScreen == AppScreen.ADMIN_USERS,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navigateTo(AppScreen.ADMIN_USERS)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Audit Activity Log") },
                        icon = { Icon(Icons.Default.History, contentDescription = null) },
                        selected = currentScreen == AppScreen.ADMIN_ACTIVITY_LOG,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navigateTo(AppScreen.ADMIN_ACTIVITY_LOG)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                HorizontalDivider()
                if (authState.isLoggedIn) {
                    NavigationDrawerItem(
                        label = { Text("Sign Out (${authState.currentUser?.fullName})") },
                        icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null) },
                        selected = false,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            viewModel.logout()
                            navigateTo(AppScreen.HOME)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                } else {
                    NavigationDrawerItem(
                        label = { Text("Sign In / Register") },
                        icon = { Icon(Icons.Default.Person, contentDescription = null) },
                        selected = currentScreen == AppScreen.LOGIN,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navigateTo(AppScreen.LOGIN)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { navigateTo(AppScreen.HOME) }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(BloodRed),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Favorite, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "BloodBridge",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                color = BloodRed
                            )
                        }
                    },
                    navigationIcon = {
                        if (screenStack.size > 1) {
                            IconButton(onClick = { navigateBack() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                            }
                        } else {
                            IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        }
                    },
                    actions = {
                        if (authState.isLoggedIn) {
                            if (authState.isAdmin) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(BloodRed)
                                        .clickable { navigateTo(AppScreen.ADMIN_DASHBOARD) }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text("ADMIN", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            IconButton(
                                onClick = {
                                    if (authState.isAdmin) navigateTo(AppScreen.ADMIN_DASHBOARD)
                                    else navigateTo(AppScreen.USER_DASHBOARD)
                                }
                            ) {
                                Icon(Icons.Default.Person, contentDescription = "Profile", tint = BloodRed)
                            }
                        } else {
                            Button(
                                onClick = { navigateTo(AppScreen.LOGIN) },
                                colors = ButtonDefaults.buttonColors(containerColor = BloodRed),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(34.dp).padding(end = 4.dp)
                            ) {
                                Text("Sign In", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
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
                        selected = currentScreen == AppScreen.HOME,
                        onClick = { navigateTo(AppScreen.HOME) },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BloodRed,
                            selectedTextColor = BloodRed,
                            indicatorColor = BloodRedContainer
                        )
                    )

                    NavigationBarItem(
                        selected = currentScreen == AppScreen.SEARCH_DONORS,
                        onClick = {
                            initialDonorSearchGroup = null
                            navigateTo(AppScreen.SEARCH_DONORS)
                        },
                        icon = { Icon(Icons.Default.Search, contentDescription = "Donors") },
                        label = { Text("Donors", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BloodRed,
                            selectedTextColor = BloodRed,
                            indicatorColor = BloodRedContainer
                        )
                    )

                    NavigationBarItem(
                        selected = currentScreen == AppScreen.BLOOD_BANKS,
                        onClick = { navigateTo(AppScreen.BLOOD_BANKS) },
                        icon = { Icon(Icons.Default.LocalHospital, contentDescription = "Banks") },
                        label = { Text("Banks", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BloodRed,
                            selectedTextColor = BloodRed,
                            indicatorColor = BloodRedContainer
                        )
                    )

                    if (authState.isAdmin) {
                        NavigationBarItem(
                            selected = currentScreen in setOf(AppScreen.ADMIN_DASHBOARD, AppScreen.ADMIN_USERS, AppScreen.ADMIN_DONORS, AppScreen.ADMIN_BLOOD_BANKS, AppScreen.ADMIN_REQUESTS, AppScreen.ADMIN_ACTIVITY_LOG),
                            onClick = { navigateTo(AppScreen.ADMIN_DASHBOARD) },
                            icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin") },
                            label = { Text("Admin", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BloodRed,
                                selectedTextColor = BloodRed,
                                indicatorColor = BloodRedContainer
                            )
                        )
                    } else if (authState.isLoggedIn) {
                        NavigationBarItem(
                            selected = currentScreen == AppScreen.USER_DASHBOARD,
                            onClick = { navigateTo(AppScreen.USER_DASHBOARD) },
                            icon = {
                                BadgedBox(badge = {
                                    if (pendingAlertsCount > 0) {
                                        Badge { Text(pendingAlertsCount.toString()) }
                                    }
                                }) {
                                    Icon(Icons.Default.Dashboard, contentDescription = "Dashboard")
                                }
                            },
                            label = { Text("Dashboard", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BloodRed,
                                selectedTextColor = BloodRed,
                                indicatorColor = BloodRedContainer
                            )
                        )
                    } else {
                        NavigationBarItem(
                            selected = currentScreen == AppScreen.LOGIN || currentScreen == AppScreen.REGISTER,
                            onClick = { navigateTo(AppScreen.LOGIN) },
                            icon = { Icon(Icons.Default.Person, contentDescription = "Sign In") },
                            label = { Text("Sign In", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BloodRed,
                                selectedTextColor = BloodRed,
                                indicatorColor = BloodRedContainer
                            )
                        )
                    }
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (currentScreen) {
                    AppScreen.HOME -> HomeScreen(
                        viewModel = viewModel,
                        onNavigateToSearchDonors = { bg ->
                            initialDonorSearchGroup = bg
                            navigateTo(AppScreen.SEARCH_DONORS)
                        },
                        onNavigateToBecomeDonor = {
                            if (authState.isLoggedIn) navigateTo(AppScreen.DONOR_PROFILE)
                            else navigateTo(AppScreen.LOGIN)
                        },
                        onNavigateToBloodBanks = { navigateTo(AppScreen.BLOOD_BANKS) },
                        onNavigateToHowItWorks = { navigateTo(AppScreen.HOW_IT_WORKS) }
                    )

                    AppScreen.SEARCH_DONORS -> SearchDonorsScreen(
                        viewModel = viewModel,
                        initialBloodGroupFilter = initialDonorSearchGroup,
                        onNavigateToLogin = { navigateTo(AppScreen.LOGIN) }
                    )

                    AppScreen.BLOOD_BANKS -> BloodBanksScreen(
                        viewModel = viewModel
                    )

                    AppScreen.HOW_IT_WORKS -> HowItWorksScreen()

                    AppScreen.LOGIN -> LoginScreen(
                        viewModel = viewModel,
                        onLoginSuccess = { isAdmin ->
                            if (isAdmin) {
                                screenStack.clear()
                                screenStack.add(AppScreen.ADMIN_DASHBOARD)
                            } else {
                                screenStack.clear()
                                screenStack.add(AppScreen.USER_DASHBOARD)
                            }
                        },
                        onNavigateToRegister = { navigateTo(AppScreen.REGISTER) }
                    )

                    AppScreen.REGISTER -> RegisterScreen(
                        viewModel = viewModel,
                        onNavigateToLogin = { navigateTo(AppScreen.LOGIN) }
                    )

                    AppScreen.USER_DASHBOARD -> UserDashboardScreen(
                        viewModel = viewModel,
                        onNavigateToBecomeDonor = { navigateTo(AppScreen.DONOR_PROFILE) },
                        onNavigateToEmergencyRequest = { navigateTo(AppScreen.NEW_REQUEST) },
                        onNavigateToMyRequests = { navigateTo(AppScreen.MY_REQUESTS) },
                        onNavigateToAlerts = { navigateTo(AppScreen.DONOR_ALERTS) },
                        onNavigateToSearchDonors = {
                            initialDonorSearchGroup = null
                            navigateTo(AppScreen.SEARCH_DONORS)
                        }
                    )

                    AppScreen.DONOR_PROFILE -> DonorProfileScreen(
                        viewModel = viewModel,
                        onSaveSuccess = { navigateTo(AppScreen.USER_DASHBOARD) }
                    )

                    AppScreen.NEW_REQUEST -> EmergencyRequestScreen(
                        viewModel = viewModel,
                        onSubmitSuccess = { navigateTo(AppScreen.MY_REQUESTS) }
                    )

                    AppScreen.MY_REQUESTS -> MyRequestsScreen(
                        viewModel = viewModel,
                        onNavigateToNewRequest = { navigateTo(AppScreen.NEW_REQUEST) }
                    )

                    AppScreen.DONOR_ALERTS -> DonorAlertsScreen(
                        viewModel = viewModel,
                        onNavigateToBecomeDonor = { navigateTo(AppScreen.DONOR_PROFILE) }
                    )

                    AppScreen.ADMIN_DASHBOARD -> AdminDashboardScreen(
                        viewModel = viewModel,
                        onNavigateToUsers = { navigateTo(AppScreen.ADMIN_USERS) },
                        onNavigateToDonors = { navigateTo(AppScreen.ADMIN_DONORS) },
                        onNavigateToBloodBanks = { navigateTo(AppScreen.ADMIN_BLOOD_BANKS) },
                        onNavigateToRequests = { navigateTo(AppScreen.ADMIN_REQUESTS) },
                        onNavigateToActivityLog = { navigateTo(AppScreen.ADMIN_ACTIVITY_LOG) }
                    )

                    AppScreen.ADMIN_USERS -> AdminUsersScreen(viewModel = viewModel)
                    AppScreen.ADMIN_DONORS -> AdminDonorsScreen(viewModel = viewModel)
                    AppScreen.ADMIN_BLOOD_BANKS -> AdminBloodBanksScreen(viewModel = viewModel)
                    AppScreen.ADMIN_REQUESTS -> AdminRequestsScreen(viewModel = viewModel)
                    AppScreen.ADMIN_ACTIVITY_LOG -> AdminActivityLogScreen(viewModel = viewModel)

                    AppScreen.NOT_FOUND -> EmptyState(
                        title = "404 – Page Not Found",
                        message = "The requested screen could not be located in BloodBridge.",
                        actionButtonText = "Back to Home",
                        onActionClick = { navigateTo(AppScreen.HOME) }
                    )
                }
            }
        }
    }
}
