package umc.link.zip.presentation.create.adapter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import umc.link.zip.data.dto.link.request.LinkExtractRequest
import umc.link.zip.domain.model.link.LinkExtractModel
import umc.link.zip.domain.repository.LinkRepository
import umc.link.zip.util.network.NetworkResult
import umc.link.zip.util.network.UiState
import umc.link.zip.util.network.onError
import umc.link.zip.util.network.onException
import umc.link.zip.util.network.onFail
import javax.inject.Inject

@HiltViewModel
class LinkExtractViewModel @Inject constructor(
    private val linkRepository: LinkRepository
) : ViewModel() {

    // 추출 요청 결과를 관리하는 StateFlow
    private val _extractResponse = MutableStateFlow<UiState<LinkExtractModel>>(UiState.Loading)
    val extractResponse: StateFlow<UiState<LinkExtractModel>> = _extractResponse.asStateFlow()

    fun fetchLinkExtract(linkExtractRequest: LinkExtractRequest) {
        viewModelScope.launch {
            linkRepository.ExtractLink(linkExtractRequest).apply {
                when (this) {
                    is NetworkResult.Success -> {
                        _extractResponse.value = UiState.Loading  // 상태를 초기화 (동일한 데이터가 와도 방출될 수 있도록)
                        _extractResponse.value = UiState.Success(this.data)
                    }
                    is NetworkResult.Error -> {
                        _extractResponse.value = UiState.Error(this.exception)
                    }
                    is NetworkResult.Fail -> {
                        _extractResponse.value = UiState.Error(Throwable("Failed to load data"))
                    }
                }
            }.onError {
                _extractResponse.value = UiState.Error(it)
            }.onException {
                _extractResponse.value = UiState.Error(it)
            }.onFail {
                _extractResponse.value = UiState.Error(Throwable("Failed to load data"))
            }
        }
    }

}


