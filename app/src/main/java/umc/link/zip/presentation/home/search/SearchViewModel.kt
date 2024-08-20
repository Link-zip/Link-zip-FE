package umc.link.zip.presentation.home.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import umc.link.zip.data.dto.list.request.UnreadRequest
import umc.link.zip.domain.model.list.UnreadModel
import umc.link.zip.domain.model.search.SearchResult
import umc.link.zip.domain.repository.SearchRepository
import umc.link.zip.util.network.NetworkResult
import umc.link.zip.util.network.UiState
import umc.link.zip.util.network.onError
import umc.link.zip.util.network.onException
import umc.link.zip.util.network.onFail
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
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
}