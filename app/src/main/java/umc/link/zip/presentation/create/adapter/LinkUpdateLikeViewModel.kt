package umc.link.zip.presentation.create.adapter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import umc.link.zip.domain.model.link.LinkUpdateLikeModel
import umc.link.zip.domain.repository.LinkRepository
import umc.link.zip.util.network.NetworkResult
import umc.link.zip.util.network.UiState
import umc.link.zip.util.network.onError
import umc.link.zip.util.network.onException
import umc.link.zip.util.network.onFail
import javax.inject.Inject

@HiltViewModel
class LinkUpdateLikeViewModel @Inject constructor(
    private val linkRepository: LinkRepository
) : ViewModel() {

    // 추출 요청 결과를 관리하는 StateFlow
    private val _linkUpdateLikeResponse = MutableStateFlow<UiState<LinkUpdateLikeModel>>(UiState.Loading)
    val linkUpdateLikeResponse: StateFlow<UiState<LinkUpdateLikeModel>> get() = _linkUpdateLikeResponse

    fun updateLikeStatusOnServer(linkId: Int) {
        viewModelScope.launch {
            linkRepository.UpdateLikeLink(linkId).apply {
                when (this) {
                    is NetworkResult.Success -> {
                        _linkUpdateLikeResponse.value = UiState.Loading  // 상태를 초기화 (동일한 데이터가 와도 방출될 수 있도록)
                        _linkUpdateLikeResponse.value = UiState.Success(this.data)
                    }
                    is NetworkResult.Error -> {
                        _linkUpdateLikeResponse.value = UiState.Error(this.exception)
                    }
                    is NetworkResult.Fail -> {
                        _linkUpdateLikeResponse.value = UiState.Error(Throwable("Failed to load data"))
                    }
                }
            }.onError {
                _linkUpdateLikeResponse.value = UiState.Error(it)
            }.onException {
                _linkUpdateLikeResponse.value = UiState.Error(it)
            }.onFail {
                _linkUpdateLikeResponse.value = UiState.Error(Throwable("Failed to load data"))
            }
        }
    }

}


