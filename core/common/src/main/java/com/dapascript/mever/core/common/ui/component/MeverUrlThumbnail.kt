package com.dapascript.mever.core.common.ui.component

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.dapascript.mever.core.common.util.getPhotoThumbnail
import com.dapascript.mever.core.common.util.getUrlContentType
import com.dapascript.mever.core.common.util.getVideoThumbnail
import kotlinx.coroutines.delay

@Composable
fun MeverUrlThumbnail(
    source: String,
    modifier: Modifier = Modifier,
    isFailedFetchImage: Boolean = false
) {
    val thumbnail = remember(source) { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(thumbnail, isFailedFetchImage) {
        while (thumbnail.value == null && isFailedFetchImage.not()) {
            try {
                thumbnail.value = if (getUrlContentType(source) == ".jpg") getPhotoThumbnail(source)
                else getVideoThumbnail(source)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (thumbnail.value == null) delay(2000)
        }
    }

    MeverImage(
        source = thumbnail.value,
        modifier = modifier,
        isImageError = isFailedFetchImage
    )

//    AnimatedContent(
//        targetState = thumbnail.value,
//        transitionSpec = { (fadeIn() togetherWith fadeOut()).using(SizeTransform(clip = false)) },
//        label = "Video Thumbnail Anim"
//    ) { bitmap ->
//        bitmap?.let {
//            AsyncImage(
//                model = it,
//                contentScale = Crop,
//                contentDescription = "Thumbnail",
//                modifier = modifier
//            )
//        } ?: if (isFailedFetchImage) Box(
//            modifier = modifier.background(MeverLightGray),
//            contentAlignment = Center
//        ) {
//            Image(
//                imageVector = ImageVector.vectorResource(R.drawable.ic_broken_image),
//                colorFilter = tint(MeverGray),
//                contentDescription = "Error",
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(Dp24)
//            )
//        } else Box(modifier = modifier.background(meverShimmer()))
//    }
}