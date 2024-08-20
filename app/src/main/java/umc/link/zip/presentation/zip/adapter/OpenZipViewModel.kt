package umc.link.zip.presentation.zip.adapter

import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import umc.link.zip.R
import umc.link.zip.databinding.FragmentOpenzipBinding
import umc.link.zip.databinding.ItemLinkBinding
import umc.link.zip.domain.model.ZipLinkItem
import umc.link.zip.domain.model.link.LinkGetItemModel
import umc.link.zip.domain.model.link.LinkGetModel
import umc.link.zip.domain.model.zip.ZipGetItemModel
import umc.link.zip.domain.model.zip.ZipGetModel
import umc.link.zip.domain.repository.LinkRepository
import umc.link.zip.domain.repository.ZipRepository
import umc.link.zip.util.network.NetworkResult
import umc.link.zip.util.network.UiState
import umc.link.zip.util.network.onError
import umc.link.zip.util.network.onException
import umc.link.zip.util.network.onFail
import umc.link.zip.util.network.onSuccess
import javax.inject.Inject


@HiltViewModel
class OpenZipViewModel @Inject constructor(
    private val linkRepository: LinkRepository,
) : ViewModel() {
    private val _linkList = MutableStateFlow(LinkGetModel(emptyList()))
    val linkList: Flow<List<LinkGetItemModel>> get() = _linkList.map { it.link_data }

    private val _uiState = MutableStateFlow<UiState<LinkGetModel>>(UiState.Loading)
    val uiState: StateFlow<UiState<LinkGetModel>> = _uiState.asStateFlow()

    private val _zipTitle = MutableLiveData<String>()
    val zipTitle: LiveData<String> get() = _zipTitle

    private val _linkCount = MutableLiveData<String>()
    val linkCount: LiveData<String> get() = _linkCount

    private val _zipColor = MutableLiveData<String>()
    val zipColor: LiveData<String> get() = _zipColor


    fun getLinkList(zip_id: Int, tag: String, sortOrder: String) {
        viewModelScope.launch {
            linkRepository.getLinkData(zip_id, tag, sortOrder).apply {
                when (this) {
                    is NetworkResult.Success -> {
                        _uiState.value = UiState.Loading  // 상태를 초기화 (동일한 데이터가 와도 방출될 수 있도록)
                        _uiState.value = UiState.Success(this.data)

                        val firstItem = this.data.link_data.firstOrNull()
                        if (firstItem != null) {
                            _zipTitle.value = firstItem.zip_title
                            _linkCount.value = "${this.data.link_data.size} 개"
                            _zipColor.value = firstItem.zip_color
                        } else {
                            _zipTitle.value = "No Title"
                            _zipColor.value = "default"
                        }

                    }

                    is NetworkResult.Error -> {
                        _uiState.value = UiState.Error(this.exception)
                    }

                    is NetworkResult.Fail -> {
                        _uiState.value = UiState.Error(Throwable("Failed to load data"))
                    }
                }
            }.onError {
                _uiState.value = UiState.Error(it)
            }.onException {
                _uiState.value = UiState.Error(it)
            }.onFail {
                _uiState.value = UiState.Error(Throwable("Failed to load data"))
            }
        }
    }

    inner class LinkViewHolder(private val binding: FragmentOpenzipBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(link: LinkGetItemModel) {
            with(binding) {
                fragmentOpenzipInsiteTv.text = link.zip_title
                fragmentOpenzipZipTitle.text = link.zip_title
                setBackgroundBasedOnColor(fragmentOpenzipInsiteIv, link.zip_color)
            }
        }
    }


    private fun setBackgroundBasedOnColor(imageView: ImageView, color: String) {
        when (color.lowercase()) {
            "yellow" -> imageView.setBackgroundResource(R.drawable.ic_zip_shadow_1)
            "lightgreen" -> imageView.setBackgroundResource(R.drawable.ic_zip_shadow_2)
            "green" -> imageView.setBackgroundResource(R.drawable.ic_zip_shadow_3)
            "lightblue" -> imageView.setBackgroundResource(R.drawable.ic_zip_shadow_4)
            "blue" -> imageView.setBackgroundResource(R.drawable.ic_zip_shadow_5)
            "darkpurple" -> imageView.setBackgroundResource(R.drawable.ic_zip_shadow_6)
            "purple" -> imageView.setBackgroundResource(R.drawable.ic_zip_shadow_7)
            else -> imageView.setBackgroundResource(R.drawable.ic_zip_clip_shadow) // default
        }
    }
}
