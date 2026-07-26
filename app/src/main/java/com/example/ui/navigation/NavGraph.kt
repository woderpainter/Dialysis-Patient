package com.example.ui.navigation

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.language.AppLanguage
import com.example.ui.language.LanguageManager
import com.example.ui.screens.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.text.font.FontWeight
import com.example.ui.theme.DialysisBlueLight
import com.example.ui.theme.DialysisBluePrimary
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.MedicalSurfaceLight
import com.example.ui.theme.MedicalTextPrimary
import com.example.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

sealed class Screen(val route: String, val titleKey: String, val icon: ImageVector) {
    object Home : Screen("home", "home", Icons.Default.Home)
    object Profile : Screen("profile", "profile", Icons.Default.Person)
    object Passport : Screen("passport", "passport", Icons.Default.MedicalServices)
    object Sessions : Screen("sessions", "sessions", Icons.Default.EventNote)
    object Monitoring : Screen("monitoring", "monitoring", Icons.Default.Favorite)
    object Labs : Screen("labs", "labs", Icons.Default.Science)
    object Meds : Screen("meds", "meds", Icons.Default.Medication)
    object Nutrition : Screen("nutrition", "nutrition", Icons.Default.WaterDrop)
    object Docs : Screen("docs", "docs", Icons.Default.FolderSpecial)
    object Emergency : Screen("emergency", "emergency_card_title", Icons.Default.Warning)
    object QrTransfer : Screen("qr_transfer", "qr_transfer", Icons.Default.QrCode2)
    object AiAssistant : Screen("ai_assistant", "ai_assistant", Icons.Default.AutoAwesome)
    object Security : Screen("security", "security", Icons.Default.Shield)
}

val mainNavItems = listOf(
    Screen.Home,
    Screen.Passport,
    Screen.Sessions,
    Screen.Monitoring,
    Screen.Labs,
    Screen.AiAssistant
)

val drawerNavItems = listOf(
    Screen.Home,
    Screen.Profile,
    Screen.Passport,
    Screen.Sessions,
    Screen.Monitoring,
    Screen.Labs,
    Screen.Meds,
    Screen.Nutrition,
    Screen.Docs,
    Screen.Emergency,
    Screen.QrTransfer,
    Screen.AiAssistant,
    Screen.Security
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val isLocked by viewModel.isLocked.collectAsState()
    val currentLanguage by viewModel.currentLanguage.collectAsState()

    if (isLocked) {
        LockScreenOverlay(onUnlock = { viewModel.unlockApp() })
        return
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "Carnet de Dialyse",
                        style = MaterialTheme.typography.titleLarge,
                        color = DialysisBluePrimary
                    )
                    Text(
                        text = "Mon Livre de Dialyse Numérique",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilterChip(
                            selected = currentLanguage == AppLanguage.FRENCH,
                            onClick = { viewModel.changeLanguage(AppLanguage.FRENCH) },
                            label = { Text("FR") }
                        )
                        FilterChip(
                            selected = currentLanguage == AppLanguage.ARABIC,
                            onClick = { viewModel.changeLanguage(AppLanguage.ARABIC) },
                            label = { Text("عربي") }
                        )
                        FilterChip(
                            selected = currentLanguage == AppLanguage.ENGLISH,
                            onClick = { viewModel.changeLanguage(AppLanguage.ENGLISH) },
                            label = { Text("EN") }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider()

                drawerNavItems.forEach { screen ->
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.route,
                                tint = if (screen == Screen.Emergency) EmergencyRed else DialysisBluePrimary
                            )
                        },
                        label = {
                            Text(
                                text = LanguageManager.getString(screen.titleKey),
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        selected = navController.currentBackStackEntryAsState().value?.destination?.route == screen.route,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                            .testTag("drawer_item_${screen.route}")
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(DialysisBluePrimary, CircleShape)
                            )
                            Text(
                                text = LanguageManager.getString("app_name"),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MedicalTextPrimary
                                )
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { scope.launch { drawerState.open() } },
                            modifier = Modifier.testTag("open_drawer_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = MedicalTextPrimary
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { navController.navigate(Screen.Emergency.route) },
                            modifier = Modifier.testTag("top_emergency_button")
                        ) {
                            Icon(imageVector = Icons.Default.Warning, contentDescription = "Emergency", tint = EmergencyRed)
                        }
                        IconButton(onClick = { navController.navigate(Screen.Security.route) }) {
                            Icon(imageVector = Icons.Default.Shield, contentDescription = "Security", tint = MedicalTextPrimary)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MedicalSurfaceLight,
                        titleContentColor = MedicalTextPrimary
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 2.dp,
                    windowInsets = NavigationBarDefaults.windowInsets
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    mainNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(imageVector = screen.icon, contentDescription = screen.route) },
                            label = {
                                Text(
                                    text = LanguageManager.getString(screen.titleKey),
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
                                )
                            },
                            selected = currentRoute == screen.route,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = DialysisBluePrimary,
                                selectedTextColor = DialysisBluePrimary,
                                indicatorColor = DialysisBlueLight,
                                unselectedIconColor = Color(0xFF64748B),
                                unselectedTextColor = Color(0xFF64748B)
                            ),
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            modifier = Modifier.testTag("bottom_nav_${screen.route}")
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(viewModel = viewModel, onNavigate = { route -> navController.navigate(route) })
                }
                composable(Screen.Profile.route) {
                    ProfileScreen(viewModel = viewModel)
                }
                composable(Screen.Passport.route) {
                    PassportScreen(viewModel = viewModel)
                }
                composable(Screen.Sessions.route) {
                    SessionsScreen(viewModel = viewModel)
                }
                composable(Screen.Monitoring.route) {
                    HomeMonitoringScreen(viewModel = viewModel)
                }
                composable(Screen.Labs.route) {
                    LabResultsScreen(viewModel = viewModel)
                }
                composable(Screen.Meds.route) {
                    MedicationsScreen(viewModel = viewModel)
                }
                composable(Screen.Nutrition.route) {
                    NutritionScreen(viewModel = viewModel)
                }
                composable(Screen.Docs.route) {
                    DocumentsScreen(viewModel = viewModel)
                }
                composable(Screen.Emergency.route) {
                    EmergencyCardScreen(viewModel = viewModel)
                }
                composable(Screen.QrTransfer.route) {
                    QrTransferScreen(viewModel = viewModel)
                }
                composable(Screen.AiAssistant.route) {
                    AiAssistantScreen(viewModel = viewModel)
                }
                composable(Screen.Security.route) {
                    SecurityScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
private fun LockScreenOverlay(onUnlock: () -> Unit) {
    var enteredPin by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Lock",
                tint = Color.White,
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = "Carnet de Dialyse Verrouillé",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )
            Text(
                text = "Entrez votre code PIN pour accéder à vos données médicales",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f)
            )

            OutlinedTextField(
                value = enteredPin,
                onValueChange = { enteredPin = it },
                label = { Text("Code PIN", color = Color.White) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier.testTag("unlock_pin_input")
            )

            Button(
                onClick = onUnlock,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = DialysisBluePrimary),
                modifier = Modifier.testTag("unlock_app_button")
            ) {
                Text("Déverrouiller L'Application")
            }
        }
    }
}
