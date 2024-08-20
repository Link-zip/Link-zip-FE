package umc.link.zip.presentation.zip.adapter

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import umc.link.zip.R
import umc.link.zip.databinding.FragmentOpenzipBinding
import umc.link.zip.domain.model.link.LinkGetItemModel
import umc.link.zip.domain.model.link.LinkGetModel
import umc.link.zip.domain.model.link.LinkUpdateLikeModel
import umc.link.zip.domain.repository.LinkRepository
import umc.link.zip.util.network.NetworkResult
import umc.link.zip.util.network.UiState
import umc.link.zip.util.network.onError
import umc.link.zip.util.network.onException
import umc.link.zip.util.network.onFail
import javax.inject.Inject


@HiltViewModel
class OpenZipViewModel @Inject constructor(
    private val linkRepository: LinkRepository,
) : ViewModel() {
    private val _linkList = MutableStateFlow(LinkGetModel(emptyList()))
    val linkList: Flow<List<LinkGetItemModel>> get() = _linkList.map { it.link_data }

    private val _uiState = MutableStateFlow<UiState<LinkGetModel>>(UiState.Loading)
    val uiState: StateFlow<UiState<LinkGetModel>> = _uiState.asStateFlow()


    private val _linkId = MutableStateFlow<UiState<LinkUpdateLikeModel>>(UiState.Loading)
    val linkId: StateFlow<UiState<LinkUpdateLikeModel>> get() = _linkId


    fun getLinkList(zip_id: Int, tag: String, sortOrder: String) {
        viewModelScope.launch {
            linkRepository.getLinkData(zip_id, tag, sortOrder).apply {
                when (this) {
                    is NetworkResult.Success -> {
                        _uiState.value = UiState.Loading  // 상태를 초기화 (동일한 데이터가 와도 방출될 수 있도록)
                        _uiState.value = UiState.Success(this.data)
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

    inner class LinkViewHolder(private val binding: FragmentOpenzipBinding) :
        RecyclerView.ViewHolder(binding.root) {
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

    fun updateLikeStatusOnServer(linkId: Int) {
        viewModelScope.launch {
            linkRepository.UpdateLikeLink(linkId).apply {
                when (this) {
                    is NetworkResult.Success -> {
                        _linkId.value = UiState.Loading  // 상태를 초기화 (동일한 데이터가 와도 방출될 수 있도록)
                        _linkId.value = UiState.Success(this.data)
                    }

                    is NetworkResult.Error -> {
                        _linkId.value = UiState.Error(this.exception)
                    }

                    is NetworkResult.Fail -> {
                        _linkId.value = UiState.Error(Throwable("Failed to load data"))
                    }
                }
            }.onError {
                _linkId.value = UiState.Error(it)
            }.onException {
                _linkId.value = UiState.Error(it)
            }.onFail {
                _linkId.value = UiState.Error(Throwable("Failed to load data"))
            }
        }
    }
}
