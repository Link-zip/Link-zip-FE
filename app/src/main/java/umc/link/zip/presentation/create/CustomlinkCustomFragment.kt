package umc.link.zip.presentation.create

import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentCustomlinkCustomBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class CustomlinkCustomFragment : BaseFragment<FragmentCustomlinkCustomBinding>(R.layout.fragment_customlink_custom){
    override fun initObserver() {

    }

    override fun initView() {
        binding.ivCustomLinkCustomToolbarBack.setOnClickListener{
            navigateToZip()
        }

        binding.btnCustomLinkCustomMemo.setOnClickListener{
            navigateToMemo()
        }

        binding.btnCustomLinkCustomAlarm.setOnClickListener{
            navigateToAlarm()
        }

        binding.clCustomLinkCustomSaveBtn.setOnClickListener {
            navigateToOpenLink()
        }
    }

    private fun navigateToZip() {
        findNavController().navigate(R.id.action_customlinkCustomFragment_to_customlinkZipFragment)
    }

    private fun navigateToMemo() {
        findNavController().navigate(R.id.action_customlinkCustomFragment_to_customlinkMemoFragment)
    }

    private fun navigateToAlarm() {
        findNavController().navigate(R.id.action_customlinkCustomFragment_to_customlinkAlarmFragment)
    }

    private fun navigateToOpenLink() {
        findNavController().navigate(R.id.action_customlinkCustomFragment_to_openLinkFragment)
    }
}