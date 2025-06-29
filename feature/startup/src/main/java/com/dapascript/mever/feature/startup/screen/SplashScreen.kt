package com.dapascript.mever.feature.startup.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle.Event.ON_STOP
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp189
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp72
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverPurple
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.hideSystemBar
import com.dapascript.mever.core.common.util.state.collectAsStateValue
import com.dapascript.mever.core.navigation.helper.navigateClearBackStack
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeLandingRoute
import com.dapascript.mever.core.navigation.route.StartupScreenRoute.OnboardRoute
import com.dapascript.mever.feature.startup.viewmodel.SplashScreenViewModel
import kotlinx.coroutines.delay

@Composable
internal fun SplashScreen(
    navController: NavController,
    viewModel: SplashScreenViewModel = hiltViewModel()
) = with(viewModel) {
    BaseScreen(
        useSystemBarsPadding = false,
        allowScreenOverlap = true,
        hideDefaultTopBar = true
    ) {
        val isOnboarded = isOnboarded.collectAsStateValue()
        val activity = LocalActivity.current
        val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
        var showLogo by remember { mutableStateOf(false) }
        var isSplashScreenFinished by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            delay(300)
            showLogo = true
        }

        LaunchedEffect(Unit) {
            delay(1500)
            showLogo = false
            delay(250)
            isSplashScreenFinished = true
        }

        LaunchedEffect(isSplashScreenFinished) {
            if (isSplashScreenFinished) {
                navController.navigateClearBackStack(if (isOnboarded) HomeLandingRoute else OnboardRoute)
            }
        }

        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                hideSystemBar(activity, event != ON_STOP)
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
        }

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
                AnimatedVisibility(
                    visible = showLogo,
                    enter = fadeIn() + slideInVertically { -it },
                    exit = slideOutVertically { -it } + fadeOut()
                ) {
                    Image(
                        modifier = Modifier
                            .width(Dp189)
                            .height(Dp72),
                        painter = painterResource(R.drawable.ic_mever),
                        colorFilter = tint(MeverWhite),
                        contentDescription = "Logo Mever"
                    )
                }
                AnimatedVisibility(
                    visible = showLogo,
                    enter = fadeIn() + slideInVertically { it },
                    exit = slideOutVertically { it } + fadeOut()
                ) {
                    Text(
                        text = "Social Media Saver",
                        style = typography.bodyBold1,
                        color = MeverWhite
                    )
                }
            }
        }
    }
}