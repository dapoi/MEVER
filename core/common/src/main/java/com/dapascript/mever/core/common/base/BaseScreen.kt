package com.dapascript.mever.core.common.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.attr.ActionMenuAttr.ActionMenu
import com.dapascript.mever.core.common.ui.component.MeverActionButton
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.util.clickableSingle

@Composable
fun BaseScreen(
    actionMenus: List<ActionMenu>,
    screenName: String = "",
    modifier: Modifier = Modifier,
    onClickBack: () -> Unit = {},
    onClickActionMenu: (String) -> Unit = {},
    content: @Composable () -> Unit
) = Column(
    modifier = modifier
        .fillMaxSize()
        .padding(Dp24)
) {
    Box(contentAlignment = Center) {
        screenName.isNotEmpty().let {
            Text(
                text = screenName,
                style = typography.h7
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = CenterVertically,
            horizontalArrangement = SpaceBetween
        ) {
            Image(
                painter = painterResource(id = if (screenName.isEmpty()) R.drawable.ic_mever else R.drawable.ic_back),
                contentDescription = "Back",
                modifier = if (screenName.isNotEmpty()) {
                    Modifier
                        .clip(CircleShape)
                        .clickableSingle { onClickBack() }
                } else Modifier
            )
            actionMenus.isNotEmpty().let {
                Row(horizontalArrangement = spacedBy(Dp16)) {
                    actionMenus.forEach { (resource, name, showBadge) ->
                        MeverActionButton(
                            resource = resource,
                            name = name,
                            showBadge = showBadge
                        ) { onClickActionMenu(name) }
                    }
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) { content() }
}