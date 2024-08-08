package umc.link.zip.presentation.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LinkViewModel @Inject constructor() : ViewModel() {
    private val _linkData = MutableLiveData(Link())
    val linkData: LiveData<Link> get() = _linkData

    fun setUrl(url: String) {
        _linkData.value = _linkData.value?.copy(url = url)
    }

    fun setZipId(zipId: String) {
        _linkData.value = _linkData.value?.copy(zipId = zipId)
    }

    fun setTitle(title: String) {
        _linkData.value = _linkData.value?.copy(title = title)
    }

    fun setMemo(memo: String) {
        _linkData.value = _linkData.value?.copy(memo = memo)
    }

    fun setAlertDate(alertDate: String) {
        _linkData.value = _linkData.value?.copy(alertDate = alertDate)
    }
}
