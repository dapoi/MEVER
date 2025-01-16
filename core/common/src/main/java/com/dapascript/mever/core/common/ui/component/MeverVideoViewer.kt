package com.dapascript.mever.core.common.ui.component

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring.DampingRatioLowBouncy
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle.Event.ON_CREATE
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.Lifecycle.Event.ON_STOP
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaItem.fromUri
import androidx.media3.common.Player
import androidx.media3.common.Player.STATE_BUFFERING
import androidx.media3.common.Player.STATE_ENDED
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer.Builder
import androidx.media3.ui.PlayerView
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.attr.MeverDialogAttr.MeverDialogArgs
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.ActionMenu
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp0
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp10
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp32
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp48
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp6
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp64
import com.dapascript.mever.core.common.ui.theme.MeverBlack
import com.dapascript.mever.core.common.ui.theme.MeverDark
import com.dapascript.mever.core.common.ui.theme.MeverPurple
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.clickableSingle
import com.dapascript.mever.core.common.util.hideStatusBar
import com.dapascript.mever.core.common.util.toTimeFormat
import kotlinx.coroutines.delay

@SuppressLint("SourceLockedOrientationActivity")
@OptIn(UnstableApi::class)
@Composable
fun MeverVideoViewer(
    source: String,
    fileName: String,
    modifier: Modifier = Modifier,
    onClickDelete: () -> Unit,
    onClickShare: () -> Unit,
    onClickBack: () -> Unit
) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    var player by remember { mutableStateOf<Player?>(null) }
    var isFullScreen by remember { mutableStateOf(false) }
    var isVideoPlaying by remember { mutableStateOf(player?.isPlaying) }
    var playbackState by remember { mutableStateOf(player?.playbackState) }
    var title by remember { mutableStateOf(player?.currentMediaItem?.mediaMetadata?.displayTitle.toString()) }
    var videoTimer by remember { mutableLongStateOf(0L) }
    var totalDuration by remember { mutableLongStateOf(0L) }
    var showController by remember { mutableStateOf(false) }
    var showDropDownMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var isVideoBuffering by remember { mutableStateOf(false) }

    val enterFullScreen = {
        isFullScreen = true
        activity.requestedOrientation = SCREEN_ORIENTATION_USER_LANDSCAPE
    }
    val exitFullScreen = {
        isFullScreen = false
        activity.requestedOrientation = SCREEN_ORIENTATION_USER_PORTRAIT
    }

    BackHandler(isFullScreen) { exitFullScreen() }

    LaunchedEffect(showController) {
        if (showController && showDropDownMenu.not()) {
            delay(3000)
            showController = false
        }
    }

    LaunchedEffect(videoTimer) {
        while (videoTimer < totalDuration) {
            delay(100)
            videoTimer = player?.currentPosition ?: 0
        }
    }

    DisposableEffect(lifecycleOwner) {
        val listener = object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                super.onEvents(player, events)
                isVideoPlaying = player.isPlaying
                videoTimer = player.currentPosition
                totalDuration = player.duration
                playbackState = player.playbackState
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                title = mediaItem?.mediaMetadata?.displayTitle.toString()
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                isVideoBuffering = playbackState == STATE_BUFFERING
            }
        }
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                ON_CREATE -> {
                    player = Builder(context).build().apply {
                        setMediaItem(fromUri(source))
                        prepare()
                    }
                    player?.addListener(listener)
                }

                ON_START -> if (player?.isPlaying == false) player?.play()
                ON_RESUME -> activity.hideStatusBar(true)
                ON_STOP -> {
                    activity.hideStatusBar(false)
                    player?.pause()
                }

                else -> Unit
            }
        }

        lifecycleOwner.value.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.value.lifecycle.removeObserver(observer)
            player?.removeListener(listener)
            player?.release()
        }
    }

    VideoPlayerContent(
        modifier = modifier.fillMaxSize(),
        player = player ?: return,
        isVideoBuffering = isVideoBuffering,
        title = fileName,
        iconPlayOrPause = if (isVideoPlaying == true) R.drawable.ic_pause else R.drawable.ic_play,
        isControllerVisible = showController,
        isFullScreen = isFullScreen,
        videoTimer = videoTimer,
        totalDuration = totalDuration,
        bufferProgress = player?.bufferedPosition?.toFloat() ?: 0f,
        onClickAny = { showController = showController.not() },
        onClickRewind = {
            player?.seekTo(player?.currentPosition?.minus(5000) ?: 0)
            showController = true
        },
        onClickPlayOrPause = {
            when {
                player?.isPlaying == true -> player?.pause()
                player?.isPlaying == false && playbackState == STATE_ENDED -> {
                    player?.seekTo(0, 0)
                    player?.playWhenReady = true
                }

                else -> player?.play()
            }
            isVideoPlaying = isVideoPlaying?.not()
        },
        onClickForward = {
            player?.seekTo(player?.currentPosition?.plus(5000) ?: 0)
            showController = true
        },
        onClickFullScreen = { if (isFullScreen) exitFullScreen() else enterFullScreen() },
        onClickActionMenu = { showDropDownMenu = showDropDownMenu.not() },
        onClickBack = if (isFullScreen) exitFullScreen else onClickBack,
        onChangeSeekbar = { position ->
            player?.seekTo(position.toLong())
            showController = true
        }
    )

    MeverPopupDropDownMenu(
        modifier = Modifier
            .padding(top = Dp64, end = Dp24)
            .then(Modifier.statusBarsPadding()),
        listDropDown = listOf("Delete", "Share"),
        showDropDownMenu = showDropDownMenu,
        backgroundColor = MeverDark,
        textColor = MeverWhite,
        onDismissDropDownMenu = { showDropDownMenu = false },
        onClick = { item ->
            when (item) {
                "Delete" -> showDeleteDialog = true
                "Share" -> onClickShare()
            }
        }
    )

    MeverDialog(
        meverDialogArgs = MeverDialogArgs(
            title = "Delete this file?",
            primaryButtonText = "Delete",
            titleColor = MeverWhite,
            backgroundColor = MeverDark,
            dismissColor = MeverWhite,
            onClickAction = {
                onClickDelete()
                onClickBack()
                showDeleteDialog = false
            },
            onDismiss = {
                activity.hideStatusBar(true)
                showDeleteDialog = false
            }
        ),
        showDialog = showDeleteDialog
    ) {
        Text(
            text = "File that has been deleted cannot be recovered",
            style = typography.body1,
            color = MeverWhite
        )
    }
}

@Composable
private fun VideoPlayerContent(
    player: Player,
    isVideoBuffering: Boolean,
    title: String,
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
    onChangeSeekbar: (Float) -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = modifier.clickable(
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
                }
            }
        )
    }
    AnimatedVisibility(
        visible = isControllerVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(modifier = modifier.background(MeverBlack.copy(alpha = 0.7f))) {
            MeverTopBar(
                modifier = Modifier.padding(horizontal = Dp24),
                topBarArgs = TopBarArgs(
                    actionMenus = listOf(
                        ActionMenu(
                            icon = R.drawable.ic_more,
                            nameIcon = "More",
                            onClickActionMenu = onClickActionMenu
                        )
                    ),
                    screenName = title,
                    topBarColor = MeverBlack,
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

@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VideoBottomControlSection(
    videoTimer: Long,
    totalDuration: Long,
    isFullScreen: Boolean,
    bufferProgress: Float,
    modifier: Modifier = Modifier,
    onChangeSeekbar: (Float) -> Unit,
    onClickFullScreen: () -> Unit
) {
    val animatedValue by animateFloatAsState(
        targetValue = videoTimer.toFloat().coerceIn(0f, totalDuration.toFloat()),
        animationSpec = spring(
            dampingRatio = DampingRatioLowBouncy
        ),
        label = "animated value"
    )
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier,
        verticalArrangement = spacedBy(Dp10)
    ) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp0) {
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = animatedValue,
                interactionSource = interactionSource,
                valueRange = 0f..totalDuration.toFloat(),
                onValueChange = { onChangeSeekbar(it) },
                track = { sliderState ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth((bufferProgress / totalDuration.toFloat()).coerceIn(0f, 1f))
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
                text = "${videoTimer.toTimeFormat()} / ${totalDuration.toTimeFormat()}",
                style = typography.body1,
                color = MeverWhite
            )
            Image(
                painter = painterResource(
                    if (isFullScreen) R.drawable.ic_fullscreen_exit else R.drawable.ic_fullscreen
                ),
                colorFilter = tint(color = MeverWhite),
                contentDescription = "Fullscreen",
                modifier = Modifier
                    .size(Dp32)
                    .padding(end = Dp6)
                    .clickableSingle { onClickFullScreen() }
            )
        }
    }
}