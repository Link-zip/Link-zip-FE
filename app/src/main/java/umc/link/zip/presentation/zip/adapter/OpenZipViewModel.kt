import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import umc.link.zip.domain.model.ZipLinkItem
import umc.link.zip.domain.model.link.LinkGetItemModel
import umc.link.zip.domain.model.link.LinkGetModel
import umc.link.zip.domain.model.zip.ZipGetItemModel
import umc.link.zip.domain.model.zip.ZipGetModel
import umc.link.zip.domain.repository.LinkRepository
import umc.link.zip.domain.repository.ZipRepository
import umc.link.zip.util.network.onSuccess
import javax.inject.Inject


@HiltViewModel
class OpenZipViewModel @Inject constructor(
    private val linkRepository: LinkRepository
) : ViewModel() {

    private val _linkList = MutableStateFlow(LinkGetModel(emptyList()))
    val linkList: Flow<List<LinkGetItemModel>> get() = _linkList.map { it.link_data }

    fun getLinkList(zip_id: Int, tag: String ) {
        viewModelScope.launch {
            val response = linkRepository.getLinkData(zip_id, tag)
            Log.d("OpenZipViewModel", "zip_id: $zip_id, tag: $tag")
            Log.d("OpenZipViewModel", "OpenZip Get API Response: $response")
            response.onSuccess { LinkGetModel ->
                _linkList.value = LinkGetModel
            }
        }
    }
}
