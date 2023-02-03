package com.zhigaras.unsplash.presentation.compose.screens.mainscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.zhigaras.unsplash.presentation.MainViewModel
import com.zhigaras.unsplash.presentation.compose.ErrorItem
import com.zhigaras.unsplash.presentation.compose.LoadingItem
import com.zhigaras.unsplash.presentation.compose.LoadingView

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
    
        items(items = pagedPhotos, key = { it.urlRegular }) {
            PhotoItemCard(photoItem = it!!)
        }
        pagedPhotos.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item { LoadingView(modifier = Modifier.fillParentMaxSize()) }
                }
                loadState.append is LoadState.Loading -> {
                    item { LoadingItem() }
                }
                loadState.refresh is LoadState.Error -> {
                    val e = pagedPhotos.loadState.refresh as LoadState.Error
                    item {
                        ErrorItem(
                            message = e.error.localizedMessage!!,
                            modifier = Modifier.fillParentMaxSize(),
                            onClickRetry = { retry() }
                        )
                    }
                }
                loadState.append is LoadState.Error -> {
                    val e = pagedPhotos.loadState.append as LoadState.Error
                    item {
                        ErrorItem(
                            message = e.error.localizedMessage!!,
                            onClickRetry = { retry() }
                        )
                    }
                }
            }
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