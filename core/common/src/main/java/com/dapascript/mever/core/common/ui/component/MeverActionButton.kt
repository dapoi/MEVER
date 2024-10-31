package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.dapascript.mever.core.common.ui.attr.ActionMenuAttr.getContentDescription
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp15
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp2
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp32
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.util.Constant.ScreenName.NOTIFICATION

@Composable
fun MeverActionButton(
    resource: Int,
    name: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(modifier = modifier.size(Dp32)) {
        IconButton(onClick = onDebounceClick { onClick() }) {
            Icon(
                modifier = Modifier.size(Dp24),
                imageVector = ImageVector.vectorResource(resource),
                contentDescription = getContentDescription(name)
            )
        }
        if (name == NOTIFICATION) Badge(
            modifier = Modifier
                .size(Dp15)
                .border(width = Dp2, color = MeverWhite, shape = CircleShape)
                .align(TopEnd)
                .clip(CircleShape),
            containerColor = colorScheme.primary
        )
    }
}

@Composable
private fun onDebounceClick(
    debounceTimeMillis: Long = 1000L,
    onClick: () -> Unit
): () -> Unit {
    var lastClickTimeMillis: Long by remember { mutableLongStateOf(value = 0L) }
    return {
        System.currentTimeMillis().let { currentTimeMillis ->
            if ((currentTimeMillis - lastClickTimeMillis) >= debounceTimeMillis) {
                lastClickTimeMillis = currentTimeMillis
                onClick()
            }
        }
    }
}