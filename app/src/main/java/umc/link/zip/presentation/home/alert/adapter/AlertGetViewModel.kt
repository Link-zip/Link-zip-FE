package umc.link.zip.presentation.home.alert.adapter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import umc.link.zip.domain.model.alert.AlertGetModel
import umc.link.zip.domain.model.alert.AlertModel
import umc.link.zip.domain.repository.AlertRepository
import umc.link.zip.util.network.NetworkResult
import umc.link.zip.util.network.UiState
import umc.link.zip.util.network.onError
import umc.link.zip.util.network.onException
import umc.link.zip.util.network.onFail
import javax.inject.Inject

@HiltViewModel
class AlertGetViewModel @Inject constructor(
    private val alertRepository: AlertRepository
) : ViewModel() {

    // 알림 요청 결과를 관리하는 StateFlow
    private val _getAlertResponse = MutableStateFlow<UiState<AlertModel>>(UiState.Loading)
    val getAlertResponse: StateFlow<UiState<AlertModel>> = _getAlertResponse.asStateFlow()

    // 알림 데이터를 가져오는 함수
    fun getAlert() {
        viewModelScope.launch {
            alertRepository.GetAlert().apply {
                when (this) {
                    is NetworkResult.Success -> {
                        _getAlertResponse.value = UiState.Loading // 상태를 초기화
                        _getAlertResponse.value = UiState.Success(this.data)
                        Log.d("AlertGetViewModel", "알림 데이터 가져오기 성공")
                    }
                    is NetworkResult.Error -> {
                        _getAlertResponse.value = UiState.Error(this.exception)
                        Log.d("AlertGetViewModel", "알림 데이터 가져오기 실패: ${this.exception}")
                    }
                    is NetworkResult.Fail -> {
                        _getAlertResponse.value = UiState.Error(Throwable("Failed to load data"))
                        Log.d("AlertGetViewModel", "알림 데이터 가져오기 실패")
                    }
                }
            }.onError {
                _getAlertResponse.value = UiState.Error(it)
            }.onException {
                _getAlertResponse.value = UiState.Error(it)
            }.onFail {
                _getAlertResponse.value = UiState.Error(Throwable("Failed to load data"))
            }
        }
    }

    // 알림 확인 결과를 관리하는 StateFlow
    private val _confirmAlertResponse = MutableStateFlow<UiState<AlertModel>>(UiState.Loading)
    val confirmAlertResponse: StateFlow<UiState<AlertModel>> = _confirmAlertResponse.asStateFlow()

    // 알림 확인 데이터를 가져오는 함수
    fun confirmAlert() {
        viewModelScope.launch {
            alertRepository.GetAlert().apply {
                when (this) {
                    is NetworkResult.Success -> {
                        _confirmAlertResponse.value = UiState.Loading // 상태를 초기화
                        _confirmAlertResponse.value = UiState.Success(this.data)
                        Log.d("AlertGetViewModel", "알림 확인 데이터 가져오기 성공")
                    }
                    is NetworkResult.Error -> {
                        _confirmAlertResponse.value = UiState.Error(this.exception)
                        Log.d("AlertGetViewModel", "알림 확인 데이터 가져오기 실패: ${this.exception}")
                    }
                    is NetworkResult.Fail -> {
                        _confirmAlertResponse.value = UiState.Error(Throwable("Failed to load data"))
                        Log.d("AlertGetViewModel", "알림 확인 데이터 가져오기 실패")
                    }
                }
            }.onError {
                _confirmAlertResponse.value = UiState.Error(it)
            }.onException {
                _confirmAlertResponse.value = UiState.Error(it)
            }.onFail {
                _confirmAlertResponse.value = UiState.Error(Throwable("Failed to load data"))
            }
        }
    }
}
