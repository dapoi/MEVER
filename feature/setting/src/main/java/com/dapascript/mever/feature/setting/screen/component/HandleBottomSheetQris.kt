package com.dapascript.mever.feature.setting.screen.component

import android.graphics.BitmapFactory.decodeResource
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale.Companion.FillBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.component.MeverBottomSheet
import com.dapascript.mever.core.common.ui.component.MeverDeclinedPermission
import com.dapascript.mever.core.common.ui.component.MeverPermissionHandler
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp14
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp2
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp500
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.getStoragePermission
import com.dapascript.mever.core.common.util.goToSetting
import com.dapascript.mever.core.common.util.onCustomClick
import com.dapascript.mever.core.common.util.saveBitmapToStorage
import kotlinx.coroutines.launch

@Composable
internal fun HandleBottomSheetQris(
    showQrisDialog: Boolean,
    onDismiss: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val resources = LocalResources.current
    val scope = rememberCoroutineScope()
    val bitmap = remember { decodeResource(resources, R.drawable.qris) }
    var isSaveSuccess by remember { mutableStateOf(false) }
    var setStoragePermission by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(isSaveSuccess) {
        if (isSaveSuccess) {
            Toast.makeText(
                context,
                resources.getString(R.string.image_has_been_downloaded),
                Toast.LENGTH_SHORT
            ).show()
            onDismiss(false)
            isSaveSuccess = false
        }
    }

    if (setStoragePermission.isNotEmpty()) {
        MeverPermissionHandler(
            permissions = setStoragePermission,
            onGranted = {
                setStoragePermission = emptyList()
                scope.launch {
                    saveBitmapToStorage(
                        context = context,
                        bitmap = bitmap,
                        fileName = "qris.jpg"
                    ).let { isSaveSuccess = it }
                }
            },
            onDenied = { isPermanentlyDeclined, onRetry ->
                MeverDeclinedPermission(
                    isPermissionsDeclined = isPermanentlyDeclined,
                    onGoToSetting = {
                        setStoragePermission = emptyList()
                        activity.goToSetting()
                    },
                    onRetry = { onRetry() },
                    onDismiss = { setStoragePermission = emptyList() }
                )
            }
        )
    }

    MeverBottomSheet(
        modifier = Modifier.wrapContentSize(),
        showBottomSheet = showQrisDialog,
        onDismissBottomSheet = { onDismiss(false) }
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = spacedBy(Dp16)
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = Dp24)
                    .clip(RoundedCornerShape(Dp16)),
                contentAlignment = Center
            ) {
                Image(
                    modifier = Modifier
                        .heightIn(max = Dp500)
                        .aspectRatio(3f / 4f),
                    painter = painterResource(R.drawable.qris),
                    contentScale = FillBounds,
                    contentDescription = "QRIS"
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dp24)
                    .background(colorScheme.background),
                verticalAlignment = CenterVertically,
                horizontalArrangement = SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Dp14))
                        .onCustomClick { onDismiss(false) }
                        .weight(1f)
                        .padding(vertical = Dp16),
                    contentAlignment = Center
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        style = typography.bodyBold1,
                        color = colorScheme.onPrimary
                    )
                }
                Box(
                    modifier = Modifier
                        .width(Dp2)
                        .height(Dp20)
                        .background(
                            color = colorScheme.onPrimary.copy(alpha = .08f),
                            shape = RoundedCornerShape(Dp8)
                        )
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Dp14))
                        .onCustomClick {
                            setStoragePermission = getStoragePermission()
                            onDismiss(false)
                        }
                        .weight(1f)
                        .padding(vertical = Dp16),
                    contentAlignment = Center
                ) {
                    Text(
                        text = stringResource(R.string.download),
                        style = typography.bodyBold1,
                        color = colorScheme.primary
                    )
                }
            }
        }
    }
}