package umc.link.zip.presentation.list

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class ListUnreadLineDialogSharedViewModel : ViewModel() {
    private val _selectedData = MutableSharedFlow<String>(replay = 1)
    val selectedData: SharedFlow<String> get() = _selectedData

    private val _dialogDismissed = MutableSharedFlow<Boolean>(replay = 1)
    val dialogDismissed: SharedFlow<Boolean> get() = _dialogDismissed

    suspend fun setSelectedData(data: String) {
        _selectedData.emit(data)
    }

    suspend fun dismissDialog() {
        _dialogDismissed.emit(true)
    }

    suspend fun resetDialogDismissed() {
        _dialogDismissed.emit(false)
    }
}