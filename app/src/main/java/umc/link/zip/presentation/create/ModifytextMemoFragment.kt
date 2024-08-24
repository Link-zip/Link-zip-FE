package umc.link.zip.presentation.create

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import umc.link.zip.R
import umc.link.zip.databinding.FragmentCustomlinkMemoBinding
import umc.link.zip.databinding.FragmentCustomtextMemoBinding
import umc.link.zip.domain.model.link.LinkExtractModel
import umc.link.zip.domain.model.link.LinkGetByLinkIDModel
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.create.adapter.CreateViewModel
import umc.link.zip.presentation.create.adapter.LinkAddViewModel
import umc.link.zip.presentation.create.adapter.LinkExtractViewModel
import umc.link.zip.presentation.create.adapter.LinkGetByIDViewModel
import umc.link.zip.util.extension.repeatOnStarted
import umc.link.zip.util.network.UiState

@AndroidEntryPoint
class ModifytextMemoFragment : BaseFragment<FragmentCustomtextMemoBinding>(R.layout.fragment_customtext_memo){

    private val linkAddViewModel: LinkAddViewModel by activityViewModels()
    private val linkGetByIDViewModel: LinkGetByIDViewModel by activityViewModels()

    private val linkId: Int? by lazy {
        arguments?.getInt("linkId")
    }

    private var isUpdated: Boolean = true

    override fun initObserver() {
        val linkId = linkId ?: return
        linkGetByIDViewModel.getLinkByLinkID(linkId)

        repeatOnStarted {
            if(isUpdated) {
                linkAddViewModel.link.collectLatest { link ->
                    // 제목
                    binding.tvCustomTextMemoLinkTitle.text = link.title ?: "제목을 추가해주세요."
                    // 메모
                    binding.etCustomTextMemoAddMemo.setText(link.memo ?: "메모를 추가해주세요.")
                }
                isUpdated = false
            }
        }
        // 썸네일 API 응답
        repeatOnStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                linkGetByIDViewModel.linkGetByLinkIDResponse.collectLatest { state ->
                    when (state) {
                        is UiState.Loading -> {
                            // 로딩 상태 처리
                            Log.d("ModifytextMemoFragment", "Loading 썸네일")
                        }

                        is UiState.Success<*> -> {
                            val data = state.data as LinkGetByLinkIDModel
                            // 썸네일
                            val thumbnailUrl = data.thumb
                            if(thumbnailUrl==null){
                                binding.ivCustomTextMemoTopImg.setImageResource(R.drawable.iv_link_thumbnail_default)
                            }else {
                                Glide.with(binding.ivCustomTextMemoTopImg.context)
                                    .load(thumbnailUrl)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(binding.ivCustomTextMemoTopImg)
                            }
                            Log.d("ModifytextMemoFragment", "썸네일 가져오기 성공")

                        }

                        is UiState.Error -> {
                            Log.d("ModifytextMemoFragment", "썸네일 가져오기 실패")
                        }

                        UiState.Empty -> Log.d("ModifytextMemoFragment", "썸네일 isEmpty")
                    }
                }
            }
        }
    }

    override fun initView() {
        binding.ivCustomTextMemoToolbarBack.setOnClickListener{
            findNavController().navigateUp()
            isUpdated = true
        }
        binding.btnCustomTextMemoComplete.setOnClickListener {
            // 메모 업데이트
            isUpdated = true
            var updatedMemo = binding.etCustomTextMemoAddMemo.text.toString()
            linkAddViewModel.updateMemo(memo = updatedMemo)

            Log.d("ModifyTextMemo", updatedMemo)
            findNavController().navigateUp()
        }
    }
}