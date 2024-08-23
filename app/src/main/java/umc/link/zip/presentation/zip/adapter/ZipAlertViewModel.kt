package umc.link.zip.presentation.zip.adapter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import umc.link.zip.domain.model.home.HomeAlertCountModel
import umc.link.zip.domain.model.home.HomeAlertExistsModel
import umc.link.zip.domain.repository.HomeRepository
import umc.link.zip.util.network.NetworkResult
import javax.inject.Inject

@HiltViewModel
class ZipAlertViewModel @Inject constructor(private val homeRepository: HomeRepository) : ViewModel() {

    private val _alertCount = MutableLiveData<NetworkResult<HomeAlertCountModel>>()
    val alertCount: LiveData<NetworkResult<HomeAlertCountModel>> get() = _alertCount

    private val _alertExists = MutableLiveData<NetworkResult<HomeAlertExistsModel>>()
    val alertExists: LiveData<NetworkResult<HomeAlertExistsModel>> get() = _alertExists

    fun getAlertCount() {
        viewModelScope.launch {
            _alertCount.value = homeRepository.getAlertCount()
        }
    }

    fun getAlertExists() {
        viewModelScope.launch {
            _alertExists.value = homeRepository.getAlertExists()
        }
    }

}