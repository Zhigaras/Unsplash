package com.zhigaras.unsplash.presentation.compose.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import androidx.paging.compose.itemsIndexed
import com.zhigaras.unsplash.presentation.MainViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val pagedPhotos = viewModel.pagedPhotos.collectAsLazyPagingItems()
    
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(horizontal = 8.dp)
    ) {
    
        items(pagedPhotos) {
            Text(it!!.id)
        }
    
//        itemsIndexed(pagedPhotos) { index, photos ->
//            Text(text = photos?.id.toString())
//
//        }
    }
//        TextButton(onClick = { viewModel.loadPhotos() }) {
//            Text(text = "get photos")
//        }

}