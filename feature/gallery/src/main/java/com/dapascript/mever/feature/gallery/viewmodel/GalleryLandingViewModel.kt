package com.dapascript.mever.feature.gallery.viewmodel

import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.PlatformType
import com.dapascript.mever.core.common.util.PlatformType.ALL
import com.dapascript.mever.core.data.repository.MeverRepository
import com.ketch.DownloadModel
import com.ketch.Status.PAUSED
import com.ketch.Status.PROGRESS
import com.ketch.Status.QUEUED
import com.ketch.Status.STARTED
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
internal class GalleryLandingViewModel @Inject constructor(
    private val repository: MeverRepository
) : BaseViewModel() {

    private val _isInitialLoading = MutableStateFlow(true)
    val isInitialLoading = _isInitialLoading.asStateFlow()

    private val _isFilterLoading = MutableStateFlow(false)
    val isFilterLoading = _isFilterLoading.asStateFlow()

    private val _selectedFilter = MutableStateFlow(ALL)
    val selectedFilter = _selectedFilter.asStateFlow()

    private val _currentPage = MutableStateFlow(1)
    val currentPage = _currentPage.asStateFlow()

    val downloadList = repository.observeDownloads()
        .distinctUntilChanged()
        .flowOn(Default)
        .stateIn(viewModelScope, WhileSubscribed(5000), null)

    val filteredDownloads = combine(downloadList, _selectedFilter) { downloads, filter ->
        downloads?.filter {
            filter == ALL || it.tag == filter.platformName
        }
    }.distinctUntilChanged()
        .flowOn(Default)
        .stateIn(viewModelScope, WhileSubscribed(5000), null)

    val paginatedDownloads = combine(filteredDownloads, _currentPage) { downloads, page ->
        val currentLoaded = INITIAL_PAGE_SIZE + (page - 1) * PAGE_SIZE
        downloads?.take(currentLoaded)
    }.distinctUntilChanged()
        .flowOn(Default)
        .stateIn(viewModelScope, WhileSubscribed(5000), null)

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore = _isLoadingMore.asStateFlow()

    val canLoadMore = combine(filteredDownloads, _currentPage) { downloads, page ->
        val total = downloads?.size ?: 0
        val currentLoaded = INITIAL_PAGE_SIZE + (page - 1) * PAGE_SIZE
        total > currentLoaded
    }.distinctUntilChanged()
        .flowOn(Default)
        .stateIn(viewModelScope, WhileSubscribed(5000), false)

    fun loadNextPage() {
        if (_isLoadingMore.value) return
        viewModelScope.launch {
            _isLoadingMore.value = true
            delay(250.milliseconds)
            _currentPage.update { it + 1 }
            _isLoadingMore.value = false
        }
    }

    fun resetPagination() {
        _currentPage.value = 1
        _isLoadingMore.value = false
    }

    val isAnyDownloadActive = downloadList
        .map { list ->
            list?.any { file ->
                file.status in listOf(QUEUED, STARTED, PAUSED, PROGRESS)
            } ?: false
        }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5000),
            initialValue = false
        )

    val platformTypes = downloadList
        .map { list ->
            val uniqueTags = list?.map { it.tag }?.toSet() ?: emptySet()
            PlatformType.entries.filter { it.platformName in uniqueTags }
        }
        .distinctUntilChanged()
        .stateIn(viewModelScope, WhileSubscribed(5000), listOf(ALL))

    private val _selectedItems = MutableStateFlow<Set<DownloadModel>>(emptySet())
    val selectedItems = _selectedItems.asStateFlow()

    init {
        viewModelScope.launch {
            delay(350.milliseconds)
            _isInitialLoading.value = false
        }
    }

    fun updateFilter(filter: PlatformType) {
        if (_selectedFilter.value == filter) return
        _selectedFilter.value = filter
        resetPagination()
        viewModelScope.launch {
            _isFilterLoading.value = true
            delay(200.milliseconds)
            _isFilterLoading.value = false
        }
    }

    fun toggleSelection(item: DownloadModel) {
        _selectedItems.update { currentItem ->
            if (item in currentItem) currentItem - item else currentItem + item
        }
    }

    fun toggleSelectionAll(items: List<DownloadModel>) {
        _selectedItems.update { items.toSet() }
    }

    fun clearSelection() {
        _selectedItems.value = emptySet()
    }

    fun resumeDownload(id: Int) = repository.resumeDownload(id)

    fun pauseDownload(id: Int) = repository.pauseDownload(id)

    fun pauseAllDownloads() = repository.pauseAllDownloads()

    fun retryDownload(id: Int) = repository.retryDownload(id)

    fun delete(id: Int) {
        repository.deleteDownload(id)
    }

    fun deleteItems(ids: List<Int>) {
        repository.deleteDownloads(ids)
    }

    fun deleteAll() {
        repository.deleteAllDownloads()
        resetPagination()
    }

    fun refreshDatabase() {
        viewModelScope.launch { repository.refreshDownloadDatabase() }
    }

    companion object {
        const val INITIAL_PAGE_SIZE = 3
        const val PAGE_SIZE = 5
    }
}