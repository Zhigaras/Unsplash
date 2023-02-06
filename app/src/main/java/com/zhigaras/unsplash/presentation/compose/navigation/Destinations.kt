package com.zhigaras.unsplash.presentation.compose.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument

interface Destinations {
    val route: String
    val icon: ImageVector?
    val pageNumber: Int
}

object Onboarding: Destinations {
    override val route = "Onboarding"
    override val icon = null
    override val pageNumber = 0
}

object Search: Destinations {
    override val route = "Search"
    override val icon = Icons.Outlined.Home
    override val pageNumber = 1
}

object Favorites: Destinations {
    override val route = "Favorites"
    override val icon = Icons.Outlined.Favorite
    override val pageNumber = 2
}

object Profile: Destinations {
    override val route = "Profile"
    override val icon = Icons.Outlined.Person
    override val pageNumber = 3
}

object Details: Destinations {
    override val route = "Details"
    override val icon = null
    override val pageNumber = 4
    const val photoIdArg = "photo_id"
    val routeWithArgs = "$route/{$photoIdArg}"
    val arguments = listOf(
        navArgument(photoIdArg) { type = NavType.StringType }
    )
}

val bottomTabList = listOf(Search, Favorites, Profile)