package com.dapascript.mever.feature.wa.viewmodel

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.DocumentsContract.Document.COLUMN_DISPLAY_NAME
import android.provider.DocumentsContract.Document.COLUMN_DOCUMENT_ID
import android.provider.DocumentsContract.Document.COLUMN_LAST_MODIFIED
import android.provider.DocumentsContract.buildChildDocumentsUriUsingTree
import android.provider.DocumentsContract.getTreeDocumentId
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.isVideo
import com.dapascript.mever.feature.wa.screen.WaStatusLandingAttr.WaMediaModel
import com.dapascript.mever.feature.wa.screen.WaStatusLandingAttr.WaMediaModel.WaType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaStatusViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context
) : BaseViewModel() {
    private val _waStatuses = MutableStateFlow<List<WaMediaModel>?>(null)
    val waStatuses = _waStatuses.asStateFlow()

    private val allStatuses = mutableListOf<WaMediaModel>()

    fun onFetchFinished() {
        if (_waStatuses.value == null) _waStatuses.value = emptyList()
    }

    fun fetchStatuses(folderUri: Uri, type: WaType) {
        viewModelScope.launch {
            val statuses = mutableListOf<WaMediaModel>()

            try {
                val treeDocumentId = getTreeDocumentId(folderUri)
                val childrenUri = buildChildDocumentsUriUsingTree(
                    folderUri,
                    treeDocumentId
                )
                val projection = arrayOf(
                    COLUMN_DOCUMENT_ID,
                    COLUMN_DISPLAY_NAME,
                    COLUMN_LAST_MODIFIED
                )

                context.contentResolver.query(childrenUri, projection, null, null, null)
                    ?.use { cursor ->
                        val idColumn = cursor.getColumnIndexOrThrow(COLUMN_DOCUMENT_ID)
                        val nameColumn = cursor.getColumnIndexOrThrow(COLUMN_DISPLAY_NAME)
                        val modifiedColumn = cursor.getColumnIndexOrThrow(COLUMN_LAST_MODIFIED)

                        while (cursor.moveToNext()) {
                            val documentId = cursor.getString(idColumn)
                            val name = cursor.getString(nameColumn)

                            if (name != null && isValidMedia(name)) {
                                val fileUri = DocumentsContract.buildDocumentUriUsingTree(
                                    folderUri,
                                    documentId
                                )
                                val lastModified = cursor.getLong(modifiedColumn)

                                statuses.add(
                                    WaMediaModel(
                                        uri = fileUri,
                                        name = name,
                                        lastModified = lastModified,
                                        waType = type,
                                        isVideo = isVideo(name)
                                    )
                                )
                            }
                        }
                    }

                allStatuses.removeAll { it.waType == type }
                allStatuses.addAll(statuses)

                _waStatuses.value = allStatuses
                    .distinctBy { it.uri }
                    .sortedByDescending { it.lastModified }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun isValidMedia(name: String): Boolean {
        val extension = name.substringAfterLast(".", "").lowercase()
        return extension in listOf("jpg", "mp4")
    }
}