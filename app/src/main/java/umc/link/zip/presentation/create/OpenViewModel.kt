package umc.link.zip.presentation.create

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import umc.link.zip.domain.model.create.Link
import javax.inject.Inject

@HiltViewModel
class OpenViewModel @Inject constructor() : ViewModel() {

    private val _link = MutableStateFlow<Link?>(null)
    val link = _link.asStateFlow()

    /*// 더미 데이터
    private val dummyLinks = listOf(
        Link(
            id = 1,
            zipId = 1,
            userId = 1,
            title = "url1의 제목입니다 (네이버)",
            url = "https://www.naver.com",
            text = "url1의 텍스트입니다",
            memo = "url1의 메모입니다",
            tag = "tag1",
            alertDate = "2024-08-10T10:00:00Z",
            thumb = "thumb1",
            like = 10,
            visit = 5,
            visitDate = "2024-08-10T10:00:00Z",
            createdAt = "2024-08-01T10:00:00Z",
            updatedAt = "2024-08-05T10:00:00Z"
        )
    )

    // URL을 통해 Link 가져오기
    fun fetchLinkByUrl(url: String) {
        _link.value = dummyLinks.find { it.url == url }
    }*/
}
