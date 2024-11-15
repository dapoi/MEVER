package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp1
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp48

@Composable
fun MeverDownloadButton(
    enabled: Boolean,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) = Box(
    modifier = modifier
        .size(Dp48)
        .background(
            color = if (enabled) colorScheme.primary else colorScheme.onSurface.copy(alpha = 0.12f),
            shape = CircleShape
        ),
    contentAlignment = Center
) {
    if (isLoading) CircularProgressIndicator(
        modifier = Modifier.size(Dp20),
        strokeCap = Round,
        color = colorScheme.onSecondary
    ) else Button(
        modifier = Modifier.fillMaxSize(),
        enabled = enabled,
        contentPadding = PaddingValues(Dp1),
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.size(Dp20),
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_download),
            contentDescription = "Download",
            tint = colorScheme.onSecondary
        )
    }
}