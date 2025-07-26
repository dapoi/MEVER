package com.dapascript.mever.core.common.ui.component

import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.dapascript.mever.core.common.util.LocalActivity

@Composable
fun MeverPermissionHandler(
    permissions: List<String>,
    onGranted: () -> Unit,
    onDenied: @Composable (permanentlyDeclined: Boolean, onRetry: () -> Unit) -> Unit
) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    var shouldRequestPermission by remember { mutableStateOf(true) }
    var isPermanentlyDeclined by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        RequestMultiplePermissions()
    ) { result ->
        val denied = result.filterValues { !it }.map { it.key }

        if (denied.isEmpty()) onGranted()
        else {
            val needsRationale = denied.any {
                activity.shouldShowRequestPermissionRationale(it)
            }
            isPermanentlyDeclined = needsRationale.not()
            shouldRequestPermission = false
        }
    }

    LaunchedEffect(shouldRequestPermission) {
        if (shouldRequestPermission.not()) return@LaunchedEffect

        val notGranted = permissions.filter {
            context.checkSelfPermission(it) != PERMISSION_GRANTED
        }

        if (notGranted.isEmpty()) {
            onGranted()
        } else {
            permissionLauncher.launch(notGranted.toTypedArray())
        }
    }

    if (shouldRequestPermission.not()) {
        onDenied( isPermanentlyDeclined) { shouldRequestPermission = true }
    }
}