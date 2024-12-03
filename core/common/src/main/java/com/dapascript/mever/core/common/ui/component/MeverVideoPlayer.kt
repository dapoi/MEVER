package com.dapascript.mever.core.common.ui.component

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring.DampingRatioLowBouncy
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults.Thumb
import androidx.compose.material3.SliderDefaults.Track
import androidx.compose.material3.SliderDefaults.colors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat.getInsetsController
import androidx.core.view.WindowInsetsCompat.Type.systemBars
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.STATE_ENDED
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer.Builder
import androidx.media3.ui.PlayerView
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp0
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp48
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp6
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverBlack
import com.dapascript.mever.core.common.ui.theme.MeverPurple
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.clickableSingle
import com.dapascript.mever.core.common.util.toTimeFormat
import kotlinx.coroutines.delay

@SuppressLint("SourceLockedOrientationActivity")
@OptIn(UnstableApi::class)
@Composable
fun MeverVideoPlayer(
    sourceVideo: String,
    fileName: String,
    onClickBack: () -> Unit
) {
    var player by remember { mutableStateOf<Player?>(null) }
    var isFullScreen by remember { mutableStateOf(false) }
    var isVideoPlaying by remember { mutableStateOf(player?.isPlaying) }
    var playbackState by remember { mutableStateOf(player?.playbackState) }
    var title by remember { mutableStateOf(player?.currentMediaItem?.mediaMetadata?.displayTitle.toString()) }
    var videoTimer by remember { mutableLongStateOf(0L) }
    var totalDuration by remember { mutableLongStateOf(0L) }
    var showController by remember { mutableStateOf(false) }
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    val context = LocalContext.current
    val activity = LocalActivity.current

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
        if (showController) {
            delay(3000)
            showController = false
        }
    }

    LaunchedEffect(videoTimer) {
        while (videoTimer < totalDuration) {
            delay(1000)
            videoTimer = player?.currentPosition ?: 0
        }
    }

    DisposableEffect(true) {
        val listener = object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                super.onEvents(player, events)
                isVideoPlaying = player.isPlaying
                playbackState = player.playbackState
                videoTimer = player.currentPosition
                totalDuration = player.duration
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                title = mediaItem?.mediaMetadata?.displayTitle.toString()
            }
        }
        player = initPlayer(context, sourceVideo).apply { addListener(listener) }
        onDispose { player?.removeListener(listener) }
    }

    DisposableEffect(lifecycleOwner) {
        val window = activity.window
        val insetsController = getInsetsController(window, window.decorView)
        val observer = object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                super.onStart(owner)
                if (player?.isPlaying?.not() == true) player?.play()
                insetsController.apply {
                    hide(systemBars())
                    systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            }

            override fun onStop(owner: LifecycleOwner) {
                player?.pause()
                insetsController.apply {
                    show(systemBars())
                    systemBarsBehavior = BEHAVIOR_DEFAULT
                }
                super.onStop(owner)
            }
        }
        lifecycleOwner.value.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.value.lifecycle.removeObserver(observer)
            player?.release()
        }
    }

    VideoPlayerContent(
        modifier = Modifier.fillMaxSize(),
        player = player ?: return,
        title = fileName,
        iconPlayOrPause = if (isVideoPlaying == true) R.drawable.ic_pause else R.drawable.ic_play,
        isControllerVisible = showController,
        isFullScreen = isFullScreen,
        videoTimer = videoTimer,
        totalDuration = totalDuration,
        onClickAny = { showController = showController.not() },
        onClickRewind = { player?.seekTo(player?.currentPosition?.minus(5000) ?: 0) },
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
        onClickForward = { player?.seekTo(player?.currentPosition?.plus(5000) ?: 0) },
        onClickFullScreen = { if (isFullScreen) exitFullScreen() else enterFullScreen() },
        onSeekChange = { position -> player?.seekTo(position.toLong()) },
        onClickBack = if (isFullScreen) exitFullScreen else onClickBack
    )
}

@Composable
private fun VideoPlayerContent(
    player: Player,
    title: String,
    iconPlayOrPause: Int,
    isControllerVisible: Boolean,
    isFullScreen: Boolean,
    videoTimer: Long,
    totalDuration: Long,
    modifier: Modifier = Modifier,
    onClickAny: () -> Unit,
    onClickRewind: () -> Unit,
    onClickPlayOrPause: () -> Unit,
    onClickForward: () -> Unit,
    onClickFullScreen: () -> Unit,
    onClickBack: () -> Unit,
    onSeekChange: (Float) -> Unit,
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
        modifier = modifier,
        visible = isControllerVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(modifier = Modifier.background(MeverBlack.copy(alpha = 0.7f))) {
            VideoTitleSection(
                modifier = Modifier.align(TopStart),
                title = title,
                onClickBack = onClickBack
            )
            VideoCenterControlSection(
                modifier = Modifier.align(Center),
                iconPlayOrPause = iconPlayOrPause,
                onClickRewind = onClickRewind,
                onClickPlay = onClickPlayOrPause,
                onClickForward = onClickForward
            )
            VideoBottomControlSection(
                modifier = Modifier.align(BottomCenter),
                videoTimer = videoTimer,
                totalDuration = totalDuration,
                isFullScreen = isFullScreen,
                onSeekChange = onSeekChange,
                onClickFullScreen = onClickFullScreen
            )
        }
    }
}

@Composable
private fun VideoTitleSection(
    title: String,
    modifier: Modifier = Modifier,
    onClickBack: () -> Unit
) = Row(
    modifier = modifier
        .fillMaxWidth()
        .padding(Dp24),
    horizontalArrangement = spacedBy(Dp16),
    verticalAlignment = CenterVertically
) {
    Image(
        painter = painterResource(R.drawable.ic_back),
        colorFilter = tint(MeverWhite),
        contentDescription = "Back",
        modifier = Modifier
            .clip(CircleShape)
            .clickable { onClickBack() }
    )
    Text(
        text = title,
        style = typography.h7,
        color = MeverWhite,
        maxLines = 1,
        overflow = Ellipsis
    )
}

@Composable
private fun VideoCenterControlSection(
    iconPlayOrPause: Int,
    modifier: Modifier = Modifier,
    onClickRewind: () -> Unit,
    onClickPlay: () -> Unit,
    onClickForward: () -> Unit
) = Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = SpaceEvenly
) {
    Image(
        painter = painterResource(R.drawable.ic_rewind),
        colorFilter = tint(color = MeverWhite),
        contentDescription = "Rewind",
        modifier = Modifier
            .size(Dp48)
            .clip(CircleShape)
            .clickable { onClickRewind() }
    )
    Image(
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
            .size(Dp48)
            .clip(CircleShape)
            .clickable { onClickForward() }
    )
}

@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VideoBottomControlSection(
    videoTimer: Long,
    totalDuration: Long,
    isFullScreen: Boolean,
    modifier: Modifier = Modifier,
    onSeekChange: (Float) -> Unit,
    onClickFullScreen: () -> Unit
) {
    val animatedValue by animateFloatAsState(
        targetValue = videoTimer.toFloat(),
        animationSpec = spring(
            dampingRatio = DampingRatioLowBouncy
        ),
        label = "animatedValue"
    )
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = Dp16)
    ) {
        Slider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dp8),
            value = animatedValue,
            interactionSource = interactionSource,
            valueRange = 0f..totalDuration.toFloat(),
            onValueChange = { onSeekChange(it) },
            track = { sliderState ->
                Track(
                    modifier = Modifier.height(Dp6),
                    sliderState = sliderState,
                    drawStopIndicator = null,
                    thumbTrackGapSize = Dp0,
                    colors = colors(
                        activeTrackColor = MeverPurple,
                        inactiveTrackColor = MeverWhite.copy(alpha = 0.5f)
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dp20),
            horizontalArrangement = SpaceBetween
        ) {
            Text(
                text = "${videoTimer.toTimeFormat()} / ${totalDuration.toTimeFormat()}",
                style = typography.body2,
                color = MeverWhite
            )
            Image(
                painter = painterResource(
                    if (isFullScreen) R.drawable.ic_fullscreen_exit else R.drawable.ic_fullscreen
                ),
                colorFilter = tint(color = MeverWhite),
                contentDescription = "Fullscreen",
                modifier = Modifier
                    .size(Dp24)
                    .clickableSingle { onClickFullScreen() }
            )
        }
    }
}

private fun initPlayer(context: Context, sourceVideo: String) = Builder(context).build().apply {
    setMediaItem(MediaItem.fromUri(sourceVideo))
    prepare()
    playWhenReady = true
}