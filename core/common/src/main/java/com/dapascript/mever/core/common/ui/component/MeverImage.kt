package com.dapascript.mever.core.common.ui.component

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp48
import com.dapascript.mever.core.common.ui.theme.MeverLightGray
import com.dapascript.mever.core.common.util.fetchPhotoFromUrl
import com.dapascript.mever.core.common.util.fetchVideoThumbnail
import com.dapascript.mever.core.common.util.getUrlContentType
import kotlinx.coroutines.delay

@Composable
fun MeverImage(
    source: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = Crop,
    isImageError: Boolean = false
) = Box(modifier = modifier) {
    val context = LocalContext.current
    var resultExtracted by remember(source) { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(source, resultExtracted, isImageError) {
        while (resultExtracted == null) {
            try {
                resultExtracted = if (getUrlContentType(source) == ".jpg") fetchPhotoFromUrl(source)
                else fetchVideoThumbnail(source)
            } catch (e: Exception) {
                e.printStackTrace()
                break
            }
            if (resultExtracted == null) delay(2000)
        }
    }

    if (isImageError) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MeverLightGray)
        )
        Image(
            modifier = Modifier
                .size(Dp48)
                .align(Center),
            imageVector = ImageVector.vectorResource(R.drawable.ic_broken_image),
            contentDescription = "Error Image"
        )
    } else {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .background(meverShimmer(true)),
            model = ImageRequest.Builder(context)
                .data(resultExtracted ?: source)
                .crossfade(true)
                .build(),
            contentScale = contentScale,
            contentDescription = "Thumbnail"
        )
    }
}