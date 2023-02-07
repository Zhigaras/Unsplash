package com.zhigaras.unsplash.presentation.compose

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zhigaras.unsplash.presentation.compose.navigation.Destinations
import com.zhigaras.unsplash.presentation.compose.navigation.Details
import com.zhigaras.unsplash.presentation.compose.navigation.Feed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnsplashTopBar(
    currentScreen: Destinations, onBackClick: () -> Unit
) {
    var isBackButtonVisible by remember { mutableStateOf(false) }
    isBackButtonVisible = currentScreen == Details
    var isSearchActive by remember { mutableStateOf(false) }
    val textInputState = remember { mutableStateOf(TextFieldValue("")) }
    
    TopAppBar(title = {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (isSearchActive) {
                SearchElement(
                    modifier = Modifier.weight(1f),
                    textInputState
                )
            } else {
                TitleElement(
                    modifier = Modifier.weight(1f),
                    currentScreen = currentScreen,
                )
            }
            IconToggleButton(
                modifier = Modifier.padding(8.dp),
                checked = isSearchActive,
                onCheckedChange = { isSearchActive = !isSearchActive }) {
                Icon(
                    imageVector = if (isSearchActive) Icons.Outlined.Close
                    else Icons.Outlined.Search, contentDescription = null
                )
            }
        }
    }, navigationIcon = {
        AnimatedVisibility(visible = isBackButtonVisible,
            enter = slideInHorizontally { -it },
            exit = slideOutHorizontally { -it }) {
            IconButton(onClick = { onBackClick() }) {
                Icon(imageVector = Icons.Default.ArrowBack, null)
            }
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchElement(
    modifier: Modifier,
    textInputState: MutableState<TextFieldValue>
) {
    TextField(
        modifier = modifier,
        value = textInputState.value,
        onValueChange = { textInputState.value = it },
        trailingIcon = {
            IconButton(
                onClick = { /*TODO*/ },
                enabled = textInputState.value != TextFieldValue("")
            ) {
                Icon(imageVector = Icons.Outlined.ArrowForward, null)
            }
        })
}

@Composable
fun TitleElement(
    modifier: Modifier,
    currentScreen: Destinations
) {
    currentScreen.title?.let { stringRes ->
        Text(
            modifier = modifier,
            text = (stringResource(id = stringRes)).uppercase(),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Preview
@Composable
fun TopBarPreview() {
    UnsplashTopBar(currentScreen = Feed) {
    
    }
}