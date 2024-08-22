package umc.link.zip.presentation.zip.adapter

import android.os.Bundle
import android.provider.Settings.Global.putInt
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import umc.link.zip.domain.model.link.LinkGetModel
import umc.link.zip.domain.model.link.MoveLinkToNewZipModel
import umc.link.zip.domain.model.zip.ZipGetItemModel
import umc.link.zip.domain.model.zip.ZipGetModel
import umc.link.zip.domain.repository.LinkRepository
import umc.link.zip.domain.repository.ZipRepository
import umc.link.zip.util.network.NetworkResult
import umc.link.zip.util.network.UiState
import umc.link.zip.util.network.onError
import umc.link.zip.util.network.onException
import umc.link.zip.util.network.onFail
import umc.link.zip.util.network.onSuccess
import javax.inject.Inject

@HiltViewModel
class OpenZipMoveDialogSharedViewModel @Inject constructor(
    private val zipRepository: ZipRepository,
    private val linkRepository: LinkRepository
) : ViewModel() {

    private val _zipList = MutableStateFlow(ZipGetModel(emptyList()))

    private val _uiState = MutableStateFlow<UiState<ZipGetModel>>(UiState.Loading)
    val uiState: StateFlow<UiState<ZipGetModel>> = _uiState.asStateFlow()

    private val _uiState_link = MutableStateFlow<UiState<MoveLinkToNewZipModel>>(UiState.Loading)
    val uiState_link: StateFlow<UiState<MoveLinkToNewZipModel>> = _uiState_link.asStateFlow()

    fun getZipList(sort: String = "latest") {
        viewModelScope.launch {
            zipRepository.getGetZip(sort).apply {
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

    fun moveLinkToNewZip(link_id : Int, new_zip_id : Int) {
        viewModelScope.launch {
            linkRepository.MoveLinkToNewZip(link_id, new_zip_id).apply {
                when (this) {
                    is NetworkResult.Success -> {
                        _uiState_link.value = UiState.Loading  // 상태를 초기화 (동일한 데이터가 와도 방출될 수 있도록)
                        _uiState_link.value = UiState.Success(this.data)
                        dismissDialog()
                    }
                    is NetworkResult.Error -> {
                        _uiState_link.value = UiState.Error(this.exception)
                    }
                    is NetworkResult.Fail -> {
                        _uiState_link.value = UiState.Error(Throwable("Failed to load data"))
                    }
                }
            }.onError {
                _uiState_link.value = UiState.Error(it)
            }.onException {
                _uiState_link.value = UiState.Error(it)
            }.onFail {
                _uiState_link.value = UiState.Error(Throwable("Failed to load data"))
            }
        }
    }

    private val _selectedData = MutableSharedFlow<String>(replay = 1)
    val selectedData: SharedFlow<String> get() = _selectedData

    private val _dialogDismissed = MutableSharedFlow<Boolean>(replay = 1)
    val dialogDismissed: SharedFlow<Boolean> get() = _dialogDismissed

    suspend fun setSelectedData(data: String) {
        _selectedData.emit(data)
    }

    suspend fun dismissDialog() {
        _dialogDismissed.emit(true)
    }

    suspend fun resetDialogDismissed() {
        _dialogDismissed.emit(false)
    }
}
