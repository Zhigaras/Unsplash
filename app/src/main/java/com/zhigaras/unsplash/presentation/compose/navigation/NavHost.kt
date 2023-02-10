package com.zhigaras.unsplash.presentation.compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.zhigaras.unsplash.domain.navigateSingleTopTo
import com.zhigaras.unsplash.presentation.compose.screens.DetailsScreen
import com.zhigaras.unsplash.presentation.compose.screens.FavoritesScreen
import com.zhigaras.unsplash.presentation.compose.screens.OnboardingScreen
import com.zhigaras.unsplash.presentation.compose.screens.ProfileScreen
import com.zhigaras.unsplash.presentation.compose.screens.searchscreen.FeedScreen

@Composable
fun SetupNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onDownloadClick: (String, String) -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Feed.route
    ) {
        composable(route = Feed.route) {
            FeedScreen(
                onPhotoClick = { photoId ->
                    navController.navigateSingleTopTo("${Details.route}/$photoId")
                },
                onLikeClick = { TODO() }
            )
        }
        
        composable(
            route = Details.routeWithArgs,
            arguments = Details.arguments,
            deepLinks = listOf(navDeepLink {
                uriPattern = "https://unsplash.com/photos/{photo_id}"
            })
        ) { navBackStackEntry ->
            val photoId = navBackStackEntry.arguments?.getString(Details.photoIdArg) ?: ""
            DetailsScreen(
                photoId = photoId,
                onDownloadClick = onDownloadClick
            )
        }
        composable(route = Onboarding.route) {
            OnboardingScreen {
                TODO()
            }
        }
        composable(route = Favorites.route) {
            FavoritesScreen()
        }
        composable(route = Profile.route) {
            ProfileScreen()
        }
    }
}

