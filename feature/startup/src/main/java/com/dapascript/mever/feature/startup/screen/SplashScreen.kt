package com.dapascript.mever.feature.startup.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.base.attr.BaseScreenAttr.BaseScreenArgs
import com.dapascript.mever.core.common.navigation.base.BaseNavigator
import com.dapascript.mever.core.common.navigation.graph.HomeNavGraph
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp189
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp72
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverPurple
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.feature.startup.navigation.route.OnboardRoute
import com.dapascript.mever.feature.startup.navigation.route.SplashRoute
import com.dapascript.mever.feature.startup.viewmodel.SplashScreenViewModel

@Composable
internal fun SplashScreen(
    navigator: BaseNavigator,
    viewModel: SplashScreenViewModel = hiltViewModel()
) = with(viewModel) {
    val isSplashScreenFinished = isSplashScreenFinished.collectAsStateValue()
    val isOnboarded = isOnboarded.collectAsStateValue()

    LaunchedEffect(isSplashScreenFinished) {
        if (isSplashScreenFinished) {
            navigator.run {
                navigate(
                    route = if (isOnboarded) getNavGraph<HomeNavGraph>().getHomeLandingRoute() else OnboardRoute,
                    popUpTo = SplashRoute,
                    inclusive = true
                )
            }
        }
    }

    BaseScreen(
        baseScreenArgs = BaseScreenArgs(
            statusBarColor = MeverPurple,
            navigationBarColor = MeverPurple
        ),
        overlappingScreen = true,
        hideTopBar = true
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MeverPurple),
            contentAlignment = Center
        ) {
            Column(
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = spacedBy(Dp8)
            ) {
                Image(
                    modifier = Modifier
                        .width(Dp189)
                        .height(Dp72),
                    painter = painterResource(R.drawable.ic_mever),
                    colorFilter = tint(MeverWhite),
                    contentDescription = "Logo Mever"
                )
                Text(
                    text = "Social Media Saver",
                    style = typography.bodyBold1,
                    color = MeverWhite
                )
            }
        }
    }
}