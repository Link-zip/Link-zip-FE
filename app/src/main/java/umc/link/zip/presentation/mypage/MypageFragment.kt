package umc.link.zip.presentation.mypage

import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentMypageBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class MypageFragment : BaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage) {

    private val navigator by lazy { findNavController() }
    override fun initObserver() {

    }

    override fun initView() {
        navigateToMypageProfile()
        navigateToMypageSetting()
    }

    private fun navigateToMypageProfile() {
        binding.clMypageProfile.setOnClickListener {
            navigator.navigate(R.id.action_mypageFragment_to_mypageProfileFragment)
        }
    }
    private fun navigateToMypageSetting() {
        binding.clMypageSetting.setOnClickListener {
            navigator.navigate(R.id.action_mypageFragment_to_mypageSettingFragment)
        }
    }
}