package com.zhigaras.unsplash.presentation

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zhigaras.unsplash.domain.navigateSingleTopTo
import com.zhigaras.unsplash.presentation.compose.BottomTabRow
import com.zhigaras.unsplash.presentation.compose.UnsplashTopBar
import com.zhigaras.unsplash.presentation.compose.navigation.*
import com.zhigaras.unsplash.presentation.compose.theme.UnsplashTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private val authViewModel: AuthViewModel by viewModels()
    private var downloadId = 0L
    private lateinit var downloadManager: DownloadManager
    
    private val authorizeLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val intentData = it.data ?: return@registerForActivityResult
            authViewModel.handleAuthResponseIntent(intentData)
        }
    
    private fun openAuthPage() {
        authViewModel.prepareAuthPageIntent { authorizeLauncher.launch(it) }
    }
    
    private fun downloadPhoto(url: String, photoId: String) {
        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            .setMimeType("image/jpeg")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_PICTURES,
                "unsplash-image.jpeg"
            )
        downloadId = downloadManager.enqueue(request)
    }
    
    @SuppressLint("Range")
    private fun openDownloadedPhoto() {
        val uri = downloadManager.getUriForDownloadedFile(downloadId)
        var imageUri = Uri.EMPTY
        this.contentResolver?.query(uri, null, null, null, null)
            ?.use { cursor ->
                cursor.moveToFirst()
                imageUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    cursor.getLong(
                        cursor.getColumnIndex(
                            MediaStore.Images.ImageColumns._ID
                        )
                    )
                )
            }
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(imageUri, "image/*")
        this.startActivity(intent)
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
            val snackBarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()
            val downloadCompleteReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    val intentId =
                        intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                    if (intentId == downloadId) {
                        scope.launch {
                            val snackBarResult = snackBarHostState.showSnackbar(
                                message = "Download Completed",
                                actionLabel = "Open photo"
                            )
                            if (snackBarResult == SnackbarResult.ActionPerformed) {
                                openDownloadedPhoto()
                            }
                        }
                    }
                }
            }
            UnsplashTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DisposableEffect(Unit) {
                        registerReceiver(
                            downloadCompleteReceiver,
                            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
                        )
                        onDispose { unregisterReceiver(downloadCompleteReceiver) }
                    }
                    
                    UnsplashApp(
                        onDownloadClick = { url, photoId ->
                            downloadPhoto(url, photoId)
                        },
                        snackBarHostState = snackBarHostState
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnsplashApp(
    toAuthorizeScreen: () -> Unit = {},
    onDownloadClick: (String, String) -> Unit,
    snackBarHostState: SnackbarHostState
) {
    
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
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { innerPaddings ->
        SetupNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPaddings),
            onDownloadClick = onDownloadClick
        )
    }
}