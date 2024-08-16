package umc.link.zip.presentation.mypage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import umc.link.zip.R
import umc.link.zip.data.dto.list.request.UnreadRequest
import umc.link.zip.data.dto.mypage.request.CheckNicknmRequest
import umc.link.zip.domain.model.list.UnreadModel
import umc.link.zip.domain.model.mypage.CheckNicknmModel
import umc.link.zip.domain.repository.MypageRepository
import umc.link.zip.util.network.NetworkResult
import umc.link.zip.util.network.UiState
import umc.link.zip.util.network.onError
import umc.link.zip.util.network.onException
import umc.link.zip.util.network.onFail

@HiltViewModel
class MypageProfileViewModel @Inject constructor(
     private val repository: MypageRepository // 후에 api 연결 시 사용
) : ViewModel() {

    private val _nicknameState = MutableSharedFlow<UiState<NicknameState>>()
    val nicknameState: SharedFlow<UiState<NicknameState>> get() = _nicknameState.asSharedFlow()

    fun checkNickname(nickname: CheckNicknmRequest) {
        viewModelScope.launch {
            // 먼저 로딩 상태를 발행합니다.
            _nicknameState.emit(UiState.Loading)

            // repository를 통해 닉네임을 확인합니다.
            repository.checkNicknm(nickname).apply {
                when (this) {
                    is NetworkResult.Success -> {
                        Log.d("MypageProfileViewModel", "checkNickname called with: $nickname")

                        val result = "ok" // 임시 결과
                        val newState = if (result == "no") {
                            NicknameState.Invalid("이미 사용 중인 유저가 있어요!", R.color.disabled_color)
                        } else {
                            NicknameState.Valid("환상적인 닉네임이에요!", R.color.abled_color)
                        }
                        _nicknameState.emit(UiState.Success(newState))
                    }
                    is NetworkResult.Error -> {
                        _nicknameState.emit(UiState.Error(this.exception))
                    }
                    is NetworkResult.Fail -> {
                        _nicknameState.emit(UiState.Error(Throwable("Failed to load data")))
                    }
                }
            }.onError {
                _nicknameState.emit(UiState.Error(it))
            }.onException {
                _nicknameState.emit(UiState.Error(it))
            }.onFail {
                _nicknameState.emit(UiState.Error(Throwable("Failed to load data")))
            }

        }
    }
}
