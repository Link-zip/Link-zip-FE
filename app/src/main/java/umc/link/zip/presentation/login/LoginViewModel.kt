package umc.link.zip.presentation.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import umc.link.zip.data.dto.request.LoginRequest
import umc.link.zip.domain.model.login.JwtModel
import umc.link.zip.domain.model.login.LoginModel
import umc.link.zip.domain.repository.LoginRepository
import umc.link.zip.util.network.NetworkResult
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel(){

    private val _loginResult = MutableLiveData<NetworkResult<LoginModel>>()
    val loginResult : LiveData<NetworkResult<LoginModel>> = _loginResult

    private val _checkJwtResult = MutableLiveData<NetworkResult<JwtModel>>()
    val checkJwtResult : LiveData<NetworkResult<JwtModel>> = _checkJwtResult

    fun login(request: LoginRequest) {
        viewModelScope.launch {
            _loginResult.value = loginRepository.login(request)
        }
    }

    fun checkJwt() {
        viewModelScope.launch {
            _checkJwtResult.value = loginRepository.checkJwt()
        }
    }
}