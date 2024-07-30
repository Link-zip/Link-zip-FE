package umc.link.zip.presentation.mypage

import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentMypageBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class MypageFragment : BaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage) {
    private val navigator by lazy {
        findNavController()
    }

    override fun initObserver() {

    }

    override fun initView() {
        binding.clMypageProfile.setOnClickListener{
            navigateToMypageProfile()
        }
    }

    private fun navigateToMypageProfile() {
        navigator.navigate(R.id.action_mypageFragment_to_mypageProfileFragment)
    }
}