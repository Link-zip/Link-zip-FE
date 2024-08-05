package umc.link.zip.presentation.mypage

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import umc.link.zip.R
import umc.link.zip.databinding.FragmentNoticeBinding
import umc.link.zip.presentation.base.BaseFragment

class NoticeFragment :BaseFragment<FragmentNoticeBinding> (R.layout.fragment_notice){
    private val navigator by lazy { findNavController() }
    override fun initObserver() {

    }

    override fun initView() {
        binding.ivNoticeToolbarBack.setOnClickListener{
            navigator.navigateUp()
        }
    }

}