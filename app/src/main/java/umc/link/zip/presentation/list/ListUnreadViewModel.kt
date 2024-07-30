package umc.link.zip.presentation.list

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ListUnreadViewModel @Inject constructor(

) : ViewModel() {
    private val _linkId = MutableStateFlow<Int>(-1)
    val linkId: StateFlow<Int> get() = _linkId

    fun setLinkId(linkId: Int){
        _linkId.value = linkId
    }
}