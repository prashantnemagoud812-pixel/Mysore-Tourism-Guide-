package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.AppDatabase
import com.example.data.repository.MysoreRepository
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MysoreViewModel
import com.example.ui.viewmodel.MysoreViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MysoreExplorerTheme {
                val context = LocalContext.current
                val database = remember { AppDatabase.getDatabase(context) }
                val repository = remember { MysoreRepository(database.mysoreDao()) }
                val viewModel: MysoreViewModel = viewModel(
                    factory = MysoreViewModelFactory(repository)
                )

                MysoreAppRoot(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MysoreAppRoot(viewModel: MysoreViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Display status messages (e.g., Bookmark saved, Trip saved)
    LaunchedEffect(uiState.statusMessage) {
        uiState.statusMessage?.let { msg ->
            snackbarHostState.showSnackbar(
                message = msg,
                duration = SnackbarDuration.Short
            )
            viewModel.clearStatusMessage()
        }
    }

    val currentScreen = uiState.currentScreen

    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        viewModel.navigateTo(AppScreen.HOME)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MidnightNavy,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .padding(bottom = 70.dp)
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) { data ->
                Snackbar(
                    containerColor = MidnightNavyCard,
                    contentColor = WarmIvory,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.border(1.dp, MysoreGold, RoundedCornerShape(12.dp))
                ) {
                    Text(
                        text = data.visuals.message,
                        fontWeight = FontWeight.SemiBold,
                        color = MysoreGoldLight
                    )
                }
            }
        },
        topBar = {
            // Floating Glass Top Header on Home screen
            if (currentScreen == AppScreen.HOME) {
                FloatingGlassTopBar(
                    onSearchClick = { viewModel.navigateTo(AppScreen.EXPLORE) },
                    onPlanClick = { viewModel.navigateTo(AppScreen.PLANNER) }
                )
            }
        },
        bottomBar = {
            // Bottom Navigation Bar (Hidden when on full detail screen for full-screen immersion)
            if (currentScreen != AppScreen.DETAIL) {
                MysoreBottomNavigation(
                    currentScreen = currentScreen,
                    onScreenSelected = { screen -> viewModel.navigateTo(screen) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                AppScreen.HOME -> HomeScreen(
                    viewModel = viewModel,
                    onNavigate = { screen -> viewModel.navigateTo(screen) },
                    onAttractionClick = { attraction -> viewModel.selectAttraction(attraction) }
                )
                AppScreen.EXPLORE -> ExploreScreen(
                    viewModel = viewModel,
                    onAttractionClick = { attraction -> viewModel.selectAttraction(attraction) },
                    onBack = { viewModel.navigateTo(AppScreen.HOME) }
                )
                AppScreen.DETAIL -> {
                    uiState.selectedAttraction?.let { attraction ->
                        AttractionDetailScreen(
                            attraction = attraction,
                            viewModel = viewModel,
                            onBack = { viewModel.navigateTo(AppScreen.EXPLORE) },
                            onNavigate = { screen -> viewModel.navigateTo(screen) }
                        )
                    } ?: run {
                        HomeScreen(
                            viewModel = viewModel,
                            onNavigate = { screen -> viewModel.navigateTo(screen) },
                            onAttractionClick = { attraction -> viewModel.selectAttraction(attraction) }
                        )
                    }
                }
                AppScreen.PLANNER -> TripPlannerScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(AppScreen.HOME) },
                    onNavigate = { screen -> viewModel.navigateTo(screen) }
                )
                AppScreen.FOOD -> TasteMysoreScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(AppScreen.HOME) },
                    onNavigate = { screen -> viewModel.navigateTo(screen) }
                )
                AppScreen.SAVED -> SavedTripsScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(AppScreen.HOME) },
                    onAttractionClick = { attraction -> viewModel.selectAttraction(attraction) },
                    onNavigate = { screen -> viewModel.navigateTo(screen) }
                )
                AppScreen.MAP -> MapScreen(
                    viewModel = viewModel,
                    onAttractionClick = { attraction -> viewModel.selectAttraction(attraction) },
                    onBack = { viewModel.navigateTo(AppScreen.HOME) }
                )
            }
        }
    }
}

@Composable
fun FloatingGlassTopBar(
    onSearchClick: () -> Unit,
    onPlanClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.statusBars),
        color = MidnightNavyLight.copy(alpha = 0.95f),
        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorderGold.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo & Subtitle
            Column {
                Text(
                    text = "👑  MYSORE",
                    style = MaterialTheme.typography.titleLarge,
                    color = MysoreGold,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "EXPLORE • EXPERIENCE • REMEMBER",
                    color = WarmIvoryMuted,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            // Right side actions
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onSearchClick,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0x33FFD700))
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MysoreGold,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Button(
                    onClick = onPlanClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MysoreGold,
                        contentColor = MidnightNavy
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("top_plan_my_trip_btn")
                ) {
                    Text(
                        text = "Plan Trip",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun MysoreBottomNavigation(
    currentScreen: AppScreen,
    onScreenSelected: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars),
        color = MidnightNavyLight.copy(alpha = 0.98f),
        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorderGold)
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            contentColor = WarmIvory,
            modifier = Modifier.height(64.dp)
        ) {
            NavigationBarItem(
                selected = currentScreen == AppScreen.HOME,
                onClick = { onScreenSelected(AppScreen.HOME) },
                icon = {
                    Icon(
                        imageVector = if (currentScreen == AppScreen.HOME) Icons.Default.Home else Icons.Default.HomeMini,
                        contentDescription = "Home"
                    )
                },
                label = { Text("Home", fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MidnightNavy,
                    selectedTextColor = MysoreGold,
                    indicatorColor = MysoreGold,
                    unselectedIconColor = WarmIvoryMuted,
                    unselectedTextColor = WarmIvoryMuted
                )
            )

            NavigationBarItem(
                selected = currentScreen == AppScreen.EXPLORE,
                onClick = { onScreenSelected(AppScreen.EXPLORE) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Explore,
                        contentDescription = "Explore"
                    )
                },
                label = { Text("Explore", fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MidnightNavy,
                    selectedTextColor = MysoreGold,
                    indicatorColor = MysoreGold,
                    unselectedIconColor = WarmIvoryMuted,
                    unselectedTextColor = WarmIvoryMuted
                )
            )

            NavigationBarItem(
                selected = currentScreen == AppScreen.PLANNER,
                onClick = { onScreenSelected(AppScreen.PLANNER) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Plan"
                    )
                },
                label = { Text("AI Trip", fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MidnightNavy,
                    selectedTextColor = MysoreGold,
                    indicatorColor = MysoreGold,
                    unselectedIconColor = WarmIvoryMuted,
                    unselectedTextColor = WarmIvoryMuted
                )
            )

            NavigationBarItem(
                selected = currentScreen == AppScreen.FOOD,
                onClick = { onScreenSelected(AppScreen.FOOD) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Restaurant,
                        contentDescription = "Food"
                    )
                },
                label = { Text("Taste", fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MidnightNavy,
                    selectedTextColor = MysoreGold,
                    indicatorColor = MysoreGold,
                    unselectedIconColor = WarmIvoryMuted,
                    unselectedTextColor = WarmIvoryMuted
                )
            )

            NavigationBarItem(
                selected = currentScreen == AppScreen.SAVED,
                onClick = { onScreenSelected(AppScreen.SAVED) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = "Saved"
                    )
                },
                label = { Text("My Trips", fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MidnightNavy,
                    selectedTextColor = MysoreGold,
                    indicatorColor = MysoreGold,
                    unselectedIconColor = WarmIvoryMuted,
                    unselectedTextColor = WarmIvoryMuted
                )
            )
        }
    }
}
