package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp1
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp48
import com.dapascript.mever.core.common.ui.theme.MeverPurple

@Composable
fun MeverDownloadButton(
    enabled: Boolean,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = modifier.size(Dp48),
        contentAlignment = Center
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(MeverPurple),
                contentAlignment = Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(Dp20),
                    strokeCap = Round,
                    color = colorScheme.onSecondary
                )
            }
        } else Button(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
            enabled = enabled,
            colors = buttonColors(
                containerColor = MeverPurple,
                disabledContainerColor = colorScheme.onSurface
            ),
            contentPadding = PaddingValues(Dp1),
            onClick = {
                onClick()
                keyboardController?.hide()
            }
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