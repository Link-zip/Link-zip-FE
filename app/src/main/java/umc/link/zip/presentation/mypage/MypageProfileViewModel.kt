package umc.link.zip.presentation.mypage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import umc.link.zip.R

@HiltViewModel
class MypageProfileViewModel @Inject constructor(
    // private val repository: ProfileRepository // 후에 api 연결 시 사용
) : ViewModel() {

    private val _nicknameState = MutableSharedFlow<NicknameState>()
    val nicknameState: SharedFlow<NicknameState> get() = _nicknameState

    fun checkNickname(nickname: String) {
        viewModelScope.launch {
            Log.d("MypageProfileViewModel", "checkNickname called with: $nickname")
            // 상태를 방출
            _nicknameState.emit(NicknameState.Empty)
            val result = "no" // 임시 결과
            val newState = if (result == "no") {
                NicknameState.Invalid("이미 사용 중인 유저가 있어요!", R.color.disabled_color)
            } else {
                NicknameState.Valid("환상적인 닉네임이에요!", R.color.abled_color)
            }
            _nicknameState.emit(newState)
        }
    }
}
