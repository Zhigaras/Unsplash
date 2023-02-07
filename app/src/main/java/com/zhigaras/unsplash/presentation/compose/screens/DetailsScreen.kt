package com.zhigaras.unsplash.presentation.compose.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
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
import com.zhigaras.unsplash.presentation.compose.ErrorView
import com.zhigaras.unsplash.presentation.compose.screens.searchscreen.PhotoBottomInfo

@Composable
fun DetailsScreen(
    photoId: String,
    viewModel: MainViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getPhotoDetail(photoId)
    }
    val photoDetails = viewModel.photoDetailsFlow.collectAsState().value
    
    val checkLocationLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { }
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
            DetailsSet(
                photoDetails = photoDetails.data,
                modifier = Modifier.fillMaxWidth(),
                onLocationClick = {
                    checkLocationLauncher.launch(Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(it)
                    ))
                }
            )
        }
        ERROR -> ErrorView(message = stringResource(R.string.no_internet_connection))
    }
}

@Composable
fun DetailsSet(
    modifier: Modifier = Modifier,
    photoDetails: PhotoDetails?,
    onLocationClick: (String) -> Unit = {}
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .verticalScroll(state = ScrollState(0))
    ) {
        if (photoDetails != null) {
            PhotoBlock(modifier.padding(bottom = 8.dp), photoDetails)
            LocationBlock(modifier.padding(horizontal = 4.dp), photoDetails, onLocationClick = onLocationClick)
            TagsBlock(modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp), photoDetails)
            AboutBlock(modifier, photoDetails)
            DownloadBlock(modifier)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PhotoBlock(
    modifier: Modifier,
    photoDetails: PhotoDetails
) {
    Box(modifier) {
        GlideImage(model = photoDetails.urls.regular, contentDescription = null)
        PhotoBottomInfo(
            modifier = modifier
                .height(40.dp)
                .align(Alignment.BottomCenter),
            userProfileImage = photoDetails.user.profileImage.medium,
            userName = photoDetails.user.name,
            userInstagramName = photoDetails.user.instagramUsername,
            likes = photoDetails.likes,
            isLiked = photoDetails.likedByUser
        )
    }
}

@Composable
fun LocationBlock(
    modifier: Modifier,
    photoDetails: PhotoDetails,
    onLocationClick: (String) -> Unit
) {
    photoDetails.location.name?.let {
        val lat = photoDetails.location.position.latitude
        val lon = photoDetails.location.position.longitude
        Row(
            modifier = modifier.clickable { onLocationClick("geo:$lat,$lon?z=14") },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.location_icon),
                contentDescription = null
            )
            Text(text = it, modifier = modifier.horizontalScroll(ScrollState(0)))
        }
    }
}

@Composable
fun TagsBlock(
    modifier: Modifier,
    photoDetails: PhotoDetails
) {
    if (photoDetails.tags.hasSearchTags()) {
        Text(
            text = photoDetails.tags.toHashTagString(),
            modifier = modifier
                .horizontalScroll(ScrollState(0)),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AboutBlock(
    modifier: Modifier,
    photoDetails: PhotoDetails
) {
    Row(
        modifier = modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(modifier.weight(1f)) {
            photoDetails.exif.make?.ExifText(fieldName = R.string.made_with)
            photoDetails.exif.model?.ExifText(fieldName = R.string.model)
            photoDetails.exif.exposureTime?.ExifText(fieldName = R.string.exposure)
            photoDetails.exif.aperture?.ExifText(fieldName = R.string.aperture)
            photoDetails.exif.focalLength?.ExifText(fieldName = R.string.focal_length)
            photoDetails.exif.iso?.ExifText(fieldName = R.string.iso)
        }
        Column(modifier.weight(1f)) {
            photoDetails.user.bio?.let {
                Text(text = buildString {
                    append(stringResource(R.string.about))
                    append(photoDetails.user.name)
                    append(":")
                }, style = MaterialTheme.typography.titleSmall)
                Text(text = it, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun DownloadBlock(modifier: Modifier) {
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = stringResource(R.string.download),
            textDecoration = TextDecoration.Underline
        )
        Image(
            painter = painterResource(id = R.drawable.download_icon),
            contentDescription = null
        )
    }
}

@Composable
fun String?.ExifText(@StringRes fieldName: Int) {
    this?.let {
        Text(text = buildString {
            append(stringResource(id = fieldName))
            append(it)
        }, style = MaterialTheme.typography.bodyMedium)
    }
}

fun List<Tag>.hasSearchTags(): Boolean {
    return this.any { it.type == "search" }
}

fun List<Tag>.toHashTagString(): String {
    val tags = this.filter { it.type == "search" }
    return buildString {
        tags.forEach {
            append(" #")
            append(it.title)
        }
    }.trim()
}

//@Preview
//@Composable
//fun LocationBlockPreview() {
//    LocationBlock(modifier = Modifier, photoDetails = PhotoDetails())
//}