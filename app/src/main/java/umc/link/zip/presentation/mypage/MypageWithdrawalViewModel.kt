package umc.link.zip.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import umc.link.zip.domain.model.mypage.WithdrawalModel
import umc.link.zip.domain.model.notice.NoticeList
import umc.link.zip.domain.repository.MypageRepository
import umc.link.zip.util.network.NetworkResult
import umc.link.zip.util.network.UiState
import umc.link.zip.util.network.onError
import umc.link.zip.util.network.onException
import umc.link.zip.util.network.onFail
import javax.inject.Inject

@HiltViewModel
class MypageWithdrawalViewModel @Inject constructor(
    private val repository: MypageRepository // 후에 api 연결 시 사용
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<WithdrawalModel>>(UiState.Loading)
    val uiState: StateFlow<UiState<WithdrawalModel>> = _uiState.asStateFlow()

    fun deleteUser() {
        viewModelScope.launch {
            repository.deleteUser().apply {
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
}