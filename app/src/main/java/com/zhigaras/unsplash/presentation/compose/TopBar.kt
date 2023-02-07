package com.zhigaras.unsplash.presentation.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.zhigaras.unsplash.presentation.compose.navigation.Destinations
import com.zhigaras.unsplash.presentation.compose.navigation.Details

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnsplashTopBar(
    currentScreen: Destinations,
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = { currentScreen.title?.let { Text(text = stringResource(id = it)) } },
        navigationIcon = {
            if (currentScreen == Details)
                IconButton(onClick = { onBackClick() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, null)
                }
        },
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            scrolledContainerColor = Color.Blue,
            navigationIconContentColor = Color.Yellow,
            titleContentColor = Color.Red,
            actionIconContentColor = Color.Green
        )
    )
}