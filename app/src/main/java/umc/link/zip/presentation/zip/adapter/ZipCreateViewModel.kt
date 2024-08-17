package umc.link.zip.presentation.zip.adapter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import umc.link.zip.data.dto.zip.request.ZipCreateRequest
import umc.link.zip.data.dto.zip.response.ZipCreateResponse
import umc.link.zip.domain.model.zip.ZipCreateModel
import umc.link.zip.domain.repository.ZipRepository
import umc.link.zip.util.network.NetworkResult
import javax.inject.Inject

@HiltViewModel
class ZipCreateViewModel @Inject constructor(
    private val zipRepository: ZipRepository
) : ViewModel() {

    private val _createResponse = MutableStateFlow<NetworkResult<ZipCreateModel>?>(null)
    val createResponse: StateFlow<NetworkResult<ZipCreateModel>?> get() = _createResponse

    fun createZip(request: ZipCreateRequest) {
        viewModelScope.launch {
            val response = zipRepository.postCreateZip(request)
            _createResponse.value = response
        }
    }
}

