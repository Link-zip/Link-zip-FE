package umc.link.zip.presentation.create.adapter

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import umc.link.zip.data.dto.link.request.LinkAddRequest
import umc.link.zip.domain.model.create.CreateLink
import umc.link.zip.domain.model.link.LinkAddModel
import umc.link.zip.domain.repository.LinkRepository
import umc.link.zip.util.network.NetworkResult
import umc.link.zip.util.network.UiState
import umc.link.zip.util.network.onError
import umc.link.zip.util.network.onException
import umc.link.zip.util.network.onFail
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class LinkAddViewModel @Inject constructor(
    private val linkRepository: LinkRepository
) : ViewModel() {

    // 추출 요청 결과를 관리하는 StateFlow
    private val _addResponse = MutableStateFlow<UiState<LinkAddModel>>(UiState.Loading)
    val addResponse: StateFlow<UiState<LinkAddModel>> = _addResponse.asStateFlow()

    fun addLink(linkAddRequest: LinkAddRequest) {
        viewModelScope.launch {
            linkRepository.AddLink(linkAddRequest).apply {
                when (this) {
                    is NetworkResult.Success -> {
                        _addResponse.value = UiState.Loading  // 상태를 초기화 (동일한 데이터가 와도 방출될 수 있도록)
                        _addResponse.value = UiState.Success(this.data)
                    }
                    is NetworkResult.Error -> {
                        _addResponse.value = UiState.Error(this.exception)
                    }
                    is NetworkResult.Fail -> {
                        _addResponse.value = UiState.Error(Throwable("Failed to load data"))
                    }
                }
            }.onError {
                _addResponse.value = UiState.Error(it)
            }.onException {
                _addResponse.value = UiState.Error(it)
            }.onFail {
                _addResponse.value = UiState.Error(Throwable("Failed to load data"))
            }
        }
    }

    // 변경사항 저장
    private val _Add_link = MutableStateFlow(
        CreateLink(
            zipId = 0,           // 초기값으로 0 사용
            title = "",
            text = "",
            url = "",
            memo = "",
            alertDate = null
        )
    )
    val link = _Add_link.asStateFlow()


    // 더미 데이터 목록
    private val dummyAddLinks = listOf(
        CreateLink(
            zipId = 0,
            title = "",
            text = "",
            url = "",
            memo = "",
            alertDate = null
        )
    )

    // 현재 링크 데이터 중 title 업데이트
    fun updateTitle(title: String) {
        _Add_link.value = _Add_link.value.copy(title = title)

        // dummyLinks에서 해당 URL의 데이터를 찾아 업데이트
        dummyAddLinks.find { it.url == _Add_link.value.url }?.apply {
            this.title = title
        }

        Log.d("LinkAddViewModel", "title 업데이트: $title")
    }

    // 현재 링크 데이터 중 memo 업데이트
    fun updateMemo(memo: String) {
        _Add_link.value = _Add_link.value.copy(memo = memo)
        Log.d("LinkAddViewModel", "메모 업데이트 $_Add_link.value")

        // dummyLinks에서 해당 URL의 데이터를 찾아 업데이트
        dummyAddLinks.find { it.url == _Add_link.value.url }?.apply {
            this.memo = memo
        }

        Log.d("LinkAddViewModel", "memo 업데이트: $memo")
    }

    // 현재 링크 데이터 중 text 업데이트
    fun updateText(text: String) {
        _Add_link.value = _Add_link.value.copy(text = text)

        // dummyLinks에서 해당 URL의 데이터를 찾아 업데이트
        dummyAddLinks.find { it.url == _Add_link.value.url }?.apply {
            this.text = text
        }

        Log.d("LinkAddViewModel", "text 업데이트: $text")
    }
    //전체
    fun updateAlertDateAll(allDate: String? = null) {

        val existingDateTime = _Add_link.value.alertDate

        _Add_link.value.alertDate = allDate ?: existingDateTime

        // 더미 데이터에서도 업데이트
        dummyAddLinks.find { it.url == _Add_link.value.url }?.apply {
            this.alertDate = allDate ?: existingDateTime
        }

        Log.d("LinkAddViewModel", "alertDate 업데이트: $allDate")
    }


    // 현재 링크 데이터 중 date alarm 업데이트
    fun updateAlertDate(date: String? = null, time: String? = null) {
        val existingDateTime = _Add_link.value.alertDate

        val currentDate = date ?: existingDateTime?.substringBefore("T")
        val currentTime = time ?: existingDateTime?.substringAfter("T")?.removeSuffix("Z")

        val updatedAlertDate = if (currentDate != null && currentTime != null) {
            "${currentDate}T${currentTime}Z"
        } else {
            null
        }

        _Add_link.value.alertDate = updatedAlertDate

        // 더미 데이터에서도 업데이트
        dummyAddLinks.find { it.url == _Add_link.value.url }?.apply {
            this.alertDate = updatedAlertDate
        }

        Log.d("LinkAddViewModel", "alertDate 업데이트: $updatedAlertDate")
    }

    // 날짜만 업데이트하는 함수
    fun updateAlertDateOnly(date: String?) {
        val existingDateTime = _Add_link.value.alertDate
        val currentDate = date ?: existingDateTime?.substringBefore("T")
        val currentTime = existingDateTime?.substringAfter("T")?.removeSuffix("Z") ?: "00:00:00.000"

        val updatedAlertDate = if (currentDate != null) {
            "${currentDate}T${currentTime}Z"
        } else {
            null
        }

        _Add_link.value.alertDate = updatedAlertDate

        // 더미 데이터에서도 업데이트
        dummyAddLinks.find { it.url == _Add_link.value.url }?.apply {
            this.alertDate = updatedAlertDate
        }

        Log.d("LinkAddViewModel", "alertDate 업데이트 (날짜만): $updatedAlertDate")
    }

    // 시간만 업데이트하는 함수
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateAlertTimeOnly(time: String?) {
        val existingDateTime = _Add_link.value.alertDate
        val todayDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        val currentDate = existingDateTime?.substringBefore("T") ?: todayDate
        val currentTime = time ?: existingDateTime?.substringAfter("T")?.removeSuffix("Z")

        val updatedAlertDate = if (currentTime != null) {
            "${currentDate}T${currentTime}Z"
        } else {
            null
        }

        _Add_link.value.alertDate = updatedAlertDate

        // 더미 데이터에서도 업데이트
        dummyAddLinks.find { it.url == _Add_link.value.url }?.apply {
            this.alertDate = updatedAlertDate
        }

        Log.d("LinkAddViewModel", "alertDate 업데이트 (시간만): $updatedAlertDate")
    }

    // 알람 날짜를 초기화 (null로 설정)
    fun clearAlertDate() {
        _Add_link.value.alertDate = null

        // 더미 데이터에서도 업데이트
        dummyAddLinks.find { it.url == _Add_link.value.url }?.apply {
            this.alertDate = null
        }

        Log.d("LinkAddViewModel", "alertDate 초기화")
    }

    // URL을 통해 더미 데이터에서 Link 가져오기
    fun fetchLinkByUrl(url: String) {
        val foundLink = dummyAddLinks.find {it.url == url }
        Log.d("LinkAddViewModel", "입력된 URL: $url")
        if (foundLink != null) {
            _Add_link.value = foundLink
        } else {
            // URL이 목록에 없을 때 기본 값을 설정
            _Add_link.value = CreateLink(
                zipId = 0,
                title = "default",
                text = "default",
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

    // CreateFragment 아이콘 visibility 상태를 관리하는 StateFlow
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

    fun resetState() {
        _Add_link.value = CreateLink(
            zipId = 0,
            title = "default",
            text = "default",
            url = "",
            memo = "",
            alertDate = null
        )

        // 입력 필드 초기화
        _linkInput.value = ""

        // 네트워크 응답 초기화
        _addResponse.value = UiState.Loading

        Log.d("LinkAddViewModel", "뷰모델 상태가 초기화되었습니다.")
    }

}


