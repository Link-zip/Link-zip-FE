package umc.link.zip.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import umc.link.zip.R

class HomeViewModel : ViewModel() {
    private val _navigateEvent = MutableSharedFlow<Int?>()
    val navigateEvent: SharedFlow<Int?> = _navigateEvent

    fun navigateToListFragment() {
        viewModelScope.launch {
            _navigateEvent.emit(R.id.listFragmentTab)
        } // ListFragment의 메뉴 ID로 설정
    }
}