package com.zhigaras.unsplash.presentation.compose

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
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.zhigaras.unsplash.model.photoentity.PhotoEntity
import com.zhigaras.unsplash.presentation.compose.screens.feedscreen.PhotoItemCard
import com.zhigaras.unsplash.presentation.compose.screens.feedscreen.items

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyVerticalGrid(
    pagedPhotos: LazyPagingItems<PhotoEntity>,
    onLikeClick: (Boolean, String) -> Unit,
    onPhotoClick: (String) -> Unit
) {
    
    val state = rememberLazyStaggeredGridState(initialFirstVisibleItemIndex = 0)
    val orientation = Resources.getSystem().configuration.orientation
    val cellsCount = if (orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 4
    val itemsPadding = 8.dp
    
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(cellsCount),
        horizontalArrangement = Arrangement.spacedBy(itemsPadding),
        verticalArrangement = Arrangement.spacedBy(itemsPadding),
        state = state
    ) {
        
        items(pagedPhotos) { photoEntity ->
            photoEntity?.let {
                PhotoItemCard(
                    photoItem = it,
                    onLikeClick = { isLiked, photoId -> onLikeClick(isLiked, photoId) },
                    onPhotoClick = onPhotoClick,
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
