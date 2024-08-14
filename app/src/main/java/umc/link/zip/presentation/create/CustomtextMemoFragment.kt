package umc.link.zip.presentation.create

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import umc.link.zip.R
import umc.link.zip.databinding.FragmentCustomtextMemoBinding
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.util.extension.repeatOnStarted

@AndroidEntryPoint
class CustomtextMemoFragment : BaseFragment<FragmentCustomtextMemoBinding>(R.layout.fragment_customtext_memo){

    private val viewModel: CreateViewModel by activityViewModels()

    override fun initObserver() {
        repeatOnStarted {
            viewModel.link.collectLatest { link ->
                // 제목
                binding.tvCustomTextMemoLinkTitle.text = link.title ?: "No Title"
            }
        }
    }

    override fun initView() {
        binding.ivCustomTextMemoToolbarBack.setOnClickListener{
            findNavController().navigateUp()
        }
        binding.clCustomTextMemoCompleteBtn.setOnClickListener {
            // 메모 업데이트
            val updatedMemo = binding.etCustomTextMemoAddMemo.text.toString()
            viewModel.updateMemo(memo = updatedMemo)

            findNavController().navigateUp()
        }
    }

}