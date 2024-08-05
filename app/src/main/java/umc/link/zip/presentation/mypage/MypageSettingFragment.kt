package umc.link.zip.presentation.mypage

import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentMypageSettingBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class MypageSettingFragment : BaseFragment<FragmentMypageSettingBinding>(R.layout.fragment_mypage_setting) {

    private val navigator by lazy { findNavController() }
    override fun initObserver() {

    }

    override fun initView() {
        binding.ivMypageSettingToolbarBack.setOnClickListener {
            navigator.navigateUp()
        }
        binding.clMypageSettingNotification.setOnClickListener{
            navigator.navigate(R.id.action_mypageSettingFragment_to_noticeFragment)
        }
    }



}