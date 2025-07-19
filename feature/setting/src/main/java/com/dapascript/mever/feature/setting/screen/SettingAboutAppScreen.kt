package com.dapascript.mever.feature.setting.screen

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
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
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
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
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.util.getAppVersion
import com.dapascript.mever.feature.setting.viewmodel.SettingAboutAppViewModel
import java.time.LocalDate

@Composable
internal fun SettingAboutAppScreen(
    navController: NavController,
    viewModel: SettingAboutAppViewModel = hiltViewModel()
) = with(viewModel) {
    BaseScreen(
        useSystemBarsPadding = false,
        allowScreenOverlap = true,
        topBarArgs = TopBarArgs(
            title = "",
            iconBackColor = MeverWhite,
            onClickBack = { navController.popBackStack() }
        )
    ) {
        val context = LocalContext.current
        SettingAboutAppContent(
            context = context,
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.primary)
                .navigationBarsPadding()
        )
    }
}

@Composable
private fun SettingAboutAppContent(
    context: Context,
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
                colorFilter = tint(MeverWhite),
                contentDescription = "Logo Mever"
            )
            Spacer(modifier = Modifier.height(Dp8))
            Text(
                text = "Social Media Saver",
                style = typography.bodyBold2,
                color = MeverWhite
            )
            Spacer(modifier = Modifier.height(Dp8))
            Image(
                modifier = Modifier.size(Dp90),
                painter = painterResource(R.drawable.ic_download),
                contentDescription = "Download Icon"
            )
            Spacer(modifier = Modifier.height(Dp16))
            // get years
            Text(
                text = stringResource(
                    R.string.copyright,
                    LocalDate.now().year
                ),
                style = typography.body2,
                color = MeverWhite
            )
            Spacer(modifier = Modifier.height(Dp24))
            MeverButton(
                title = stringResource(R.string.license).uppercase(),
                buttonType = Outlined(
                    borderColor = MeverWhite,
                    contentColor = MeverWhite
                )
            ) {
                val intent = Intent(ACTION_VIEW, "https://mever.zeabur.app/license".toUri())
                context.startActivity(intent)
            }
        }
        Text(
            modifier = Modifier
                .align(BottomCenter)
                .padding(bottom = Dp16),
            text = "v${getAppVersion(context)}",
            style = typography.body1,
            color = MeverWhite
        )
    }
}