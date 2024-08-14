package umc.link.zip.presentation.zip.adapter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import umc.link.zip.domain.model.zip.ZipGetItemModel

import umc.link.zip.domain.repository.ZipRepository
import umc.link.zip.util.network.onSuccess
import javax.inject.Inject

@HiltViewModel
class ZipGetViewModel @Inject constructor(
    private val zipRepository: ZipRepository
) : ViewModel() {

    private val _zipList = MutableStateFlow<List<ZipGetItemModel>>(emptyList())
    val zipList: StateFlow<List<ZipGetItemModel>> get() = _zipList

    fun getZipList(sort: String = "latest") {
        viewModelScope.launch {
            val response = zipRepository.getGetZip(sort)
            response.onSuccess { zipGetModel ->
                _zipList.value = zipGetModel.zips
            }
        }
    }
}

