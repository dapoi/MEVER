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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle.Event.ON_STOP
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.attr.MeverDialogAttr.MeverDialogArgs
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.component.MeverDialogError
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp189
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp200
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp72
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverPurple
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.util.ErrorHandle.ErrorType
import com.dapascript.mever.core.common.util.ErrorHandle.ErrorType.NETWORK
import com.dapascript.mever.core.common.util.ErrorHandle.ErrorType.RESPONSE
import com.dapascript.mever.core.common.util.ErrorHandle.getErrorResponseContent
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.hideSystemBar
import com.dapascript.mever.core.common.util.state.UiState.StateSuccess
import com.dapascript.mever.core.common.util.state.collectAsStateValue
import com.dapascript.mever.core.navigation.helper.navigateClearBackStack
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeLandingRoute
import com.dapascript.mever.core.navigation.route.StartupScreenRoute.OnboardRoute
import com.dapascript.mever.feature.startup.viewmodel.SplashScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        val appConfigState = appConfigState.collectAsStateValue()
        val isNetworkAvailable = isNetworkAvailable.collectAsStateValue()
        val activity = LocalActivity.current
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
        var showLogo by remember { mutableStateOf(false) }
        var showMaintenanceModal by remember { mutableStateOf(false) }
        var showErrorModal by remember { mutableStateOf<ErrorType?>(null) }
        var errorMessage by remember { mutableStateOf("") }

        LaunchedEffect(appConfigState) {
            appConfigState.handleUiState(
                onLoading = { showLogo = true },
                onSuccess = { response ->
                    if (response.maintenanceDay != null && today == response.maintenanceDay) {
                        showMaintenanceModal = true
                    } else scope.launch {
                        delay(1000)
                        showLogo = false
                        delay(250)
                        navController.navigateClearBackStack(
                            if (isOnboarded) HomeLandingRoute else OnboardRoute
                        )
                    }
                },
                onFailed = { message ->
                    showErrorModal = RESPONSE
                    errorMessage = message ?: context.getString(R.string.unknown_error_desc)
                }
            )
        }

        LaunchedEffect(isNetworkAvailable) {
            if (appConfigState !is StateSuccess) getNetworkStatus(
                isNetworkAvailable = isNetworkAvailable,
                onNetworkAvailable = ::getAppConfig,
                onNetworkUnavailable = { showErrorModal = NETWORK }
            )
        }

        MeverDialog(
            showDialog = showMaintenanceModal,
            meverDialogArgs = MeverDialogArgs(
                title = stringResource(R.string.maintenance_title)
            ),
            hideInteractionButton = true
        ) {
            Image(
                modifier = Modifier
                    .size(Dp200)
                    .align(CenterHorizontally),
                painter = painterResource(R.drawable.ic_coffee),
                contentScale = Crop,
                contentDescription = "Maintenance Image"
            )
            Text(
                text = stringResource(R.string.maintenance_message, today),
                textAlign = TextAlign.Center,
                style = typography.body1,
                color = colorScheme.onPrimary
            )
        }

        getErrorResponseContent(
            context = context,
            errorType = showErrorModal,
            message = errorMessage,
        )?.let { (title, desc) ->
            MeverDialogError(
                showDialog = true,
                errorTitle = stringResource(title),
                errorDescription = desc,
                onClickPrimary = {
                    showErrorModal = null
                    getNetworkStatus(
                        isNetworkAvailable = isNetworkAvailable,
                        onNetworkAvailable = ::getAppConfig,
                        onNetworkUnavailable = { showErrorModal = NETWORK }
                    )
                },
                onClickSecondary = {
                    showErrorModal = null
                    navController.popBackStack()
                }
            )
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