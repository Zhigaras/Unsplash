package com.zhigaras.unsplash.presentation.compose.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.zhigaras.unsplash.R
import com.zhigaras.unsplash.presentation.MainViewModel
import com.zhigaras.unsplash.presentation.compose.screens.feedscreen.PhotoItemCard

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CollectionDetailsScreen(
    collectionId: String,
    viewModel: MainViewModel = hiltViewModel(),
    onPhotoClick: (String) -> Unit
) {
    val pagedPhotos =
        viewModel.getPagedPhotos(collectionId = collectionId).collectAsLazyPagingItems()
    val context = LocalContext.current
    
    val collection = viewModel.selectedCollectionFlow.collectAsState().value
    Log.d("AAA nullableCollection", collection.toString())
    Column() {
        if (collection != null) {
            Box(modifier = Modifier.height(100.dp)) {
                GlideImage(
                    model = Uri.parse(collection.coverPhoto.urls.regular),
                    contentDescription = stringResource(
                        id = R.string.collection_details
                    ),
                    contentScale = ContentScale.FillWidth,
                    alpha = 0.5f
                )
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(text = collection.title.uppercase())
                    Text(text = collection.tagsString)
                    Text(text = collection.description ?: "")
                }
            }
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(pagedPhotos) { photoItem ->
                photoItem?.let {
                    PhotoItemCard(
                        photoItem = photoItem,
                        onLikeClick = { isLiked, photoId ->
                            viewModel.onLikeClick(
                                isLiked,
                                photoId
                            )
                        },
                        onPhotoClick = onPhotoClick,
                        needToShowShareButton = true,
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
            }
        }
    }
}