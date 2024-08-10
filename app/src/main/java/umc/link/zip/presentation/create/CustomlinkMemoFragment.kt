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

@AndroidEntryPoint
class CustomlinkMemoFragment : BaseFragment<FragmentCustomlinkMemoBinding>(R.layout.fragment_customlink_memo){

    private val viewModel: CreateViewModel by activityViewModels()

    override fun initObserver() {
        lifecycleScope.launch {
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
            navigateToCustom()
        }
    }

    private fun navigateToCustom() {
        findNavController().navigate(R.id.action_customlinkMemoFragment_to_customlinkCustomFragment)
    }
}