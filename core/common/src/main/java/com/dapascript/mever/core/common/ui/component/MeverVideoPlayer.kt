package com.dapascript.mever.core.common.ui.component

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PictureInPictureParams
import android.content.Context
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
import android.graphics.Rect
import android.net.Uri.fromFile
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.S
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring.DampingRatioNoBouncy
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults.Thumb
import androidx.compose.material3.SliderDefaults.Track
import androidx.compose.material3.SliderDefaults.colors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.graphics.toAndroidRectF
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.toRect
import androidx.core.net.toUri
import androidx.core.view.doOnAttach
import androidx.core.view.doOnDetach
import androidx.lifecycle.Lifecycle.Event.ON_STOP
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaItem.ClippingConfiguration
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Player.Events
import androidx.media3.common.Player.Listener
import androidx.media3.common.Player.STATE_BUFFERING
import androidx.media3.common.Player.STATE_ENDED
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.PlayerView
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.attr.MeverContentViewerAttr.ContentViewerActionMenu
import com.dapascript.mever.core.common.ui.attr.MeverContentViewerAttr.ContentViewerActionMenu.DELETE
import com.dapascript.mever.core.common.ui.attr.MeverContentViewerAttr.ContentViewerActionMenu.SHARE
import com.dapascript.mever.core.common.ui.attr.MeverDialogAttr.MeverDialogArgs
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.ActionMenu
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp0
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp10
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp14
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp32
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp48
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp6
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp64
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverBlack
import com.dapascript.mever.core.common.ui.theme.MeverDark
import com.dapascript.mever.core.common.ui.theme.MeverPurple
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.MeverTransparent
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.convertToTimeFormat
import com.dapascript.mever.core.common.util.hideSystemBar
import com.dapascript.mever.core.common.util.isSystemBarVisible
import com.dapascript.mever.core.common.util.onCustomClick
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import java.io.File
import kotlin.math.max

@androidx.annotation.OptIn(UnstableApi::class)
@SuppressLint("SourceLockedOrientationActivity", "ImplicitSamInstance")
@Composable
fun MeverVideoPlayer(
    fileName: String,
    isOnlineContent: Boolean,
    videoSource: String,
    isAutoplayTarget: Boolean,
    isPageVisible: Boolean,
    isFullScreen: Boolean,
    isScrolling: Boolean,
    isPipEnabled: Boolean,
    modifier: Modifier = Modifier,
    onClickDelete: () -> Unit,
    onClickShare: () -> Unit,
    onClickBack: () -> Unit,
    onFullScreenChange: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val activity = LocalActivity.current

    // Player
    val httpFactory = remember {
        DefaultHttpDataSource.Factory()
            .setConnectTimeoutMs(10_000)
            .setReadTimeoutMs(10_000)
            .setAllowCrossProtocolRedirects(true)
    }
    val dataSourceFactory = remember { DefaultDataSource.Factory(context, httpFactory) }
    val mediaSourceFactory = remember { DefaultMediaSourceFactory(dataSourceFactory) }
    val player = remember(context) {
        ExoPlayer.Builder(context)
            .setMediaSourceFactory(mediaSourceFactory)
            .setLoadControl(
                DefaultLoadControl.Builder()
                    .setBufferDurationsMs(
                        15_000, // minBuffer
                        50_000, // maxBuffer
                        1_500,  // bufferForPlayback
                        3_000   // bufferForPlaybackAfterRebuffer
                    )
                    .build()
            )
            .build()
    }

    // State
    var totalDuration by rememberSaveable { mutableLongStateOf(0L) }
    var videoTimer by remember { mutableLongStateOf(0L) }
    var isVideoPlaying by remember { mutableStateOf(false) }
    var playbackState by remember { mutableStateOf<Int?>(null) }
    var isVideoBuffering by remember { mutableStateOf(false) }

    // UI State
    var hasAutoplayed by rememberSaveable { mutableStateOf(false) }
    var showController by remember { mutableStateOf(true) }
    var showDropDownMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showErrorPlayingDialog by remember { mutableStateOf(false) }
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    val shouldEnterPipMode by rememberUpdatedState(isVideoPlaying)
    var viewAttached by remember { mutableStateOf(false) }
    var didRecoverOnce by remember { mutableStateOf(false) }
    var lastRect by remember { mutableStateOf<Rect?>(null) }
    var pendingRect by remember { mutableStateOf<Rect?>(null) }

    // Fullscreen Handlers
    val enterFullScreen = {
        onFullScreenChange(true)
        activity.requestedOrientation = SCREEN_ORIENTATION_USER_LANDSCAPE
    }
    val exitFullScreen = {
        onFullScreenChange(false)
        activity.requestedOrientation = SCREEN_ORIENTATION_USER_PORTRAIT
    }

    BackHandler(isFullScreen) { exitFullScreen() }

    LaunchedEffect(isPageVisible, isAutoplayTarget, isScrolling) {
        if (isPageVisible) {
            snapshotFlow { isScrolling }.first { it.not() }
            if (isAutoplayTarget && hasAutoplayed.not()) {
                player.playWhenReady = true
                showController = true
                hasAutoplayed = true
            }
        } else {
            player.pause()
        }
    }

    LaunchedEffect(showController, showDropDownMenu, isVideoBuffering, isFullScreen) {
        hideSystemBar(activity, showController.not() && isFullScreen)
        if (showController && showDropDownMenu.not() && isVideoBuffering.not()) {
            delay(2000L)
            showController = false
        }
    }

    LaunchedEffect(player) {
        while (isActive) {
            videoTimer = player.currentPosition
            delay(300L)
        }
    }

    LaunchedEffect(isVideoBuffering) {
        if (isVideoBuffering && isOnlineContent.not() && didRecoverOnce.not()) {
            didRecoverOnce = true
            delay(1500)
            player.seekTo(max(0L, player.currentPosition - 5_000))
            player.playWhenReady = true
        }
        if (isVideoBuffering.not()) didRecoverOnce = false
    }

    LaunchedEffect(viewAttached, videoSource, isOnlineContent) {
        if (viewAttached) {
            val itemBuilder = MediaItem.Builder()
                .setUri(
                    if (isOnlineContent) videoSource.toUri()
                    else fromFile(File(videoSource))
                )
                .setClipping(isOnlineContent)

            player.setMediaItem(itemBuilder.build())
            player.prepare()
        }
    }

    LaunchedEffect(pendingRect, isPipEnabled, isVideoPlaying) {
        if (isPipEnabled && isVideoPlaying && pendingRect != null && pendingRect != lastRect) {
            delay(60)
            activity.updatePipParams(autoEnter = true, sourceRect = pendingRect)
            lastRect = pendingRect
        }
    }

    DisposableEffect(videoSource, isPipEnabled) {
        val listener = object : Listener {
            override fun onEvents(player: Player, events: Events) {
                super.onEvents(player, events)
                totalDuration = player.duration.coerceAtLeast(0L)
                playbackState = player.playbackState
            }

            override fun onPlaybackStateChanged(state: Int) {
                super.onPlaybackStateChanged(state)
                playbackState = state
                isVideoBuffering = state == STATE_BUFFERING
            }

            override fun onRenderedFirstFrame() {
                super.onRenderedFirstFrame()
                isVideoBuffering = false
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                isVideoPlaying = isPlaying
            }

            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                showErrorPlayingDialog = true
            }
        }
        val onUserLeaveBehavior: () -> Unit = {
            if (shouldEnterPipMode && isPipEnabled) {
                activity.enterPictureInPictureMode(
                    PictureInPictureParams.Builder().build()
                )
            }
        }
        val observer = LifecycleEventObserver { _, event ->
            if (event == ON_STOP && isPipEnabled.not()) player.pause()
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        if (SDK_INT < S) activity.addOnUserLeaveHintListener(onUserLeaveBehavior)

        player.addListener(listener)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            player.removeListener(listener)
            if (SDK_INT < S) activity.removeOnUserLeaveHintListener(onUserLeaveBehavior)
            if (SDK_INT >= S) activity.updatePipParams(autoEnter = false)
            player.stop()
            player.clearMediaItems()
            hideSystemBar(activity, isSystemBarVisible(activity).not())
        }
    }

    DisposableEffect(Unit) { onDispose { player.release() } }

    MeverDialog(
        showDialog = showErrorPlayingDialog,
        meverDialogArgs = MeverDialogArgs(title = stringResource(R.string.error_title)),
        hideInteractionButton = true
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.failed_playback),
            textAlign = TextAlign.Center,
            style = typography.body1,
            color = colorScheme.onPrimary
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Dp14))
                .onCustomClick { onClickBack() }
                .padding(vertical = Dp8),
            contentAlignment = Center
        ) {
            Text(
                text = stringResource(R.string.close),
                style = typography.bodyBold2,
                color = colorScheme.primary
            )
        }
    }

    Box(modifier = modifier) {
        VideoPlayer(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned { layoutCoordinates ->
                    pendingRect = if (isVideoPlaying && isPipEnabled) {
                        layoutCoordinates.boundsInWindow().toAndroidRectF().toRect()
                    } else null
                },
            context = context,
            player = player,
            title = fileName,
            isStreaming = isOnlineContent,
            isVideoBuffering = isVideoBuffering,
            iconPlayOrPause = if (isVideoPlaying) R.drawable.ic_pause else R.drawable.ic_play,
            isControllerVisible = showController,
            isFullScreen = isFullScreen,
            videoTimer = videoTimer,
            totalDuration = totalDuration.coerceAtLeast(0L),
            bufferProgress = player.bufferedPosition.toFloat(),
            onClickAny = { showController = showController.not() },
            onClickRewind = {
                player.seekTo(player.currentPosition.minus(5000))
                showController = true
            },
            onClickPlayOrPause = {
                when {
                    player.isPlaying -> player.pause()
                    player.isPlaying.not() && playbackState == STATE_ENDED -> {
                        player.seekTo(0, 0)
                        player.playWhenReady = true
                    }

                    else -> player.play()
                }
            },
            onClickForward = {
                player.seekTo(player.currentPosition.plus(5000))
                showController = true
            },
            onClickFullScreen = { if (isFullScreen) exitFullScreen() else enterFullScreen() },
            onClickActionMenu = { showDropDownMenu = showDropDownMenu.not() },
            onClickBack = if (isFullScreen) exitFullScreen else onClickBack,
            onChangeSeekbar = { position ->
                player.seekTo(position.toLong())
                showController = true
            },
            onViewAttached = { attached -> viewAttached = attached }
        )
        MeverPopupDropDownMenu(
            modifier = Modifier
                .padding(PaddingValues(top = Dp64, end = Dp24))
                .statusBarsPadding(),
            listDropDown = ContentViewerActionMenu.entries,
            label = { it.getText(context) },
            showDropDownMenu = showDropDownMenu,
            backgroundColor = MeverDark,
            textColor = MeverWhite,
            onDismissDropDownMenu = { showDropDownMenu = false },
            onClick = { item ->
                when (item) {
                    DELETE -> showDeleteDialog = true
                    SHARE -> onClickShare()
                }
            }
        )
    }

    MeverDialog(
        meverDialogArgs = MeverDialogArgs(
            title = stringResource(R.string.delete_title),
            primaryButtonText = stringResource(R.string.delete_button),
            titleColor = MeverWhite,
            backgroundColor = MeverDark,
            secondaryButtonColor = MeverWhite,
            onClickPrimaryButton = {
                onClickDelete()
                onClickBack()
                showDeleteDialog = false
            },
            onClickSecondaryButton = {
                hideSystemBar(activity, true)
                showDeleteDialog = false
            }
        ),
        showDialog = showDeleteDialog
    ) {
        Text(
            text = stringResource(R.string.delete_desc),
            textAlign = TextAlign.Center,
            style = typography.body1,
            color = MeverWhite
        )
    }
}

@Composable
private fun VideoPlayer(
    context: Context,
    player: Player,
    title: String,
    isStreaming: Boolean,
    isVideoBuffering: Boolean,
    iconPlayOrPause: Int,
    isControllerVisible: Boolean,
    isFullScreen: Boolean,
    videoTimer: Long,
    totalDuration: Long,
    bufferProgress: Float,
    modifier: Modifier = Modifier,
    onClickAny: () -> Unit,
    onClickRewind: () -> Unit,
    onClickPlayOrPause: () -> Unit,
    onClickForward: () -> Unit,
    onClickFullScreen: () -> Unit,
    onClickActionMenu: (String) -> Unit,
    onClickBack: () -> Unit,
    onChangeSeekbar: (Float) -> Unit,
    onViewAttached: (Boolean) -> Unit
) = Box(
    modifier = modifier
        .background(MeverBlack)
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) { onClickAny() }
) {
    AndroidView(
        modifier = Modifier.align(Center),
        factory = {
            PlayerView(context).apply {
                this.player = player
                useController = false
                doOnAttach { onViewAttached(true) }
                doOnDetach { onViewAttached(false) }
            }
        },
        update = { view -> if (view.player !== player) view.player = player }
    )
    AnimatedVisibility(
        visible = isControllerVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MeverBlack.copy(alpha = 0.7f))
        ) {
            MeverTopBar(
                modifier = Modifier.padding(horizontal = Dp24),
                topBarArgs = TopBarArgs(
                    actionMenus = if (isStreaming) emptyList() else listOf(
                        ActionMenu(
                            icon = R.drawable.ic_more,
                            nameIcon = "More",
                            onClickActionMenu = onClickActionMenu
                        )
                    ),
                    title = title,
                    topBarColor = MeverTransparent,
                    titleColor = MeverWhite,
                    iconBackColor = MeverWhite,
                    actionMenusColor = MeverWhite,
                    onClickBack = onClickBack
                ),
                useCenterTopBar = false
            )
            VideoCenterControlSection(
                modifier = Modifier.align(Center),
                isVideoBuffering = isVideoBuffering,
                iconPlayOrPause = iconPlayOrPause,
                onClickRewind = onClickRewind,
                onClickPlay = onClickPlayOrPause,
                onClickForward = onClickForward
            )
            VideoBottomControlSection(
                modifier = Modifier
                    .padding(horizontal = Dp24)
                    .navigationBarsPadding()
                    .align(BottomCenter),
                videoTimer = videoTimer,
                totalDuration = totalDuration,
                isFullScreen = isFullScreen,
                isStreaming = isStreaming,
                bufferProgress = bufferProgress,
                onChangeSeekbar = onChangeSeekbar,
                onClickFullScreen = onClickFullScreen
            )
        }
    }
}

@Composable
private fun VideoCenterControlSection(
    isVideoBuffering: Boolean,
    iconPlayOrPause: Int,
    modifier: Modifier = Modifier,
    onClickRewind: () -> Unit,
    onClickPlay: () -> Unit,
    onClickForward: () -> Unit
) = Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = SpaceEvenly
) {
    var isRewindRotated by remember { mutableStateOf(false) }
    var isForwardRotated by remember { mutableStateOf(false) }
    val rotationRewind by animateFloatAsState(
        targetValue = if (isRewindRotated) -45f else 0f,
        animationSpec = spring(dampingRatio = DampingRatioNoBouncy),
        label = "rotationRewind"
    ) { isRewindRotated = false }
    val rotationForward by animateFloatAsState(
        targetValue = if (isForwardRotated) 45f else 0f,
        animationSpec = spring(dampingRatio = DampingRatioNoBouncy),
        label = "rotationForward"
    ) { isForwardRotated = false }

    Image(
        painter = painterResource(R.drawable.ic_rewind),
        colorFilter = tint(color = MeverWhite),
        contentDescription = "Rewind",
        modifier = Modifier
            .rotate(rotationRewind)
            .size(Dp48)
            .clip(CircleShape)
            .clickable {
                onClickRewind()
                isRewindRotated = true
            }
    )
    if (isVideoBuffering) CircularProgressIndicator(
        modifier = Modifier.size(Dp48),
        color = MeverWhite
    ) else Image(
        painter = painterResource(iconPlayOrPause),
        colorFilter = tint(color = MeverWhite),
        contentDescription = "Play/Pause",
        modifier = Modifier
            .size(Dp48)
            .clip(CircleShape)
            .clickable { onClickPlay() }
    )
    Image(
        painter = painterResource(R.drawable.ic_forward),
        colorFilter = tint(color = MeverWhite),
        contentDescription = "Forward",
        modifier = Modifier
            .rotate(rotationForward)
            .size(Dp48)
            .clip(CircleShape)
            .clickable {
                onClickForward()
                isForwardRotated = true
            }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VideoBottomControlSection(
    videoTimer: Long,
    totalDuration: Long,
    isFullScreen: Boolean,
    isStreaming: Boolean,
    bufferProgress: Float,
    modifier: Modifier = Modifier,
    onChangeSeekbar: (Float) -> Unit,
    onClickFullScreen: () -> Unit
) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var isSeeking by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(videoTimer) {
        if (!isSeeking) {
            sliderPosition = videoTimer.toFloat()
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = spacedBy(Dp10)
    ) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp0) {
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = sliderPosition.coerceIn(0f, totalDuration.toFloat()),
                interactionSource = interactionSource,
                valueRange = 0f..totalDuration.toFloat(),
                onValueChange = {
                    isSeeking = true
                    sliderPosition = it
                },
                onValueChangeFinished = {
                    onChangeSeekbar(sliderPosition)
                    isSeeking = false
                },
                track = { sliderState ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(
                                (bufferProgress / totalDuration.toFloat()).coerceIn(
                                    0f,
                                    1f
                                )
                            )
                            .height(Dp6)
                            .background(color = MeverWhite.copy(alpha = 0.7f), shape = CircleShape)
                    )
                    Track(
                        modifier = Modifier.height(Dp6),
                        sliderState = sliderState,
                        drawStopIndicator = null,
                        thumbTrackGapSize = Dp0,
                        trackInsideCornerSize = Dp0,
                        colors = colors(
                            activeTrackColor = MeverPurple,
                            inactiveTrackColor = MeverWhite.copy(alpha = 0.3f)
                        )
                    )
                },
                thumb = {
                    Thumb(
                        modifier = Modifier
                            .size(Dp16)
                            .background(color = MeverPurple, shape = CircleShape),
                        interactionSource = interactionSource
                    )
                }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dp16),
            horizontalArrangement = SpaceBetween,
            verticalAlignment = CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(start = Dp12),
                text = "${convertToTimeFormat(sliderPosition.toLong())} / ${
                    convertToTimeFormat(
                        totalDuration
                    )
                }",
                style = typography.body1,
                color = MeverWhite
            )
            if (isStreaming.not()) Image(
                painter = painterResource(
                    if (isFullScreen) R.drawable.ic_fullscreen_exit else R.drawable.ic_fullscreen
                ),
                colorFilter = tint(color = MeverWhite),
                contentDescription = "Fullscreen",
                modifier = Modifier
                    .size(Dp32)
                    .padding(end = Dp6)
                    .onCustomClick { onClickFullScreen() }
            )
        }
    }
}

private fun Activity.updatePipParams(
    autoEnter: Boolean? = null,
    sourceRect: Rect? = null
) {
    val builder = PictureInPictureParams.Builder()
    sourceRect?.let { builder.setSourceRectHint(it) }
    if (SDK_INT >= S) autoEnter?.let { builder.setAutoEnterEnabled(it) }
    setPictureInPictureParams(builder.build())
}

private fun MediaItem.Builder.setClipping(isStreaming: Boolean) = if (isStreaming) {
    setClippingConfiguration(
        ClippingConfiguration.Builder()
            .setStartPositionMs(0)
            .setEndPositionMs(10000L)
            .setRelativeToLiveWindow(true)
            .build()
    )
} else this