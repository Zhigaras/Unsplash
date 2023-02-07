package com.zhigaras.unsplash.presentation.compose.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.zhigaras.unsplash.R

interface Destinations {
    val title: Int?
    val route: String
    val icon: Int?
    val pageNumber: Int
}

object Onboarding: Destinations {
    override val title = null
    override val route: String = this::class.java.simpleName
    override val icon = null
    override val pageNumber = 0
}

object Feed: Destinations {
    override val title = R.string.feed
    override val route: String = this::class.java.simpleName
    override val icon = R.drawable.home_icon
    override val pageNumber = 1
}

object Favorites: Destinations {
    override val title = R.string.favorites
    override val route: String = this::class.java.simpleName
    override val icon = R.drawable.favorites_icon
    override val pageNumber = 2
}

object Profile: Destinations {
    override val title = R.string.profile
    override val route: String = this::class.java.simpleName
    override val icon = R.drawable.profile_icon
    override val pageNumber = 3
}

object Details: Destinations {
    override val title = R.string.photo
    override val route: String = this::class.java.simpleName
    override val icon = null
    override val pageNumber = 4
    const val photoIdArg = "photo_id"
    val routeWithArgs = "$route/{$photoIdArg}"
    val arguments = listOf(
        navArgument(photoIdArg) { type = NavType.StringType }
    )
}

val allScreensList = listOf(Onboarding, Feed, Favorites, Profile, Details)
val bottomTabList = listOf(Feed, Favorites, Profile)