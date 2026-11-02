package com.dapascript.mever.feature.setting.screen

import android.widget.Toast
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize.Max
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.component.MeverImage
import com.dapascript.mever.core.common.ui.component.MeverProBadge
import com.dapascript.mever.core.common.ui.component.MeverRadioButton
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp1
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp10
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp18
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp2
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp3
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp32
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp6
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp64
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp80
import com.dapascript.mever.core.common.ui.theme.MeverBlack
import com.dapascript.mever.core.common.ui.theme.MeverLightSoftPurple
import com.dapascript.mever.core.common.ui.theme.MeverPurple
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.colors
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.typography
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp10
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp32
import com.dapascript.mever.core.common.ui.theme.ThemePalette
import com.dapascript.mever.core.common.ui.theme.ThemePalette.CLASSIC
import com.dapascript.mever.core.common.ui.theme.ThemePalette.Doodle
import com.dapascript.mever.core.common.ui.theme.ThemeType
import com.dapascript.mever.core.common.ui.theme.comicSansFamily
import com.dapascript.mever.core.common.ui.theme.poppinsFamily
import com.dapascript.mever.core.common.util.AppIconType
import com.dapascript.mever.core.common.util.AppIconType.Default
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
    var pendingAppIcon by remember { mutableStateOf<AppIconType?>(null) }
    val isProActive = isProActive.collectAsStateValue()
    val rawPalette = themePalette.collectAsStateValue()
    val selectedPalette = remember(rawPalette, isProActive) {
        if (isProActive || !rawPalette.isProOnly) rawPalette
        else CLASSIC
    }
    val rawIcon = appIcon.collectAsStateValue()
    val selectedIcon = remember(rawIcon, isProActive) {
        if (isProActive || !rawIcon.isProOnly) rawIcon
        else Default
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
                        .padding(bottom = Dp80)
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
                    Spacer(modifier = Modifier.height(Dp32))
                    Text(
                        text = stringResource(R.string.color_theme),
                        style = typography.h3,
                        color = colors.blackWhite,
                        modifier = Modifier.padding(horizontal = Dp24)
                    )
                    Text(
                        text = stringResource(
                            if (isProActive) R.string.color_theme_desc_pro
                            else R.string.color_theme_desc
                        ),
                        style = typography.body2,
                        color = colors.blackWhite.copy(alpha = 0.65f),
                        modifier = Modifier.padding(horizontal = Dp24, vertical = Dp4)
                    )
                    Spacer(modifier = Modifier.height(Dp16))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Max)
                            .padding(horizontal = Dp24),
                        horizontalArrangement = spacedBy(Dp12)
                    ) {
                        ThemePalette.entries.forEach { palette ->
                            val isSelected = selectedPalette == palette
                            val isLocked = palette.isProOnly && isProActive.not()
                            val isDoodle = palette == Doodle

                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(Dp16))
                                    .border(
                                        width = Dp2,
                                        color = if (isSelected) colors.alwaysPurple else colors.blackWhite.copy(
                                            alpha = 0.1f
                                        ),
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
                                Column(
                                    modifier = Modifier.padding(Dp12),
                                    horizontalAlignment = CenterHorizontally,
                                    verticalArrangement = spacedBy(Dp8)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(Dp64)
                                            .clip(RoundedCornerShape(Dp10))
                                            .background(if (isDoodle) MeverWhite else MeverLightSoftPurple)
                                            .border(
                                                width = if (isDoodle) Dp2 else Dp1,
                                                color = if (isDoodle) MeverBlack else MeverPurple.copy(
                                                    alpha = 0.25f
                                                ),
                                                shape = RoundedCornerShape(Dp10)
                                            )
                                            .padding(Dp8)
                                    ) {
                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            verticalArrangement = SpaceBetween
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(Dp12)
                                                    .clip(RoundedCornerShape(Dp4))
                                                    .background(if (isDoodle) MeverBlack else MeverPurple)
                                                    .padding(horizontal = Dp6),
                                                verticalAlignment = CenterVertically,
                                                horizontalArrangement = SpaceBetween
                                            ) {
                                                Text(
                                                    text = "MEVER",
                                                    style = typography.label3.copy(
                                                        fontSize = Sp10,
                                                        fontFamily = if (isDoodle) comicSansFamily else poppinsFamily
                                                    ),
                                                    color = MeverWhite
                                                )
                                                Box(
                                                    modifier = Modifier
                                                        .size(Dp6)
                                                        .clip(CircleShape)
                                                        .background(MeverWhite)
                                                )
                                            }
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = spacedBy(Dp6),
                                                verticalAlignment = CenterVertically
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(Dp20)
                                                        .clip(RoundedCornerShape(Dp4))
                                                        .background(if (isDoodle) MeverBlack else MeverPurple)
                                                ) {
                                                    Text(
                                                        modifier = Modifier.align(Center),
                                                        text = "Aa",
                                                        style = typography.labelBold2.copy(
                                                            fontSize = Sp10,
                                                            fontFamily = if (isDoodle) comicSansFamily else poppinsFamily
                                                        ),
                                                        color = MeverWhite
                                                    )
                                                }
                                                Column(
                                                    modifier = Modifier.weight(1f),
                                                    verticalArrangement = spacedBy(Dp3)
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxWidth(0.8f)
                                                            .height(Dp6)
                                                            .clip(RoundedCornerShape(Dp2))
                                                            .background(
                                                                if (isDoodle) MeverBlack else MeverPurple.copy(
                                                                    alpha = 0.35f
                                                                )
                                                            )
                                                    )
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxWidth(0.5f)
                                                            .height(Dp4)
                                                            .clip(RoundedCornerShape(Dp2))
                                                            .background(
                                                                if (isDoodle) MeverBlack.copy(
                                                                    alpha = 0.5f
                                                                ) else MeverPurple.copy(alpha = 0.2f)
                                                            )
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    Row(
                                        verticalAlignment = CenterVertically,
                                        horizontalArrangement = spacedBy(Dp6)
                                    ) {
                                        Text(
                                            text = stringResource(palette.titleRes),
                                            style = typography.bodyBold2.copy(
                                                fontFamily = if (isDoodle) comicSansFamily else poppinsFamily
                                            ),
                                            color = colors.blackWhite,
                                            maxLines = 2,
                                            overflow = Ellipsis,
                                            modifier = Modifier.weight(1f, fill = false)
                                        )
                                        if (isLocked) MeverProBadge()
                                        if (isSelected) {
                                            Icon(
                                                modifier = Modifier.size(Dp18),
                                                imageVector = ImageVector.vectorResource(R.drawable.ic_round_checked),
                                                contentDescription = "Selected",
                                                tint = colors.alwaysPurple
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(Dp32))
                    Text(
                        modifier = Modifier.padding(horizontal = Dp24),
                        text = stringResource(R.string.app_icon),
                        style = typography.h3,
                        color = colors.blackWhite
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = Dp24, vertical = Dp4),
                        text = stringResource(R.string.app_icon_desc),
                        style = typography.body2,
                        color = colors.blackWhite.copy(alpha = 0.65f)
                    )
                    Spacer(modifier = Modifier.height(Dp16))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Max)
                            .padding(horizontal = Dp24),
                        horizontalArrangement = spacedBy(Dp16)
                    ) {
                        AppIconType.entries.forEach { iconType ->
                            val isSelected = selectedIcon == iconType
                            val isLocked = iconType.isProOnly && isProActive.not()
                            val isDoodle = iconType == AppIconType.Doodle

                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(Dp16))
                                    .border(
                                        width = Dp2,
                                        color = if (isSelected) colors.alwaysPurple else colors.blackWhite.copy(
                                            alpha = 0.1f
                                        ),
                                        shape = RoundedCornerShape(Dp16)
                                    )
                                    .clickable {
                                        if (isLocked) {
                                            navigator.navigate(SettingPaywallRoute)
                                        } else if (!isSelected) {
                                            pendingAppIcon = iconType
                                        }
                                    },
                                color = if (isSelected) colors.lightPurpleDarkGray else colors.whiteDark
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = Dp16, horizontal = Dp12),
                                    horizontalAlignment = CenterHorizontally,
                                    verticalArrangement = spacedBy(Dp12)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(Dp64)
                                            .clip(RoundedCornerShape(Dp16))
                                            .background(iconType.backgroundColor)
                                            .border(
                                                width = Dp1,
                                                color = when {
                                                    isDoodle -> MeverBlack
                                                    else -> colors.blackWhite.copy(alpha = 0.15f)
                                                },
                                                shape = RoundedCornerShape(Dp16)
                                            ),
                                        contentAlignment = Center
                                    ) { MeverImage(source = iconType.icon) }

                                    Row(
                                        verticalAlignment = CenterVertically,
                                        horizontalArrangement = spacedBy(Dp6)
                                    ) {
                                        Text(
                                            text = stringResource(iconType.titleRes),
                                            style = typography.bodyBold2.copy(
                                                fontFamily = if (isDoodle) comicSansFamily else poppinsFamily
                                            ),
                                            color = colors.blackWhite,
                                            maxLines = 1,
                                            overflow = Ellipsis
                                        )
                                        if (isLocked) MeverProBadge()
                                        if (isSelected) {
                                            Icon(
                                                modifier = Modifier.size(Dp18),
                                                imageVector = ImageVector.vectorResource(R.drawable.ic_round_checked),
                                                tint = colors.alwaysPurple,
                                                contentDescription = "Selected"
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    pendingAppIcon?.let { targetIcon ->
                        val appIconSuccessMsg = stringResource(R.string.app_icon_changed)
                        MeverDialog(
                            showDialog = true,
                            image = null,
                            title = stringResource(R.string.change_app_icon_title),
                            description = stringResource(R.string.change_app_icon_desc),
                            primaryActionLabel = stringResource(R.string.apply),
                            secondaryActionLabel = stringResource(R.string.cancel),
                            onDismiss = { pendingAppIcon = null },
                            onClickSecondaryAction = { pendingAppIcon = null },
                            onClickPrimaryAction = {
                                pendingAppIcon = null
                                setAppIcon(context, targetIcon)
                                Toast.makeText(context, appIconSuccessMsg, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        )
                    }
                }
            }
        }
    }
}