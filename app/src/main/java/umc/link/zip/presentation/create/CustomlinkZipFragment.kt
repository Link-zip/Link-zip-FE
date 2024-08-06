package umc.link.zip.presentation.create

import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentCustomlinkZipBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class CustomlinkZipFragment : BaseFragment<FragmentCustomlinkZipBinding>(R.layout.fragment_customlink_zip){
    override fun initObserver() {

    }

    override fun initView() {
        binding.ivCustomLinkZipToolbarBack.setOnClickListener{
            navigateToCreate()
        }

        binding.clCustomLinkZipNextBtn.setOnClickListener{
            navigateToCustom()
        }
    }

    private fun navigateToCreate() {
        findNavController().navigate(R.id.action_savelinkZipFragment_to_createFragment)
    }

    private fun navigateToCustom() {
        findNavController().navigate(R.id.action_savelinkZipFragment_to_savelinkCustomFragment)
    }
}