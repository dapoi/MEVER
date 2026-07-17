package com.dapascript.mever.feature.wa.screen

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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.Filled
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.Outlined
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.MeverBannerAd
import com.dapascript.mever.core.common.ui.component.MeverButton
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.component.MeverEmptyItem
import com.dapascript.mever.core.common.ui.component.MeverImage
import com.dapascript.mever.core.common.ui.component.meverShimmer
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp0
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp1
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
import com.dapascript.mever.core.common.util.navigateToWaStore
import com.dapascript.mever.core.common.util.isAppInstalled
import com.dapascript.mever.core.common.util.onCustomClick
import com.dapascript.mever.core.common.util.state.collectAsStateValue
import com.dapascript.mever.core.navigation.helper.Navigator
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryContentDetailRoute
import com.dapascript.mever.core.navigation.route.GalleryScreenRoute.GalleryContentDetailRoute.Content
import com.dapascript.mever.feature.wa.screen.WaStatusLandingAttr.WaMediaModel
import com.dapascript.mever.feature.wa.screen.WaStatusLandingAttr.WaMediaModel.WaType
import com.dapascript.mever.feature.wa.screen.WaStatusLandingAttr.WaMediaModel.WaType.ALL
import com.dapascript.mever.feature.wa.screen.WaStatusLandingAttr.WaMediaModel.WaType.BUSINESS
import com.dapascript.mever.feature.wa.screen.WaStatusLandingAttr.WaMediaModel.WaType.REGULAR
import com.dapascript.mever.feature.wa.viewmodel.WaStatusViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
internal fun WaStatusLandingScreen(
    navigator: Navigator,
    viewModel: WaStatusViewModel = hiltViewModel()
) = with(viewModel) {
    val activity = LocalActivity.current
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()
    val waStatuses = waStatuses.collectAsStateValue()
    val isWaRegularInstalled = remember { isAppInstalled(context, "com.whatsapp") }
    val isWaBusinessInstalled = remember { isAppInstalled(context, "com.whatsapp.w4b") }
    val listState = rememberLazyGridState()
    var titleHeight by rememberSaveable { mutableIntStateOf(0) }
    val isExpanded by remember(titleHeight) {
        derivedStateOf {
            if (titleHeight == 0) return@derivedStateOf true
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset < (titleHeight / 2)
        }
    }
    var showWaNotInstalledDialog by remember { mutableStateOf(false) }
    var permissionDialogType by rememberSaveable { mutableStateOf<WaType?>(null) }
    var regularPermissionGranted by rememberSaveable { mutableStateOf(false) }
    var businessPermissionGranted by rememberSaveable { mutableStateOf(false) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == ON_RESUME) {
                if (isWaRegularInstalled.not() && isWaBusinessInstalled.not()) {
                    showWaNotInstalledDialog = true
                    return@LifecycleEventObserver
                }
                val persistedUris = context.contentResolver.persistedUriPermissions
                val regularUri = getWaUriPermission(persistedUris, "com.whatsapp%2FWhatsApp")
                val businessUri = getWaUriPermission(persistedUris, "com.whatsapp.w4b")

                regularPermissionGranted = regularUri != null
                businessPermissionGranted = businessUri != null

                if (isWaRegularInstalled && regularUri != null) {
                    fetchStatuses(folderUri = regularUri, type = REGULAR)
                }

                if (isWaBusinessInstalled && businessUri != null) {
                    fetchStatuses(folderUri = businessUri, type = BUSINESS)
                }

                onFetchFinished()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val waLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        uri?.let { selectedUri ->
            val isGranted = (permissionDialogType == REGULAR || permissionDialogType == BUSINESS) &&
                    selectedUri.toString().contains(".Statuses")

            if (isGranted) {
                context.contentResolver.takePersistableUriPermission(
                    selectedUri,
                    FLAG_GRANT_READ_URI_PERMISSION
                )

                scope.launch {
                    fetchStatuses(
                        folderUri = selectedUri,
                        type = if (permissionDialogType == REGULAR) REGULAR else BUSINESS
                    )
                }

                when (permissionDialogType) {
                    REGULAR -> regularPermissionGranted = true
                    BUSINESS -> businessPermissionGranted = true
                    else -> Unit
                }

                permissionDialogType = null
            } else {
                permissionDialogType = null
            }
        } ?: run {
            permissionDialogType = null
        }
    }

    BaseScreen(
        topBarArgs = TopBarArgs(title = if (isExpanded) "" else stringResource(R.string.wa_status)),
        onBackHandler = { navigator.goBack() }
    ) {
        LaunchedEffect(listState, titleHeight) {
            delay(500.milliseconds)
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
            MeverDialog(
                showDialog = true,
                image = R.drawable.wa_permission,
                title = stringResource(R.string.permission_request_title),
                description = stringResource(R.string.permission_request_wa),
                primaryActionLabel = stringResource(R.string.ok),
                onClickPrimaryAction = { launchWaPath(waLauncher, type) },
                onClickSecondaryAction = { permissionDialogType = null }
            )
        }

        MeverDialog(
            showDialog = showWaNotInstalledDialog,
            title = stringResource(R.string.error_title),
            description = stringResource(R.string.wa_not_installed),
            primaryActionLabel = stringResource(R.string.install),
            onClickPrimaryAction = {
                showWaNotInstalledDialog = false
                navigateToWaStore(activity)
                navigator.goBack()
            },
            onClickSecondaryAction = { navigator.goBack() }
        )

        WaStatusContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = Dp64),
            isWaRegularInstalled = isWaRegularInstalled,
            isWaBusinessInstalled = isWaBusinessInstalled,
            listState = listState,
            isExpanded = isExpanded,
            regularPermissionGranted = regularPermissionGranted,
            businessPermissionGranted = businessPermissionGranted,
            onSetTitleHeight = { titleHeight = it },
            onRequestPermission = { permissionDialogType = it },
            waStatuses = waStatuses,
            onClickNavigate = { item ->
                navigator.navigate(
                    route = GalleryContentDetailRoute(
                        contents = waStatuses?.map {
                            Content(
                                id = it.uri.hashCode(),
                                isVideo = it.isVideo,
                                isDeletable = false,
                                fileName = it.name,
                                media = it.uri.toString()
                            )
                        }.orEmpty(),
                        initialIndex = waStatuses?.indexOf(item) ?: -1
                    )
                )
            }
        )
    }
}

@Composable
private fun WaStatusContent(
    isWaRegularInstalled: Boolean,
    isWaBusinessInstalled: Boolean,
    listState: LazyGridState,
    isExpanded: Boolean,
    regularPermissionGranted: Boolean,
    businessPermissionGranted: Boolean,
    waStatuses: List<WaMediaModel>?,
    modifier: Modifier = Modifier,
    onSetTitleHeight: (Int) -> Unit,
    onRequestPermission: (WaType) -> Unit,
    onClickNavigate: (WaMediaModel) -> Unit
) {
    var selectedFilter by rememberSaveable {
        mutableStateOf(
            if (isWaRegularInstalled && isWaBusinessInstalled &&
                regularPermissionGranted && businessPermissionGranted
            ) ALL else if (isWaRegularInstalled) REGULAR else BUSINESS
        )
    }
    val deviceType = LocalDeviceType.current
    val headerScroll = rememberScrollState()
    val filteredList = when (selectedFilter) {
        ALL -> waStatuses
        REGULAR -> waStatuses?.filter { it.waType == REGULAR }
        BUSINESS -> waStatuses?.filter { it.waType == BUSINESS }
    }
    val isPermissionGranted = when (selectedFilter) {
        ALL -> regularPermissionGranted && businessPermissionGranted
        REGULAR -> regularPermissionGranted
        BUSINESS -> businessPermissionGranted
    }
    val filters = availableFilters(
        isWaRegularInstalled = isWaRegularInstalled,
        isWaBusinessInstalled = isWaBusinessInstalled,
        regularPermissionGranted = regularPermissionGranted,
        businessPermissionGranted = businessPermissionGranted
    )

    LaunchedEffect(filters) {
        if (selectedFilter !in filters && filters.isNotEmpty()) {
            selectedFilter = filters.first()
        }
    }

    CompositionLocalProvider(LocalOverscrollFactory provides null) {
        Box(modifier = modifier) {
            Column(modifier = Modifier.fillMaxSize()) {
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Text(
                        text = stringResource(R.string.wa_status),
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
                    filters.forEach { filter ->
                        MeverButton(
                            title = stringResource(filter.label),
                            shape = RoundedCornerShape(Dp64),
                            buttonType = if (selectedFilter == filter) Filled(
                                backgroundColor = colors.alwaysPurple,
                                contentColor = MeverWhite
                            ) else Outlined(
                                borderColor = colors.alwaysPurple,
                                contentColor = colors.alwaysPurple
                            )
                        ) { selectedFilter = filter }
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
                filteredList?.let { data ->
                    if (data.isNotEmpty()) LazyVerticalGrid(
                        modifier = Modifier.weight(1f),
                        state = listState,
                        columns = if (deviceType == PHONE) Fixed(2) else Adaptive(Dp150),
                        contentPadding = PaddingValues(
                            start = Dp24,
                            end = Dp24,
                            bottom = Dp150
                        ),
                        horizontalArrangement = spacedBy(Dp16),
                        verticalArrangement = spacedBy(Dp16)
                    ) {
                        items(
                            items = data,
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
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = Dp24),
                            image = R.drawable.ic_empty_state,
                            title = stringResource(R.string.wa_empty_title),
                            description = stringResource(
                                if (isPermissionGranted) R.string.empty_wa_status_desc
                                else R.string.permission_request_wa
                            ),
                            actionButtonLabel = if (isPermissionGranted) null else stringResource(R.string.permission_request_title),
                            onClickAction = if (isPermissionGranted) null else {
                                { onRequestPermission(selectedFilter) }
                            }
                        )
                    }
                } ?: LazyVerticalGrid(
                    modifier = Modifier.weight(1f),
                    state = listState,
                    columns = if (deviceType == PHONE) Fixed(2) else Adaptive(Dp150),
                    contentPadding = PaddingValues(
                        start = Dp24,
                        end = Dp24,
                        bottom = Dp150
                    ),
                    horizontalArrangement = spacedBy(Dp16),
                    verticalArrangement = spacedBy(Dp16)
                ) {
                    items(8) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(Dp8))
                                .aspectRatio(9f / 16f)
                                .meverShimmer()
                        )
                    }
                }
            }
            MeverBannerAd(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(BottomCenter)
                    .navigationBarsPadding()
            )
        }
    }
}

private fun getWaUriPermission(
    persistedUris: List<UriPermission>,
    uri: String
) = persistedUris.find { it.uri.toString().contains(uri) }?.uri

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

private fun availableFilters(
    isWaRegularInstalled: Boolean,
    isWaBusinessInstalled: Boolean,
    regularPermissionGranted: Boolean,
    businessPermissionGranted: Boolean
): List<WaType> = buildList {
    if (isWaRegularInstalled && isWaBusinessInstalled &&
        regularPermissionGranted && businessPermissionGranted
    ) {
        add(ALL)
    }
    if (isWaRegularInstalled) add(REGULAR)
    if (isWaBusinessInstalled) add(BUSINESS)
}