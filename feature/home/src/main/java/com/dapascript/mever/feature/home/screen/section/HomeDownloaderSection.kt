package com.dapascript.mever.feature.home.screen.section

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.FILLED
import com.dapascript.mever.core.common.ui.attr.MeverCardAttr.MeverCardArgs
import com.dapascript.mever.core.common.ui.attr.MeverIconAttr.getPlatformIcon
import com.dapascript.mever.core.common.ui.attr.MeverIconAttr.getPlatformIconBackgroundColor
import com.dapascript.mever.core.common.ui.component.MeverButton
import com.dapascript.mever.core.common.ui.component.MeverCard
import com.dapascript.mever.core.common.ui.component.MeverEmptyItem
import com.dapascript.mever.core.common.ui.component.MeverIcon
import com.dapascript.mever.core.common.ui.component.MeverTextField
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp10
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp150
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp40
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp48
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp5
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp22
import com.dapascript.mever.core.common.util.Constant.PlatformName.UNKNOWN
import com.dapascript.mever.core.common.util.Constant.PlatformType
import com.dapascript.mever.core.common.util.getPlatformType
import com.dapascript.mever.core.common.util.onCustomClick
import com.ketch.DownloadModel
import com.dapascript.mever.core.common.util.Constant.PlatformType.UNKNOWN as UNKNOWN_PLAT

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun HomeDownloaderSection(
    isLoading: Boolean,
    urlSocialMediaState: TextFieldValue,
    downloadList: List<DownloadModel>?,
    modifier: Modifier = Modifier,
    onClickCard: (DownloadModel) -> Unit,
    onClickDelete: (DownloadModel) -> Unit,
    onClickShare: (DownloadModel) -> Unit,
    onValueChange: (TextFieldValue) -> Unit,
    onClickDownload: () -> Unit,
    onClickViewAll: () -> Unit
) = CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
    LazyColumn(modifier = modifier) {
        item {
            Text(
                text = stringResource(R.string.downloader_title),
                style = typography.h2.copy(fontSize = Sp22),
                color = colorScheme.onPrimary
            )
        }
        item {
            Spacer(modifier = Modifier.size(Dp16))
            Text(
                text = stringResource(R.string.downloader_desc),
                style = typography.body2,
                color = colorScheme.secondary
            )
        }
        item {
            Spacer(modifier = Modifier.size(Dp24))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = SpaceBetween
            ) {
                PlatformType.entries.filter { it.platformName != UNKNOWN }.map {
                    MeverIcon(
                        icon = getPlatformIcon(it.platformName),
                        iconBackgroundColor = getPlatformIconBackgroundColor(it.platformName),
                        iconSize = Dp48,
                        iconPadding = Dp10
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.size(Dp24))
            MeverTextField(
                modifier = Modifier.fillMaxWidth(),
                webDomainValue = urlSocialMediaState,
                onValueChange = { onValueChange(it) }
            )
        }
        item {
            Spacer(modifier = Modifier.size(Dp10))
            MeverButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dp40),
                title = stringResource(R.string.download),
                buttonType = FILLED,
                isEnabled = urlSocialMediaState.text.trim().getPlatformType() != UNKNOWN_PLAT,
                isLoading = isLoading
            ) { onClickDownload() }
            Spacer(modifier = Modifier.size(Dp24))
        }
        stickyHeader {
            Row(
                modifier = Modifier
                    .background(color = colorScheme.background)
                    .fillMaxWidth()
                    .padding(top = Dp16),
                verticalAlignment = CenterVertically,
                horizontalArrangement = SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.recently_downloaded),
                    style = typography.bodyBold1,
                    color = colorScheme.onPrimary
                )
                if (downloadList.isNullOrEmpty().not()) Text(
                    text = stringResource(R.string.view_all),
                    style = typography.body2,
                    color = colorScheme.primary,
                    modifier = Modifier
                        .animateItem()
                        .clip(RoundedCornerShape(Dp8))
                        .onCustomClick { onClickViewAll() }
                )
            }
        }
        downloadList?.let { files ->
            if (files.isNotEmpty()) items(
                items = files.take(5),
                key = { it.id }
            ) {
                MeverCard(
                    modifier = Modifier.animateItem(),
                    cardArgs = MeverCardArgs(
                        source = it.url,
                        tag = it.tag,
                        fileName = it.fileName,
                        status = it.status,
                        progress = it.progress,
                        total = it.total,
                        path = it.path,
                        urlThumbnail = it.metaData,
                        icon = getPlatformIcon(it.tag),
                        iconBackgroundColor = getPlatformIconBackgroundColor(it.tag),
                        iconSize = Dp24,
                        iconPadding = Dp5
                    ),
                    onClickCard = { onClickCard(it) },
                    onClickShare = { onClickShare(it) },
                    onClickDelete = { onClickDelete(it) }
                )
            } else item {
                MeverEmptyItem(
                    image = R.drawable.ic_not_found,
                    size = Dp150.plus(Dp16),
                    description = stringResource(R.string.empty_list_desc)
                )
            }
        }
    }
}