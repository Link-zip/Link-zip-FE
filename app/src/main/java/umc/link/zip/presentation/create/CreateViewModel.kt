package umc.link.zip.presentation.create

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import umc.link.zip.domain.model.create.CreateLink
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor() : ViewModel() {

    private val _Create_link = MutableStateFlow(
        CreateLink(
            zipId = 0,           // 초기값으로 0 사용
            title = "",
            text = "",
            url = "",
            memo = "",
            alertDate = null
        )
    )
    val link = _Create_link.asStateFlow()

    // 더미 데이터 목록
    private val dummyCreateLinks = listOf(
        CreateLink(
            zipId = 1,
            title = "url1의 제목입니다 네이버 네이버 네이버 네이버 네이버",
            text = "url1의 텍스트입니다",
            url = "https://www.naver.com",
            memo = "",
            alertDate = null
        ),
        CreateLink(
            zipId = 2,
            title = "url2의 제목입니다 (유튜브)",
            text = "url2의 텍스트입니다",
            url = "https://www.youtube.com",
            memo = "",
            alertDate = "2024-08-20T14:00:00Z"
        )
    )

    // 현재 링크 데이터 중 title 업데이트
    fun updateTitle(title: String) {
        _Create_link.value = _Create_link.value.copy(title = title)

        // dummyLinks에서 해당 URL의 데이터를 찾아 업데이트
        dummyCreateLinks.find { it.url == _Create_link.value.url }?.apply {
            this.title = title
        }
    }

    // 현재 링크 데이터 중 memo 업데이트
    fun updateMemo(memo: String) {
        _Create_link.value = _Create_link.value.copy(memo = memo)

        // dummyLinks에서 해당 URL의 데이터를 찾아 업데이트
        dummyCreateLinks.find { it.url == _Create_link.value.url }?.apply {
            this.memo = memo
        }
    }

    // 현재 링크 데이터 중 date alarm 업데이트
    fun updateAlertDate(date: String? = null, time: String? = null) {
        val existingDateTime = _Create_link.value.alertDate

        val newDate = date ?: existingDateTime?.substringBefore("T")
        val newTime = time ?: existingDateTime?.substringAfter("T")?.removeSuffix("Z")

        val updatedAlertDate = if (newDate != null && newTime != null) {
            "${newDate}T${newTime}Z"
        } else {
            null
        }

        // _Create_link의 alertDate 값 업데이트
        _Create_link.value = _Create_link.value.copy(alertDate = updatedAlertDate)

        // dummyCreateLinks에서 해당 URL의 데이터를 찾아 alertDate 값 업데이트
        dummyCreateLinks.find { it.url == _Create_link.value.url }?.apply {
            this.alertDate = updatedAlertDate
        }
    }

    // URL을 통해 더미 데이터에서 Link 가져오기
    fun fetchLinkByUrl(url: String) {
        val foundLink = dummyCreateLinks.find { it.url == url }
        if (foundLink != null) {
            _Create_link.value = foundLink
        } else {
            // URL이 목록에 없을 때 기본 값을 설정
            _Create_link.value = CreateLink(
                zipId = 0,
                title = "Unknown Title",
                text = "Unknown Text",
                url = url,
                memo = "",
                alertDate = null
            )
        }
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
