package umc.link.zip.presentation.create

import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentSavelinkZipBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class SavelinkZipFragment : BaseFragment<FragmentSavelinkZipBinding>(R.layout.fragment_savelink_zip){
    override fun initObserver() {

    }

    override fun initView() {
        binding.ivSaveLinkZipToolbarBack.setOnClickListener{
            navigateToCreate()
        }

        binding.clSaveLinkZipNextBtn.setOnClickListener{
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