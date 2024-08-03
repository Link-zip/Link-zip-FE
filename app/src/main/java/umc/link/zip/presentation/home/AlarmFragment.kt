package umc.link.zip.presentation.home

import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentAlarmBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class AlarmFragment : BaseFragment<FragmentAlarmBinding>(R.layout.fragment_alarm){
    override fun initObserver() {

    }

    override fun initView() {
        binding.ivAlarmToolbarBack.setOnClickListener{
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        findNavController().navigate(R.id.action_alarmFragment_to_homeFragment)
    }
}