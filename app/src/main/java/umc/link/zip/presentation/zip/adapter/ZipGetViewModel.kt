package umc.link.zip.presentation.zip.adapter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import umc.link.zip.domain.model.ZipItem
import umc.link.zip.domain.model.zip.ZipGetItemModel
import umc.link.zip.domain.model.zip.ZipGetModel

import umc.link.zip.domain.repository.ZipRepository
import umc.link.zip.util.network.onSuccess
import javax.inject.Inject

@HiltViewModel
class ZipGetViewModel @Inject constructor(
    private val zipRepository: ZipRepository
) : ViewModel() {

    private val _zipList = MutableStateFlow(ZipGetModel(emptyList()))
    val zipList: Flow<List<ZipGetItemModel>> get() = _zipList.map { it.zips }

    fun getZipList(sort: String = "latest") {
        viewModelScope.launch {
            val response = zipRepository.getGetZip(sort)
            Log.d("ZipGetViewModel", "Sort: $sort")
            Log.d("ZipGetViewModel", "Get API Response: $response")
            response.onSuccess { zipGetModel ->
                _zipList.value = zipGetModel
            }
        }
    }
}

