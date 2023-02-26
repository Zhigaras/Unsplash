package com.zhigaras.unsplash.presentation.compose.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.zhigaras.unsplash.R
import com.zhigaras.unsplash.R.string.user_profile_image
import com.zhigaras.unsplash.data.remote.ApiStatus.*
import com.zhigaras.unsplash.model.photoentity.PhotoEntity
import com.zhigaras.unsplash.model.photoentity.User
import com.zhigaras.unsplash.presentation.MainViewModel
import com.zhigaras.unsplash.presentation.compose.ErrorItem
import com.zhigaras.unsplash.presentation.compose.ErrorView
import com.zhigaras.unsplash.presentation.compose.LoadingItem
import com.zhigaras.unsplash.presentation.compose.LoadingView
import com.zhigaras.unsplash.presentation.compose.screens.feedscreen.PhotoItemCard

@Composable
fun ProfileScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onPhotoClick: (String) -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getProfileInfo()
    }
    val profileInfo = viewModel.profileInfoFlow.collectAsState().value
    val context = LocalContext.current
    
    when (profileInfo.status) {
        LOADING, NOT_LOADED_YET -> {
            LoadingView(Modifier.fillMaxSize())
        }
        SUCCESS -> {
            val username = profileInfo.data?.username
            ProfileInfo(
                nullableUser = profileInfo.data,
                modifier = Modifier.fillMaxWidth(),
                pagedPhotos = viewModel.getPagedPhotos(username = username)
                    .collectAsLazyPagingItems(),
                onLikeClick = { isLiked, photoId ->
                    viewModel.onLikeClick(
                        isLiked,
                        photoId
                    )
                },
                onPhotoClick = onPhotoClick,
                onShareClick = {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.putExtra(Intent.EXTRA_TEXT, it)
                    intent.type = "text/plain"
                    try {
                        context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
        ERROR -> {
            ErrorView(message = profileInfo.errorMessage ?: "")
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileInfo(
    nullableUser: User?,
    modifier: Modifier = Modifier,
    pagedPhotos: LazyPagingItems<PhotoEntity>,
    onPhotoClick: (String) -> Unit,
    onLikeClick: (Boolean, String) -> Unit,
    onShareClick: (String) -> Unit
) {
    nullableUser?.let { user ->
        Column() {
            Row() {
                GlideImage(
                    model = user.profileImage.large,
                    contentDescription = stringResource(id = user_profile_image)
                )
                Column() {
                    user.fullName?.let {
                        Text(text = it, style = MaterialTheme.typography.displaySmall)
                    }
                    Text(text = user.instagramUsernameCorrect)
                    user.userLocation?.let { location ->
                        LocationBlock(locationName = location)
                    }
                }
            }
            Text(text = String.format(stringResource(id = R.string.liked_photo), user.totalLikes))
            LikedLazyPhotos(
                pagedPhotos = pagedPhotos,
                onLikeClick = onLikeClick,
                onPhotoClick = onPhotoClick,
                onShareClick = onShareClick
            )
        }
    }
}

@Composable
fun LikedLazyPhotos(
    modifier: Modifier = Modifier,
    pagedPhotos: LazyPagingItems<PhotoEntity>,
    onPhotoClick: (String) -> Unit,
    onLikeClick: (Boolean, String) -> Unit,
    onShareClick: (String) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(pagedPhotos) { photoItem ->
            photoItem?.let {
                PhotoItemCard(
                    photoItem = photoItem,
                    onLikeClick = onLikeClick,
                    onPhotoClick = onPhotoClick,
                    needToShowShareButton = true,
                    onShareClick = onShareClick
                )
            }
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
                            message = e.error.localizedMessage
                                ?: stringResource(id = R.string.something_went_wrong),
                            modifier = Modifier.fillParentMaxSize(),
                            onClickRetry = { retry() }
                        )
                    }
                }
                loadState.append is LoadState.Error -> {
                    val e = pagedPhotos.loadState.append as LoadState.Error
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