package com.dapascript.mever.feature.explore.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.state.UiState
import com.dapascript.mever.core.common.util.state.UiState.StateFailed
import com.dapascript.mever.core.common.util.state.UiState.StateInitial
import com.dapascript.mever.core.common.util.state.UiState.StateLoading
import com.dapascript.mever.core.common.util.state.UiState.StateSuccess
import com.dapascript.mever.core.data.model.local.ContentEntity
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.feature.explore.BuildConfig.DEBUG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ExploreLandingViewModel @Inject constructor(
    private val repository: MeverRepository
) : BaseViewModel() {

    var query by mutableStateOf("")

    private val _exploreResponseState = MutableStateFlow<UiState<List<ContentEntity>>>(StateInitial)
    val exploreResponseState = _exploreResponseState.asStateFlow()

    fun getExploreContents(query: String) = collectApiAsUiState(
        response = repository.getImageSearch(query),
        onLoading = { _exploreResponseState.value = StateLoading },
        onSuccess = { _exploreResponseState.value = StateSuccess(it) },
        onFailed = { _exploreResponseState.value = StateFailed(it) }
    )

    init {
        getExploreContents(
            if (DEBUG) setOf(
                "technology",
                "cute animal",
                "wallpaper",
                "automotive",
                "nature"
            ).random() else ""
        )
    }
}