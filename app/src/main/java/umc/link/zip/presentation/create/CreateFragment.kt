package umc.link.zip.presentation.create

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentCreateBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class CreateFragment : BaseFragment<FragmentCreateBinding>(R.layout.fragment_create) {
    private val viewModel: CustomLinkViewModel by viewModels()

    override fun initObserver() {
        viewModel.linkData.observe(viewLifecycleOwner) { linkData ->
            // 필요시 데이터를 UI에 반영합니다.
        }
    }

    override fun initView() {
        binding.viewModel = viewModel // 데이터 바인딩에 ViewModel을 설정합니다.

        binding.ivCreateToolbarBack.setOnClickListener {
            navigateToHome()
        }

        binding.btnCreateSaveText.setOnClickListener {
            navigateToText()
        }

        binding.btnCreateSaveLink.setOnClickListener {
            viewModel.setUrl(binding.etCreateLink.text.toString())
            navigateToLink()
        }
    }

    private fun navigateToHome() {
        findNavController().navigate(R.id.action_createFragment_to_homeFragment)
    }

    private fun navigateToText() {
        findNavController().navigate(R.id.action_createFragment_to_customtextZipFragment)
    }

    private fun navigateToLink() {
        findNavController().navigate(R.id.action_createFragment_to_customlinkZipFragment)
    }
}
