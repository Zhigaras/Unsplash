package com.zhigaras.unsplash.presentation.compose.screens

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.zhigaras.unsplash.presentation.MainViewModel

@Composable
fun DetailsScreen(viewModel: MainViewModel = hiltViewModel()) {
    
    val photoDetailsFlow = viewModel.photoDetailsFlow.collectAsState()
    
    TextButton(onClick = { viewModel.getPhotoDetail("6kajEqr84iY") }) {
        Text(text = "Show Details")
    }
    Text(text = photoDetailsFlow.value?.urls?.small.toString())
}