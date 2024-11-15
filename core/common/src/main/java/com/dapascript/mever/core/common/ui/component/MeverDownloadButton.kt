package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp1
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp48

@Composable
fun MeverDownloadButton(
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier.height(Dp48)
    ) {
        Button(
            modifier = Modifier.size(Dp48),
            enabled = enabled,
            shape = CircleShape,
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
}