package com.dapascript.mever.feature.notification.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.base.attr.BaseScreenAttr.BaseScreenArgs
import com.dapascript.mever.core.common.navigation.base.BaseNavigator
import com.dapascript.mever.core.common.ui.attr.MeverCardAttr.MeverCardArgs
import com.dapascript.mever.core.common.ui.component.MeverCard
import com.dapascript.mever.core.common.ui.component.MeverEmptyItem
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.util.Constant.ScreenName.NOTIFICATION
import com.dapascript.mever.feature.notification.viewmodel.NotificationLandingViewModel
import com.ketch.Status.PAUSED

@Composable
internal fun NotificationLandingScreen(
    navigator: BaseNavigator,
    viewModel: NotificationLandingViewModel = hiltViewModel()
) = with(viewModel) {
    val downloadList = downloadList.collectAsStateValue()

    BaseScreen(
        baseScreenArgs = BaseScreenArgs(
            screenName = NOTIFICATION,
            onClickBack = { navigator.popBackStack() }
        )
    ) {
        LaunchedEffect(Unit) { getAllDownloads() }

        if (downloadList.isEmpty()) MeverEmptyItem("There are no files being downloaded")
        else LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(
                items = downloadList,
                key = { it.id }
            ) {
                MeverCard(
                    modifier = Modifier
                        .padding(vertical = Dp12)
                        .animateItem(),
                    meverCardArgs = MeverCardArgs(
                        image = it.url,
                        fileName = it.fileName,
                        status = it.status,
                        progress = it.progress,
                        total = it.total,
                        path = it.path,
                        onDownloadingClick = { stateResumeOrPauseDownload(isPause = it.status == PAUSED, id = it.id) }
                    )
                )
            }
        }
    }
}