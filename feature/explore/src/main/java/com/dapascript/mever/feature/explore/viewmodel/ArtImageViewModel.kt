package com.dapascript.mever.feature.explore.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.PlatformType.ART
import com.dapascript.mever.core.common.util.state.UiState
import com.dapascript.mever.core.common.util.state.UiState.StateInitial
import com.dapascript.mever.core.data.model.local.ArtEntity
import com.dapascript.mever.core.data.repository.MeverRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
internal class ArtImageViewModel @Inject constructor(
    private val repository: MeverRepository
) : BaseViewModel() {

    var query by mutableStateOf("")
    var artworks by mutableStateOf<List<ArtEntity>?>(null)
    var currentPage by mutableIntStateOf(1)
        private set
    var canPaginate by mutableStateOf(true)
        private set
    var isPagingLoading by mutableStateOf(false)
        private set

    private val _artResponseState = MutableStateFlow<UiState<List<ArtEntity>>>(StateInitial)
    val artResponseState = _artResponseState.asStateFlow()

    init {
        getArtworks()
    }

    fun getArtworks(query: String = "") {
        this.query = query
        currentPage = 1
        canPaginate = true
        isPagingLoading = false
        collectApiAsUiState(
            response = repository.getArtworks(query = query, page = 1),
            onLoading = {
                _artResponseState.value = UiState.StateLoading
            },
            onSuccess = { result ->
                artworks = result
                _artResponseState.value = UiState.StateSuccess(result)
            },
            onFailed = { message ->
                _artResponseState.value = UiState.StateFailed(message)
            }
        )
    }

    fun loadNextPage() {
        if (isPagingLoading || !canPaginate || _artResponseState.value !is UiState.StateSuccess) return

        val nextPage = currentPage + 1
        collectApiAsUiState(
            response = repository.getArtworks(query = query, page = nextPage),
            onLoading = { isPagingLoading = true },
            onSuccess = { newArtworks ->
                if (newArtworks.isEmpty()) {
                    canPaginate = false
                } else {
                    val currentList = artworks.orEmpty()
                    artworks = (currentList + newArtworks).distinctBy { it.id }
                    currentPage = nextPage
                }
                isPagingLoading = false
            },
            onFailed = { isPagingLoading = false }
        )
    }

    fun startDownload(
        url: String,
        fileName: String
    ) {
        if (url.isBlank()) return
        repository.download(
            url = url,
            fileName = fileName,
            tag = ART.platformName
        )
    }
}