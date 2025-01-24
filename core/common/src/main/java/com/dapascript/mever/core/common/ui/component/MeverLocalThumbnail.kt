package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import coil3.compose.AsyncImage

@Composable
fun MeverLocalThumbnail(
    source: Any,
    modifier: Modifier = Modifier
) {
    var showShimmer by remember { mutableStateOf(true) }

    AsyncImage(
        modifier = modifier.background(meverShimmer(showShimmer = true)),
        model = source,
        contentScale = Crop,
        onSuccess = { showShimmer = false },
        contentDescription = "Thumbnail"
    )
}