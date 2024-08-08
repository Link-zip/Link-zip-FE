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
import umc.link.zip.databinding.FragmentCreateBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class CreateFragment : BaseFragment<FragmentCreateBinding>(R.layout.fragment_create) {

    override fun initObserver() {

    }

    override fun initView() {

        binding.ivCreateToolbarBack.setOnClickListener {
            navigateToHome()
        }

        binding.btnCreateSaveText.setOnClickListener {
            navigateToText()
        }

        binding.btnCreateSaveLink.setOnClickListener {
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
