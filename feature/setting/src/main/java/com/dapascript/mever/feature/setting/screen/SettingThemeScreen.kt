package com.dapascript.mever.feature.setting.screen

import android.widget.Toast
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.MeverRadioButton
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp1
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp10
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp120
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp14
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp2
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp28
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp3
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp32
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp40
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp6
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp60
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp64
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverBlack
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.colors
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.typography
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp32
import com.dapascript.mever.core.common.ui.theme.ThemePalette
import com.dapascript.mever.core.common.ui.theme.ThemeType
import com.dapascript.mever.core.common.util.AppIconType
import com.dapascript.mever.core.common.util.state.collectAsStateValue
import com.dapascript.mever.core.navigation.helper.Navigator
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingPaywallRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingThemeRoute
import com.dapascript.mever.feature.setting.viewmodel.SettingThemeViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@Composable
internal fun SettingThemeScreen(
    navigator: Navigator,
    args: SettingThemeRoute,
    viewModel: SettingThemeViewModel = hiltViewModel()
) = with(viewModel) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    var themeType by remember { mutableStateOf(args.themeType) }
    var titleHeight by rememberSaveable { mutableIntStateOf(0) }
    val isExpanded by remember { derivedStateOf { scrollState.value < titleHeight / 2 } }

    val isProActive = isProActive.collectAsStateValue()
    val rawPalette = themePalette.collectAsStateValue()
    val selectedPalette = remember(rawPalette, isProActive) {
        if (isProActive || !rawPalette.isProOnly) rawPalette
        else ThemePalette.Default
    }
    val rawIcon = appIcon.collectAsStateValue()
    val selectedIcon = remember(rawIcon, isProActive) {
        if (isProActive || !rawIcon.isProOnly) rawIcon
        else AppIconType.Default
    }

    BaseScreen(
        topBarArgs = TopBarArgs(title = if (isExpanded.not()) stringResource(R.string.appearance_and_icons) else ""),
        onBackHandler = { navigator.navigateBack() }
    ) {
        LaunchedEffect(scrollState, titleHeight) {
            snapshotFlow { scrollState.isScrollInProgress }
                .filter { scroll -> scroll.not() }
                .collect {
                    if (titleHeight == 0) return@collect
                    val threshold = titleHeight / 2
                    if (scrollState.value in (threshold + 1)..<titleHeight) {
                        scope.launch { scrollState.animateScrollTo(titleHeight) }
                    } else if (scrollState.value in 1..threshold) {
                        scope.launch { scrollState.animateScrollTo(0) }
                    }
                }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = Dp64)
        ) {
            if (isExpanded.not()) HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dp1)
                    .shadow(Dp3),
                thickness = Dp1,
                color = colors.blackWhite.copy(alpha = 0.12f)
            )
            CompositionLocalProvider(LocalOverscrollFactory provides null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(bottom = Dp40)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { titleHeight = it.size.height }
                    ) {
                        Spacer(modifier = Modifier.height(Dp16))
                        Text(
                            text = stringResource(R.string.appearance_and_icons),
                            style = typography.h2.copy(fontSize = Sp32),
                            color = colors.blackWhite,
                            modifier = Modifier.padding(horizontal = Dp24)
                        )
                    }

                    // Section 1: Theme Mode
                    Spacer(modifier = Modifier.height(Dp32))
                    Text(
                        text = stringResource(R.string.theme_mode),
                        style = typography.h3,
                        color = colors.blackWhite,
                        modifier = Modifier.padding(horizontal = Dp24)
                    )
                    Spacer(modifier = Modifier.height(Dp16))
                    ThemeType.entries.forEach { type ->
                        MeverRadioButton(
                            value = stringResource(type.themeResId),
                            isChoosen = themeType == type
                        ) {
                            if (themeType == type) return@MeverRadioButton
                            themeType = type
                            setThemeType(type)
                        }
                    }

                    // Section 2: Color Palette
                    Spacer(modifier = Modifier.height(Dp32))
                    Text(
                        text = stringResource(R.string.color_theme),
                        style = typography.h3,
                        color = colors.blackWhite,
                        modifier = Modifier.padding(horizontal = Dp24)
                    )
                    Text(
                        text = stringResource(R.string.color_theme_desc),
                        style = typography.body2,
                        color = colors.blackWhite.copy(alpha = 0.65f),
                        modifier = Modifier.padding(horizontal = Dp24, vertical = Dp4)
                    )
                    Spacer(modifier = Modifier.height(Dp16))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dp24),
                        verticalArrangement = spacedBy(Dp10)
                    ) {
                        ThemePalette.entries.forEach { palette ->
                            val isSelected = selectedPalette == palette
                            val isLocked = palette.isProOnly && !isProActive

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(Dp16))
                                    .border(
                                        width = if (isSelected) Dp2 else Dp1,
                                        color = if (isSelected) colors.alwaysPurple else colors.blackWhite.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(Dp16)
                                    )
                                    .clickable {
                                        if (isLocked) {
                                            navigator.navigate(SettingPaywallRoute)
                                        } else {
                                            setThemePalette(palette)
                                        }
                                    },
                                color = if (isSelected) colors.lightPurpleDarkGray else colors.whiteDark
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = Dp16, vertical = Dp14),
                                    verticalAlignment = CenterVertically,
                                    horizontalArrangement = spacedBy(Dp14)
                                ) {
                                    // Palette preview circle
                                    Box(
                                        modifier = Modifier
                                            .size(Dp28)
                                            .clip(CircleShape)
                                            .background(palette.previewColor)
                                            .border(
                                                width = Dp1,
                                                color = if (palette == ThemePalette.Doodle) colors.blackWhite
                                                else colors.blackWhite.copy(alpha = 0.15f),
                                                shape = CircleShape
                                            )
                                    )

                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = spacedBy(Dp2)
                                    ) {
                                        Row(
                                            verticalAlignment = CenterVertically,
                                            horizontalArrangement = spacedBy(Dp8)
                                        ) {
                                            Text(
                                                text = stringResource(palette.titleRes),
                                                style = typography.bodyBold2,
                                                color = colors.blackWhite
                                            )
                                            if (palette.isProOnly) {
                                                ProBadge()
                                            }
                                        }
                                    }

                                    if (isSelected) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.ic_round_checked),
                                            contentDescription = "Selected",
                                            tint = colors.alwaysPurple,
                                            modifier = Modifier.size(Dp20)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Section 3: App Launcher Icon
                    Spacer(modifier = Modifier.height(Dp32))
                    Text(
                        text = stringResource(R.string.app_icon),
                        style = typography.h3,
                        color = colors.blackWhite,
                        modifier = Modifier.padding(horizontal = Dp24)
                    )
                    Text(
                        text = stringResource(R.string.app_icon_desc),
                        style = typography.body2,
                        color = colors.blackWhite.copy(alpha = 0.65f),
                        modifier = Modifier.padding(horizontal = Dp24, vertical = Dp4)
                    )
                    Spacer(modifier = Modifier.height(Dp16))

                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dp24),
                        horizontalArrangement = spacedBy(Dp20),
                        verticalArrangement = spacedBy(Dp16),
                        maxItemsInEachRow = 2
                    ) {
                        val cardWidth = Dp120
                        val appIconSuccessMsg = stringResource(R.string.app_icon_changed)

                        AppIconType.entries.forEach { icon ->
                            val isSelected = selectedIcon == icon
                            val isLocked = icon.isProOnly && !isProActive

                            Column(
                                modifier = Modifier
                                    .width(cardWidth)
                                    .clip(RoundedCornerShape(Dp16))
                                    .clickable {
                                        if (isLocked) {
                                            navigator.navigate(SettingPaywallRoute)
                                        } else {
                                            setAppIcon(context, icon)
                                            Toast.makeText(context, appIconSuccessMsg, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    .padding(vertical = Dp8),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = spacedBy(Dp8)
                            ) {
                                // Launcher preview box
                                Box(
                                    modifier = Modifier
                                        .size(Dp60)
                                        .clip(RoundedCornerShape(Dp14))
                                        .background(icon.backgroundColor)
                                        .border(
                                            width = if (isSelected) Dp2 else Dp1,
                                            color = when {
                                                isSelected -> colors.alwaysPurple
                                                icon == AppIconType.Doodle -> MeverBlack
                                                else -> colors.blackWhite.copy(alpha = 0.15f)
                                            },
                                            shape = RoundedCornerShape(Dp14)
                                        ),
                                    contentAlignment = Center
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.ic_download),
                                        contentDescription = null,
                                        tint = icon.iconTint,
                                        modifier = Modifier.size(Dp32)
                                    )

                                    if (isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.BottomEnd)
                                                .padding(Dp2)
                                                .size(Dp16)
                                                .clip(CircleShape)
                                                .background(colors.alwaysPurple),
                                            contentAlignment = Center
                                        ) {
                                            Icon(
                                                imageVector = ImageVector.vectorResource(R.drawable.ic_round_checked),
                                                contentDescription = null,
                                                tint = colors.alwaysWhiteBlack,
                                                modifier = Modifier.size(Dp12)
                                            )
                                        }
                                    }
                                }

                                // Title and Pro status
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = spacedBy(Dp2)
                                ) {
                                    Text(
                                        text = stringResource(icon.titleRes),
                                        style = typography.labelBold2,
                                        color = colors.blackWhite,
                                        textAlign = TextAlign.Center,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    if (icon.isProOnly) {
                                        ProBadge()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProBadge() {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(Dp10))
            .background(colors.alwaysPurple.copy(alpha = 0.15f))
            .padding(horizontal = Dp6, vertical = Dp2),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(Dp2)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_crown),
            contentDescription = null,
            tint = colors.alwaysPurple,
            modifier = Modifier.size(Dp10)
        )
        Text(
            text = stringResource(R.string.pro),
            style = typography.label3,
            color = colors.alwaysPurple
        )
    }
}
