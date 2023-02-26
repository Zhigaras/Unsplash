package com.zhigaras.unsplash.presentation.compose.screens.feedscreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.core.net.toUri
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.zhigaras.unsplash.R
import com.zhigaras.unsplash.data.remote.ApiStatus.*
import com.zhigaras.unsplash.domain.toShortForm
import com.zhigaras.unsplash.model.photoentity.PhotoEntity
import com.zhigaras.unsplash.model.photoentity.User

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PhotoItemCard(
    photoItem: PhotoEntity,
    modifier: Modifier = Modifier,
    onPhotoClick: (String) -> Unit = {},
    onLikeClick: (Boolean, String) -> Unit,
    needToShowShareButton: Boolean = false,
    onShareClick: (String) -> Unit = {}
) {
    val aspectRatio = photoItem.width.toFloat() / photoItem.height.toFloat()
    Box(
        modifier = modifier
            .aspectRatio(aspectRatio)
            .clickable { onPhotoClick(photoItem.id) }
            .background(color = Color(photoItem.color.toColorInt()))
    ) {
        GlideImage(
            model = photoItem.urls.regular.toUri(),
            contentDescription = null
        )
        PhotoBottomInfo(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(40.dp),
            photo = photoItem,
            onLikeClick = onLikeClick,
        )
        if (needToShowShareButton) {
            Icon(
                imageVector = Icons.Default.Share, contentDescription = "share",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(48.dp)
                    .padding(8.dp)
                    .clickable { onShareClick(photoItem.photo_links.html) })
        }
    }
}

@Composable
fun PhotoBottomInfo(
    modifier: Modifier,
    photo: PhotoEntity,
    onLikeClick: (Boolean, String) -> Unit,
) {
    with(photo) {
        Row(
            modifier = modifier
                .padding(4.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserInfoArea(user = photo.user, modifier = Modifier.weight(1f))
            LikesArea(
                likes = likes,
                _isLiked = likedByUser,
                enabled = true,
                onLikeClick = onLikeClick,
                photoId = id
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserInfoArea(
    user: User,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
            model = user.profileImage.medium.toUri(),
            contentDescription = stringResource(R.string.user_profile_image)
        ) {
            it.circleCrop()
        }
        Column(modifier = Modifier, verticalArrangement = Arrangement.SpaceEvenly) {
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
    
    Text(text = likes.toShortForm())
    IconToggleButton(
        checked = _isLiked,
        onCheckedChange = { onLikeClick(_isLiked, photoId); Log.d("AAA", "1clicked") },
        modifier = Modifier
            .padding(vertical = 4.dp),
        enabled = enabled
    ) {
        Icon(
            painter = painterResource(id = if (_isLiked) R.drawable.is_liked_icon else R.drawable.is_not_liked_2),
            contentDescription = null
        )
    }
}