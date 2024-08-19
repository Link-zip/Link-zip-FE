package umc.link.zip.presentation.create.adapter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import umc.link.zip.data.dto.link.request.LinkExtractRequest
import umc.link.zip.domain.model.link.LinkExtractModel
import umc.link.zip.domain.model.link.LinkVisitModel
import umc.link.zip.domain.repository.LinkRepository
import umc.link.zip.util.network.NetworkResult
import umc.link.zip.util.network.UiState
import umc.link.zip.util.network.onError
import umc.link.zip.util.network.onException
import umc.link.zip.util.network.onFail
import javax.inject.Inject

@HiltViewModel
class LinkVisitViewModel @Inject constructor(
    private val linkRepository: LinkRepository
) : ViewModel() {

    // 추출 요청 결과를 관리하는 StateFlow
    private val _visitLinkResponse = MutableStateFlow<UiState<LinkVisitModel>>(UiState.Loading)
    val visitLinkResponse: StateFlow<UiState<LinkVisitModel>> = _visitLinkResponse.asStateFlow()

    fun visitLink(linkId : Int) {
        viewModelScope.launch {
            linkRepository.VisitLink(linkId).apply {
                when (this) {
                    is NetworkResult.Success -> {
                        _visitLinkResponse.value = UiState.Loading  // 상태를 초기화 (동일한 데이터가 와도 방출될 수 있도록)
                        _visitLinkResponse.value = UiState.Success(this.data)
                        Log.d("LinkVisitViewModel", "Loading visit link data 성공")
                    }

                    is NetworkResult.Error -> {
                        _visitLinkResponse.value = UiState.Error(this.exception)
                        Log.d("LinkVisitViewModel", "Loading visit link data 에러 ${this.exception}")
                    }

                    is NetworkResult.Fail -> {
                        _visitLinkResponse.value = UiState.Error(Throwable("Failed to load visit data"))
                        Log.d("LinkVisitViewModel", "Loading visit link data 실패")
                    }
                }
            }.onError {
                _visitLinkResponse.value = UiState.Error(it)
            }.onException {
                _visitLinkResponse.value = UiState.Error(it)
            }.onFail {
                _visitLinkResponse.value = UiState.Error(Throwable("Failed to load visit data"))
            }
        }
    }

}


