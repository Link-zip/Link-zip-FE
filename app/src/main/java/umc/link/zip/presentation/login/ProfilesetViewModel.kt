package umc.link.zip.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import umc.link.zip.data.dto.request.SignupRequest
import umc.link.zip.domain.model.login.NameCheckModel
import umc.link.zip.domain.model.login.TokenModel
import umc.link.zip.domain.repository.LoginRepository
import umc.link.zip.util.network.NetworkResult
import javax.inject.Inject

@HiltViewModel
class ProfilesetViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _nameCheckResult = MutableLiveData<NetworkResult<NameCheckModel>>()
    val nameCheckResult: LiveData<NetworkResult<NameCheckModel>> = _nameCheckResult

    private val _signupResult = MutableLiveData<NetworkResult<TokenModel>>()
    val signupResult: LiveData<NetworkResult<TokenModel>> = _signupResult

    fun nameCheck(nickname: String) {
        viewModelScope.launch {
            _nameCheckResult.value = loginRepository.nameCheck(nickname)
        }
    }

    fun signup(request: SignupRequest) {
        viewModelScope.launch {
            _signupResult.value = loginRepository.signup(request)
        }
    }
}