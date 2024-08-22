package umc.link.zip.presentation.create.adapter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import umc.link.zip.domain.model.create.CreateLink
import umc.link.zip.domain.model.link.LinkGetByLinkIDModel
import umc.link.zip.domain.repository.LinkRepository
import umc.link.zip.util.network.NetworkResult
import umc.link.zip.util.network.UiState
import umc.link.zip.util.network.onError
import umc.link.zip.util.network.onException
import umc.link.zip.util.network.onFail
import javax.inject.Inject

@HiltViewModel
class LinkGetByIDViewModel @Inject constructor(
    private val linkRepository: LinkRepository
) : ViewModel() {

    // 추출 요청 결과를 관리하는 StateFlow
    private val _linkGetByLinkIDResponse = MutableStateFlow<UiState<LinkGetByLinkIDModel>>(UiState.Loading)
    val linkGetByLinkIDResponse: StateFlow<UiState<LinkGetByLinkIDModel>> = _linkGetByLinkIDResponse.asStateFlow()

    fun getLinkByLinkID(linkId : Int) {
        viewModelScope.launch {
            linkRepository.GetLinkByLinkID(linkId).apply {
                when (this) {
                    is NetworkResult.Success -> {
                        _linkGetByLinkIDResponse.value = UiState.Loading  // 상태를 초기화 (동일한 데이터가 와도 방출될 수 있도록)
                        _linkGetByLinkIDResponse.value = UiState.Success(this.data)
                        Log.d("LinkGetByIDViewModel", "Loading get link data 성공")
                    }

                    is NetworkResult.Error -> {
                        _linkGetByLinkIDResponse.value = UiState.Error(this.exception)
                        Log.d("LinkGetByIDViewModel", "Loading get link data 에러 ${this.exception}")
                    }

                    is NetworkResult.Fail -> {
                        _linkGetByLinkIDResponse.value = UiState.Error(Throwable("Failed to load data"))
                        Log.d("LinkGetByIDViewModel", "Loading get link data 실패")
                    }
                }
            }.onError {
                _linkGetByLinkIDResponse.value = UiState.Error(it)
            }.onException {
                _linkGetByLinkIDResponse.value = UiState.Error(it)
            }.onFail {
                _linkGetByLinkIDResponse.value = UiState.Error(Throwable("Failed to load data"))
            }
        }
    }
    fun resetState() {
        // 네트워크 응답 초기화
        _linkGetByLinkIDResponse.value = UiState.Loading

        Log.d("LinkGetByIDViewModel", "뷰모델 상태가 초기화되었습니다.")
    }
}
