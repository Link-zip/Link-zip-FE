package umc.link.zip.presentation.mypage

import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentMypageWithdrawalBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class MypageWithdrawalFragment : BaseFragment<FragmentMypageWithdrawalBinding>(R.layout.fragment_mypage_withdrawal) {

    private val navigator by lazy { findNavController() }
    override fun initObserver() {

    }

    override fun initView() {

    }

}