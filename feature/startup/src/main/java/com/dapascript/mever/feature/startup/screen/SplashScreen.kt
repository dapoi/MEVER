package com.dapascript.mever.feature.startup.screen

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.Lifecycle.Event.ON_STOP
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp189
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp48
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp72
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverPurple
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.util.InAppUpdateManager
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.hideSystemBar
import com.dapascript.mever.core.common.util.state.collectAsStateValue
import com.dapascript.mever.core.navigation.helper.navigateClearBackStack
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeLandingRoute
import com.dapascript.mever.core.navigation.route.StartupScreenRoute.OnboardRoute
import com.dapascript.mever.feature.startup.viewmodel.SplashScreenViewModel
import com.google.android.play.core.install.model.AppUpdateType.FLEXIBLE
import com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE
import com.google.android.play.core.install.model.UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
import com.google.android.play.core.install.model.UpdateAvailability.UPDATE_AVAILABLE

@Composable
internal fun SplashScreen(
    navController: NavController,
    viewModel: SplashScreenViewModel = hiltViewModel()
) = with(viewModel) {
    BaseScreen(
        hideDefaultTopBar = true,
        useStatusBarsPadding = false
    ) {
        val isOnboarded = isOnboarded.collectAsStateValue()
        val appConfigState = appConfigState.collectAsStateValue()
        val appVersion = getAppVersion.collectAsStateValue()
        val activity = LocalActivity.current
        val resources = LocalResources.current
        val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
        var showMaintenanceModal by remember { mutableStateOf(false) }
        var forceUpdateInProgress by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf("") }
        val logoVisibleState = remember { MutableTransitionState(false) }
        val inAppUpdateManager = remember { InAppUpdateManager(activity) }
        val updateLauncher = rememberLauncherForActivityResult(
            contract = StartIntentSenderForResult()
        ) { result ->
            when {
                result.resultCode == RESULT_CANCELED -> {
                    if (forceUpdateInProgress) activity.finish()
                    else logoVisibleState.targetState = false
                }

                result.resultCode != RESULT_OK -> {
                    forceUpdateInProgress = false
                    logoVisibleState.targetState = false
                }

                result.resultCode == RESULT_OK && !forceUpdateInProgress -> {
                    logoVisibleState.targetState = false
                }
            }
        }

        LaunchedEffect(Unit) { getAppConfig() }

        LaunchedEffect(appConfigState) {
            appConfigState.handleUiState(
                onLoading = { logoVisibleState.targetState = true },
                onSuccess = { response ->
                    if (response.maintenanceDay != null && today == response.maintenanceDay) {
                        showMaintenanceModal = true
                    } else {
                        forceUpdateInProgress = response.isForceUpdateRequired

                        inAppUpdateManager.startUpdate(
                            updateType = if (forceUpdateInProgress) IMMEDIATE else FLEXIBLE,
                            updateAvailability = UPDATE_AVAILABLE,
                            launcher = updateLauncher,
                            onUpdateNotAvailable = {
                                forceUpdateInProgress = false
                                logoVisibleState.targetState = false
                            }
                        )
                    }
                },
                onFailed = { message ->
                    errorMessage = message ?: resources.getString(R.string.error_desc)
                }
            )
        }

        LaunchedEffect(logoVisibleState.isIdle, logoVisibleState.currentState) {
            if (logoVisibleState.isIdle && logoVisibleState.currentState.not()) {
                navController.navigateClearBackStack(
                    if (isOnboarded) HomeLandingRoute else OnboardRoute
                )
            }
        }

        MeverDialog(
            showDialog = showMaintenanceModal,
            title = stringResource(R.string.maintenance_title),
            description = stringResource(R.string.maintenance_message, today),
            primaryActionLabel = null,
            secondaryActionLabel = null
        )

        MeverDialog(
            showDialog = errorMessage.isNotEmpty(),
            description = errorMessage,
            onClickPrimaryAction = {
                errorMessage = ""
                getAppConfig()
            },
            onClickSecondaryAction = { activity.finish() }
        )

        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                hideSystemBar(activity = activity, value = event != ON_STOP)

                if (event == ON_RESUME && forceUpdateInProgress) {
                    inAppUpdateManager.startUpdate(
                        updateType = IMMEDIATE,
                        updateAvailability = DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS,
                        launcher = updateLauncher,
                        onUpdateNotAvailable = {
                            forceUpdateInProgress = false
                            logoVisibleState.targetState = false
                        }
                    )
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MeverPurple)
        ) {
            Column(
                modifier = Modifier.align(Center),
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = spacedBy(Dp8)
            ) {
                AnimatedVisibility(
                    visibleState = logoVisibleState,
                    enter = fadeIn(animationSpec = tween(durationMillis = 300)) +
                            slideInVertically(animationSpec = tween(300)) { -it },
                    exit = slideOutVertically(
                        animationSpec = tween(
                            durationMillis = 250,
                            delayMillis = 1000
                        )
                    ) { -it } + fadeOut(
                        animationSpec = tween(
                            durationMillis = 250,
                            delayMillis = 1000
                        )
                    )
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
                    visibleState = logoVisibleState,
                    enter = fadeIn(animationSpec = tween(300)) +
                            slideInVertically(animationSpec = tween(300)) { it },
                    exit = slideOutVertically(
                        animationSpec = tween(
                            durationMillis = 250,
                            delayMillis = 1000
                        )
                    ) { it } + fadeOut(
                        animationSpec = tween(
                            durationMillis = 250,
                            delayMillis = 1000
                        )
                    )
                ) {
                    Text(
                        text = "Media Saver",
                        style = typography.bodyBold1,
                        color = MeverWhite
                    )
                }
            }
            AnimatedVisibility(
                modifier = Modifier
                    .align(BottomCenter)
                    .padding(bottom = Dp48),
                visibleState = logoVisibleState,
                enter = fadeIn(animationSpec = tween(300)) +
                        slideInVertically(animationSpec = tween(300)) { it },
                exit = slideOutVertically(
                    animationSpec = tween(
                        durationMillis = 250,
                        delayMillis = 1000
                    )
                ) { it } + fadeOut(
                    animationSpec = tween(
                        durationMillis = 250,
                        delayMillis = 1000
                    )
                )
            ) {
                Text(
                    text = "v${appVersion}",
                    style = typography.body1,
                    color = MeverWhite
                )
            }
        }
    }
}