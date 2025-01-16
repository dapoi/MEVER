package com.dapascript.mever.feature.setting.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.hilt.navigation.compose.hiltViewModel
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.navigation.base.BaseNavigator
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.util.Constant.ScreenName.SETTING
import com.dapascript.mever.feature.setting.viewmodel.SettingLandingViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun SettingLandingScreen(
    navigator: BaseNavigator,
    viewModel: SettingLandingViewModel = hiltViewModel()
) = with(viewModel) {
    val scrollState = rememberScrollState()
    val isExpanded by remember { derivedStateOf { scrollState.value == 0 } }

    BaseScreen(
        topBarArgs = TopBarArgs(
            screenName = if (isExpanded.not()) SETTING else "",
            onClickBack = { navigator.popBackStack() }
        )
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = SETTING,
                    style = typography.h2.copy(fontWeight = SemiBold),
                    color = colorScheme.onPrimary
                )
                Column(
                    modifier = Modifier
                        .height(this@BoxWithConstraints.maxHeight)
                        .nestedScroll(
                            object : NestedScrollConnection {
                                override fun onPreScroll(
                                    available: Offset,
                                    source: NestedScrollSource
                                ) = if (available.y > 0 || isExpanded) Offset.Zero
                                else Offset(x = 0f, y = -scrollState.dispatchRawDelta(-available.y))
                            }
                        )
                ) {
                    CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(settingMenus) { Text(it) }
                        }
                    }
                }
            }
        }
    }
}