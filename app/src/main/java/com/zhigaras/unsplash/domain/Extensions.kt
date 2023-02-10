package com.zhigaras.unsplash.domain

import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.zhigaras.unsplash.model.photodetails.Tag

fun Int.toShortForm(): String {
    return when (this) {
        in 0..999 -> this.toString()
        in 1_000..999_999 -> "${this / 1_000}K"
        else -> "${this / 1_000_000}M"
    }
}

fun List<Tag>.hasSearchTags(): Boolean {
    return this.any { it.type == "search" }
}

fun List<Tag>.toHashTagString(): String {
    val tags = this.filter { it.type == "search" }
    return buildString {
        tags.forEach {
            append(" #")
            append(it.title)
        }
    }.trim()
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

@Composable
fun String?.ExifText(@StringRes fieldName: Int) {
    this?.let {
        Text(text = buildString {
            append(stringResource(id = fieldName))
            append(it)
        }, style = MaterialTheme.typography.bodyMedium)
    }
}