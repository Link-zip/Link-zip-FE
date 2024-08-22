package umc.link.zip.presentation.create.adapter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import umc.link.zip.data.dto.link.request.LinkModifyRequest
import umc.link.zip.domain.model.link.LinkModifyModel
import umc.link.zip.domain.repository.LinkRepository
import umc.link.zip.util.network.NetworkResult
import umc.link.zip.util.network.UiState
import javax.inject.Inject

@HiltViewModel
class LinkModifyViewModel @Inject constructor(
    private val linkRepository: LinkRepository
) : ViewModel() {

    // 수정 요청 결과를 관리하는 StateFlow
    private val _linkModifyResponse = MutableStateFlow<UiState<LinkModifyModel>>(UiState.Empty)
    val linkModifyResponse: StateFlow<UiState<LinkModifyModel>> = _linkModifyResponse.asStateFlow()

    // 링크 수정 요청
    fun modifyLink(linkId: Int, linkModifyRequest: LinkModifyRequest) {
        viewModelScope.launch {
            // 요청이 시작되면 상태를 Loading으로 설정
            _linkModifyResponse.value = UiState.Loading

            // API 요청 처리
            val result = linkRepository.ModifyLink(linkId, linkModifyRequest)

            // API 응답에 따른 상태 변경 처리
            when (result) {
                is NetworkResult.Success -> {
                    _linkModifyResponse.value = UiState.Success(result.data)
                    Log.d("LinkModifyViewModel", "링크 수정 성공: ${result.data}")
                }

                is NetworkResult.Error -> {
                    _linkModifyResponse.value = UiState.Error(result.exception)
                    Log.d("LinkModifyViewModel", "링크 수정 에러: ${result.exception}")
                }

                is NetworkResult.Fail -> {
                    _linkModifyResponse.value = UiState.Error(Throwable("Failed to modify link"))
                    Log.d("LinkModifyViewModel", "링크 수정 실패")
                }
            }
        }
    }
}
