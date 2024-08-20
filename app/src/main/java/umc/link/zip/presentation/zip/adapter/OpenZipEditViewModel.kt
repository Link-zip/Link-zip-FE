package umc.link.zip.presentation.zip.adapter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import umc.link.zip.data.dto.zip.request.ZipCreateRequest
import umc.link.zip.data.dto.zip.request.ZipEditRequest
import umc.link.zip.data.dto.zip.response.ZipCreateResponse
import umc.link.zip.domain.model.zip.ZipCreateModel
import umc.link.zip.domain.model.zip.ZipEditModel
import umc.link.zip.domain.repository.ZipRepository
import umc.link.zip.util.network.NetworkResult
import javax.inject.Inject

@HiltViewModel
class OpenZipEditViewModel @Inject constructor(
    private val zipRepository: ZipRepository
) : ViewModel() {

    private val _editResponse = MutableStateFlow<NetworkResult<ZipEditModel>?>(null)
    val editResponse: StateFlow<NetworkResult<ZipEditModel>?> get() = _editResponse

    fun editZip(request: ZipEditRequest) {
        viewModelScope.launch {
            val response = zipRepository.patchEditZip(request)
            Log.d("OpenZipEditViewModel", "Requested Data: id=${request.id}, color=${request.color}, title=${request.title}")
            Log.d("OpenZipEditViewModel", "API Response: $response")
            _editResponse.value = response // API 응답 값을 업데이트
        }
    }
}