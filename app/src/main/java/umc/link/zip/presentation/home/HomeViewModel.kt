package umc.link.zip.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import umc.link.zip.R
import umc.link.zip.domain.model.home.HomeAlertCountModel
import umc.link.zip.domain.model.home.HomeAlertExistsModel
import umc.link.zip.domain.model.home.HomeOldCountModel
import umc.link.zip.domain.model.home.HomeRecentModel
import umc.link.zip.domain.model.home.HomeTotalCountModel
import umc.link.zip.domain.model.home.HomeUnreadCountModel
import umc.link.zip.domain.repository.HomeRepository
import umc.link.zip.util.network.NetworkResult
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) : ViewModel() {

    private val _recentLinks = MutableLiveData<NetworkResult<HomeRecentModel>>()
    val recentLinks: LiveData<NetworkResult<HomeRecentModel>> get() = _recentLinks

    private val _navigateEvent = MutableSharedFlow<Int?>()
    val navigateEvent: SharedFlow<Int?> = _navigateEvent

    private val _alertCount = MutableLiveData<NetworkResult<HomeAlertCountModel>>()
    val alertCount: LiveData<NetworkResult<HomeAlertCountModel>> get() = _alertCount

    private val _unreadCount = MutableLiveData<NetworkResult<HomeUnreadCountModel>>()
    val unreadCount: LiveData<NetworkResult<HomeUnreadCountModel>> get() = _unreadCount

    private val _oldCount = MutableLiveData<NetworkResult<HomeOldCountModel>>()
    val oldCount: LiveData<NetworkResult<HomeOldCountModel>> get() = _oldCount

    private val _totalCount = MutableLiveData<NetworkResult<HomeTotalCountModel>>()
    val totalCount: LiveData<NetworkResult<HomeTotalCountModel>> get() = _totalCount

    private val _alertExists = MutableLiveData<NetworkResult<HomeAlertExistsModel>>()
    val alertExists: LiveData<NetworkResult<HomeAlertExistsModel>> get() = _alertExists

    fun getRecentLinks() {
        viewModelScope.launch {
            _recentLinks.value = homeRepository.getRecentLinks()
        }
    }

    fun getAlertCount() {
        viewModelScope.launch {
            _alertCount.value = homeRepository.getAlertCount()
        }
    }

    fun getUnreadCount() {
        viewModelScope.launch {
            _unreadCount.value = homeRepository.getUnreadCount()
        }
    }

    fun getOldCount() {
        viewModelScope.launch {
            _oldCount.value = homeRepository.getOldCount()
        }
    }

    fun getTotalCount() {
        viewModelScope.launch {
            _totalCount.value = homeRepository.getTotalCount()
        }
    }

    fun getAlertExists() {
        viewModelScope.launch {
            _alertExists.value = homeRepository.getAlertExists()
        }
    }

    fun navigateToListFragment() {
        viewModelScope.launch {
            _navigateEvent.emit(R.id.listFragment)
        } // ListFragment의 메뉴 ID로 설정
    }
}