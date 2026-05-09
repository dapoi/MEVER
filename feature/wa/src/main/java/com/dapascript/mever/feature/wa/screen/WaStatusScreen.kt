package com.dapascript.mever.feature.wa.screen

import android.content.Context
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.content.UriPermission
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells.Adaptive
import androidx.compose.foundation.lazy.grid.GridCells.Fixed
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.Filled
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.Outlined
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.MeverBannerAd
import com.dapascript.mever.core.common.ui.component.MeverButton
import com.dapascript.mever.core.common.ui.component.MeverDialogError
import com.dapascript.mever.core.common.ui.component.MeverEmptyItem
import com.dapascript.mever.core.common.ui.component.MeverImage
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp0
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp1
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp120
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp150
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp3
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp64
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme.colors
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp32
import com.dapascript.mever.core.common.util.DeviceType.PHONE
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.LocalDeviceType
import com.dapascript.mever.core.common.util.WaManager.WaMediaModel
import com.dapascript.mever.core.common.util.WaManager.WaType
import com.dapascript.mever.core.common.util.WaManager.WaType.ALL
import com.dapascript.mever.core.common.util.WaManager.WaType.BUSINESS
import com.dapascript.mever.core.common.util.WaManager.WaType.REGULAR
import com.dapascript.mever.core.common.util.goToWaStore
import com.dapascript.mever.core.common.util.isAppInstalled
import com.dapascript.mever.core.common.util.onCustomClick
import com.dapascript.mever.core.common.util.state.collectAsStateValue
import com.dapascript.mever.core.navigation.helper.navigateTo
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryContentDetailRoute
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryContentDetailRoute.Content
import com.dapascript.mever.feature.wa.viewmodel.WaStatusViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
internal fun WaStatusScreen(
    navController: NavController,
    viewModel: WaStatusViewModel = hiltViewModel()
) = with(viewModel) {
    val activity = LocalActivity.current
    val context = LocalContext.current
    val waStatuses = waStatuses.collectAsStateValue()
    val isWaRegularInstalled = remember { isAppInstalled(context, "com.whatsapp") }
    val isWaBusinessInstalled = remember { isAppInstalled(context, "com.whatsapp.w4b") }
    val listState = rememberLazyGridState()
    val isExpanded by remember(titleHeight) {
        derivedStateOf {
            if (titleHeight == 0) return@derivedStateOf true
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset < (titleHeight / 2)
        }
    }
    var showWaNotInstalledDialog by remember { mutableStateOf(false) }
    var permissionDialogType by remember { mutableStateOf<WaType?>(null) }
    val waLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it,
                FLAG_GRANT_READ_URI_PERMISSION
            )
            permissionDialogType = null

            val type = if (
                it.toString().contains("com.whatsapp.w4b")
                || it.toString().contains("Business")
            ) BUSINESS else REGULAR

            viewModel.fetchStatuses(it, type)
        }
    }

    BaseScreen(
        topBarArgs = TopBarArgs(
            title = if (isExpanded) "" else "WhatsApp Status",
            onClickBack = { navController.popBackStack() }
        )
    ) {
        LaunchedEffect(Unit) {
            if (isWaRegularInstalled.not() && isWaBusinessInstalled.not()) {
                showWaNotInstalledDialog = true
                return@LaunchedEffect
            }

            clearStatuses()
            val persistedUris = context.contentResolver.persistedUriPermissions
            val regularUri = getWaUriPermission(persistedUris, "com.whatsapp%2FWhatsApp")
            val businessUri = getWaUriPermission(persistedUris, "com.whatsapp.w4b")

            if (isWaRegularInstalled && regularUri != null) viewModel.fetchStatuses(
                regularUri,
                REGULAR
            )
            if (isWaBusinessInstalled && businessUri != null) viewModel.fetchStatuses(
                businessUri,
                BUSINESS
            )

            if (isWaRegularInstalled && regularUri == null) {
                permissionDialogType = REGULAR
            } else if (isWaRegularInstalled.not() && isWaBusinessInstalled && businessUri == null) {
                permissionDialogType = BUSINESS
            }
        }

        LaunchedEffect(listState, titleHeight) {
            delay(500L)
            snapshotFlow { listState.isScrollInProgress }
                .distinctUntilChanged()
                .filter { it.not() }
                .collect {
                    if (titleHeight == 0 || listState.firstVisibleItemIndex > 0) return@collect

                    val threshold = titleHeight / 2
                    val currentOffset = listState.firstVisibleItemScrollOffset

                    if (currentOffset in 1 until titleHeight) {
                        val targetIndex = if (currentOffset < threshold) 0 else 1
                        listState.animateScrollToItem(targetIndex)
                    }
                }
        }

        permissionDialogType?.let { type ->
            MeverDialogError(
                showDialog = true,
                errorImage = R.drawable.wa_permission,
                errorTitle = stringResource(R.string.permission_request_title),
                errorDescription = stringResource(R.string.permission_request_wa),
                primaryButtonText = stringResource(R.string.ok),
                onClickPrimary = { launchWaPath(waLauncher, type) },
                onClickSecondary = {
                    permissionDialogType = null
                    navController.popBackStack()
                }
            )
        }

        MeverDialogError(
            showDialog = showWaNotInstalledDialog,
            errorImage = R.drawable.ic_storage,
            errorTitle = stringResource(R.string.error_title),
            errorDescription = stringResource(R.string.wa_not_installed),
            primaryButtonText = stringResource(R.string.install),
            onClickPrimary = {
                showWaNotInstalledDialog = false
                activity.goToWaStore()
                navController.popBackStack()
            },
            onClickSecondary = {
                showWaNotInstalledDialog = false
                navController.popBackStack()
            }
        )

        WaStatusContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = Dp64),
            context = context,
            isWaRegularInstalled = isWaRegularInstalled,
            isWaBusinessInstalled = isWaBusinessInstalled,
            waMediaList = waStatuses,
            listState = listState,
            isExpanded = isExpanded,
            onSetTitleHeight = { titleHeight = it },
            onRequestPermission = { permissionDialogType = it },
            onClickNavigate = { item ->
                navController.navigateTo(
                    route = GalleryContentDetailRoute(
                        contents = waStatuses.map {
                            Content(
                                id = it.uri.hashCode(),
                                isVideo = it.isVideo,
                                isDeletable = false,
                                fileName = it.name,
                                media = it.uri.toString()
                            )
                        },
                        initialIndex = waStatuses.indexOf(item)
                    )
                )
            }
        )
    }
}

@Composable
private fun WaStatusContent(
    context: Context,
    isWaRegularInstalled: Boolean,
    isWaBusinessInstalled: Boolean,
    waMediaList: List<WaMediaModel>,
    listState: LazyGridState,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    onSetTitleHeight: (Int) -> Unit,
    onRequestPermission: (WaType) -> Unit,
    onClickNavigate: (WaMediaModel) -> Unit
) {
    var selectedFilter by rememberSaveable { mutableStateOf(ALL) }
    val deviceType = LocalDeviceType.current
    val headerScroll = rememberScrollState()
    val filteredList = remember(waMediaList, selectedFilter) {
        waMediaList.filter {
            selectedFilter == ALL || it.waType == selectedFilter
        }
    }

    CompositionLocalProvider(LocalOverscrollFactory provides null) {
        if (waMediaList.isNotEmpty()) {
            Box(modifier = modifier) {
                Column(modifier = Modifier.fillMaxSize()) {
                    AnimatedVisibility(
                        visible = isExpanded,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Text(
                            text = "WhatsApp Status",
                            style = typography.h2.copy(fontSize = Sp32),
                            color = colors.blackWhite,
                            modifier = Modifier
                                .padding(
                                    top = Dp16,
                                    start = Dp24,
                                    end = Dp24,
                                    bottom = if (isWaRegularInstalled && isWaBusinessInstalled) Dp0 else Dp24
                                )
                                .onGloballyPositioned { onSetTitleHeight(it.size.height) }
                        )
                    }
                    if (isWaRegularInstalled && isWaBusinessInstalled) Row(
                        modifier = Modifier
                            .background(colors.whiteDark)
                            .fillMaxWidth()
                            .horizontalScroll(headerScroll)
                            .padding(start = Dp24, top = Dp16, bottom = Dp24),
                        horizontalArrangement = spacedBy(Dp8),
                        verticalAlignment = CenterVertically
                    ) {
                        WaType.entries.forEach { filter ->
                            MeverButton(
                                title = filter.label,
                                shape = RoundedCornerShape(Dp64),
                                buttonType = if (selectedFilter == filter) Filled(
                                    backgroundColor = colors.alwaysPurple,
                                    contentColor = MeverWhite
                                ) else Outlined(
                                    borderColor = colors.alwaysPurple,
                                    contentColor = colors.alwaysPurple
                                )
                            ) {
                                selectedFilter = filter

                                if (filter == BUSINESS) {
                                    val hasBizPermission =
                                        context.contentResolver.persistedUriPermissions.any {
                                            it.uri.toString().contains("com.whatsapp.w4b")
                                        }
                                    if (!hasBizPermission) onRequestPermission(BUSINESS)
                                } else if (filter == REGULAR) {
                                    val hasRegPermission =
                                        context.contentResolver.persistedUriPermissions.any {
                                            it.uri.toString().contains("com.whatsapp%2FWhatsApp")
                                        }
                                    if (!hasRegPermission) onRequestPermission(REGULAR)
                                }
                            }
                        }
                    }
                    if (isExpanded.not()) {
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(Dp3),
                            thickness = Dp1,
                            color = colors.blackWhite.copy(alpha = 0.12f)
                        )
                    }
                    if (filteredList.isNotEmpty()) LazyVerticalGrid(
                        modifier = Modifier.weight(1f),
                        state = listState,
                        columns = if (deviceType == PHONE) Fixed(2) else Adaptive(Dp150),
                        contentPadding = PaddingValues(
                            start = Dp24,
                            end = Dp24,
                            bottom = Dp120
                        ),
                        horizontalArrangement = spacedBy(Dp16),
                        verticalArrangement = spacedBy(Dp16)
                    ) {
                        items(
                            items = filteredList,
                            key = { it.uri.toString() },
                            contentType = { "wa_status_item" }
                        ) { item ->
                            MeverImage(
                                modifier = Modifier
                                    .animateItem()
                                    .clip(RoundedCornerShape(Dp8))
                                    .aspectRatio(9f / 16f)
                                    .onCustomClick { onClickNavigate(item) },
                                source = item.uri,
                                isVideoThumbnail = item.isVideo
                            )
                        }
                    } else {
                        MeverEmptyItem(
                            modifier = Modifier.weight(1f),
                            image = R.drawable.ic_empty_file,
                            size = Dp150.plus(Dp16),
                            description = stringResource(R.string.empty_wa_status_desc)
                        )
                    }
                }
                MeverBannerAd(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(BottomCenter)
                        .navigationBarsPadding()
                )
            }
        } else {
            MeverEmptyItem(
                modifier = Modifier.fillMaxSize(),
                image = R.drawable.ic_empty_file,
                size = Dp150.plus(Dp16),
                description = stringResource(R.string.empty_wa_status_desc)
            )
        }
    }
}

private fun getWaUriPermission(
    persistedUris: List<UriPermission>,
    uri: String
) = persistedUris.find { it.toString().contains(uri) }?.uri

private fun launchWaPath(
    waLauncher: ManagedActivityResultLauncher<Uri?, Uri?>,
    type: WaType
) {
    try {
        val waUri = if (type == BUSINESS) {
            "content://com.android.externalstorage.documents/document/primary%3AAndroid%2Fmedia%2Fcom.whatsapp.w4b%2FWhatsApp%20Business%2FMedia%2F.Statuses"
        } else {
            "content://com.android.externalstorage.documents/document/primary%3AAndroid%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses"
        }
        waLauncher.launch(waUri.toUri())
    } catch (_: Exception) {
        waLauncher.launch(null)
    }
}