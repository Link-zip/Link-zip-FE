package umc.link.zip.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import umc.link.zip.domain.model.create.Link
import umc.link.zip.domain.model.create.Zip
import javax.inject.Inject

@HiltViewModel
class CustomLinkViewModel @Inject constructor() : ViewModel() {

    private val _link = MutableStateFlow<Link>(
            Link(
                    id = "",
                    title = "",
                    url = "",
                    text = "",
                    thumbnail = "",
                    likes = 0,
                    createdAt = "",
                    zip = Zip(id = "", title = "", color = "")
            )
    )
    val link: StateFlow<Link> get() = _link

    private val _linkSaved = MutableSharedFlow<Link>()
    val linkSaved: SharedFlow<Link> get() = _linkSaved

    fun setId(id: String) {
        _link.value = _link.value.copy(id = id)
    }

    fun setTitle(title: String) {
        _link.value = _link.value.copy(title = title)
    }

    fun setUrl(url: String) {
        _link.value = _link.value.copy(url = url)
    }

    fun setText(text: String) {
        _link.value = _link.value.copy(text = text)
    }

    fun setThumbnail(thumbnail: String) {
        _link.value = _link.value.copy(thumbnail = thumbnail)
    }

    fun setLikes(likes: Int) {
        _link.value = _link.value.copy(likes = likes)
    }

    fun setCreatedAt(createdAt: String) {
        _link.value = _link.value.copy(createdAt = createdAt)
    }

    fun setZip(zip: Zip) {
        _link.value = _link.value.copy(zip = zip)
    }

    fun saveLink() {
        viewModelScope.launch {
            _linkSaved.emit(_link.value)
        }
    }
}
