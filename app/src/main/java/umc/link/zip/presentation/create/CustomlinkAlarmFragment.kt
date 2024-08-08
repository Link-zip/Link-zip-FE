package umc.link.zip.presentation.create

import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentCustomlinkAlarmBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class CustomlinkAlarmFragment : BaseFragment<FragmentCustomlinkAlarmBinding>(R.layout.fragment_customlink_alarm){
    override fun initObserver() {

    }

    override fun initView() {
        binding.ivCustomLinkAlarmToolbarBack.setOnClickListener{
            navigateToCustom()
        }
        binding.clCustomLinkAlarmCompleteBtn.setOnClickListener {
            navigateToCustom()
        }
    }

    private fun navigateToCustom() {
        findNavController().navigate(R.id.action_customlinkAlarmFragment_to_customlinkCustomFragment)
    }
}