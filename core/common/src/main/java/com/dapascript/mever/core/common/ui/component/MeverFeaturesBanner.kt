package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp2
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.util.LocalDeviceType
import com.dapascript.mever.core.common.util.onCustomClick

@Composable
fun MeverFeaturesBanner(
    icon: Int,
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .shadow(elevation = Dp2, shape = RoundedCornerShape(Dp12))
            .background(
                color = colorScheme.surface,
                shape = RoundedCornerShape(Dp12)
            )
            .clip(RoundedCornerShape(Dp12))
            .onCustomClick { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(Dp8),
            horizontalArrangement = spacedBy(Dp8),
            verticalAlignment = CenterVertically
        ) {
            Image(
                modifier = Modifier.size(Dp24),
                imageVector = ImageVector.vectorResource(icon),
                contentDescription = title
            )
            Text(
                text = title,
                style = typography.bodyBold2,
                color = colorScheme.onPrimary
            )
        }
    }
}