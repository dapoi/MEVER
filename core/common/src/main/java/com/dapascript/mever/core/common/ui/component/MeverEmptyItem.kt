package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale.Companion.Fit
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp250
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp40
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme.colors
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import androidx.compose.ui.text.style.TextAlign.Companion.Center as TextAlignCenter

@Composable
fun MeverEmptyItem(
    image: Int,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    imageSize: Dp = Dp250,
    isHorizontal: Boolean = false,
    actionButtonLabel: String? = null,
    onClickAction: (() -> Unit)? = null
) {
    if (isHorizontal) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Center
        ) {
            MeverImage(
                modifier = Modifier.size(imageSize),
                source = image,
                contentScale = Fit
            )
            Spacer(modifier = Modifier.size(Dp16))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Center
            ) {
                MeverEmptyItemContent(
                    title = title,
                    description = description,
                    textAlign = TextAlign.Start,
                    actionButtonLabel = actionButtonLabel,
                    onClickAction = onClickAction
                )
            }
        }
    } else {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Center
        ) {
            MeverImage(
                modifier = Modifier.size(imageSize),
                source = image,
                contentScale = Fit
            )
            MeverEmptyItemContent(
                title = title,
                description = description,
                textAlign = TextAlignCenter,
                actionButtonLabel = actionButtonLabel,
                onClickAction = onClickAction
            )
        }
    }
}

@Composable
private fun MeverEmptyItemContent(
    title: String,
    description: String,
    textAlign: TextAlign,
    actionButtonLabel: String?,
    onClickAction: (() -> Unit)?
) {
    Text(
        text = title,
        textAlign = textAlign,
        style = typography.h4,
        color = colors.blackWhite
    )
    Spacer(modifier = Modifier.size(Dp8))
    Text(
        text = description,
        textAlign = textAlign,
        style = typography.body2,
        color = colors.grayLightGray
    )
    onClickAction?.let {
        Spacer(modifier = Modifier.size(Dp24))
        MeverButton(
            modifier = Modifier.height(Dp40),
            title = actionButtonLabel ?: stringResource(R.string.ok),
            buttonType = MeverButtonAttr.MeverButtonType.Filled(
                backgroundColor = colors.alwaysPurple,
                contentColor = colors.alwaysWhite
            )
        ) { onClickAction() }
    }
}
