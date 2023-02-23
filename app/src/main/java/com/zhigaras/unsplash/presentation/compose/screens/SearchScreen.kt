package com.zhigaras.unsplash.presentation.compose.screens

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.zhigaras.unsplash.presentation.MainViewModel
import com.zhigaras.unsplash.presentation.compose.LazyVerticalGrid

@Composable
fun SearchScreen(
    query: String,
    viewModel: MainViewModel = hiltViewModel(),
    onPhotoClick: (String) -> Unit
) {
    LazyVerticalGrid(
        pagedPhotos = viewModel.getPagedSearchPhotos(query).collectAsLazyPagingItems(),
        onLikeClick = { isLiked, photoId -> viewModel.onLikeClick(isLiked, photoId) },
        onPhotoClick = onPhotoClick
    )
    
}