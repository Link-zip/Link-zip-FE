package umc.link.zip.presentation.zip.adapter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import umc.link.zip.domain.model.ZipLinkItem
import umc.link.zip.domain.model.link.LinkGetItemModel
import umc.link.zip.domain.model.link.LinkGetModel
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
class OpenZipViewModel @Inject constructor(
    private val linkRepository: LinkRepository,
) : ViewModel() {

    /*private val _linkList = MutableStateFlow<UiState<LinkGetModel>>(UiState.Loading)
    val linkId: StateFlow<UiState<LinkGetModel>> get() = _linkList*/

    private val _uiState = MutableStateFlow<UiState<LinkGetModel>>(UiState.Loading)
    val uiState: StateFlow<UiState<LinkGetModel>> = _uiState.asStateFlow()

    fun getLinkList(zip_id: Int, tag: String, sortOrder: String) {
        viewModelScope.launch {
            linkRepository.getLinkData(zip_id, tag, sortOrder).apply {
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
}