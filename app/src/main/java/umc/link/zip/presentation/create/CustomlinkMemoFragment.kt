package umc.link.zip.presentation.create

import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentCustomlinkMemoBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class CustomlinkMemoFragment : BaseFragment<FragmentCustomlinkMemoBinding>(R.layout.fragment_customlink_memo){
    override fun initObserver() {

    }

    override fun initView() {
        binding.ivCustomLinkMemoToolbarBack.setOnClickListener{
            navigateToCustom()
        }
        binding.clCustomLinkMemoCompleteBtn.setOnClickListener {
            navigateToCustom()
        }
    }

    private fun navigateToCustom() {
        findNavController().navigate(R.id.action_customlinkMemoFragment_to_customlinkCustomFragment)
    }
}