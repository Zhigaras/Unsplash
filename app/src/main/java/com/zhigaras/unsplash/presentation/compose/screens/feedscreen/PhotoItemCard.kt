package com.zhigaras.unsplash.presentation.compose.screens.feedscreen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.core.net.toUri
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.zhigaras.unsplash.R
import com.zhigaras.unsplash.data.remote.ApiResult
import com.zhigaras.unsplash.data.remote.ApiStatus.*
import com.zhigaras.unsplash.domain.toShortForm
import com.zhigaras.unsplash.model.LikeResponseModel
import com.zhigaras.unsplash.model.photoentity.PhotoEntity

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PhotoItemCard(
    photoItem: PhotoEntity,
    itemWidth: Dp,
    childModifier: Modifier = Modifier,
    onPhotoClick: (String) -> Unit,
    onLikeClick: (Boolean, String) -> Unit,
    likeChangingState: State<ApiResult<LikeResponseModel>>
) {
    val imageHeight = photoItem.height * itemWidth / photoItem.width
    
    Box(
        modifier = Modifier
            .size(
                width = itemWidth,
                height = imageHeight
            )
            .clickable { onPhotoClick(photoItem.id) }
    ) {
        GlideImage(
            model = photoItem.urls.regular.toUri(),
            contentDescription = null,
            modifier = childModifier
        )
        PhotoBottomInfo(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(40.dp),
            photo = photoItem,
            onLikeClick = onLikeClick,
            likeChangingState = likeChangingState
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PhotoBottomInfo(
    modifier: Modifier,
    photo: PhotoEntity,
    onLikeClick: (Boolean, String) -> Unit,
    likeChangingState: State<ApiResult<LikeResponseModel>>
) {
    with(photo) {
        Row(
            modifier = modifier
                .padding(4.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlideImage(
                model = user.profileImage.medium.toUri(),
                contentDescription = stringResource(R.string.user_profile_image),
                modifier = Modifier.padding(end = 4.dp)
            ) {
                it.circleCrop()
                
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.SpaceEvenly) {
                Text(
                    text = user.fullName.toString(),
                    maxLines = 1,
                    style = MaterialTheme.typography.labelMedium,
                    overflow = TextOverflow.Ellipsis
                )
                user.instagramUsernameCorrect.let {
                    if (it.isNotBlank()) {
                        Text(
                            text = "@$it",
                            maxLines = 1,
                            style = MaterialTheme.typography.labelSmall,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
            
            when (likeChangingState.value.status) {
                NOT_LOADED_YET, SUCCESS -> {
                    LikesArea(
                        likes = likes,
                        _isLiked = likedByUser,
                        enabled = true,
                        onLikeClick = onLikeClick,
                        photoId = id
                    )
                }
                ERROR -> {
                    Toast.makeText(
                        LocalContext.current,
                        likeChangingState.value.errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                LOADING -> {
                    LikesArea(
                        likes = likes,
                        _isLiked = likedByUser,
                        onLikeClick = onLikeClick,
                        enabled = false,
                        photoId = id
                    )
                }
            }
        }
    }
}

@Composable
fun LikesArea(
    likes: Int,
    _isLiked: Boolean,
    photoId: String,
    onLikeClick: (Boolean, String) -> Unit,
    enabled: Boolean
) {
    val isLiked by remember { mutableStateOf(_isLiked) }
    
    Text(text = likes.toShortForm())
    IconToggleButton(
        checked = isLiked,
        onCheckedChange = { onLikeClick(_isLiked, photoId); Log.d("AAA", "1clicked") },
        modifier = Modifier
            .padding(4.dp),
        enabled = enabled
    ) {
        Icon(
            painter = painterResource(id = if (isLiked) R.drawable.is_liked_icon else R.drawable.is_not_liked_2),
            contentDescription = null
        )
    }
}