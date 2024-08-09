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
