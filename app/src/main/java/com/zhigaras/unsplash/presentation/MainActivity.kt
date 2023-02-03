package com.zhigaras.unsplash.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.zhigaras.unsplash.presentation.compose.screens.mainscreen.MainScreen
import com.zhigaras.unsplash.presentation.compose.screens.OnboardingScreen
import com.zhigaras.unsplash.presentation.compose.theme.UnsplashTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private val authViewModel: AuthViewModel by viewModels()
    
    private val authorizeLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val intentData = it.data ?: return@registerForActivityResult
            Log.d("AAA", it.data?.scheme.toString())
            authViewModel.handleAuthResponseIntent(intentData)
        }
    
    private fun openAuthPage() {
        authViewModel.prepareAuthPageIntent { authorizeLauncher.launch(it) }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.authSuccessEventChannel.collect {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                }
            }
        }
        
        setContent {
            UnsplashTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
//                    UnsplashApp { openAuthPage() }
                }
            }
        }
    }
}

@Composable
fun UnsplashApp(toAuthorizeScreen: () -> Unit) {
    OnboardingScreen(toAuthorizeScreen)
}

//@Preview(showSystemUi = true)
//@Composable
//fun DefaultPreview() {
//    UnsplashTheme {
//        OnboardingScreen()
//    }
//}