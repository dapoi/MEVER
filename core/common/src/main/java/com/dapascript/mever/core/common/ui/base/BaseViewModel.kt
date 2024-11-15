package com.dapascript.mever.core.common.ui.base

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel() {

    val showDialogPermission = mutableStateListOf<String>()

    fun dismissDialog() {
        showDialogPermission.removeAt(0)
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean,
        onAction: () -> Unit
    ) {
        if (isGranted.not() && showDialogPermission.contains(permission).not()) {
            showDialogPermission.add(permission)
        } else onAction()
    }
}