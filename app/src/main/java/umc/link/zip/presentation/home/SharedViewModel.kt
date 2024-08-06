package umc.link.zip.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    private val selectedItem = MutableLiveData<String>()
    fun selectItem(item: String) {
        selectedItem.value = item
    }

    fun getSelectedItem(): LiveData<String> {
        return selectedItem
    }
}