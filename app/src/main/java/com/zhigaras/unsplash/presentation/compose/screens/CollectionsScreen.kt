package com.zhigaras.unsplash.presentation.compose.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.zhigaras.unsplash.R
import com.zhigaras.unsplash.model.collectionentity.CollectionEntity
import com.zhigaras.unsplash.presentation.MainViewModel
import com.zhigaras.unsplash.presentation.compose.ErrorItem
import com.zhigaras.unsplash.presentation.compose.LoadingItem
import com.zhigaras.unsplash.presentation.compose.LoadingView
import com.zhigaras.unsplash.presentation.compose.screens.feedscreen.UserInfoArea

@Composable
fun CollectionScreen(
    viewModel: MainViewModel = hiltViewModel(),
    navigateToCollectionDetails: (String) -> Unit
) {
    val pagedCollections = viewModel.pagedCollections.collectAsLazyPagingItems()
    fun onCollectionClick(collection: CollectionEntity) {
        Log.d("AAA collection sent", collection.collectionId)
        viewModel.saveSelectedCollection(collection)
        navigateToCollectionDetails(collection.collectionId)
    }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(pagedCollections) { collection ->
            collection?.let {
                CollectionItem(collection = it, onCollectionClick = { onCollectionClick(it) })
            }
        }
        pagedCollections.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item { LoadingView(modifier = Modifier.fillParentMaxSize()) }
                }
                loadState.append is LoadState.Loading -> {
                    item { LoadingItem() }
                }
                loadState.refresh is LoadState.Error -> {
                    val e = pagedCollections.loadState.refresh as LoadState.Error
                    item {
                        ErrorItem(
                            message = e.error.localizedMessage
                                ?: stringResource(id = R.string.something_went_wrong),
                            modifier = Modifier.fillParentMaxSize(),
                            onClickRetry = { retry() }
                        )
                    }
                }
                loadState.append is LoadState.Error -> {
                    val e = pagedCollections.loadState.append as LoadState.Error
                    item {
                        ErrorItem(
                            message = e.error.localizedMessage
                                ?: stringResource(id = R.string.something_went_wrong),
                            onClickRetry = { retry() }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CollectionItem(
    collection: CollectionEntity,
    onCollectionClick: (CollectionEntity) -> Unit
) {
    Box(modifier = Modifier.clickable { onCollectionClick(collection) }) {
        with(collection) {
            GlideImage(
                model = Uri.parse(this.coverPhoto.urls.regular),
                contentDescription = stringResource(id = R.string.cover_photo)
            )
            Column(
                modifier = Modifier.padding(top = 8.dp, start = 16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = String.format(
                        stringResource(id = R.string.photos),
                        this@with.totalPhotos
                    ).uppercase(),
                    style = MaterialTheme.typography.displaySmall
                )
                Text(
                    text = this@with.title.uppercase(),
                    style = MaterialTheme.typography.displayMedium
                )
            }
            UserInfoArea(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .height(40.dp)
                    .padding(4.dp),
                user = this.user
            )
        }
    }
}