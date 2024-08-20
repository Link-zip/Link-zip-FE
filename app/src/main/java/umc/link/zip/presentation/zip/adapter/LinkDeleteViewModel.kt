package umc.link.zip.presentation.zip.adapter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import umc.link.zip.domain.model.link.LinkDeleteModel
import umc.link.zip.domain.model.zip.ZipRmModel
import umc.link.zip.domain.repository.LinkRepository
import umc.link.zip.domain.repository.ZipRepository
import umc.link.zip.util.network.NetworkResult
import umc.link.zip.util.network.UiState
import javax.inject.Inject

@HiltViewModel
class LinkDeleteViewModel @Inject constructor(
    private val linkRepository: LinkRepository
) : ViewModel() {

    private val _deleteResponse = MutableStateFlow<NetworkResult<LinkDeleteModel>?>(null)
    val deleteResponse: StateFlow<NetworkResult<LinkDeleteModel>?> get() = _deleteResponse

        fun deleteLink(id : Int) {
            viewModelScope.launch {
                val response = linkRepository.DeleteLink(id)
                _deleteResponse.value = response // API 응답 값을 업데이트
                when (response) {
                    is NetworkResult.Success -> {
                        Log.d("LinkDeleteViewModel", "API Success: ${response.data?.message}")
                    }
                    is NetworkResult.Error -> {
                        Log.e("LinkDeleteViewModel", "API Error : ${response.exception.message}")
                    }
                    is NetworkResult.Fail -> {
                        Log.e("LinkDeleteViewModel", "API Fail: ${response.message}")
                    }
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