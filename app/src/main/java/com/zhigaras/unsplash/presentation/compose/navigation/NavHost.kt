package com.zhigaras.unsplash.presentation.compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.zhigaras.unsplash.domain.navigateSingleTopTo
import com.zhigaras.unsplash.presentation.AuthViewModel
import com.zhigaras.unsplash.presentation.compose.screens.*
import com.zhigaras.unsplash.presentation.compose.screens.feedscreen.FeedScreen

@Composable
fun SetupNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onDownloadClick: (String, String) -> Unit,
    toAuthorizeScreen: () -> Unit,
    authViewModel: AuthViewModel
) {
    LaunchedEffect(key1 = Unit) {
        authViewModel.checkAuthToken()
    }
    val isAuthorized = authViewModel.authCheckFlow.collectAsState().value
    
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = if (isAuthorized.data == true) Feed.route else Onboarding.route
    ) {
        composable(route = Feed.route) {
            FeedScreen(
                onPhotoClick = { photoId ->
                    navController.navigateSingleTopTo("${PhotoDetails.route}/$photoId")
                }
            )
        }
        
        composable(
            route = PhotoDetails.routeWithArgs,
            arguments = PhotoDetails.arguments,
            deepLinks = listOf(navDeepLink {
                uriPattern = "https://unsplash.com/photos/{photo_id}"
            })
        ) { navBackStackEntry ->
            val photoId = navBackStackEntry.arguments?.getString(PhotoDetails.photoIdArg) ?: ""
            PhotoDetailsScreen(
                photoId = photoId,
                onDownloadClick = onDownloadClick
            )
        }
        composable(
            route = Search.routeWithArgs,
            arguments = Search.arguments
        ) { navBackStackEntry ->
            val query = navBackStackEntry.arguments?.getString((Search.queryArg)) ?: ""
            SearchScreen(
                query = query,
                onPhotoClick = { photoId ->
                    navController.navigateSingleTopTo("${PhotoDetails.route}/$photoId")
                }
            )
            
        }
        composable(route = Onboarding.route) {
            OnboardingScreen {
                toAuthorizeScreen()
            }
        }
        composable(route = Collections.route) {
            CollectionScreen(
                onCollectionClick = { collectionId ->
                    navController.navigate("${CollectionDetails.route}/$collectionId")
                }
            )
        }
        composable(
            route = CollectionDetails.routeWithArgs,
            arguments = CollectionDetails.arguments
        ) { navBackStackEntry ->
            val collectionId =
                navBackStackEntry.arguments?.getString(CollectionDetails.collectionIdArg) ?: ""
            CollectionDetailsScreen(collectionId = collectionId)
        }
        composable(route = Profile.route) {
            ProfileScreen()
        }
    }
}

