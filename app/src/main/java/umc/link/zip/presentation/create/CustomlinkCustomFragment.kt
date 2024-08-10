package umc.link.zip.presentation.create

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import umc.link.zip.R
import umc.link.zip.databinding.FragmentCustomlinkCustomBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class CustomlinkCustomFragment : BaseFragment<FragmentCustomlinkCustomBinding>(R.layout.fragment_customlink_custom){

    private val viewModel: CreateViewModel by activityViewModels()

    override fun initObserver() {
        lifecycleScope.launch {
            viewModel.link.collectLatest { link ->
                binding.etCustomLinkCustomLinkTitle.hint = link.title
            }
        }
    }

    override fun initView() {
        binding.ivCustomLinkCustomToolbarBack.setOnClickListener{
            findNavController().navigateUp()
        }

        binding.btnCustomLinkCustomMemo.setOnClickListener{
            navigateToMemo()
        }

        binding.btnCustomLinkCustomAlarm.setOnClickListener{
            navigateToAlarm()
        }

        binding.clCustomLinkCustomSaveBtn.setOnClickListener {
            navigateToOpenLink()
        }
    }

    private fun navigateToMemo() {
        findNavController().navigate(R.id.action_customlinkCustomFragment_to_customlinkMemoFragment)
    }

    private fun navigateToAlarm() {
        findNavController().navigate(R.id.action_customlinkCustomFragment_to_customlinkAlarmFragment)
    }

    private fun navigateToOpenLink() {
        findNavController().navigate(R.id.action_customlinkCustomFragment_to_openLinkFragment)
    }
}