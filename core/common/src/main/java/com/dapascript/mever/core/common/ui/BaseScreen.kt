package com.dapascript.mever.core.common.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24

@Composable
fun BaseScreen(
    isHome: Boolean = false,
    listMenuAction: Map<Int, String> = emptyMap(),
    modifier: Modifier = Modifier,
    onClickBack: () -> Unit = {},
    onClickMenu: (String) -> Unit = {},
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dp24)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = CenterVertically,
            horizontalArrangement = SpaceBetween
        ) {
            Image(
                painter = painterResource(id = if (isHome) R.drawable.ic_mever else R.drawable.ic_back),
                contentDescription = "Logo or back",
                modifier = if (isHome.not()) Modifier.clickable { onClickBack() } else Modifier
            )
            Row(
                horizontalArrangement = spacedBy(Dp16)
            ) {
                listMenuAction.forEach { (resource, name) ->
                    Image(
                        painter = painterResource(id = resource),
                        contentDescription = name,
                        modifier = Modifier.clickable { onClickMenu(name) }
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) { content() }
    }
}