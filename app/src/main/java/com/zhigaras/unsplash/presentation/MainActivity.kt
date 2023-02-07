package com.zhigaras.unsplash.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zhigaras.unsplash.presentation.compose.BottomTabRow
import com.zhigaras.unsplash.presentation.compose.UnsplashTopBar
import com.zhigaras.unsplash.presentation.compose.navigation.*
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
                    UnsplashApp {
                    
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnsplashApp(toAuthorizeScreen: () -> Unit) {
    
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    val currentScreen =
        allScreensList.find { currentDestination?.route?.contains(it.route) == true } ?: Feed
    
    Scaffold(
        topBar = {
            UnsplashTopBar(currentScreen = currentScreen, onBackClick = {
                navController.popBackStack()
            })
        },
        bottomBar = {
            BottomTabRow(
                allScreens = bottomTabList,
                onTabSelected = { newScreen ->
                    navController.navigateSingleTopTo(newScreen.route)
                },
                currentScreen = currentScreen
            )
        }
    ) { innerPaddings ->
        SetupNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPaddings)
        )
    }
    
    
}