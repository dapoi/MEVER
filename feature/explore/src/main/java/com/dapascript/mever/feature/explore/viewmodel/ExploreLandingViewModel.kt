package com.dapascript.mever.feature.explore.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.PlatformType.EXPLORE
import com.dapascript.mever.core.common.util.state.UiState
import com.dapascript.mever.core.common.util.state.UiState.StateInitial
import com.dapascript.mever.core.common.util.storage.StorageUtil.getMeverFolder
import com.dapascript.mever.core.data.model.local.ContentEntity
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.feature.explore.BuildConfig.DEBUG
import com.ketch.Ketch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ExploreLandingViewModel @Inject constructor(
    private val repository: MeverRepository,
    private val ketch: Ketch
) : BaseViewModel() {

    private val meverFolder by lazy { getMeverFolder() }

    var query by mutableStateOf("")
    var contents by mutableStateOf<List<ContentEntity>?>(null)

    private val _exploreResponseState = MutableStateFlow<UiState<List<ContentEntity>>>(StateInitial)
    val exploreResponseState = _exploreResponseState.asStateFlow()

    fun getExploreContents(query: String) = collectApiAsUiState(
        response = repository.getImageSearch(query),
        state = _exploreResponseState
    )

    init {
        getExploreContents(if (DEBUG) randomQuery() else "")
    }

    fun randomQuery() = setOf(
        "technology",
        "cute animal",
        "automotive",
        "nature",
        "waifu"
    ).random()

    fun startDownload(
        url: String,
        fileName: String
    ) {
        if (url.isBlank()) return
        ketch.download(
            url = url,
            fileName = "$fileName.jpg",
            path = meverFolder.path,
            tag = EXPLORE.platformName
        )
    }
}