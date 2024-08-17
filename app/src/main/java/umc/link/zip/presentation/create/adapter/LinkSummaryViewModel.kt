package umc.link.zip.presentation.create.adapter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import umc.link.zip.data.dto.link.request.LinkSummaryRequest
import umc.link.zip.domain.model.link.LinkSummaryModel
import umc.link.zip.domain.repository.LinkRepository
import umc.link.zip.util.network.NetworkResult
import javax.inject.Inject

@HiltViewModel
class LinkSummaryViewModel @Inject constructor(
    private val linkRepository: LinkRepository
) : ViewModel() {

    // 요약 요청 결과를 관리하는 StateFlow
    private val _summaryResponse = MutableStateFlow<NetworkResult<LinkSummaryModel>?>(null)
    val summaryResponse: StateFlow<NetworkResult<LinkSummaryModel>?> get() = _summaryResponse

    // URL을 기반으로 링크 요약을 요청하는 함수
    fun fetchLinkSummary(url: String) {
        viewModelScope.launch {
            // LinkSummaryRequest 객체 생성
            val request = LinkSummaryRequest(url)

            // 링크 요약 요청
            val response = linkRepository.SummaryLink(request)

            // 결과를 StateFlow에 저장
            _summaryResponse.value = response
        }
    }
}

