package umc.link.zip.presentation.create

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import umc.link.zip.domain.model.create.Link
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor() : ViewModel() {

    private val _link = MutableStateFlow<Link>(
        Link(
            zipId = 0,           // 초기값으로 0 사용
            title = "",
            text = "",
            url = "",
            memo = "",
            alertDate = ""
        )
    )
    val link = _link.asStateFlow()

    // 더미 데이터 목록
    private val dummyLinks = listOf(
        Link(
            zipId = 1,
            title = "url1의 제목입니다",
            text = "url1의 텍스트입니다",
            url = "url1",
            memo = "",
            alertDate = "2024-08-10T10:00:00Z"
        ),
        Link(
            zipId = 2,
            title = "url2의 제목입니다",
            text = "url2의 텍스트입니다",
            url = "url2",
            memo = "",
            alertDate = "2024-08-20T14:00:00Z"
        )
    )

    // URL을 통해 더미 데이터에서 Link 가져오기
    fun fetchLinkByUrl(url: String) {
        _link.value = dummyLinks.find { it.url == url } ?: Link(
            zipId = 0,
            title = "Unknown Title",
            text = "Unknown Text",
            url = url,
            memo = "",
            alertDate = ""
        )
    }

    // EditText의 내용을 감지하는 StateFlow
    private val _linkInput = MutableStateFlow("")
    val linkInput = _linkInput.asStateFlow()

    // 버튼의 visibility 상태를 관리하는 StateFlow
    val isSaveButtonVisible = linkInput.map { it.isNotEmpty() }

    // 이미지 뷰의 visibility 상태를 관리하는 StateFlow
    val isLinkIconVisible = linkInput.map { it.isEmpty() }
    val isDeleteIconVisible = linkInput.map { it.isNotEmpty() }

    // EditText의 내용을 업데이트하는 함수
    fun updateLinkInput(input: String) {
        _linkInput.value = input
    }

    // EditText의 내용을 초기화하는 함수
    fun clearLinkInput() {
        _linkInput.value = ""
    }
}
