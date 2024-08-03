package umc.link.zip.presentation.create

import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentCustomtextAlarmBinding
import umc.link.zip.databinding.FragmentSavelinkAlarmBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class SavelinkAlarmFragment : BaseFragment<FragmentSavelinkAlarmBinding>(R.layout.fragment_savelink_alarm){
    override fun initObserver() {

    }

    override fun initView() {
        binding.ivSaveLinkAlarmToolbarBack.setOnClickListener{
            navigateToCustom()
        }
        binding.clSaveLinkAlarmCompleteBtn.setOnClickListener {
            navigateToCustom()
        }
    }

    private fun navigateToCustom() {
        findNavController().navigate(R.id.action_savelinkAlarmFragment_to_savelinkCustomFragment)
    }
}