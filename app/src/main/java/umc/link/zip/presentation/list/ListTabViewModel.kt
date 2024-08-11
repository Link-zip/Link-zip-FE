package umc.link.zip.presentation.list

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class ListTabViewModel @Inject constructor(

) : ViewModel() {

    private val _selectedLineup = MutableSharedFlow<String>(replay = 1)  // Replay를 1로 설정해서 마지막 값 유지
    val selectedLineup: SharedFlow<String> get() = _selectedLineup.asSharedFlow()

    suspend fun setSelectedLineup(lineup: String) {
        _selectedLineup.emit(lineup)
    }
}
