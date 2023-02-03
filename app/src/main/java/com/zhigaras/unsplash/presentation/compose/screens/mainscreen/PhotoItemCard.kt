package com.zhigaras.unsplash.presentation.compose.screens.mainscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.zhigaras.unsplash.R
import com.zhigaras.unsplash.data.locale.db.PhotoEntity

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PhotoItemCard(
    photoItem: PhotoEntity
) {
    
    Box(modifier = Modifier.size(100.dp)) {
        
        GlideImage(
            model = photoItem.urlRegular,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        ) { requestBuilder ->
            requestBuilder.placeholder(R.drawable.icon_background)
        }
        
        Text(text = photoItem.id, modifier = Modifier.fillMaxSize())
    }
    
}

@Preview(showSystemUi = true)
@Composable
fun PhotoItemPreview() {
    PhotoItemCard(photoItem = TestPhotoModel.testPhotoModel)
    
}