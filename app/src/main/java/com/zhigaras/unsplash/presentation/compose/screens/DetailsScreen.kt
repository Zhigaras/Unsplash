package com.zhigaras.unsplash.presentation.compose.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.zhigaras.unsplash.data.remote.ApiResult
import com.zhigaras.unsplash.data.remote.ApiStatus.*
import com.zhigaras.unsplash.domain.ExifText
import com.zhigaras.unsplash.domain.toShortForm
import com.zhigaras.unsplash.model.LikeResponseModel
import com.zhigaras.unsplash.model.photoentity.PhotoEntity
import com.zhigaras.unsplash.presentation.MainViewModel
import com.zhigaras.unsplash.presentation.compose.ErrorView
import com.zhigaras.unsplash.presentation.compose.screens.feedscreen.PhotoBottomInfo

@Composable
fun DetailsScreen(
    photoId: String,
    viewModel: MainViewModel = hiltViewModel(),
    onDownloadClick: (String, String) -> Unit
) {
//    LaunchedEffect(key1 = Unit) {
//        viewModel.getPhotoDetail(photoId)
//    }
    val photoDetails = viewModel.getPhotoDetail(photoId).collectAsState(initial = ApiResult.Loading()).value
    val likeChangingState = viewModel.likeChangingFlow.collectAsState()
    val context = LocalContext.current
    
    when (photoDetails.status) {
        LOADING, NOT_LOADED_YET -> {
            DetailsSet(
                modifier = Modifier.placeholder(
                    visible = true,
                    color = Color.Gray,
                    highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White)
                ),
                photoDetails = photoDetails.data,
                likeChangingState = likeChangingState
            )
        }
        SUCCESS -> {
            DetailsSet(
                photoDetails = photoDetails.data,
                modifier = Modifier.fillMaxWidth(),
                onLocationClick = {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(it)
                        )
                    )
                },
                onShareClick = {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.putExtra(Intent.EXTRA_TEXT, it)
                    intent.type = "text/plain"
                    try {
                        context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    }
                },
                onDownloadClick = onDownloadClick,
                likeChangingState = likeChangingState,
                onLikeClick = { isLiked, id -> viewModel.onLikeClick(isLiked, id)}
            )
        }
        ERROR -> ErrorView(message = photoDetails.errorMessage ?: "")
    }
}

@Composable
fun DetailsSet(
    modifier: Modifier = Modifier,
    photoDetails: PhotoEntity?,
    onLocationClick: (String) -> Unit = {},
    onShareClick: (String) -> Unit = {},
    onDownloadClick: (String, String) -> Unit = { _, _ -> },
    likeChangingState: State<ApiResult<LikeResponseModel>>,
    onLikeClick: (Boolean, String) -> Unit = { _, _ -> }
) {
    Column(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .verticalScroll(state = ScrollState(0))
    ) {
        if (photoDetails != null) {
            PhotoBlock(
                modifier.padding(vertical = 8.dp),
                photoDetails,
                onShareClick = onShareClick,
                likeChangingState = likeChangingState,
                onLikeClick = onLikeClick
            )
            LocationBlock(
                modifier.padding(horizontal = 4.dp),
                photoDetails,
                onLocationClick = { onLocationClick(it) }
            )
            TagsBlock(modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp), photoDetails)
            AboutBlock(modifier, photoDetails)
            DownloadBlock(
                modifier = modifier,
                onDownloadClick = onDownloadClick,
                photoDetails = photoDetails
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PhotoBlock(
    modifier: Modifier,
    photoDetails: PhotoEntity,
    onShareClick: (String) -> Unit,
    likeChangingState: State<ApiResult<LikeResponseModel>>,
    onLikeClick: (Boolean, String) -> Unit
) {
    Box(modifier) {
        GlideImage(model = photoDetails.urls.regular, contentDescription = null)
        Icon(
            imageVector = Icons.Default.Share, contentDescription = "share",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(48.dp)
                .padding(8.dp)
                .clickable { onShareClick(photoDetails.links.html) })
        PhotoBottomInfo(
            modifier = Modifier
                .height(40.dp)
                .align(Alignment.BottomCenter),
            photo = photoDetails,
            likeChangingState = likeChangingState,
            onLikeClick = onLikeClick
        )
    }
}

@Composable
fun LocationBlock(
    modifier: Modifier,
    photoDetails: PhotoEntity,
    onLocationClick: (String) -> Unit
) {
    photoDetails.location?.locationName?.let {
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
    photoDetails: PhotoEntity
) {
    if (photoDetails.tagsString.isNotBlank()) {
        Text(
            text = photoDetails.tagsString,
            modifier = modifier
                .horizontalScroll(ScrollState(0)),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AboutBlock(
    modifier: Modifier,
    photoDetails: PhotoEntity
) {
    Row(
        modifier = modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(modifier.weight(1f)) {
            photoDetails.exif?.let {
                
                it.make?.ExifText(fieldName = R.string.made_with)
                it.model?.ExifText(fieldName = R.string.model)
                it.exposureTime?.ExifText(fieldName = R.string.exposure)
                it.aperture?.ExifText(fieldName = R.string.aperture)
                it.focalLength?.ExifText(fieldName = R.string.focal_length)
                it.iso?.ExifText(fieldName = R.string.iso)
            }
        }
        
        Column(modifier.weight(1f)) {
            photoDetails.user.bio?.let {
                Text(
                    text = buildString {
                        append(stringResource(R.string.about))
                        append(photoDetails.user.fullName)
                        append(":")
                    },
                    style = MaterialTheme.typography.titleSmall
                )
                Text(text = it, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun DownloadBlock(
    modifier: Modifier,
    onDownloadClick: (String, String) -> Unit,
    photoDetails: PhotoEntity
) {
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onDownloadClick(photoDetails.urls.regular, photoDetails.id)
            }
    ) {
        Text(
            text = stringResource(R.string.download),
            textDecoration = TextDecoration.Underline
        )
        Text(text = buildString {
            append(" (")
            append(photoDetails.downloads!!.toShortForm())
            append(")")
        })
        Image(
            painter = painterResource(id = R.drawable.download_icon),
            contentDescription = null
        )
    }
}

//@Preview
//@Composable
//fun LocationBlockPreview() {
//    LocationBlock(modifier = Modifier, photoDetails = PhotoDetails())
//}