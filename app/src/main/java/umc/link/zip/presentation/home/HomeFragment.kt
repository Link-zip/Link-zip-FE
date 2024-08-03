package umc.link.zip.presentation.home

import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentHomeBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home){
    override fun initObserver() {

    }

    override fun initView() {
        binding.clHomeAlarm.setOnClickListener{
            navigateToAlarm()
        }
    }

    private fun navigateToAlarm() {
        findNavController().navigate(R.id.action_homeFragment_to_alarmFragment)
    }
}