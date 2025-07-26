package com.dapascript.mever.feature.startup.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.component.MeverPermissionHandler
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp0
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp40
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp48
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverPurple
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.ui.theme.MeverYellow
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp18
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp40
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp50
import com.dapascript.mever.core.common.util.getNotificationPermission
import com.dapascript.mever.core.common.util.isAndroidTiramisuAbove
import com.dapascript.mever.core.navigation.helper.navigateClearBackStack
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeLandingRoute
import com.dapascript.mever.feature.startup.R
import com.dapascript.mever.feature.startup.viewmodel.OnboardViewModel

@Composable
internal fun OnboardScreen(
    navController: NavController,
    viewModel: OnboardViewModel = hiltViewModel()
) = with(viewModel) {
    BaseScreen(
        useSystemBarsPadding = false,
        allowScreenOverlap = true,
        hideDefaultTopBar = true
    ) {
        var buttonSize by remember { mutableStateOf(Dp0) }
        var setRequestPermission by remember { mutableStateOf<List<String>>(emptyList()) }
        val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

        if (setRequestPermission.isNotEmpty()) {
            MeverPermissionHandler(
                permissions = setRequestPermission,
                onGranted = {
                    setRequestPermission = emptyList()
                    navController.navigateClearBackStack(HomeLandingRoute)
                },
                onDenied = { _, _ ->
                    setRequestPermission = emptyList()
                    navController.navigateClearBackStack(HomeLandingRoute)
                }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = statusBarHeight)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Image(
                    modifier = Modifier.aspectRatio(0.9f),
                    painter = painterResource(
                        if (isSystemInDarkTheme()) R.drawable.bg_onboard_dark
                        else R.drawable.bg_onboard_light
                    ),
                    contentDescription = "Background Onboard"
                )
                DescriptionOnboardSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dp40, bottom = buttonSize, start = Dp24, end = Dp24)
                )
            }
            ButtonOnboardSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = Dp24, end = Dp24, bottom = Dp16)
                    .navigationBarsPadding()
                    .align(BottomCenter)
                    .onGloballyPositioned { buttonSize = it.size.height.dp }
            ) {
                setIsOnboarded(true)
                if (isAndroidTiramisuAbove()) {
                    setRequestPermission = getNotificationPermission
                } else navController.navigateClearBackStack(HomeLandingRoute)
            }
        }
    }
}

@Composable
private fun ButtonOnboardSection(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) = Button(
    modifier = modifier,
    colors = buttonColors(containerColor = MeverPurple),
    shape = RoundedCornerShape(Dp48),
    onClick = onClick,
    content = {
        Text(
            text = "Let's Started",
            style = typography.body1.copy(fontSize = Sp18),
            color = MeverWhite
        )
        Spacer(modifier = Modifier.size(Dp16))
        Icon(
            painter = painterResource(R.drawable.ic_arrow_started),
            tint = MeverYellow,
            contentDescription = "Arrow Right"
        )
    }
)

@Composable
private fun DescriptionOnboardSection(modifier: Modifier = Modifier) = Column(modifier = modifier) {
    Text(
        text = "Easy to use and 100% free",
        style = typography.body2,
        color = colorScheme.secondary
    )
    Spacer(modifier = Modifier.size(Dp8))
    Text(
        text = "Supports",
        style = typography.h2.copy(fontSize = Sp40),
        color = colorScheme.onPrimary
    )
    Spacer(modifier = Modifier.size(Dp4))
    Text(
        text = buildAnnotatedString {
            val baseStyle = SpanStyle(
                fontSize = typography.h2.copy(fontSize = Sp40).fontSize,
                fontFamily = typography.h2.fontFamily,
                fontWeight = typography.h2.fontWeight
            )
            withStyle(baseStyle.copy(color = MeverPurple)) { append("Multiple ") }
            withStyle(baseStyle.copy(color = colorScheme.onPrimary)) { append("Source") }
        },
        lineHeight = Sp50
    )
    Spacer(modifier = Modifier.size(Dp8))
    Text(
        text = "Download media from Social Media easily.",
        style = typography.body2,
        color = colorScheme.secondary
    )
}