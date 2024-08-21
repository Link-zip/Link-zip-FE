package umc.link.zip.presentation.mypage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asSharedFlow
import umc.link.zip.data.dto.mypage.request.EditNicknmRequest
import umc.link.zip.domain.model.mypage.CheckNicknmModel
import umc.link.zip.domain.model.mypage.EditNicknmModel
import umc.link.zip.domain.model.mypage.UserInfoModel
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

    private val _nicknameState = MutableSharedFlow<UiState<CheckNicknmModel>>()
    val nicknameState: SharedFlow<UiState<CheckNicknmModel>> get() = _nicknameState.asSharedFlow()

    private val _userInfoState = MutableSharedFlow<UiState<UserInfoModel>>()
    val userInfoState: SharedFlow<UiState<UserInfoModel>> get() = _userInfoState.asSharedFlow()

    private val _editNicknm = MutableSharedFlow<UiState<EditNicknmModel>>()
    val editNicknm: SharedFlow<UiState<EditNicknmModel>> get() = _editNicknm.asSharedFlow()

    fun checkNickname(nickname: String) {
        viewModelScope.launch {
            // 먼저 로딩 상태를 발행합니다.
            _nicknameState.emit(UiState.Loading)

            // repository를 통해 닉네임을 확인합니다.
            repository.checkNicknm(nickname).apply {
                when (this) {
                    is NetworkResult.Success -> {
                        Log.d("MypageProfileViewModel", "checkNickname called with: $nickname")
                        _nicknameState.emit(UiState.Success(this.data))
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
    fun getUserInfo(){
        viewModelScope.launch {
            // 먼저 로딩 상태를 발행합니다.
            _userInfoState.emit(UiState.Loading)

            // repository를 통해 닉네임을 확인합니다.
            repository.getUserInfo().apply {
                when (this) {
                    is NetworkResult.Success -> {
                        Log.d("MypageProfileViewModel", "userInfo called with: $userInfoState")

                        _userInfoState.emit(UiState.Success(this.data))
                    }
                    is NetworkResult.Error -> {
                        _userInfoState.emit(UiState.Error(this.exception))
                    }
                    is NetworkResult.Fail -> {
                        _userInfoState.emit(UiState.Error(Throwable("Failed to load data")))
                    }
                }
            }.onError {
                _userInfoState.emit(UiState.Error(it))
            }.onException {
                _userInfoState.emit(UiState.Error(it))
            }.onFail {
                _userInfoState.emit(UiState.Error(Throwable("Failed to load data")))
            }

        }
    }

    fun editNicknm(nickname: EditNicknmRequest) {
        viewModelScope.launch {
            // 먼저 로딩 상태를 발행합니다.
            _editNicknm.emit(UiState.Loading)

            // repository를 통해 닉네임을 확인합니다.
            repository.editNicknm(nickname).apply {
                when (this) {
                    is NetworkResult.Success -> {
                        Log.d("MypageProfileViewModel", "editNickname called with: $nickname")
                        _editNicknm.emit(UiState.Success(this.data))
                    }
                    is NetworkResult.Error -> {
                        _editNicknm.emit(UiState.Error(this.exception))
                    }
                    is NetworkResult.Fail -> {
                        _editNicknm.emit(UiState.Error(Throwable("Failed to load data")))
                    }
                }
            }.onError {
                _editNicknm.emit(UiState.Error(it))
            }.onException {
                _editNicknm.emit(UiState.Error(it))
            }.onFail {
                _editNicknm.emit(UiState.Error(Throwable("Failed to load data")))
            }

        }
    }
}
