package umc.link.zip.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import umc.link.zip.domain.repository.ListRepository
import umc.link.zip.util.network.NetworkResult
import umc.link.zip.util.network.UiState
import javax.inject.Inject
import umc.link.zip.data.dto.list.request.UnreadRequest
import umc.link.zip.data.dto.request.TestRequest
import umc.link.zip.domain.model.link.LinkUpdateLikeModel
import umc.link.zip.domain.model.list.UnreadModel
import umc.link.zip.domain.repository.LinkRepository
import umc.link.zip.util.network.onError
import umc.link.zip.util.network.onException
import umc.link.zip.util.network.onFail
import umc.link.zip.util.network.onSuccess

@HiltViewModel
class ListUnreadViewModel @Inject constructor(
    private val listRepository: ListRepository,
    private val linkRepository: LinkRepository
) : ViewModel() {

    private val _linkId = MutableStateFlow<UiState<LinkUpdateLikeModel>>(UiState.Loading)
    val linkId: StateFlow<UiState<LinkUpdateLikeModel>> get() = _linkId

    private val _uiState = MutableStateFlow<UiState<UnreadModel>>(UiState.Loading)
    val uiState: StateFlow<UiState<UnreadModel>> = _uiState.asStateFlow()


    fun fetchUnreadList(request: UnreadRequest) {
        viewModelScope.launch {
            listRepository.getUnreadList(request).apply {
                when (this) {
                    is NetworkResult.Success -> {
                        _uiState.value = UiState.Loading  // 상태를 초기화 (동일한 데이터가 와도 방출될 수 있도록)
                        _uiState.value = UiState.Success(this.data)
                    }
                    is NetworkResult.Error -> {
                        _uiState.value = UiState.Error(this.exception)
                    }
                    is NetworkResult.Fail -> {
                        _uiState.value = UiState.Error(Throwable("Failed to load data"))
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

    fun updateLikeStatusOnServer(linkId: Int) {
        viewModelScope.launch {
            linkRepository.UpdateLikeLink(linkId).apply {
                when (this) {
                    is NetworkResult.Success -> {
                        _linkId.value = UiState.Loading  // 상태를 초기화 (동일한 데이터가 와도 방출될 수 있도록)
                        _linkId.value = UiState.Success(this.data)
                    }
                    is NetworkResult.Error -> {
                        _linkId.value = UiState.Error(this.exception)
                    }
                    is NetworkResult.Fail -> {
                        _linkId.value = UiState.Error(Throwable("Failed to load data"))
                    }
                }
            }.onError {
                _linkId.value = UiState.Error(it)
            }.onException {
                _linkId.value = UiState.Error(it)
            }.onFail {
                _linkId.value = UiState.Error(Throwable("Failed to load data"))
            }
        }
    }
}