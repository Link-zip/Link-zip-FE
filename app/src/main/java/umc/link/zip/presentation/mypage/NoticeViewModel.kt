package umc.link.zip.presentation.mypage

import androidx.lifecycle.ViewModel
import umc.link.zip.domain.repository.MypageRepository
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import umc.link.zip.domain.model.notice.Notice
import umc.link.zip.domain.model.notice.NoticeList
import umc.link.zip.util.network.NetworkResult
import umc.link.zip.util.network.UiState
import umc.link.zip.util.network.onError
import umc.link.zip.util.network.onException
import umc.link.zip.util.network.onFail

@HiltViewModel
class NoticeViewModel @Inject constructor(
    private val repository: MypageRepository // 후에 api 연결 시 사용
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<NoticeList>>(UiState.Loading)
    val uiState: StateFlow<UiState<NoticeList>> = _uiState.asStateFlow()

    private val _detail = MutableStateFlow<UiState<Notice>>(UiState.Loading)
    val detail: StateFlow<UiState<Notice>> = _detail.asStateFlow()


    fun getNotice() {
        viewModelScope.launch {
            repository.getNotice().apply {
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

    fun getNoticeDetail(id: Int) {
        viewModelScope.launch {
            repository.getNoticeDetail(id).apply {
                when (this) {
                    is NetworkResult.Success -> {
                        _detail.value = UiState.Loading  // 상태를 초기화 (동일한 데이터가 와도 방출될 수 있도록)
                        _detail.value = UiState.Success(this.data)
                    }
                    is NetworkResult.Error -> {
                        _detail.value = UiState.Error(this.exception)
                    }
                    is NetworkResult.Fail -> {
                        _detail.value = UiState.Error(Throwable("Failed to load data"))
                    }
                }
            }.onError {
                _detail.value = UiState.Error(it)
            }.onException {
                _detail.value = UiState.Error(it)
            }.onFail {
                _detail.value = UiState.Error(Throwable("Failed to load data"))
            }
        }
    }

}