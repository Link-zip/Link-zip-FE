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
import umc.link.zip.databinding.FragmentCustomtextMemoBinding
import umc.link.zip.domain.model.link.LinkExtractModel
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.create.adapter.CreateViewModel
import umc.link.zip.presentation.create.adapter.LinkAddViewModel
import umc.link.zip.presentation.create.adapter.LinkExtractViewModel
import umc.link.zip.util.extension.repeatOnStarted
import umc.link.zip.util.network.UiState

@AndroidEntryPoint
class CustomtextMemoFragment : BaseFragment<FragmentCustomtextMemoBinding>(R.layout.fragment_customtext_memo){

    private val linkAddViewModel: LinkAddViewModel by activityViewModels()
    private val linkExtractViewModel: LinkExtractViewModel by activityViewModels()

    override fun initObserver() {
        repeatOnStarted {
            linkAddViewModel.link.collectLatest { link ->
                // 제목
                binding.tvCustomTextMemoLinkTitle.text = link.title ?: "제목을 추가해주세요."
                // 메모
                binding.etCustomTextMemoAddMemo.setText(link.memo ?: "메모를 추가해주세요.")
            }
        }
        // 썸네일 API 응답
        repeatOnStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                linkExtractViewModel.extractResponse.collectLatest { state ->
                    when (state) {
                        is UiState.Loading -> {
                            // 로딩 상태 처리
                            Log.d("CustomtextMemoFragment", "Loading 제목/썸네일")
                        }

                        is UiState.Success<*> -> {
                            val data = state.data as LinkExtractModel
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
                            Log.d("CustomtextMemoFragment", "제목/썸네일 가져오기 성공")

                        }

                        is UiState.Error -> {
                            Toast.makeText(requireContext(), "제목/썸네일 추출 실패", Toast.LENGTH_SHORT).show()
                            Log.d("CustomtextMemoFragment", "제목/썸네일 가져오기 실패")
                        }

                        UiState.Empty -> Log.d("CustomtextMemoFragment", "제목/썸네일 isEmpty")
                    }
                }
            }
        }
    }

    override fun initView() {
        binding.ivCustomTextMemoToolbarBack.setOnClickListener{
            findNavController().navigateUp()
        }
        binding.btnCustomTextMemoComplete.setOnClickListener {
            // 메모 업데이트
            val updatedMemo = binding.etCustomTextMemoAddMemo.text.toString()
            linkAddViewModel.updateMemo(memo = updatedMemo)

            findNavController().navigateUp()
        }
    }

}