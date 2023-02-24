package com.zhigaras.unsplash.presentation.compose.navigation

import androidx.annotation.StringRes
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.zhigaras.unsplash.R

interface Destinations {
    @get:StringRes val title: Int?
    val route: String
        get() = this::class.java.simpleName
    val icon: Int?
    val pageNumber: Int
}

object Onboarding : Destinations {
    override val title = null
    override val icon = null
    override val pageNumber = 0
}

object Feed : Destinations {
    override val title = R.string.feed
    override val icon = R.drawable.home_icon
    override val pageNumber = 1
}

object Collections : Destinations {
    override val title = R.string.collections
    override val icon = R.drawable.favorites_icon
    override val pageNumber = 2
}

object Profile : Destinations {
    override val title = R.string.profile
    override val icon = R.drawable.profile_icon
    override val pageNumber = 3
}

object PhotoDetails : Destinations {
    override val title = R.string.photo
    override val icon = null
    override val pageNumber = 4
    const val photoIdArg = "photo_id"
    val routeWithArgs = "$route/{$photoIdArg}"
    val arguments = listOf(
        navArgument(photoIdArg) { type = NavType.StringType }
    )
}

object Search : Destinations {
    override val title = R.string.search_string
    override val icon = null
    override val pageNumber = 5
    const val queryArg = "query"
    val routeWithArgs = "$route/{$queryArg}"
    val arguments = listOf(
        navArgument(queryArg) { type = NavType.StringType }
    )
}

object CollectionDetails : Destinations {
    override val title = R.string.collection_details
    override val icon = null
    override val pageNumber = 6
    const val collectionIdArg = "collection_id"
    val routeWithArgs = "$route/{$collectionIdArg}"
    val arguments = listOf(
        navArgument(collectionIdArg) { type = NavType.StringType}
    )
}

val allScreensList = listOf(Onboarding, Feed, Collections, Profile, PhotoDetails, Search, CollectionDetails)
val bottomTabList = listOf(Feed, Collections, Profile)