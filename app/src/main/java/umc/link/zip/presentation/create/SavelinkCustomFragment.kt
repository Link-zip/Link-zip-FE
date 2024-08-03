package umc.link.zip.presentation.create

import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentSavelinkCustomBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class SavelinkCustomFragment : BaseFragment<FragmentSavelinkCustomBinding>(R.layout.fragment_savelink_custom){
    override fun initObserver() {

    }

    override fun initView() {
        binding.ivSaveLinkCustomToolbarBack.setOnClickListener{
            navigateToZip()
        }

        binding.clSaveLinkCustomMemoBtn.setOnClickListener{
            navigateToMemo()
        }

        binding.clSaveLinkCustomAlarmBtn.setOnClickListener{
            navigateToAlarm()
        }

        binding.clSaveLinkCustomSaveBtn.setOnClickListener {
            navigateToOpenLink()
        }
    }

    private fun navigateToZip() {
        findNavController().navigate(R.id.action_savelinkCustomFragment_to_savelinkZipFragment)
    }

    private fun navigateToMemo() {
        findNavController().navigate(R.id.action_savelinkCustomFragment_to_savelinkMemoFragment)
    }

    private fun navigateToAlarm() {
        findNavController().navigate(R.id.action_savelinkCustomFragment_to_savelinkAlarmFragment)
    }

    private fun navigateToOpenLink() {
        findNavController().navigate(R.id.action_savelinkCustomFragment_to_openLinkFragment)
    }
}