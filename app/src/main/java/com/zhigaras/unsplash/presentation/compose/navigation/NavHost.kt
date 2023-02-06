package com.zhigaras.unsplash.presentation.compose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.zhigaras.unsplash.presentation.compose.screens.DetailsScreen
import com.zhigaras.unsplash.presentation.compose.screens.OnboardingScreen
import com.zhigaras.unsplash.presentation.compose.screens.mainscreen.SearchScreen

@Composable
fun SetupNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Search.route
    ) {
        composable(route = Search.route) {
            SearchScreen(
                onPhotoClick = { photoId ->
                    navController.navigateSingleTopTo("${Details.route}/$photoId")
                },
                onLikeClick = { TODO() }
            )
        }
        
        composable(route = Details.routeWithArgs, arguments = Details.arguments) { navBackStackEntry ->
            val photoId = navBackStackEntry.arguments?.getString(Details.photoIdArg) ?: ""
            DetailsScreen(photoId = photoId)
        }
        composable(route = Onboarding.route) {
            OnboardingScreen {
                TODO()
            }
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }