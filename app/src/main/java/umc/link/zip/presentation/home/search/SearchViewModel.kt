package umc.link.zip.presentation.home.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import umc.link.zip.data.dto.list.request.UnreadRequest
import umc.link.zip.domain.model.list.UnreadModel
import umc.link.zip.domain.model.search.KeywordLocalSource
import umc.link.zip.domain.model.search.SearchKeywordEntity
import umc.link.zip.domain.model.search.SearchRecent
import umc.link.zip.domain.model.search.SearchResult
import umc.link.zip.domain.model.search.toEntity
import umc.link.zip.domain.repository.SearchRepository
import umc.link.zip.util.network.NetworkResult
import umc.link.zip.util.network.UiState
import umc.link.zip.util.network.onError
import umc.link.zip.util.network.onException
import umc.link.zip.util.network.onFail
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val keywordLocalSource: KeywordLocalSource
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<SearchResult>>(UiState.Loading)
    val uiState: StateFlow<UiState<SearchResult>> = _uiState.asStateFlow()

    fun getSearchLink(keyword: String) {
        viewModelScope.launch {
            searchRepository.searchLink(keyword).apply {
                when (this) {
                    is NetworkResult.Success -> {
                        _uiState.value = UiState.Loading  // 상태를 초기화 (동일한 데이터가 와도 방출될 수 있도록)
                        _uiState.value = UiState.Success(this.data)
                    }
                    is NetworkResult.Error -> {
                        _uiState.value = UiState.Error(this.exception)
                    }
                    is NetworkResult.Fail -> {
                        // 서버에서 "SEARCH002" 코드를 반환한 경우
                        if (this.message.contains("SEARCH002")) {
                            _uiState.value = UiState.Empty
                        } else {
                            _uiState.value = UiState.Error(Throwable("Failed to load data"))
                        }
                    }
                }
            }.onError {
                _uiState.value = UiState.Error(it)
            }.onException {
                _uiState.value = UiState.Error(it)
            }.onFail {
                _uiState.value = UiState.Error(Throwable("Failed to load data"))
            }
        }
    }

    private val _recentKeywords = MutableStateFlow<List<SearchRecent>>(emptyList())
    val recentKeywords: StateFlow<List<SearchRecent>> = _recentKeywords

    init {
        fetchRecentKeywords()
    }

    fun fetchRecentKeywords() {
        viewModelScope.launch {
            keywordLocalSource.getKeywordList()
                .map { list -> list.map { it.toSearchRecent() } }
                .catch { e ->
                    Log.e("SearchViewModel", "Error fetching recent keywords", e)
                }
                .collect { keywords ->
                    _recentKeywords.value = keywords
                }
        }
    }

    fun addKeyword(keyword: String) {
        viewModelScope.launch {
            val searchRecent = SearchRecent(
                id = 0,  // auto-generate ID
                keyword = keyword
            )
            keywordLocalSource.setKeyword(searchRecent.toEntity())
            fetchRecentKeywords()  // 키워드 추가 후 리스트 갱신
        }
    }

    fun clearOldKeywords() {
        viewModelScope.launch {
            keywordLocalSource.deleteOldKeyword()
        }
    }
    fun deleteKeyword(searchRecent: SearchRecent) {
        viewModelScope.launch {
            keywordLocalSource.deleteKeyword(searchRecent.toEntity())
            fetchRecentKeywords()
        }
    }

    fun clearAllKeywords() {
        viewModelScope.launch {
            keywordLocalSource.deleteKeywordList()
            _recentKeywords.value = emptyList()  // 리스트를 비웁니다.
            fetchRecentKeywords()
        }
    }
}