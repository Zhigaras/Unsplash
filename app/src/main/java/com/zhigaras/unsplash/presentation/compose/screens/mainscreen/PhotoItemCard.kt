package com.zhigaras.unsplash.presentation.compose.screens.mainscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.zhigaras.unsplash.data.locale.db.PhotoEntity

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PhotoItemCard(
    photoItem: PhotoEntity
) {
    
    Box() {
        
        GlideImage(model = photoItem.urlRegular, contentDescription = null, modifier = Modifier.fillMaxSize())
        
        Text(text = photoItem.id)
    }
    
}