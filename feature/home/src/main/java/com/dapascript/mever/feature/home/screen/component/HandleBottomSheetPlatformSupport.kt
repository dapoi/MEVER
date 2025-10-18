package com.dapascript.mever.feature.home.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.component.MeverBottomSheet
import com.dapascript.mever.core.common.ui.component.MeverPlatformSupportedItem
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp1
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp10
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp48
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverLightViolet
import com.dapascript.mever.core.common.ui.theme.MeverPurple
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp18
import com.dapascript.mever.core.common.util.PlatformType
import com.dapascript.mever.core.common.util.PlatformType.AI
import com.dapascript.mever.core.common.util.PlatformType.ALL
import com.dapascript.mever.core.common.util.PlatformType.EXPLORE
import com.dapascript.mever.core.common.util.PlatformType.YOUTUBE
import com.dapascript.mever.core.common.util.PlatformType.YOUTUBE_MUSIC
import com.dapascript.mever.core.common.util.onCustomClick

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HandleBottomSheetPlatformSupport(
    showPlatformSupportDialog: Boolean,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) = MeverBottomSheet(
    modifier = modifier,
    skipPartiallyExpanded = false,
    shouldDismissOnClickOutside = true,
    showBottomSheet = showPlatformSupportDialog,
    onDismissBottomSheet = onDismiss
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dp8),
            text = stringResource(R.string.platforms_supported),
            textAlign = TextAlign.Center,
            style = typography.bodyBold1.copy(fontSize = Sp18),
            color = colorScheme.onPrimary
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = Dp1,
            color = colorScheme.onPrimary.copy(alpha = 0.12f)
        )
        Column(
            modifier = Modifier
                .weight(weight = 1f, fill = false)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Dp16),
            verticalArrangement = Arrangement.spacedBy(Dp16)
        ) {
            Spacer(modifier = Modifier.size(Dp4))
            val platforms = PlatformType.entries
                .filterNot { it in listOf(AI, ALL, EXPLORE, YOUTUBE, YOUTUBE_MUSIC) }
                .map { it.platformName }

            platforms.forEachIndexed { index, platform ->
                MeverPlatformSupportedItem(
                    modifier = Modifier.fillMaxWidth(),
                    iconSize = Dp48,
                    iconPadding = Dp10,
                    platformName = platform
                )

                if (index < platforms.size - 1) {
                    HorizontalDivider()
                }
            }
            HorizontalDivider()
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dp16),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = MeverLightViolet,
                            shape = CircleShape
                        )
                        .size(Dp48),
                    contentAlignment = Center
                ) {
                    Text(
                        text = "+2",
                        textAlign = TextAlign.Center,
                        style = typography.bodyBold1,
                        color = MeverPurple
                    )
                }
                Text(
                    text = stringResource(R.string.more),
                    style = typography.body1,
                    color = colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.size(Dp4))
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = Dp1,
            color = colorScheme.onPrimary.copy(alpha = 0.12f)
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dp16, vertical = Dp8),
            text = stringResource(R.string.disclaimer_supported_platforms),
            textAlign = TextAlign.Center,
            style = typography.body2,
            color = colorScheme.onPrimary.copy(alpha = 0.6f)
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .onCustomClick { onDismiss() }
                .padding(horizontal = Dp16, vertical = Dp8),
            text = stringResource(R.string.close),
            textAlign = TextAlign.Center,
            style = typography.bodyBold1,
            color = colorScheme.primary
        )
    }
}