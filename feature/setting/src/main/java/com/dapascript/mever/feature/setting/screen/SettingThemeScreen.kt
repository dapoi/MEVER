package com.dapascript.mever.feature.setting.screen

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.hilt.navigation.compose.hiltViewModel
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.navigation.base.BaseNavigator
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.MeverRadioButton
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp1
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp3
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp40
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp64
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp32
import com.dapascript.mever.core.common.ui.theme.ThemeType
import com.dapascript.mever.core.common.ui.theme.ThemeType.System
import com.dapascript.mever.core.common.util.clickableSingle
import com.dapascript.mever.feature.setting.viewmodel.SettingThemeViewModel

@Composable
internal fun SettingThemeScreen(
    navigator: BaseNavigator,
    viewModel: SettingThemeViewModel = hiltViewModel()
) = with(viewModel) {
    val themeType = themeType.collectAsStateValue()
    val scrollState = rememberScrollState()
    val isExpanded by remember { derivedStateOf { scrollState.value <= titleHeight } }

    BaseScreen(
        topBarArgs = TopBarArgs(
            screenName = if (isExpanded.not()) "Theme" else "",
            onClickBack = { navigator.popBackStack() }
        ),
        allowScreenOverlap = true
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = Dp64)
                .systemBarsPadding()
        ) {
            if (isExpanded.not()) HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dp1)
                    .shadow(Dp3),
                thickness = Dp1,
                color = colorScheme.onPrimary.copy(alpha = 0.12f)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Dp24)
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(Dp16))
                Text(
                    text = "Theme",
                    style = typography.h2.copy(fontSize = Sp32),
                    color = colorScheme.onPrimary,
                    modifier = Modifier.onGloballyPositioned { titleHeight = it.size.height }
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
                    Spacer(modifier = Modifier.height(Dp40))
                    Text(
                        text = "Choose your preference",
                        style = typography.h3,
                        color = colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.height(Dp20))
                    ThemeType.entries.forEach { type ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = Dp12)
                                .clip(RoundedCornerShape(Dp8))
                                .clickableSingle { setThemeType(type) },
                            horizontalArrangement = spacedBy(Dp16),
                            verticalAlignment = CenterVertically
                        ) {
                            MeverRadioButton(
                                isChecked = themeType == type,
                                onCheckedChange = { setThemeType(type) }
                            )
                            Text(
                                text = if (type == System) "System Default" else type.name,
                                style = typography.bodyBold1,
                                color = colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}