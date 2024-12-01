package com.dapascript.mever.core.common.ui.component

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat.getInsetsController
import androidx.core.view.WindowInsetsCompat.Type.systemBars
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.Lifecycle.Event.ON_STOP
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer.Builder
import androidx.media3.ui.PlayerView
import com.dapascript.mever.core.common.util.LocalActivity

@SuppressLint("SourceLockedOrientationActivity")
@OptIn(UnstableApi::class)
@Composable
fun MeverVideoPlayer(
    sourceVideo: String,
    modifier: Modifier = Modifier
) {
    var player by remember { mutableStateOf<Player?>(null) }
    var isFullScreen by remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current
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
    val playerView = createPlayerView(context, player)

    with(playerView) {
        keepScreenOn = true
        setShowNextButton(false)
        setShowPreviousButton(false)
        setFullscreenButtonState(isFullScreen)
        setFullscreenButtonClickListener { isPortrait -> if (isPortrait) enterFullScreen() else exitFullScreen() }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                ON_START -> {
                    player = initPlayer(context, sourceVideo)
                    playerView.onResume()
                }

                ON_STOP -> {
                    playerView.apply {
                        player?.release()
                        onPause()
                        player = null
                    }
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    DisposableEffect(Unit) {
        val window = activity.window
        val insetsController = getInsetsController(window, window.decorView)

        insetsController.apply {
            hide(systemBars())
            systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        onDispose {
            insetsController.apply {
                show(systemBars())
                systemBarsBehavior = BEHAVIOR_DEFAULT
            }
        }
    }

    BackHandler(isFullScreen) { exitFullScreen() }

    AndroidView(
        factory = { playerView },
        modifier = modifier.fillMaxSize()
    )
}

@Composable
private fun createPlayerView(
    context: Context,
    player: Player?
): PlayerView {
    val playerView = remember { PlayerView(context).apply { this.player = player } }
    DisposableEffect(player) {
        playerView.player = player
        onDispose { playerView.player = null }
    }
    return playerView
}

private fun initPlayer(context: Context, sourceVideo: String) = Builder(context).build().apply {
    setMediaItem(MediaItem.fromUri(sourceVideo))
    playWhenReady = true
    prepare()
}