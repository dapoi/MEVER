package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
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
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp40

@Composable
fun MeverImage(
    source: Any?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = Crop,
    isImageError: Boolean = false
) = Box(modifier = modifier) {
    val context = LocalContext.current

    AsyncImage(
        modifier = Modifier
            .fillMaxSize()
            .background(meverShimmer(showShimmer = true)),
        model = ImageRequest.Builder(context)
            .data(source)
            .crossfade(true)
            .build(),
        contentScale = contentScale,
        contentDescription = "Thumbnail"
    )
    if (isImageError) Image(
        modifier = Modifier
            .size(Dp40)
            .align(Center),
        imageVector = ImageVector.vectorResource(R.drawable.ic_broken_image),
        contentDescription = "Error Image"
    )
}