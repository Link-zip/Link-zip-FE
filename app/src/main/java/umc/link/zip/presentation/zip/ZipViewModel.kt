package umc.link.zip.presentation.zip

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import umc.link.zip.R
import umc.link.zip.domain.model.ZipItem

class ZipViewModel : ViewModel() {

    private val _zipItems = MutableLiveData<List<ZipItem>>()
    val zipItems: LiveData<List<ZipItem>> get() = _zipItems

    init {
        // 초기 데이터 로드
        loadInitialZipItems()
    }

    private fun loadInitialZipItems() {
        // 초기 데이터를 설정합니다.
        _zipItems.value = listOf(
            ZipItem(id = "1", user_id = "user1", title = "빠른저장", color = R.drawable.ic_zip_clip_shadow),
            ZipItem(id = "2", user_id = "user2", title = "Title 2", color = R.drawable.ic_zip_shadow_2)
        )
    }

    fun refreshData() {
        // 현재 데이터가 변경되지 않도록 처리합니다.
        _zipItems.value = _zipItems.value
    }

    fun addZipItem(zipItem: ZipItem) {
        val currentList = _zipItems.value?.toMutableList() ?: mutableListOf()
        currentList.add(zipItem)
        _zipItems.value = currentList
        Log.d("Zip", "ZipItem updated: $_zipItems")
    }
}
