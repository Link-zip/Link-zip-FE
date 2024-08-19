package umc.link.zip.presentation.zip.adapter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import umc.link.zip.data.dto.zip.request.ZipRmRequest
import umc.link.zip.domain.model.zip.ZipRmModel
import umc.link.zip.domain.repository.ZipRepository
import umc.link.zip.util.network.NetworkResult
import javax.inject.Inject

@HiltViewModel
class ZipDeleteViewModel @Inject constructor(
    private val zipRepository: ZipRepository
) : ViewModel() {

    private val _deleteResponse = MutableStateFlow<NetworkResult<ZipRmModel>?>(null)
    val deleteResponse: StateFlow<NetworkResult<ZipRmModel>?> get() = _deleteResponse

    fun deleteZip(request: ZipRmRequest) {
        viewModelScope.launch {
            val response = zipRepository.deleteRmZip(request)
            _deleteResponse.value = response // API 응답 값을 업데이트
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