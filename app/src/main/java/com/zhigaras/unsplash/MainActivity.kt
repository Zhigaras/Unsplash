package com.zhigaras.unsplash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.zhigaras.unsplash.compose.theme.UnsplashTheme
import com.zhigaras.unsplash.screens.OnboardingScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UnsplashTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UnsplashApp()
                }
            }
        }
    }
}

@Composable
fun UnsplashApp() {
    OnboardingScreen()
}

@Preview(showSystemUi = true)
@Composable
fun DefaultPreview() {
    UnsplashTheme {
        OnboardingScreen()
    }
}