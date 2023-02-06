package com.zhigaras.unsplash.presentation.compose.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import com.zhigaras.unsplash.R
import com.zhigaras.unsplash.data.remote.ApiStatus.*
import com.zhigaras.unsplash.model.photodetails.PhotoDetails
import com.zhigaras.unsplash.model.photodetails.Tag
import com.zhigaras.unsplash.presentation.MainViewModel
import com.zhigaras.unsplash.presentation.compose.screens.mainscreen.PhotoBottomInfo

@Composable
fun DetailsScreen(
    photoId: String,
    viewModel: MainViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getPhotoDetail(photoId)
    }
    val photoDetails = viewModel.photoDetailsFlow.collectAsState().value
    
    when (photoDetails.status) {
        LOADING -> {
            DetailsSet(
                modifier = Modifier.placeholder(
                    visible = true,
                    color = Color.Gray,
                    highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White)
                ),
                photoDetails = photoDetails.data
            )
        }
        SUCCESS -> {
            DetailsSet(photoDetails = photoDetails.data)
        }
        ERROR -> TODO()
    }
    
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailsSet(
    modifier: Modifier = Modifier,
    photoDetails: PhotoDetails?
) {
    Column(modifier = modifier.padding(8.dp)) {
        if (photoDetails != null) {
            Box(modifier.fillMaxWidth()) {
                GlideImage(model = photoDetails.urls.regular, contentDescription = null)
                PhotoBottomInfo(
                    modifier = modifier,
                    userProfileImage = photoDetails.user.profileImage.medium,
                    userName = photoDetails.user.name,
                    userInstagramName = photoDetails.user.instagramUsername,
                    likes = photoDetails.likes,
                    isLiked = photoDetails.likedByUser
                )
            }
            photoDetails.location.name?.let {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.location_icon),
                        contentDescription = null
                    )
                    Text(text = it)
                }
            }
            Text(
                text = photoDetails.tags.toHashTagString(),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

fun List<Tag>.toHashTagString(): String {
    val tags = this.filter { it.type == "search" }
    return buildString {
        tags.forEach {
            append(" #")
            append(it.title)
        }
    }
}