package umc.link.zip.presentation.mypage

import android.content.Intent
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.data.UserPreferences
import umc.link.zip.databinding.FragmentMypageBinding
import umc.link.zip.presentation.SplashActivity
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class MypageFragment : BaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage) {

    private val navigator by lazy { findNavController() }
    override fun initObserver() {

    }

    override fun initView() {
        navigateToMypageProfile()
        navigateToMypageSetting()

        //로그아웃 기능 구현
        binding.tvMypageLogout.setOnClickListener {
            fnLogout()
        }
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

    private fun fnLogout(){
        UserPreferences(requireContext()).deleteUserId()
        val splashIntent = Intent(requireContext(), SplashActivity::class.java)
        splashIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(splashIntent)
        activity?.finish()
    }
}