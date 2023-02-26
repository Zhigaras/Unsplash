package com.zhigaras.unsplash.presentation.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zhigaras.unsplash.presentation.compose.navigation.Destinations

@Composable
fun BottomTabRow(
    allScreens: List<Destinations>,
    onTabSelected: (Destinations) -> Unit,
    currentScreen: Destinations,
    topAndBottomBarState: Boolean
) {
    AnimatedVisibility(visible = topAndBottomBarState) {
        Surface(
            Modifier
                .height(72.dp)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            Row(
                modifier = Modifier
                    .selectableGroup()
                    .border(width = 1.dp, color = Color.DarkGray)
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                allScreens.forEach { screen ->
                    Icon(
                        painter = painterResource(id = screen.icon!!),
                        contentDescription = screen.route,
                        tint = if (currentScreen == screen) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onTertiary,
                        modifier = Modifier
                            .size(16.dp)
                            .selectable(
                                selected = currentScreen == screen,
                                onClick = { onTabSelected(screen) },
                                role = Role.Tab,
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(
                                    bounded = false,
                                    radius = Dp.Unspecified,
                                    color = Color.Unspecified
                                )
                            )
                    )
                }
            }
        }
    }
}

