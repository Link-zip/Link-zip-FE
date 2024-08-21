package umc.link.zip.presentation.create.adapter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import umc.link.zip.data.dto.link.request.LinkSummaryRequest
import umc.link.zip.domain.model.link.LinkSummaryModel
import umc.link.zip.domain.repository.LinkRepository
import umc.link.zip.util.network.NetworkResult
import umc.link.zip.util.network.UiState
import umc.link.zip.util.network.onError
import umc.link.zip.util.network.onException
import umc.link.zip.util.network.onFail
import javax.inject.Inject

@HiltViewModel
class LinkSummaryViewModel @Inject constructor(
    private val linkRepository: LinkRepository
) : ViewModel() {

    // 요약 요청 결과를 관리하는 StateFlow
    private val _summaryResponse = MutableStateFlow<UiState<LinkSummaryModel>>(UiState.Loading)
    val summaryResponse: StateFlow<UiState<LinkSummaryModel>> = _summaryResponse.asStateFlow()

    fun fetchLinkSummary(linkSummaryRequest: LinkSummaryRequest) {
        viewModelScope.launch {
            linkRepository.SummaryLink(linkSummaryRequest).apply {
                when (this) {
                    is NetworkResult.Success -> {
                        _summaryResponse.value = UiState.Loading  // 상태를 초기화 (동일한 데이터가 와도 방출될 수 있도록)
                        _summaryResponse.value = UiState.Success(this.data)
                        Log.d("LinkSummaryViewModel", "Loading text summary data 성공")
                    }
                    is NetworkResult.Error -> {
                        _summaryResponse.value = UiState.Error(this.exception)
                        Log.d("LinkSummaryViewModel", "Loading text summary data 에러 ${this.exception}")
                    }
                    is NetworkResult.Fail -> {
                        _summaryResponse.value = UiState.Error(Throwable("Failed to load data"))
                        Log.d("LinkSummaryViewModel", "Loading text summary data 실패")
                    }
                }
            }.onError {
                _summaryResponse.value = UiState.Error(it)
            }.onException {
                _summaryResponse.value = UiState.Error(it)
            }.onFail {
                _summaryResponse.value = UiState.Error(Throwable("Failed to load data"))
            }
        }
    }
}
