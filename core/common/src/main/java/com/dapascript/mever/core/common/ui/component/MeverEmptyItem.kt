package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp250
import com.dapascript.mever.core.common.ui.theme.MeverTheme.colors
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import androidx.compose.ui.text.style.TextAlign.Companion.Center as TextAlignCenter

@Composable
fun MeverEmptyItem(
    image: Int,
    description: String,
    modifier: Modifier = Modifier,
    size: Dp = Dp250,
    actionButtonLabel: String? = null,
    onClickAction: (() -> Unit)? = null
) = Column(
    modifier = modifier
        .fillMaxSize()
        .padding(Dp24),
    horizontalAlignment = CenterHorizontally,
    verticalArrangement = Center
) {
    Image(
        modifier = Modifier.size(size),
        painter = painterResource(image),
        contentDescription = "Empty Ilustration"
    )
    Spacer(modifier = Modifier.size(Dp16))
    Text(
        text = description,
        textAlign = TextAlignCenter,
        style = typography.body1,
        color = colors.blackWhite
    )
    onClickAction?.let {
        Spacer(modifier = Modifier.size(Dp16))
        MeverButton(
            title = actionButtonLabel ?: stringResource(R.string.ok),
            buttonType = MeverButtonAttr.MeverButtonType.Filled(
                backgroundColor = colors.alwaysPurple,
                contentColor = colors.alwaysWhite
            )
        ) { onClickAction() }
    }
}