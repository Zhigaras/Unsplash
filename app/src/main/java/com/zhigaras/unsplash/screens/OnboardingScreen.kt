package com.zhigaras.unsplash.screens

import android.content.res.Resources
import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zhigaras.unsplash.R
import com.zhigaras.unsplash.compose.theme.Black
import com.zhigaras.unsplash.compose.theme.PrimaryColor
import com.zhigaras.unsplash.compose.theme.White

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OnboardingScreen() {
    var onboardingPage by rememberSaveable { (mutableStateOf(1)) }
    var onboardingText = ""
    var circleXOffset = 0.0f
    
    when (onboardingPage) {
        1 -> {
            onboardingText = stringResource(id = R.string.onboarding_text_1)
            circleXOffset = 0.0f
        }
        2 -> {
            onboardingText = stringResource(id = R.string.onboarding_text_2)
            circleXOffset = 300.0f
        }
        3 -> {
            onboardingText = stringResource(id = R.string.onboarding_text_3)
            circleXOffset = 600.0f
        }
    }
    if (onboardingPage != 4) {
        Box(contentAlignment = Alignment.BottomCenter) {
            AnimatedContent(
                targetState = onboardingPage,
                transitionSpec = { fadeIn() with fadeOut() }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Circle(xOffset = circleXOffset)
                }
            }
            AnimatedContent(
                targetState = onboardingPage
            ) {
                Text(
                    text = onboardingText,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(16.dp)
                        .animateEnterExit(
                            enter = slideInHorizontally(),
                            exit = slideOutHorizontally()
                        ),
                    color = Black
                )
            }
        }
        Box {
            Image(
                painter = painterResource(id = R.drawable.onboarding_img),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                alignment = Alignment.Center
            )
        }
    } else {
        AuthScreen()
    }
    Row {
        if (onboardingPage == 2 || onboardingPage == 3) {
            BackForwardButton(
                id = R.drawable.back_button,
                onCLick = { onboardingPage-- },
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        if (onboardingPage == 4) {
            BackForwardButton(
                id = R.drawable.back_button,
                onCLick = { onboardingPage-- },
                tint = White
            )
        }
        Spacer(modifier = Modifier.weight(1.0f))
        if (onboardingPage != 4) {
            BackForwardButton(
                id = R.drawable.forward_button,
                onCLick = { onboardingPage++ },
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun AuthScreen() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            color = Black, modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.unsplash_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(70.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 68.sp
                    ),
                    color = Color.White,
                )
            }
        }
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .weight(1f)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { },
                    shape = Shapes().small,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.log_in_text),
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                }
            }
            
        }
    }
}

@Composable
fun BackForwardButton(id: Int, onCLick: () -> Unit, tint: Color) {
    IconButton(
        onClick = { onCLick() },
        modifier = Modifier
            .size(72.dp)
            .padding(16.dp)
    ) {
        Icon(
            painter = painterResource(id = id),
            contentDescription = null,
            tint = tint
        )
    }
}

@Composable
fun Circle(xOffset: Float) {
    val displayWidth = Resources.getSystem().displayMetrics.widthPixels
    val radius = (displayWidth).toFloat()
    
    Canvas(
        modifier = Modifier
    ) {
        drawCircle(
            color = PrimaryColor,
            radius = 1000f,
            center = Offset(xOffset, 100f)
        )
    }
}

//@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
//@Composable
//fun AuthPreviewNight() {
//    AuthScreen()
//}

@Preview(showSystemUi = true)
@Composable
fun AuthPreview() {
    AuthScreen()
}
