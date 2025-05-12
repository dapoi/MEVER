package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp250
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import androidx.compose.ui.text.style.TextAlign.Companion.Center as TextAlignCenter

@Composable
fun MeverEmptyItem(
    image: Int,
    description: String,
    modifier: Modifier = Modifier,
    size: Dp = Dp250
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
        contentScale = Crop,
        contentDescription = "Empty Ilustration"
    )
    Spacer(modifier = Modifier.size(Dp8))
    Text(
        text = description,
        textAlign = TextAlignCenter,
        style = typography.body1,
        color = colorScheme.onPrimary
    )
}