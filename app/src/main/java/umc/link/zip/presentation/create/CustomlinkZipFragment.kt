package umc.link.zip.presentation.create

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import umc.link.zip.R
import umc.link.zip.databinding.FragmentCustomlinkZipBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class CustomlinkZipFragment : BaseFragment<FragmentCustomlinkZipBinding>(R.layout.fragment_customlink_zip) {
    private val viewModel: CustomLinkViewModel by viewModels()

    override fun initObserver() {
        // ViewModel의 StateFlow를 관찰합니다.
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.link.collectLatest { linkData ->
                    // 필요시 데이터를 UI에 반영합니다.
                }
            }
        }
    }

    override fun initView() {
        binding.viewModel = viewModel // 데이터 바인딩에 ViewModel을 설정합니다.

        binding.ivCustomLinkZipToolbarBack.setOnClickListener {
            navigateToCreate()
        }

        binding.clCustomLinkZipNextBtn.setOnClickListener {
            // 실제 Zip 선택 로직 필요
            // viewModel.setZip("chosenZipId")
            navigateToCustom()
        }
    }

    private fun navigateToCreate() {
        findNavController().navigate(R.id.action_customlinkZipFragment_to_createFragment)
    }

    private fun navigateToCustom() {
        findNavController().navigate(R.id.action_customlinkZipFragment_to_customlinkCustomFragment)
    }
}
