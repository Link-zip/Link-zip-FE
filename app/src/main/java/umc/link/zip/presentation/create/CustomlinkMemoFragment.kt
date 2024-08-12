package umc.link.zip.presentation.create

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import umc.link.zip.R
import umc.link.zip.databinding.FragmentCustomlinkMemoBinding
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.util.extension.repeatOnStarted

@AndroidEntryPoint
class CustomlinkMemoFragment : BaseFragment<FragmentCustomlinkMemoBinding>(R.layout.fragment_customlink_memo){

    private val viewModel: CreateViewModel by activityViewModels()

    override fun initObserver() {
        repeatOnStarted {
            viewModel.link.collectLatest { link ->
                // 제목
                binding.tvCustomLinkMemoLinkTitle.text = link.title ?: "No Title"
            }
        }
    }

    override fun initView() {
        binding.ivCustomLinkMemoToolbarBack.setOnClickListener{
            findNavController().navigateUp()
        }
        binding.clCustomLinkMemoCompleteBtn.setOnClickListener {
            // 메모 업데이트
            val updatedMemo = binding.etCustomLinkMemoAddMemo.text.toString()
            viewModel.updateMemo(memo = updatedMemo)

            findNavController().navigateUp()
        }
    }
}