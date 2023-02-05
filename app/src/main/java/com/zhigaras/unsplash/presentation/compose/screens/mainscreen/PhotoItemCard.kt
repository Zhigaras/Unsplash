package com.zhigaras.unsplash.presentation.compose.screens.mainscreen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.zhigaras.unsplash.R
import com.zhigaras.unsplash.data.locale.db.PhotoEntity
import kotlinx.coroutines.NonDisposableHandle.parent

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PhotoItemCard(
    photoItem: PhotoEntity,
    itemWidth: Dp
) {
    val imageHeight = photoItem.height * itemWidth / photoItem.width
    Log.d("AAA", "width - $itemWidth, height - $imageHeight")
    GlideImage(
        model = photoItem.urlRegular,
        contentDescription = null,
        modifier = Modifier.size(
            width = itemWidth,
            height = imageHeight
        )
    )

//        Text(text = photoItem.id, modifier = Modifier.fillMaxSize())

    
}

//@Preview(showSystemUi = true)
//@Composable
//fun PhotoItemPreview() {
//    PhotoItemCard(photoItem = TestPhotoModel.testPhotoModel)
//
//}