package com.nexo.app.ui.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nexo.app.ui.about.AboutScreen
import com.nexo.app.ui.characters.CharactersScreen
import com.nexo.app.ui.chat.ChatScreen
import com.nexo.app.ui.home.HomeScreen
import com.nexo.app.ui.provider.ProviderScreen
import com.nexo.app.ui.settings.SettingsScreen

private data class Tab(val route: String, val label: String, val icon: ImageVector)

private val TABS = listOf(
    Tab(Routes.HOME, "Inicio", Icons.Filled.Home),
    Tab(Routes.CHARACTERS, "Personajes", Icons.Filled.Person),
    Tab(Routes.SETTINGS, "Ajustes", Icons.Filled.Settings),
)

@Composable
fun NexoRoot(factory: ViewModelProvider.Factory) {
    val nav = rememberNavController()
    val entry by nav.currentBackStackEntryAsState()
    val route = entry?.destination?.route
    val showBar = route != null && route in Routes.tabs

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        bottomBar = {
            if (showBar) {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                    TABS.forEach { tab ->
                        NavigationBarItem(
                            selected = route == tab.route,
                            onClick = {
                                nav.navigate(tab.route) {
                                    popUpTo(Routes.HOME) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(tab.icon, contentDescription = tab.label) },
                            label = { Text(tab.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.secondary,
                                selectedTextColor = MaterialTheme.colorScheme.secondary,
                                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            ),
                        )
                    }
                }
            }
        },
    ) { inner ->
        NavHost(
            navController = nav,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(inner),
        ) {
            composable(Routes.HOME) {
                HomeScreen(onChat = { nav.navigate(Routes.CHAT) }, factory = factory)
            }
            composable(Routes.CHARACTERS) { CharactersScreen(factory) }
            composable(Routes.SETTINGS) {
                SettingsScreen(
                    onOpenProvider = { nav.navigate(Routes.PROVIDER) },
                    onOpenAbout = { nav.navigate(Routes.ABOUT) },
                    factory = factory,
                )
            }
            composable(Routes.PROVIDER) {
                ProviderScreen(onBack = { nav.popBackStack() }, factory = factory)
            }
            composable(Routes.CHAT) {
                ChatScreen(
                    onBack = { nav.popBackStack() },
                    onOpenSettings = { nav.navigate(Routes.PROVIDER) },
                    factory = factory,
                )
            }
            composable(Routes.ABOUT) { AboutScreen(onBack = { nav.popBackStack() }) }
        }
    }
}
