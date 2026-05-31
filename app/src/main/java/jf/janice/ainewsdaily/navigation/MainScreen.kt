package jf.janice.ainewsdaily.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import jf.janice.ainewsdaily.R
import jf.janice.ainewsdaily.feature.articles.presentation.ui.ArticleScreen
import jf.janice.ainewsdaily.feature.sources.presentation.ui.SourcesScreen

private object Routes {
    const val ARTICLES = "articles"
    const val SOURCES = "sources"
}

private data class BottomNavItem(
    val route: String,
    val labelResId: Int,
    val icon: ImageVector,
)

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()

    val snackBarHostState = remember { SnackbarHostState() }

    val bottomNavItems = listOf(
        BottomNavItem(
            route = Routes.ARTICLES,
            labelResId = R.string.nav_articles,
            icon = ImageVector.vectorResource(R.drawable.ic_articles),
        ),
        BottomNavItem(
            route = Routes.SOURCES,
            labelResId = R.string.nav_sources,
            icon = ImageVector.vectorResource(R.drawable.ic_sources),
        ),
    )

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(stringResource(item.labelResId)) },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.ARTICLES,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Routes.ARTICLES) {
                ArticleScreen(snackBarHostState = snackBarHostState)
            }
            composable(Routes.SOURCES) {
                SourcesScreen()
            }
        }
    }
}
