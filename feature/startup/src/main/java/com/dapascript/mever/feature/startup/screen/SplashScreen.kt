package com.dapascript.mever.feature.startup.screen

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp189
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp48
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp72
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverPurple
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.typography
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.util.InAppUpdateManager
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.hideSystemBar
import com.dapascript.mever.core.common.util.state.collectAsStateValue
import com.dapascript.mever.core.navigation.helper.Navigator
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeLandingRoute
import com.dapascript.mever.core.navigation.route.StartupScreenRoute.OnboardRoute
import com.dapascript.mever.core.navigation.route.StartupScreenRoute.SplashRoute
import com.dapascript.mever.feature.startup.viewmodel.SplashScreenViewModel
import com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE
import com.google.android.play.core.install.model.UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
import com.google.android.play.core.install.model.UpdateAvailability.UPDATE_AVAILABLE
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
internal fun SplashScreen(
    navigator: Navigator,
    viewModel: SplashScreenViewModel = hiltViewModel()
) = with(viewModel) {
    BaseScreen(
        hideDefaultTopBar = true,
        useStatusBarsPadding = false,
        backgroundColor = MeverPurple,
        onBackHandler = { navigator.navigateBack() }
    ) {
        val isOnboarded = isOnboarded.collectAsStateValue()
        val appConfigState = appConfigState.collectAsStateValue()
        val appVersion = getAppVersion.collectAsStateValue()
        val activity = LocalActivity.current
        val resources = LocalResources.current
        val lifecycleOwner = LocalLifecycleOwner.current
        var showMaintenanceModal by remember { mutableStateOf(false) }
        var forceUpdateInProgress by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf("") }
        val logoVisibleState = remember {
            MutableTransitionState(false).apply { targetState = true }
        }
        val transition = rememberTransition(logoVisibleState, label = "SplashTransition")
        val contentAlpha by transition.animateFloat(
            transitionSpec = { tween(200) },
            label = "ContentAlpha"
        ) { state -> if (state) 1f else 0f }
        val contentOffset by transition.animateFloat(
            transitionSpec = { tween(200) },
            label = "ContentOffset"
        ) { state -> if (state) 0f else 50f }
        val inAppUpdateManager = remember { InAppUpdateManager(activity) }
        val isCanNavigate = logoVisibleState.isIdle && !logoVisibleState.currentState && isOnboarded != null
        val updateLauncher = rememberLauncherForActivityResult(
            contract = StartIntentSenderForResult()
        ) { result ->
            when (result.resultCode) {
                RESULT_CANCELED -> activity.finish()
                RESULT_OK -> Unit
                else -> {
                    forceUpdateInProgress = false
                    logoVisibleState.targetState = false
                }
            }
        }

        LaunchedEffect(logoVisibleState.currentState) {
            if (logoVisibleState.currentState) {
                delay(100.milliseconds)
                getAppConfig()
            }
        }

        LaunchedEffect(appConfigState) {
            appConfigState.handleUiState(
                onSuccess = { response ->
                    if (response.maintenanceDay != null) {
                        showMaintenanceModal = true
                    } else {
                        forceUpdateInProgress = response.isForceUpdateRequired

                        if (forceUpdateInProgress) {
                            inAppUpdateManager.startUpdate(
                                updateType = IMMEDIATE,
                                updateAvailability = UPDATE_AVAILABLE,
                                launcher = updateLauncher,
                                onUpdateNotAvailable = {
                                    forceUpdateInProgress = false
                                    logoVisibleState.targetState = false
                                }
                            )
                        } else {
                            logoVisibleState.targetState = false
                        }
                    }
                },
                onFailed = { message ->
                    errorMessage = message ?: resources.getString(R.string.error_desc)
                }
            )
        }

        LaunchedEffect(isCanNavigate) {
            if (isCanNavigate) {
                navigator.navigate(
                    route = if (isOnboarded) HomeLandingRoute else OnboardRoute,
                    popUpTo = SplashRoute,
                    isInclusive = true
                )
            }
        }

        MeverDialog(
            showDialog = showMaintenanceModal,
            title = stringResource(R.string.maintenance_title),
            description = stringResource(R.string.maintenance_message),
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
            hideSystemBar(activity, true)
            val observer = LifecycleEventObserver { _, event ->
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
            onDispose {
                hideSystemBar(activity, false)
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .align(Center)
                    .graphicsLayer { alpha = contentAlpha },
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = spacedBy(Dp8)
            ) {
                Image(
                    modifier = Modifier
                        .width(Dp189)
                        .height(Dp72)
                        .graphicsLayer { translationY = -contentOffset },
                    painter = painterResource(R.drawable.ic_mever),
                    colorFilter = tint(MeverWhite),
                    contentDescription = "Logo Mever"
                )
                Text(
                    modifier = Modifier.graphicsLayer { translationY = contentOffset },
                    text = "Media Saver",
                    style = typography.bodyBold1,
                    color = MeverWhite
                )
            }
            Text(
                modifier = Modifier
                    .align(BottomCenter)
                    .padding(bottom = Dp48)
                    .graphicsLayer { alpha = contentAlpha },
                text = "v${appVersion}",
                style = typography.body1,
                color = MeverWhite
            )
        }
    }
}