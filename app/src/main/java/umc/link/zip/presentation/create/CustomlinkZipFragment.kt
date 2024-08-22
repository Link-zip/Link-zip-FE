package umc.link.zip.presentation.create

import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentCustomlinkZipBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class CustomlinkZipFragment : BaseFragment<FragmentCustomlinkZipBinding>(R.layout.fragment_customlink_zip) {

    override fun initObserver() {

    }

    override fun initView() {
        binding.ivCustomLinkZipToolbarBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.clCustomLinkZipNextBtn.setOnClickListener {
            navigateToCustom()
        }

        binding.clCustomLinkZipEasySaveBtn.setOnClickListener {
            navigateToOpenLink()
        }
    }


    private fun navigateToCustom() {
        findNavController().navigate(R.id.action_customlinkZipFragment_to_customlinkCustomFragment)
    }

    private fun navigateToOpenLink(){
        findNavController().navigate(R.id.action_customlinkZipFragment_to_openLinkFragment)
    }
}
