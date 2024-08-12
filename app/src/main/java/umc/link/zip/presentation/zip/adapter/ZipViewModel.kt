package umc.link.zip.presentation.zip.adapter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import umc.link.zip.data.dto.zip.request.ZipCreateRequest
import umc.link.zip.data.dto.zip.request.ZipEditRequest
import umc.link.zip.data.dto.zip.request.ZipRmRequest
import umc.link.zip.domain.model.ZipItem
import umc.link.zip.domain.repository.ZipRepository
import javax.inject.Inject

@HiltViewModel
class ZipViewModel @Inject constructor(
    private val zipRepository: ZipRepository
) : ViewModel() {

    private val _zipItems = MutableLiveData<List<ZipItem>>()
    val zipItems: LiveData<List<ZipItem>> get() = _zipItems

    init {
        getZipList()
    }

    private fun getZipList() {
        viewModelScope.launch {
            try {
                val response = zipRepository.getInquiryZip("latest") // "latest"는 정렬 기준 예시
                if (response.isSuccess) {
                    response.body()?.let {
                        _zipItems.value = it.zipItems // zipItems는 response에서 반환되는 리스트로 가정
                    }
                } else {
                    // 오류 처리 로직 추가
                }
            } catch (e: Exception) {
                // 예외 처리 로직 추가
            }
        }
    }

    fun createZipItem(title: String, color: String) {
        val zipCreateRequest = ZipCreateRequest(title = title, color = color)
        viewModelScope.launch {
            try {
                val response = zipRepository.postCreateZip(zipCreateRequest)
                if (response.isSuccessful) {
                    // 아이템이 성공적으로 생성되면 리스트를 갱신
                    getZipList()
                } else {
                    // 오류 처리 로직 추가
                }
            } catch (e: Exception) {
                // 예외 처리 로직 추가
            }
        }
    }

    fun editZipItem(zipItem: ZipItem, newTitle: String, newColor: String) {
        val zipEditRequest = ZipEditRequest(id = zipItem.id, title = newTitle, color = newColor)
        viewModelScope.launch {
            try {
                val response = zipRepository.patchEditZip(zipEditRequest)
                if (response.isSuccessful) {
                    // 아이템이 성공적으로 수정되면 리스트를 갱신
                    getZipList()
                } else {
                    // 오류 처리 로직 추가
                }
            } catch (e: Exception) {
                // 예외 처리 로직 추가
            }
        }
    }

    fun deleteZipItem(zipItem: ZipItem) {
        val zipRmRequest = ZipRmRequest(id = zipItem.id)
        viewModelScope.launch {
            try {
                val response = zipRepository.deleteRmZip(zipRmRequest)
                if (response.isSuccessful) {
                    // 아이템이 성공적으로 삭제되면 리스트를 갱신
                    getZipList()
                } else {
                    // 오류 처리 로직 추가
                }
            } catch (e: Exception) {
                // 예외 처리 로직 추가
            }
        }
    }
}

