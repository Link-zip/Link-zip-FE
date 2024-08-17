package umc.link.zip.presentation.create.adapter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import umc.link.zip.data.dto.link.request.LinkExtractRequest
import umc.link.zip.domain.model.link.LinkExtractModel
import umc.link.zip.domain.repository.LinkRepository
import umc.link.zip.util.network.NetworkResult
import javax.inject.Inject

@HiltViewModel
class LinkExtractViewModel @Inject constructor(
    private val linkRepository: LinkRepository
) : ViewModel() {

    // 추출 요청 결과를 관리하는 StateFlow
    private val _extractResponse = MutableStateFlow<NetworkResult<LinkExtractModel>?>(null)
    val extractResponse: StateFlow<NetworkResult<LinkExtractModel>?> get() = _extractResponse

    // URL을 기반으로 링크 추출을 요청하는 함수
    fun fetchLinkExtract(url: String) {
        viewModelScope.launch {
            // LinkExtractRequest 객체 생성
            val request = LinkExtractRequest(url)

            // 링크 추출 요청
            val response = linkRepository.ExtractLink(request)

            // 결과를 StateFlow에 저장
            _extractResponse.value = response
        }
    }
}


