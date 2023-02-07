package com.zhigaras.unsplash.presentation.compose.screens.searchscreen

import android.content.res.Configuration
import android.content.res.Resources
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.zhigaras.unsplash.presentation.MainViewModel
import com.zhigaras.unsplash.presentation.compose.ErrorItem
import com.zhigaras.unsplash.presentation.compose.LoadingItem
import com.zhigaras.unsplash.presentation.compose.LoadingView


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onPhotoClick: (String) -> Unit,
    onLikeClick: () -> Unit
) {
    val state = rememberLazyStaggeredGridState(initialFirstVisibleItemIndex = 0)
    val pagedPhotos = viewModel.pagedPhotos.collectAsLazyPagingItems()
    val orientation = Resources.getSystem().configuration.orientation
    val cellsCount = if (orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 4
    val itemsPadding = 8.dp
    val screenWidth =
        (Resources.getSystem().displayMetrics.widthPixels / Resources.getSystem().displayMetrics.density).dp
    val itemWidth = (screenWidth - (cellsCount - 1) * itemsPadding) / cellsCount
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(cellsCount),
        horizontalArrangement = Arrangement.spacedBy(itemsPadding),
        verticalArrangement = Arrangement.spacedBy(itemsPadding),
        state = state
    ) {
        
        items(pagedPhotos) { photoEntity ->
            photoEntity?.let {
                PhotoItemCard(
                    photoItem = it, itemWidth,
                    onLikeClick = onLikeClick,
                    onPhotoClick = onPhotoClick
                )
            }
        }
        pagedPhotos.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item { LoadingView(modifier = Modifier.fillMaxSize()) }
                }
                loadState.append is LoadState.Loading -> {
                    item { LoadingItem() }
                }
                loadState.refresh is LoadState.Error -> {
                    val e = pagedPhotos.loadState.refresh as LoadState.Error
                    item {
                        ErrorItem(
                            message = e.error.localizedMessage!!,
                            modifier = Modifier.fillMaxSize(),
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
    }
}

