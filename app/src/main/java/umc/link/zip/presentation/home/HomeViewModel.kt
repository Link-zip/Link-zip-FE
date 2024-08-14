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
import umc.link.zip.domain.model.home.HomeRecentModel
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

    fun navigateToListFragment() {
        viewModelScope.launch {
            _navigateEvent.emit(R.id.listFragment)
        } // ListFragment의 메뉴 ID로 설정
    }
}