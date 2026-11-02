package com.dapascript.mever.feature.setting.screen

import android.content.Context
import androidx.activity.SystemBarStyle.Companion.dark
import androidx.activity.SystemBarStyle.Companion.light
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.Outlined
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.MeverButton
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp150
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp52
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp90
import com.dapascript.mever.core.common.ui.theme.MeverDark
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.colors
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.isDarkMode
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.isDoodle
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.typography
import com.dapascript.mever.core.common.ui.theme.MeverTransparent
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.getAppVersion
import com.dapascript.mever.core.common.util.navigateToBrowser
import com.dapascript.mever.core.navigation.helper.Navigator
import java.time.LocalDate

@Composable
internal fun SettingAboutAppScreen(navigator: Navigator) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val isDarkMode = isDarkMode
    val isDoodle = isDoodle
    val contentColor = if (isDoodle) colors.blackWhite else MeverWhite

    BaseScreen(
        topBarArgs = TopBarArgs(
            title = "",
            iconBackColor = contentColor
        ),
        useStatusBarsPadding = false,
        onBackHandler = { navigator.navigateBack() }
    ) {
        DisposableEffect(isDarkMode, isDoodle) {
            val isDarkBar = isDoodle.not() || isDarkMode
            activity.enableEdgeToEdge(
                statusBarStyle = if (isDarkBar) {
                    dark(scrim = MeverTransparent.toArgb())
                } else {
                    light(
                        scrim = MeverTransparent.toArgb(),
                        darkScrim = MeverDark.toArgb()
                    )
                },
                navigationBarStyle = if (isDarkBar) {
                    dark(scrim = MeverTransparent.toArgb())
                } else {
                    light(
                        scrim = MeverTransparent.toArgb(),
                        darkScrim = MeverDark.toArgb()
                    )
                }
            )
            onDispose {
                activity.enableEdgeToEdge(
                    statusBarStyle = if (isDarkMode) {
                        dark(scrim = MeverTransparent.toArgb())
                    } else {
                        light(
                            scrim = MeverTransparent.toArgb(),
                            darkScrim = MeverDark.toArgb()
                        )
                    },
                    navigationBarStyle = if (isDarkMode) {
                        dark(scrim = MeverTransparent.toArgb())
                    } else {
                        light(
                            scrim = MeverTransparent.toArgb(),
                            darkScrim = MeverDark.toArgb()
                        )
                    }
                )
            }
        }

        SettingAboutAppContent(
            context = context,
            contentColor = contentColor,
            isDoodle = isDoodle,
            modifier = Modifier
                .fillMaxSize()
                .background(if (isDoodle) colors.whiteDark else colors.alwaysPurple)
                .navigationBarsPadding()
        )
    }
}

@Composable
private fun SettingAboutAppContent(
    context: Context,
    contentColor: Color,
    isDoodle: Boolean,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Center
        ) {
            Image(
                modifier = Modifier
                    .width(Dp150)
                    .height(Dp52),
                painter = painterResource(R.drawable.ic_mever),
                colorFilter = tint(contentColor),
                contentDescription = "Logo Mever"
            )
            Spacer(modifier = Modifier.height(Dp8))
            Text(
                text = "Media Saver",
                style = typography.bodyBold2,
                color = contentColor
            )
            Spacer(modifier = Modifier.height(Dp8))
            Image(
                modifier = Modifier.size(Dp90),
                painter = painterResource(R.drawable.ic_download),
                colorFilter = if (isDoodle) tint(contentColor) else null,
                contentDescription = "Download Icon"
            )
            Spacer(modifier = Modifier.height(Dp16))
            Text(
                text = stringResource(
                    R.string.copyright,
                    LocalDate.now().year
                ),
                style = typography.body2,
                color = contentColor
            )
            Spacer(modifier = Modifier.height(Dp24))
            MeverButton(
                title = stringResource(R.string.license).uppercase(),
                buttonType = Outlined(
                    borderColor = contentColor,
                    contentColor = contentColor
                )
            ) { navigateToBrowser(context, "https://mever.my.id/license") }
            Spacer(modifier = Modifier.height(Dp16))
            TextButton(
                onClick = { navigateToBrowser(context, "https://mever.my.id/privacy-policy") },
                content = {
                    Text(
                        text = stringResource(R.string.policy),
                        style = typography.body2,
                        color = contentColor,
                        textDecoration = Underline
                    )
                }
            )
        }
        Text(
            modifier = Modifier
                .align(BottomCenter)
                .padding(bottom = Dp16),
            text = getAppVersion(context),
            style = typography.body1,
            color = contentColor
        )
    }
}