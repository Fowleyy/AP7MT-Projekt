package com.example.habitflow.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.*
import com.example.habitflow.viewmodel.HabitViewModel
import com.example.habitflow.ui.screens.HomeScreen
import com.example.habitflow.ui.screens.HabitsScreen
import androidx.compose.ui.unit.dp

private val DarkGreen = Color(0xFF244A3A)
private val Gray = Color(0xFF929B94)

@Composable
fun AppNavigation(habitViewModel: HabitViewModel) {

    val navController = rememberNavController()

    val items = listOf(
        NavigationItem("home", "Domů", Icons.Default.Home),
        NavigationItem("habits", "Návyky", Icons.Default.CheckCircle),
        NavigationItem("statistics", "Statistiky", Icons.Default.BarChart),
        NavigationItem("calendar", "Kalendář", Icons.Default.CalendarMonth),
        NavigationItem("settings", "Nastavení", Icons.Default.Settings)
    )

    val currentRoute = navController
        .currentBackStackEntryAsState()
        .value?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 0.dp
            ) {
                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo("home") {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label
                            )
                        },
                        label = {
                            Text(item.label)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DarkGreen,
                            selectedTextColor = DarkGreen,
                            unselectedIconColor = Gray,
                            unselectedTextColor = Gray,
                            indicatorColor = Color(0xFFE2EDE4)
                        )
                    )
                }
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {
            composable("home") {
                HomeScreen(habitViewModel)
            }

            composable("habits") {
                HabitsScreen(habitViewModel)
            }

            composable("statistics") {
                PlaceholderScreen("Statistiky")
            }

            composable("calendar") {
                PlaceholderScreen("Kalendář")
            }

            composable("settings") {
                PlaceholderScreen("Nastavení")
            }
        }
    }
}

private data class NavigationItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun PlaceholderScreen(title: String) {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier.padding(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(title)
    }
}