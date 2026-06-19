package com.dapascript.mever.feature.setting.screen

import androidx.activity.SystemBarStyle.Companion.dark
import androidx.activity.SystemBarStyle.Companion.light
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.MeverRadioButton
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp1
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp3
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp40
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp64
import com.dapascript.mever.core.common.ui.theme.MeverDark
import com.dapascript.mever.core.common.ui.theme.MeverTheme.colors
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.MeverTransparent
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp32
import com.dapascript.mever.core.common.ui.theme.ThemeType.Dark
import com.dapascript.mever.core.common.ui.theme.ThemeType.Light
import com.dapascript.mever.core.common.util.LanguageManager
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.recreateActivity
import com.dapascript.mever.core.common.util.state.collectAsStateValue
import com.dapascript.mever.core.navigation.helper.Navigator
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingLanguageRoute
import com.dapascript.mever.feature.setting.viewmodel.SettingLanguageViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@Composable
internal fun SettingLanguageScreen(
    navigator: Navigator,
    args: SettingLanguageRoute,
    viewModel: SettingLanguageViewModel = hiltViewModel()
) = with(viewModel) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val isFirstTimeChangeLanguage = isFirstTimeChangeLanguage.collectAsStateValue()
    val themeType = themeType.collectAsStateValue()
    val darkTheme = when (themeType) {
        Light -> false
        Dark -> true
        else -> isSystemInDarkTheme()
    }
    var titleHeight by rememberSaveable { mutableIntStateOf(0) }
    val isExpanded by remember { derivedStateOf { scrollState.value < titleHeight / 2 } }
    var languageCode by rememberSaveable { mutableStateOf(args.languageCode) }

    BaseScreen(
        topBarArgs = TopBarArgs(
            title = if (isExpanded.not()) stringResource(R.string.language) else "",
            onClickBack = { navigator.goBack() }
        )
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

        LaunchedEffect(languageCode, darkTheme) {
            if (languages.any { it.second == languageCode }) activity.enableEdgeToEdge(
                statusBarStyle = if (darkTheme) {
                    dark(scrim = MeverTransparent.toArgb())
                } else {
                    light(
                        scrim = MeverTransparent.toArgb(),
                        darkScrim = MeverDark.toArgb()
                    )
                },
                navigationBarStyle = if (darkTheme) {
                    dark(scrim = MeverTransparent.toArgb())
                } else {
                    light(
                        scrim = MeverTransparent.toArgb(),
                        darkScrim = MeverDark.toArgb()
                    )
                }
            )
        }

        BoxWithConstraints(
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
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { titleHeight = it.size.height }
                    ) {
                        Spacer(modifier = Modifier.height(Dp16))
                        Text(
                            text = stringResource(R.string.language),
                            style = typography.h2.copy(fontSize = Sp32),
                            color = colors.blackWhite,
                            modifier = Modifier.padding(horizontal = Dp24)
                        )
                    }
                    Column(
                        modifier = Modifier
                            .height(this@BoxWithConstraints.maxHeight)
                            .nestedScroll(
                                object : NestedScrollConnection {
                                    override fun onPreScroll(
                                        available: Offset,
                                        source: NestedScrollSource
                                    ) = if (available.y > 0) Offset.Zero
                                    else Offset(
                                        x = 0f,
                                        y = -scrollState.dispatchRawDelta(-available.y)
                                    )
                                }
                            )
                    ) {
                        Spacer(modifier = Modifier.height(Dp40))
                        Text(
                            text = stringResource(R.string.choose_preferrence),
                            style = typography.h3,
                            color = colors.blackWhite,
                            modifier = Modifier.padding(horizontal = Dp24)
                        )
                        Spacer(modifier = Modifier.height(Dp20))
                        languages.forEach { (language, code) ->
                            MeverRadioButton(
                                value = language,
                                isChoosen = languageCode == code,
                                onValueChoose = {
                                    if (languageCode == code) return@MeverRadioButton

                                    LanguageManager.changeLanguage(
                                        context = context,
                                        languageCode = code
                                    )
                                    languageCode = code
                                    if (isFirstTimeChangeLanguage) {
                                        setIsFirstTimeChangeLanguage(false)
                                        recreateActivity(context, activity)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}