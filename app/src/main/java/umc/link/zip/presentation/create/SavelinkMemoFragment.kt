package umc.link.zip.presentation.create

import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentSavelinkMemoBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class SavelinkMemoFragment : BaseFragment<FragmentSavelinkMemoBinding>(R.layout.fragment_savelink_memo){
    override fun initObserver() {

    }

    override fun initView() {
        binding.ivSaveLinkMemoToolbarBack.setOnClickListener{
            navigateToCustom()
        }
        binding.clSaveLinkMemoCompleteBtn.setOnClickListener {
            navigateToCustom()
        }
    }

    private fun navigateToCustom() {
        findNavController().navigate(R.id.action_savelinkMemoFragment_to_savelinkCustomFragment)
    }
}