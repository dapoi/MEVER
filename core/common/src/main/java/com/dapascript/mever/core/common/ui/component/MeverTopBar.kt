package com.dapascript.mever.core.common.ui.component

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.centerAlignedTopAppBarColors
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.actions
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.navigationIcon
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeverTopBar(
    topBarArgs: TopBarArgs,
    useCenterTopBar: Boolean = true,
    modifier: Modifier = Modifier
) = with(topBarArgs) {
    if (useCenterTopBar) {
        CenterAlignedTopAppBar(
            modifier = modifier,
            title = title(screenName),
            navigationIcon = navigationIcon(
                screenName = screenName,
                onClickBack = onClickBack
            ),
            actions = actions(actionMenus),
            colors = centerAlignedTopAppBarColors(
                containerColor = topBarColor ?: colorScheme.background,
                navigationIconContentColor = iconBackColor ?: colorScheme.onPrimary,
                titleContentColor = titleColor ?: colorScheme.onPrimary,
                actionIconContentColor = actionMenusColor ?: colorScheme.onPrimary
            )
        )
    } else {
        TopAppBar(
            modifier = modifier,
            title = title(screenName),
            navigationIcon = navigationIcon(
                screenName = screenName,
                onClickBack = onClickBack
            ),
            actions = actions(actionMenus),
            colors = topAppBarColors(
                containerColor = topBarColor ?: colorScheme.background,
                navigationIconContentColor = iconBackColor ?: colorScheme.onPrimary,
                titleContentColor = titleColor ?: colorScheme.onPrimary,
                actionIconContentColor = actionMenusColor ?: colorScheme.onPrimary
            )
        )
    }
}