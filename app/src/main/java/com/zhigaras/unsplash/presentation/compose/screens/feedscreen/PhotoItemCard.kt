package com.zhigaras.unsplash.presentation.compose.screens.feedscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.zhigaras.unsplash.data.locale.db.PhotoEntity
import com.zhigaras.unsplash.domain.toShortForm

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PhotoItemCard(
    photoItem: PhotoEntity,
    itemWidth: Dp,
    childModifier: Modifier = Modifier,
    onPhotoClick: (String) -> Unit,
    onLikeClick: () -> Unit
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
            model = photoItem.urlRegular.toUri(),
            contentDescription = null,
            modifier = childModifier
        )
        PhotoBottomInfo(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(40.dp),
            userProfileImage = photoItem.userProfileImage,
            userName = photoItem.userUsername,
            userInstagramName = photoItem.userInstagramUsername,
            likes = photoItem.likes,
            _isLiked = photoItem.likedByUser,
            onLikeClick = onLikeClick
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PhotoBottomInfo(
    modifier: Modifier,
    userProfileImage: String,
    userName: String?,
    userInstagramName: String?,
    likes: Int,
    _isLiked: Boolean,
    onLikeClick: () -> Unit = {}
) {
    var isLiked by remember { mutableStateOf(_isLiked) }
    
    Row(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
            model = userProfileImage.toUri(),
            contentDescription = stringResource(R.string.user_profile_image),
            modifier = Modifier.padding(end = 4.dp)
        ) {
            it.circleCrop()
            
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.SpaceEvenly) {
            Text(
                text = userName.toString(),
                maxLines = 1,
                style = MaterialTheme.typography.labelMedium,
                overflow = TextOverflow.Ellipsis
            )
            if (userInstagramName != null && userInstagramName != "") {
                val userInstagram = if (userInstagramName.contains('/'))
                    userInstagramName.split('/').last { it != "" }
                else userInstagramName
                Text(
                    text = "@$userInstagram",
                    maxLines = 1,
                    style = MaterialTheme.typography.labelSmall,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Text(text = likes.toShortForm())
        IconToggleButton(checked = isLiked,
            onCheckedChange = { isLiked = !isLiked },
            modifier = Modifier
                .padding(4.dp)
                .clickable { onLikeClick() }
        ) {
            Icon(
                painter = painterResource(id = if (isLiked) R.drawable.is_liked_icon else R.drawable.is_not_liked_2),
                contentDescription = null
            )
        }
    }
}